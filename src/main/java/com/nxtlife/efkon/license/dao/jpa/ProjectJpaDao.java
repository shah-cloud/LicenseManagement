package com.nxtlife.efkon.license.dao.jpa;

import com.nxtlife.efkon.license.entity.project.Project;
import com.nxtlife.efkon.license.view.project.ProjectResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectJpaDao extends JpaRepository<Project, Long> {

	public Boolean existsByIdAndActive(Long id, Boolean active);

	public Project findByCustomerName(String name);

	public ProjectResponse findResponseById(Long id);

	public List<ProjectResponse> findByActive(Boolean active);

	public List<ProjectResponse> findByCustomerEmailAndActive(String customerEmail, Boolean active);

	public List<ProjectResponse> findByProjectManagerIdAndActive(Long projectManagerId, Boolean active);

	public Boolean existsByCustomerContactNoAndActive(String customerContactNo, Boolean active);

}
