package com.nxtlife.efkon.license.dao.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nxtlife.efkon.license.entity.license.License;
import com.nxtlife.efkon.license.view.license.LicenseResponse;

@Repository
public interface LicenseJpaDao extends JpaRepository<License, Long> {

	public Boolean existsByProjectProductIdAndActive(Long projectProductId, Boolean active);

	public Boolean existsByProjectProductIdAndCodeAndAccessIdAndActive(Long projectProductId, String code,
			Long accessId, Boolean active);

	public List<LicenseResponse> findByProjectProductIdAndActive(Long projectProductId, Boolean active);

	public LicenseResponse findResponseByIdAndActiveTrue(Long id);

	public List<LicenseResponse> findByActive(Boolean active);

	public List<LicenseResponse> findAllByActiveTrue();

	public LicenseResponse findByIdAndActiveTrue(Long unmaskId);

	public List<LicenseResponse> findByProjectProductIdAndActiveTrue(Long id);

	@Modifying
	@Query(value = "update License set generatedKey=?2, name=?3, modifiedBy.id=?4, modifiedAt=?5 where id =?1")
	public int update(Long unmaskId, String generatedKey, String name, Long userId, Date date);

	@Modifying
	@Query(value = "update License set active=?2, modifiedBy.id=?3, modifiedAt=?4 where id =?1")
	public int update(Long unmaskId, Boolean active, Long userId, Date date);

}
