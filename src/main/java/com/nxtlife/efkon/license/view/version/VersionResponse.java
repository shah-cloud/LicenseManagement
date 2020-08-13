package com.nxtlife.efkon.license.view.version;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.license.entity.version.Version;
import com.nxtlife.efkon.license.view.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class VersionResponse implements Response {

	@Schema(description = "Id of the version")
	private Long id;

	@Schema(description = "Version of the product", example = "2.2")
	private String version;

	private Long productDetailId;

	public VersionResponse(Long id, String version) {
		super();
		this.id = id;
		this.version = version;
	}

	public Long getId() {
		return mask(id);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getProductDetailId() {
		return mask(productDetailId);
	}

	public void setProductDetailId(Long productDetailId) {
		this.productDetailId = productDetailId;
	}

	public static VersionResponse get(Version version) {
		if (version != null) {
			return new VersionResponse(version.getId(), version.getVersion());
		}
		return null;
	}

}
