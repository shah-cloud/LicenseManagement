package com.nxtlife.efkon.license.service;

import java.util.List;

import com.nxtlife.efkon.license.view.SuccessResponse;
import com.nxtlife.efkon.license.view.license.LicenseTypeResponse;

public interface LicenseTypeService {

	/**
	 * this method used to fetch license types
	 * 
	 * @return list of {@link LicenseTypeResponse}
	 */
	public List<LicenseTypeResponse> findAll();

	/**
	 * this method used to update license type month detail
	 * 
	 * @param id
	 * @param monthCount
	 * @return success message if updated successfully
	 */
	public SuccessResponse update(Long id, Integer monthCount);

}
