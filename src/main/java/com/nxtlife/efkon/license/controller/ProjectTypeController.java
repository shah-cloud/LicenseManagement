package com.nxtlife.efkon.license.controller;

import com.nxtlife.efkon.license.ex.ApiError;
import com.nxtlife.efkon.license.service.ProjectTypeService;
import com.nxtlife.efkon.license.view.project.ProjectTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Project Type", description = "Project type api's for fetch the project type")
@RequestMapping("/api/")
public class ProjectTypeController {

    @Autowired
    private ProjectTypeService projectTypeService;

    @GetMapping(value = "project/types", produces = {"application/json"})
    @Operation(summary = "Find all project types", description = "return a list of project types", tags = {
            "Project", "Project Type"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project types successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectTypeResponse.class)))),
            @ApiResponse(responseCode = "403", description = "user don't have access to fetch project types", content = @Content(schema = @Schema(implementation = ApiError.class)))})
    public List<ProjectTypeResponse> findAll() {
        return projectTypeService.findAll();

    }

}
