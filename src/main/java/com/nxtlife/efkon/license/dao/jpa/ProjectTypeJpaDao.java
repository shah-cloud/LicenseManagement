package com.nxtlife.efkon.license.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.efkon.license.entity.project.ProjectType;
import com.nxtlife.efkon.license.view.project.ProjectTypeResponse;

@Repository
public interface ProjectTypeJpaDao extends JpaRepository<ProjectType, Long> {

	public ProjectType findByName(String name);

	public Boolean existsByName(String name);

	public ProjectTypeResponse findResponseById(Long id);

}
