package com.nxtlife.efkon.license.service.impl;

import com.nxtlife.efkon.license.dao.jpa.ProjectTypeJpaDao;
import com.nxtlife.efkon.license.entity.project.ProjectType;
import com.nxtlife.efkon.license.service.ProjectTypeService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.project.ProjectTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("projectTypeServiceImpl")
@Transactional
public class ProjectTypeServiceImpl implements ProjectTypeService {

    @Autowired
    private ProjectTypeJpaDao projectTypeDao;

    @PostConstruct
    private void init() {
        List<ProjectType> projectTypes = new ArrayList<>();
        if (!projectTypeDao.existsByName("Traffic Control")) {
            projectTypes.add(new ProjectType("Traffic Control"));
        }
        if (!projectTypeDao.existsByName("Swach Bharat")) {
            projectTypes.add(new ProjectType("Swach Bharat"));
        }
        projectTypeDao.saveAll(projectTypes);
    }

    @Override
    @Secured(AuthorityUtils.PROJECT_TYPE_FETCH)
    public List<ProjectTypeResponse> findAll() {
        return projectTypeDao.findAll().stream().map(ProjectTypeResponse::new).collect(Collectors.toList());
    }

}
