package com.nxtlife.efkon.license.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.license.dao.jpa.LicenseJpaDao;
import com.nxtlife.efkon.license.dao.jpa.LicenseTypeJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProductDetailJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectProductCommentJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectProductJpaDao;
import com.nxtlife.efkon.license.entity.license.License;
import com.nxtlife.efkon.license.entity.license.LicenseType;
import com.nxtlife.efkon.license.entity.project.product.ProjectProduct;
import com.nxtlife.efkon.license.entity.project.product.ProjectProductComment;
import com.nxtlife.efkon.license.entity.user.User;
import com.nxtlife.efkon.license.enums.ExpirationPeriodType;
import com.nxtlife.efkon.license.enums.LicenseStatus;
import com.nxtlife.efkon.license.enums.LicenseTypeEnum;
import com.nxtlife.efkon.license.enums.ProjectProductStatus;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.ProjectProductService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.license.LicenseResponse;
import com.nxtlife.efkon.license.view.product.ProductDetailResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductRequest;
import com.nxtlife.efkon.license.view.project.product.ProjectProductResponse;

@Service("projectProductServiceImpl")
@Transactional
public class ProjectProductServiceImpl extends BaseService implements ProjectProductService {

	@Autowired
	private ProjectProductJpaDao projectProductDao;

	@Autowired
	private ProjectJpaDao projectDao;

	@Autowired
	private ProductDetailJpaDao productDetailDao;

	@Autowired
	private ProjectProductCommentJpaDao projectProductCommentDao;

	@Autowired
	private LicenseJpaDao licenseDao;

	@Autowired
	private LicenseTypeJpaDao licenseTypeJpaDao;

	private static Logger logger = LoggerFactory.getLogger(ProjectProductServiceImpl.class);

	private void validate(String expirationPeriodType, String licenseType, Integer expirationMonthCount) {
		if (expirationPeriodType != null && !ExpirationPeriodType.matches(expirationPeriodType)) {
			throw new ValidationException(
					String.format("Expiration period type (%s) is not valid", expirationPeriodType));
		}
		if (licenseType != null && !LicenseTypeEnum.matches(licenseType)) {
			throw new ValidationException(String.format("License type (%s) is not valid", licenseType));
		}
		if (licenseType != null && licenseType.equals(LicenseTypeEnum.DEMO.name()) && expirationPeriodType != null
				&& expirationPeriodType.equals(ExpirationPeriodType.LIFETIME.name())) {
			throw new ValidationException("Demo license can't be for lifetime");
		}
		if (expirationPeriodType != null && expirationPeriodType.equals(ExpirationPeriodType.LIMITED.name())
				&& expirationMonthCount == null) {
			throw new ValidationException("Expiration month count can't be null for limited expiration period");
		}
	}

	private void validate(ProjectProductRequest request, String licenseType) {
		if (!projectDao.existsByIdAndActive(request.getProjectId(), true)) {
			throw new ValidationException(String.format("Project (%s) not exist", mask(request.getProjectId())));
		}
		if (!productDetailDao.existsByIdAndActive(request.getProductDetailId(), true)) {
			throw new ValidationException(
					String.format("Product detail (%s) not exist", mask(request.getProductDetailId())));
		}
		validate(request.getExpirationPeriodType(), licenseType, request.getExpirationMonthCount());
	}

	private ProjectProductResponse getProjectProductResponse(ProjectProduct projectProduct, Long projectId,
			Long productDetailId) {
		ProjectProductResponse response = ProjectProductResponse.get(projectProduct);
		response.setProjectResponse(projectDao.findResponseById(projectId));
		ProductDetailResponse productDetailResponse = productDetailDao.findResponseById(productDetailId);
		response.setProductDetailResponse(productDetailResponse);
		return response;
	}

