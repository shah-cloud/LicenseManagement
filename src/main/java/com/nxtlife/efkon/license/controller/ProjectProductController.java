package com.nxtlife.efkon.license.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.license.enums.ProjectProductStatus;
import com.nxtlife.efkon.license.ex.ApiError;
import com.nxtlife.efkon.license.service.ProjectProductService;
import com.nxtlife.efkon.license.util.AuthorityUtils;
import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.project.product.ProjectProductCommentRequest;
import com.nxtlife.efkon.license.view.project.product.ProjectProductRequest;
import com.nxtlife.efkon.license.view.project.product.ProjectProductResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Project Product", description = "Project product api's for fetch,save,update and delete the products")
@RequestMapping("/api/")
public class ProjectProductController {

	@Autowired
	private ProjectProductService projectProductService;

	@PostMapping(value = "project/product", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Save product in project ", description = "return project product response after saved the products in project", tags = {
			"Project", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product response after successfully saved the product in project", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save product in project", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or project/product not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectProductResponse save(@Valid @RequestBody ProjectProductRequest request) {
		return projectProductService.save(request);

	}

	@PutMapping(value = "project/product/{id}", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "update product and license detail ", description = "return project product response after updated details", tags = {
			"Project", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product response after successfully update the product details in project", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update product details in project", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project product id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or project/product not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectProductResponse update(@PathVariable Long id, @Valid @RequestBody ProjectProductRequest request) {
		return projectProductService.update(id, request);
	}

	@PutMapping(value = "project/product/{id}/approve", produces = { "application/json" }, consumes = {
			"application/json" })
	@Operation(summary = "update product status to approve ", description = "return project product response after updated details", tags = {
			"Project", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product response after successfully update the product details in project", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update product details in project", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project product id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or project/product not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@Secured(AuthorityUtils.PROJECT_PRODUCT_APPROVE)
	public ProjectProductResponse approve(@PathVariable Long id, @RequestBody ProjectProductCommentRequest request) {
		return projectProductService.updateStatus(id, ProjectProductStatus.APPROVED, request.getComment());
	}

	@PutMapping(value = "project/product/{id}/submit", produces = { "application/json" }, consumes = {
			"application/json" })
	@Operation(summary = "update product status to submit ", description = "return project product response after updated details", tags = {
			"Project", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product response after successfully update the product details in project", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update product details in project", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project product id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or project/product not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@Secured(AuthorityUtils.PROJECT_PRODUCT_SUBMIT)
	public ProjectProductResponse submit(@PathVariable Long id, @RequestBody ProjectProductCommentRequest request) {
		return projectProductService.updateStatus(id, ProjectProductStatus.SUBMIT, request.getComment());
	}

	@PutMapping(value = "project/product/{id}/review", produces = { "application/json" }, consumes = {
			"application/json" })
	@Operation(summary = "update product status to review ", description = "return project product response after updated details", tags = {
			"Project", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product response after successfully update the product details in project", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update product details in project", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project product id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or project/product not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@Secured(AuthorityUtils.PROJECT_PRODUCT_REVIEW)
	public ProjectProductResponse review(@PathVariable Long id, @RequestBody ProjectProductCommentRequest request) {
		return projectProductService.updateStatus(id, ProjectProductStatus.REVIEWED, request.getComment());
	}

	@PutMapping(value = "project/product/{id}/reject", produces = { "application/json" }, consumes = {
			"application/json" })
	@Operation(summary = "update product status to reject ", description = "return project product response after updated details", tags = {
			"Project", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product response after successfully update the product details in project", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update product details in project", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project product id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or project/product not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	@Secured({ AuthorityUtils.PROJECT_PRODUCT_REVIEW, AuthorityUtils.PROJECT_PRODUCT_APPROVE })
	public ProjectProductResponse reject(@PathVariable Long id, @RequestBody ProjectProductCommentRequest request) {
		return projectProductService.updateStatus(id, ProjectProductStatus.REJECT, request.getComment());
	}

	@PutMapping(value = "project/product/{id}/renew", produces = { "application/json" }, consumes = {
			"application/json" })
	@Operation(summary = "Renew project product ", description = "return project product response after renew details", tags = {
			"Project", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product response after successfully renew the product details in project", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductResponse.class))),
			@ApiResponse(description = "If user doesn't have access to renew product details in project", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project product id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or project/product not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectProductResponse renew(@PathVariable Long id, @RequestBody ProjectProductRequest request) {
		return projectProductService.renew(id, request);
	}

	@GetMapping(value = "project/product/{id}", produces = { "application/json" })
	@Operation(summary = "fetch product and license detail ", description = "return project product response", tags = {
			"Project", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "Project Product response", responseCode = "200", content = @Content(schema = @Schema(implementation = ProjectProductResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update product details in project", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project product id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If required field are not filled properly or project/product not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public ProjectProductResponse findById(@PathVariable Long id) {
		return projectProductService.findById(id);
	}

	@GetMapping(value = "project/product", produces = { "application/json" })
	@Operation(summary = "Find all projects", description = "return a list of projects", tags = { "Project" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Project product  successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectProductResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch project products", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<ProjectProductResponse> findAll() {
		return projectProductService.findAll();

	}

	@GetMapping(value = "project/{projectId}/product", produces = { "application/json" })
	@Operation(summary = "Find all project types", description = "return a list of project types", tags = { "Project" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Project product  successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectProductResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch project products", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<ProjectProductResponse> findByProjectId(@PathVariable Long projectId) {
		return projectProductService.findByProjectId(projectId);

	}

	@DeleteMapping(value = "project/product/{id}", produces = { "application/json" })
	@Operation(summary = "Delete product in a project ", description = "return success response after successfully deleting the product in a project", tags = {
			"Project", "Project Product" })
	@ApiResponses(value = {
			@ApiResponse(description = "product successfully deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to delete product in project", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project product id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(@PathVariable Long id) {
		return projectProductService.delete(id);
	}
}
