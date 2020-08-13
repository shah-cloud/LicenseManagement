package com.nxtlife.efkon.license.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.license.ex.ApiError;
import com.nxtlife.efkon.license.service.LicenseService;
import com.nxtlife.efkon.license.view.license.LicenseRequest;
import com.nxtlife.efkon.license.view.license.LicenseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "License", description = "License api's for fetch,save,update and delete the license")
@RequestMapping("/api/")
public class LicenseController {

	@Autowired
	private LicenseService licenseService;

	@PutMapping(value = "license/{id}/replace", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "replace generated license key of product", description = "return license resonse after generating license key of project product", tags = {
			"license" })
	@ApiResponses(value = {
			@ApiResponse(description = "license response after successfully replace the license key of product in a project", responseCode = "200", content = @Content(schema = @Schema(implementation = LicenseResponse.class))),
			@ApiResponse(description = "If user doesn't have access to replace license key of product in project", responseCode = "403", content = @Content(schema = @Schema(implementation = LicenseResponse.class))),
			@ApiResponse(description = "if license id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = LicenseResponse.class))),
			@ApiResponse(description = "if required fileds are not filled properly or license doesn't exist", responseCode = "404", content = @Content(schema = @Schema(implementation = LicenseResponse.class))) })
	public LicenseResponse replaceGenerateKey(@PathVariable Long id) {
		return licenseService.replaceGenerateKey(id);

	}

	@PutMapping(value = "license/{id}/generate-key", produces = { "application/json" }, consumes = {
			"application/json" })
	@Operation(summary = "generate license key of product", description = "return license resonse after generating license key of project product", tags = {
			"license" })
	@ApiResponses(value = {
			@ApiResponse(description = "license response after successfully generate the license key of product in a project", responseCode = "200", content = @Content(schema = @Schema(implementation = LicenseResponse.class))),
			@ApiResponse(description = "If user doesn't have access to generate license key of product in project", responseCode = "403", content = @Content(schema = @Schema(implementation = LicenseResponse.class))),
			@ApiResponse(description = "if license id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = LicenseResponse.class))),
			@ApiResponse(description = "if required fileds are not filled properly or license doesn't exist", responseCode = "404", content = @Content(schema = @Schema(implementation = LicenseResponse.class))) })
	public LicenseResponse generateKey(@PathVariable Long id, @Valid @RequestBody LicenseRequest request) {
		return licenseService.generateKey(id, request);

	}

	@GetMapping(value = "licenses", produces = { "application/json" })
	@Operation(summary = "Find all license", description = "return a list of license", tags = { "License" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "License successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch license", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LicenseResponse> findAll() {
		return licenseService.findAll();

	}

	@GetMapping(value = "project/{projectId}/product/{productId}/licenses", produces = { "application/json" })
	@Operation(summary = "Find all license", description = "return a list of license", tags = { "License" })
	@ApiResponses(value = {
			@ApiResponse(description = "List of license", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch license details ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If project id or product detail id is incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LicenseResponse> findByProjectIdandProductId(@PathVariable Long projectId,
			@PathVariable Long productId) {
		return licenseService.findByProjectIdandProductId(projectId, productId);
	}
}
