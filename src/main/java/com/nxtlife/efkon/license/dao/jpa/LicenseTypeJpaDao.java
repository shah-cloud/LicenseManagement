package com.nxtlife.efkon.license.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.license.entity.license.LicenseType;

public interface LicenseTypeJpaDao extends JpaRepository<LicenseType, Long> {

	public Boolean existsByName(String name);

	@Query(value = "select name from LicenseType where id =?1")
	public String findNameById(Long id);

}
