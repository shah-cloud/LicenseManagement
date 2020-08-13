package com.nxtlife.efkon.license.service;

import com.nxtlife.efkon.license.view.project.ProjectTypeResponse;

import java.util.List;


public interface ProjectTypeService {


    /**
     * this method used to fetch all projectTypes
     *
     * @return list of <tt>ProjectTypeResponse</tt>
     */
    public List<ProjectTypeResponse> findAll();

}
