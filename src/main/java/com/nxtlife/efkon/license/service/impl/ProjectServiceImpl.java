package com.nxtlife.efkon.license.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.license.dao.jpa.ProjectJpaDao;
import com.nxtlife.efkon.license.dao.jpa.ProjectTypeJpaDao;
import com.nxtlife.efkon.license.dao.jpa.RoleJpaDao;
import com.nxtlife.efkon.license.dao.jpa.UserJpaDao;
import com.nxtlife.efkon.license.dao.jpa.UserRoleJpaDao;
import com.nxtlife.efkon.license.entity.project.Project;
import com.nxtlife.efkon.license.entity.user.User;
import com.nxtlife.efkon.license.entity.user.UserRole;
import com.nxtlife.efkon.license.ex.ValidationException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.ProjectService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.project.ProjectRequest;
import com.nxtlife.efkon.license.view.project.ProjectResponse;
import com.nxtlife.efkon.license.view.user.UserResponse;

@Service("projectServiceImpl")
@Transactional
public class ProjectServiceImpl extends BaseService implements ProjectService {

	private static PasswordEncoder userPasswordEncoder = new BCryptPasswordEncoder();

	@Autowired
	private ProjectJpaDao projectDao;

	@Autowired
	private ProjectTypeJpaDao projectTypeDao;

	@Autowired
	private RoleJpaDao roleDao;

	@Autowired
	private UserJpaDao userDao;

	@Autowired
	private UserRoleJpaDao userRoleDao;

	/**
	 * this method used to validate request. In this we are validating that
	 * project type and project manager are valid
	 * 
	 * @param request
	 */
	private void validate(ProjectRequest request) {
		if (!projectTypeDao.existsById(request.getProjectTypeId())) {
			throw new ValidationException(
					String.format("Project type (%s) not found", mask(request.getProjectTypeId())));
		}
		if (!userRoleDao.existsByUserIdAndRoleName(request.getProjectManagerId(), "Project Manager")) {
			throw new ValidationException(
					String.format("Project manager doesn't exists", mask(request.getProjectManagerId())));
		}
	}

	@Override
	@Secured(AuthorityUtils.PROJECT_CREATE)
	public ProjectResponse save(ProjectRequest request) {
		validate(request);
		UserResponse existUser = userDao.findByEmailAndActive(request.getCustomerEmail(), true);
		Long customerRoleId = roleDao.findIdByName("Customer");
		if (customerRoleId == null) {
			throw new ValidationException("Customer role not created please ask admin to generate role for customer");
		}
		if (existUser != null) {
			Set<Long> roleIds = userRoleDao.findRoleIdsByUserId(unmask(existUser.getId()));
			roleIds.remove(customerRoleId);
			if (!roleIds.isEmpty()) {
				throw new ValidationException(
						"This email id already used for management that's why you can't use this as a customer");
			}
		}
		User user = null;
		Project project = request.toEntity();
		if (existUser == null) {
			String code = String.format("%04d", sequenceGenerator("User"));
			user = userDao.save(new User(request.getCustomerName(), code, request.getCustomerEmail(),
					userPasswordEncoder.encode("12345"), request.getCustomerEmail(), project.getIsEmailSend(),
					request.getCustomerContactNo()));
			userRoleDao.save(new UserRole(user.getId(), customerRoleId));
		}
		project.setCustomerCode(user == null ? existUser.getCode() : user.getCode());
		projectDao.save(project);
		ProjectResponse response = projectDao.findResponseById(project.getId());
		return response;

	}

	/**
	 * return a list of projects which is active
	 *
	 * @return List of <tt>ProjectResponse</tt>
	 */
	@Override
	@Secured(AuthorityUtils.PROJECT_FETCH)
	public List<ProjectResponse> findAll() {
		User user = getUser();
		Set<String> roles = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet());
		List<ProjectResponse> projects;
		if (roles.contains("Customer")) {
			projects = projectDao.findByCustomerEmailAndActive(user.getEmail(), true);
		} else {
			Boolean isProjectManager = false;
			if (roles.contains("Project Manager")) {
				isProjectManager = true;
				roles.remove("Project Manager");
			}
			if (roles.isEmpty() && isProjectManager) {
				projects = projectDao.findByProjectManagerIdAndActive(user.getUserId(), true);
			} else {
				projects = projectDao.findByActive(true);
			}
		}
		return projects;
	}

}
