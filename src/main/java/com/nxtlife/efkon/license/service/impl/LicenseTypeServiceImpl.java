package com.nxtlife.efkon.license.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.license.dao.jpa.LicenseTypeJpaDao;
import com.nxtlife.efkon.license.entity.license.LicenseType;
import com.nxtlife.efkon.license.enums.LicenseTypeEnum;
import com.nxtlife.efkon.license.ex.NotFoundException;
import com.nxtlife.efkon.license.service.BaseService;
import com.nxtlife.efkon.license.service.LicenseTypeService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.license.LicenseTypeResponse;

@Service("licenseTypeServiceImpl")
@Transactional
public class LicenseTypeServiceImpl extends BaseService implements LicenseTypeService {

	@Autowired
	private LicenseTypeJpaDao licenseTypeJpaDao;

	@PostConstruct
	private void init() {
		for (LicenseTypeEnum licenseType : LicenseTypeEnum.values()) {
			if (!licenseTypeJpaDao.existsByName(licenseType.name())) {
				licenseTypeJpaDao.save(
						new LicenseType(licenseType.name(), licenseType.getCode(), licenseType.getDefaultLimit()));
			}
		}
	}

	@Override
	public List<LicenseTypeResponse> findAll() {
		return licenseTypeJpaDao.findAll().stream().map(LicenseTypeResponse::get).collect(Collectors.toList());
	}

	@Override
	@Secured(AuthorityUtils.LICENSE_TYPE_UPDATE)
	public SuccessResponse update(Long id, Integer monthCount) {
		Long unmaskId = unmask(id);
		Optional<LicenseType> licenseType = licenseTypeJpaDao.findById(unmaskId);
		if (!licenseType.isPresent()) {
			throw new NotFoundException(String.format("This license type(%s) not exists", id));
		}
		licenseType.get().setMaxMonthCount(monthCount);
		licenseTypeJpaDao.save(licenseType.get());
		return new SuccessResponse(HttpStatus.OK.value(), "License type successfully updated");
	}

}