	/**
	 * this method used to set end date according to start date and expiration
	 * month count
	 * <p>
	 * if addition of month of start date and expiration month count greater
	 * than 12 then year will be incremented and month will be add result minus
	 * 12.
	 *
	 * @return String
	 */
	private String setEndDate(String startDate, Integer expirationMonthCount) {
		if (startDate == null || expirationMonthCount == null) {
			return null;
		}
		try {
			LocalDate endDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			endDate = endDate.plusMonths(expirationMonthCount);
			return endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (DateTimeParseException ex) {
			throw new ValidationException(String.format("Start date (%s) isn't valid", startDate));
		}

	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_CREATE)
	public ProjectProductResponse save(ProjectProductRequest request) {
		Optional<LicenseType> licenseType = licenseTypeJpaDao.findById(request.getLicenseTypeId());
		if (!licenseType.isPresent()) {
			throw new ValidationException("License type is not valid");
		}
		validate(request, licenseType.get().getName());
		ProjectProduct projectProduct = request.toEntity();
		String endDate;
		Integer expirationMonth = request.getExpirationMonthCount();
		if (request.getLicenseTypeId() != null) {
			if (request.getExpirationMonthCount() != null
					&& request.getExpirationMonthCount() > licenseType.get().getMaxMonthCount()) {
				throw new ValidationException(
						String.format("License month (%d) limit exceed", licenseType.get().getMaxMonthCount()));
			}
			if (request.getExpirationMonthCount() == null) {
				expirationMonth = licenseType.get().getMaxMonthCount();
			}
		}

		endDate = setEndDate(request.getStartDate(), expirationMonth);
		projectProduct.setEndDate(endDate);
		projectProduct.setStatus(ProjectProductStatus.DRAFT);
		projectProductDao.save(projectProduct);
		return getProjectProductResponse(projectProduct, request.getProjectId(), request.getProductDetailId());
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_UPDATE)
	public ProjectProductResponse update(Long id, ProjectProductRequest request) {
		Long unmaskId = unmask(id);
		ProjectProductResponse projectProduct = projectProductDao.findResponseById(unmaskId);
		if (projectProduct == null) {
			throw new NotFoundException(String.format("Project product (%s) not found", id));
		}
		if (!projectProduct.getStatus().equals(ProjectProductStatus.DRAFT)) {
			throw new ValidationException("Project product can't be updated after submitting");
		}
		Optional<LicenseType> licenseType = licenseTypeJpaDao.findById(request.getLicenseTypeId());
		if (!licenseType.isPresent()) {
			throw new ValidationException("License type is not valid");
		}
		validate(request.getExpirationPeriodType(), licenseType.get().getName(), request.getExpirationMonthCount());

		Integer expirationMonth = request.getExpirationMonthCount();
		if (request.getLicenseTypeId() != null) {
			if (request.getExpirationMonthCount() != null
					&& request.getExpirationMonthCount() > licenseType.get().getMaxMonthCount()) {
				throw new ValidationException(
						String.format("License month (%d) limit exceed", licenseType.get().getMaxMonthCount()));
			}
			if (request.getExpirationMonthCount() == null) {
				expirationMonth = licenseType.get().getMaxMonthCount();
			}
		}
		int rows = projectProductDao.update(unmaskId,
				request.getLicenseCount() == null ? projectProduct.getLicenseCount() : request.getLicenseCount(),
				request.getLicenseTypeId() == null ? projectProduct.getLicenseTypeId() : request.getLicenseTypeId(),
				request.getExpirationPeriodType() == null ? projectProduct.getExpirationPeriodType()
						: ExpirationPeriodType.valueOf(request.getExpirationPeriodType()),
				request.getExpirationMonthCount() == null ? projectProduct.getExpirationMonthCount()
						: request.getExpirationMonthCount(),
				request.getStartDate() == null ? projectProduct.getStartDate() : request.getStartDate(),
				setEndDate(request.getStartDate(), expirationMonth), getUserId(), new Date());
		if (rows > 0) {
			logger.info("Project product {} updated successfully", unmaskId);
		}
		ProjectProductResponse projectProductResponse = projectProductDao.findResponseById(unmaskId);
		projectProductResponse.setProductDetailResponse(
				productDetailDao.findResponseById(unmask(projectProductResponse.getProductDetailId())));
		projectProductResponse
				.setProjectResponse(projectDao.findResponseById(unmask(projectProductResponse.getProjectId())));
		return null;
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_FETCH)
	public List<ProjectProductResponse> findAll() {
		User user = getUser();
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		List<ProjectProductResponse> projectProductResponseList;
		if (roles.contains("Customer")) {
			projectProductResponseList = projectProductDao.findByProjectCustomerEmailAndActive(user.getEmail(), true);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				projectProductResponseList = projectProductDao.findByProjectProjectManagerIdAndActive(user.getUserId(),
						true);
			} else {
				projectProductResponseList = projectProductDao.findByActive(true);
			}
		}
		projectProductResponseList = projectProductResponseList.stream()
				.filter(projectProduct -> !(projectProduct.getStatus().equals(ProjectProductStatus.DRAFT)
						&& !projectProduct.getCreatedById().equals(getUserId())))
				.collect(Collectors.toList());
		projectProductResponseList.forEach(projectProduct -> {
			projectProduct.setProductDetailResponse(
					productDetailDao.findResponseById(unmask(projectProduct.getProductDetailId())));
			projectProduct.setProjectResponse(projectDao.findResponseById(unmask(projectProduct.getProjectId())));
			projectProduct.setComments(projectProductCommentDao.findByProjectProductId(unmask(projectProduct.getId())));
			projectProduct.setLicenses(licenseDao.findByProjectProductIdAndActive(unmask(projectProduct.getId()), true));
		});
		return projectProductResponseList;
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_FETCH)
	public List<ProjectProductResponse> findByProjectId(Long projectId) {
		User user = getUser();
		Long unmaskProjectId = unmask(projectId);
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		List<ProjectProductResponse> projectProductResponseList;
		if (roles.contains("Customer")) {
			projectProductResponseList = projectProductDao
					.findByProjectIdAndProjectCustomerEmailAndActive(unmaskProjectId, user.getEmail(), true);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				projectProductResponseList = projectProductDao
						.findByProjectIdAndProjectProjectManagerIdAndActive(unmaskProjectId, user.getUserId(), true);
			} else {
				projectProductResponseList = projectProductDao.findByProjectIdAndActive(unmaskProjectId, true);
			}
		}
		projectProductResponseList = projectProductResponseList.stream()
				.filter(projectProduct -> !(projectProduct.getStatus().equals(ProjectProductStatus.DRAFT)
						&& !projectProduct.getCreatedById().equals(getUserId())))
				.collect(Collectors.toList());
		projectProductResponseList.forEach(projectProduct -> {
			projectProduct.setProductDetailResponse(
					productDetailDao.findResponseById(unmask(projectProduct.getProductDetailId())));
			projectProduct.setProjectResponse(projectDao.findResponseById(unmask(projectProduct.getProjectId())));
			projectProduct.setComments(projectProductCommentDao.findByProjectProductId(unmask(projectProduct.getId())));
		});
		return projectProductResponseList;
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_FETCH)
	public ProjectProductResponse findById(Long id) {
		User user = getUser();
		Long unmaskId = unmask(id);
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		ProjectProductResponse projectProductResponse;
		if (roles.contains("Customer")) {
			projectProductResponse = projectProductDao.findByIdAndProjectCustomerEmailAndActive(unmaskId,
					user.getEmail(), true);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				projectProductResponse = projectProductDao.findByIdAndProjectProjectManagerIdAndActive(unmaskId,
						user.getUserId(), true);
			} else {
				projectProductResponse = projectProductDao.findByIdAndActive(unmaskId, true);
			}
		}
		if (projectProductResponse != null) {
			projectProductResponse.setProductDetailResponse(
					productDetailDao.findResponseById(unmask(projectProductResponse.getProductDetailId())));
			projectProductResponse
					.setProjectResponse(projectDao.findResponseById(unmask(projectProductResponse.getProjectId())));
			projectProductResponse.setComments(projectProductCommentDao.findByProjectProductId(unmaskId));
		} else {
			throw new NotFoundException(String.format("Project product (%s) not found", id));
		}
		return projectProductResponse;
	}

	@Override
	public ProjectProductResponse updateStatus(Long id, ProjectProductStatus status, String comment) {
		User user = getUser();
		Long unmaskId = unmask(id);
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		ProjectProductStatus projectProductStatus;
		if (roles.contains("Customer")) {
			projectProductStatus = projectProductDao.findStatusByIdAndProjectCustomerEmailAndActive(unmaskId,
					user.getEmail(), true);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				projectProductStatus = projectProductDao.findStatusByIdAndProjectProjectManagerIdAndActive(unmaskId,
						user.getUserId(), true);
			} else {
				projectProductStatus = projectProductDao.findStatusByIdAndActive(unmaskId, true);
			}
		}
		if (projectProductStatus != null) {
			if (status.equals(ProjectProductStatus.APPROVED)
					&& !projectProductStatus.equals(ProjectProductStatus.REVIEWED)) {
				throw new ValidationException("You can approve project product after review only");
			}
			if (status.equals(ProjectProductStatus.SUBMIT)
					&& !projectProductStatus.equals(ProjectProductStatus.DRAFT)) {
				throw new ValidationException("You can submit project product after draft only");
			}
			if (status.equals(ProjectProductStatus.REVIEWED)
					&& !projectProductStatus.equals(ProjectProductStatus.SUBMIT)) {
				throw new ValidationException("You can review project product after submit only");
			}
			if (status.equals(ProjectProductStatus.REJECT) && (comment == null || comment.isEmpty())) {
				throw new ValidationException("Comment is required at the reject time");
			}
			if (status.equals(ProjectProductStatus.REJECT) && (projectProductStatus.equals(ProjectProductStatus.DRAFT)
					|| projectProductStatus.equals(ProjectProductStatus.APPROVED))) {
				throw new ValidationException("You can't reject project product in draft and approved mode");
			}
			int rows;
			if (status.equals(ProjectProductStatus.REJECT)) {
				rows = projectProductDao.update(unmaskId, ProjectProductStatus.DRAFT, getUserId(), new Date());
			} else {
				rows = projectProductDao.update(unmaskId, status, getUserId(), new Date());
			}
			if (comment != null && !comment.isEmpty()) {
				projectProductCommentDao.save(new ProjectProductComment(comment, getUserId(), status.name(), unmaskId));
			}
			ProjectProductResponse projectProductResponse = projectProductDao.findByIdAndActive(unmaskId, true);
			if (projectProductResponse != null) {
				projectProductResponse.setProductDetailResponse(
						productDetailDao.findResponseById(unmask(projectProductResponse.getProductDetailId())));
				projectProductResponse
						.setProjectResponse(projectDao.findResponseById(unmask(projectProductResponse.getProjectId())));
				if (status.equals(ProjectProductStatus.APPROVED) && rows > 0) {
					logger.info("Project product {} approved successfully", unmaskId);
					Integer licenseCount = projectProductDao.findLicenseCountById(unmaskId);
					for (int i = 0; i < licenseCount; i++) {
						licenseDao.save(
								new License(
										String.format("EF-%s-%s-%s-%s-%s-%04d-%04d-%s-%s",
												projectProductResponse.getProjectResponse().getCustomerCode(),
												projectProductResponse.getProductDetailResponse()
														.getProductFamilyCode(),
												projectProductResponse.getProductDetailResponse().getProductCodeCode(),
												getUser().getCode(), projectProductResponse.getLicenseTypeCode(),
												(i + 1),
												projectProductResponse.getExpirationMonthCount() == null ? 0
														: projectProductResponse.getExpirationMonthCount(),
												LocalDate
														.parse(projectProductResponse.getStartDate(),
																DateTimeFormatter.ofPattern("yyyy-MM-dd"))
														.format(DateTimeFormatter.ofPattern("ddMMyyyy")),
												projectProductResponse.getEndDate() == null ? "NA"
														: LocalDate
																.parse(projectProductResponse.getEndDate(),
																		DateTimeFormatter.ofPattern("yyyy-MM-dd"))
																.format(DateTimeFormatter.ofPattern("ddMMyyyy"))),
										LicenseStatus.ACTIVE, unmaskId));
					}
				}
				projectProductResponse.setComments(projectProductCommentDao.findByProjectProductId(unmaskId));
			}
			return projectProductResponse;
		} else {
			throw new NotFoundException(String.format("Project product (%s) not found", id));
		}

	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_RENEW)
	public ProjectProductResponse renew(Long id, ProjectProductRequest request) {
		User user = getUser();
		Long unmaskId = unmask(id);
		if (request.getExpirationMonthCount() == null && request.getStartDate() == null) {
			throw new ValidationException("Start date and exipration month count is mandatory for renewal");
		}
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		ProjectProductResponse projectProduct = null;
		if (roles.contains("Customer")) {
			projectProduct = projectProductDao.findByIdAndProjectCustomerEmailAndActive(unmaskId, user.getEmail(),
					true);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				projectProduct = projectProductDao.findByIdAndProjectProjectManagerIdAndActive(unmaskId,
						user.getUserId(), true);
			} else {
				projectProduct = projectProductDao.findByIdAndActive(unmaskId, true);
			}
		}
		if (projectProduct != null) {
			if (projectProduct.getLicenseTypeName().equals(LicenseTypeEnum.DEMO.name())) {
				throw new ValidationException("You can't renew demo product");
			}
			if (!(projectProduct.getStatus().equals(ProjectProductStatus.APPROVED)
					|| !projectProduct.getStatus().equals(ProjectProductStatus.RENEWED))) {
				throw new ValidationException(
						String.format("You can't renew if project product(%s) not approved", projectProduct.getId()));
			}
			if (projectProduct.getExpirationPeriodType().equals(ExpirationPeriodType.LIFETIME)) {
				throw new ValidationException("You can't renew this product because it's already for lifetime");
			}
			ProjectProduct renewedProjectProduct = new ProjectProduct(projectProduct.getLicenseCount(),
					unmask(projectProduct.getLicenseTypeId()), projectProduct.getExpirationPeriodType(),
					projectProduct.getExpirationMonthCount(), request.getStartDate(),
					setEndDate(request.getStartDate(), request.getExpirationMonthCount()),
					ProjectProductStatus.RENEWED);
			renewedProjectProduct.settProductDetailId(unmask(projectProduct.getProductDetailId()));
			renewedProjectProduct.settProjectId(unmask(projectProduct.getProjectId()));
			renewedProjectProduct = projectProductDao.save(renewedProjectProduct);
			projectProductCommentDao.save(new ProjectProductComment("Project Renewed", getUserId(),
					ProjectProductStatus.RENEWED.name(), renewedProjectProduct.getId()));
			ProjectProductResponse projectProductResponse = projectProductDao
					.findResponseById(renewedProjectProduct.getId());
			projectProductResponse.setProductDetailResponse(
					productDetailDao.findResponseById(unmask(projectProductResponse.getProductDetailId())));
			projectProductResponse
					.setProjectResponse(projectDao.findResponseById(unmask(projectProductResponse.getProjectId())));
			logger.info("Project product {} renewed successfully", unmaskId);
			List<LicenseResponse> licenseList = licenseDao
					.findByProjectProductIdAndActive(renewedProjectProduct.getId(), true);
			int i = 0;
			License license;
			for (LicenseResponse oldLicense : licenseList) {
				license = new License(
						String.format("EF-%s-%s-%s-%s-%s-%04d-%04d-%s-%s",
								projectProductResponse.getProjectResponse().getCustomerCode(),
								projectProductResponse.getProductDetailResponse().getProductFamilyCode(),
								projectProductResponse.getProductDetailResponse().getProductCodeCode(),
								getUser().getCode(), projectProductResponse.getLicenseTypeCode(), (++i),
								projectProductResponse.getExpirationMonthCount() == null ? 0
										: projectProductResponse.getExpirationMonthCount(),
								LocalDate
										.parse(projectProductResponse.getStartDate(),
												DateTimeFormatter.ofPattern("yyyy-MM-dd"))
										.format(DateTimeFormatter.ofPattern("ddMMyyyy")),
								projectProductResponse.getEndDate() == null ? "NA"
										: LocalDate
												.parse(projectProductResponse.getEndDate(),
														DateTimeFormatter.ofPattern("yyyy-MM-dd"))
												.format(DateTimeFormatter.ofPattern("ddMMyyyy"))),
						LicenseStatus.ACTIVE, renewedProjectProduct.getId());
				license.setAccessId(oldLicense.getAccessId());
				license.setName(oldLicense.getName());
				licenseDao.save(license);
			}
			projectProductResponse
					.setComments(projectProductCommentDao.findByProjectProductId(renewedProjectProduct.getId()));
			return projectProductResponse;

		} else {
			throw new NotFoundException(String.format("Project product (%s) not found", id));
		}

	}

	@Override
	@Secured(AuthorityUtils.PROJECT_PRODUCT_DELETE)
	public SuccessResponse delete(Long id) {
		Long unmaskId = unmask(id);
		if (!projectProductDao.existsById(unmaskId)) {
			throw new NotFoundException(String.format("Project product (%s) not found", id));
		}
		if (licenseDao.existsByProjectProductIdAndActive(unmaskId, true)) {
			throw new ValidationException(String.format(
					"Project product (%s) can't be delete as some of the project product have got the licenses ", id));
		}
		List<Long> projectProductCommentIds = projectProductCommentDao.findAllIdsByProjectProductIdAndActive(unmaskId,
				true);
		if (!projectProductCommentIds.isEmpty()) {
			projectProductCommentDao.delete(projectProductCommentIds, getUserId(), new Date());
		}
		int rows = projectProductDao.delete(unmaskId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("project product {} successfully deleted", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Project product successfully deleted");

	}

}
