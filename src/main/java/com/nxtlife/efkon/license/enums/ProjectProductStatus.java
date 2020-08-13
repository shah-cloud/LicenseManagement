package com.nxtlife.efkon.license.enums;

public enum ProjectProductStatus {
	DRAFT, SUBMIT, REVIEWED, APPROVED, REJECT, RENEWED;

	public static boolean matches(String status) {
		for (ProjectProductStatus projectProductStatus : ProjectProductStatus.values()) {
			if (projectProductStatus.name().equals(status)) {
				return true;
			}
		}
		return false;
	}
}
