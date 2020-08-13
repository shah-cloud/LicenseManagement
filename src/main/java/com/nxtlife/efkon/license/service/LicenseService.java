package com.nxtlife.efkon.license.service;

import java.util.List;

import com.nxtlife.efkon.license.view.license.LicenseRequest;
import com.nxtlife.efkon.license.view.license.LicenseResponse;

public interface LicenseService {

	// public LicenseResponse update(Long id, LicenseRequest request);

	public LicenseResponse findByLicenseId(Long licenseId);

	/**
	 * this method is used to fetch all the license details
	 * 
	 * @return list of <tt>LicenseResponse</tt>
	 */

	public List<LicenseResponse> findAll();

	/**
	 * this method is used to generate license key of product in a project
	 * 
	 * @param id
	 * @param request
	 * @return {@link LicenseResponse}}
	 */

	public LicenseResponse generateKey(Long id, LicenseRequest request);

	/**
	 * this method is used to replace the generated license key
	 * 
	 * @param id
	 * @return {@link LicenseResponse}}
	 */
	public LicenseResponse replaceGenerateKey(Long id);

	/**
	 * this method is used to find all the licenses of particular product in a
	 * project
	 * 
	 * @param projectId
	 * @param productId
	 * @return {@link LicenseResponse}
	 */

	public List<LicenseResponse> findByProjectIdandProductId(Long projectId, Long productId);
}
