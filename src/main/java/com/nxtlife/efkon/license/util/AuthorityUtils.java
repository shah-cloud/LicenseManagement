package com.nxtlife.efkon.license.util;

/**
 * <tt>AuthorityUtil</tt> contains all static <tt>Authority</tt>. It mainly used
 * with @Secured annotation. so who ever has that authority can access the code.
 * <p>
 * 
 * Add here whenever we added a new authority for user.
 * <p>
 * 
 * 
 * @author ajay
 *
 */
public final class AuthorityUtils {

	/**
	 * AUTHORITY TO FETCH AUTHORITY
	 */
	public static final String AUTHORITY_FETCH = "ROLE_AUTHORITY_FETCH";

	/**
	 * AUTHORITY TO FETCH ROLE
	 */
	public static final String ROLE_FETCH = "ROLE_ROLE_FETCH";

	/**
	 * AUTHORITY TO CREATE ROLE
	 */
	public static final String ROLE_CREATE = "ROLE_ROLE_CREATE";

	/**
	 * AUTHORITY TO UPDATE ROLE
	 */
	public static final String ROLE_UPDATE = "ROLE_ROLE_UPDATE";

	/**
	 * AUTHORITY TO DELETE ROLE
	 */
	public static final String ROLE_DELETE = "ROLE_ROLE_DELETE";

	/**
	 * AUTHORITY TO FETCH USER
	 */
	public static final String USER_FETCH = "ROLE_USER_FETCH";

	/**
	 * AUTHORITY TO CREATE USER
	 */
	public static final String USER_CREATE = "ROLE_USER_CREATE";

	/**
	 * AUTHORITY TO UPDATE USER
	 */
	public static final String USER_UPDATE = "ROLE_USER_UPDATE";

	/**
	 * AUTHORITY TO DELETE USER
	 */
	public static final String USER_DELETE = "ROLE_USER_DELETE";

	/**
	 * AUTHORITY TO FETCH PROJECT TYPE
	 */
	public static final String PROJECT_TYPE_FETCH = "ROLE_PROJECT_TYPE_FETCH";

	/**
	 * AUTHORITY TO FETCH PROJECT
	 */
	public static final String PROJECT_FETCH = "ROLE_PROJECT_FETCH";

	/**
	 * AUTHORITY TO CREATE PROJECT
	 */
	public static final String PROJECT_CREATE = "ROLE_PROJECT_CREATE";

	/**
	 * AUTHORITY TO UPDATE PROJECT
	 */
	public static final String PROJECT_UPDATE = "ROLE_PROJECT_UPDATE";

	/**
	 * AUTHORITY TO DELETE PROJECT
	 */
	public static final String PROJECT_DELETE = "ROLE_PROJECT_DELETE";

	/**
	 * AUTHORITY TO FETCH PRODUCT FAMILY
	 */
	public static final String PRODUCT_FAMILY_FETCH = "ROLE_PRODUCT_FAMILY_FETCH";

	/**
	 * AUTHORITY TO CREATE PRODUCT FAMILY
	 */
	public static final String PRODUCT_FAMILY_CREATE = "ROLE_PRODUCT_FAMILY_CREATE";

	/**
	 * AUTHORITY TO UPDATE PRODUCT FAMILY
	 */
	public static final String PRODUCT_FAMILY_UPDATE = "ROLE_PRODUCT_FAMILY_UPDATE";

	/**
	 * AUTHORITY TO DELETE PRODUCT FAMILY
	 */
	public static final String PRODUCT_FAMILY_DELETE = "ROLE_PRODUCT_FAMILY_DELETE";

	/**
	 * AUTHORITY TO FETCH PRODUCT CODE
	 */
	public static final String PRODUCT_CODE_FETCH = "ROLE_PRODUCT_CODE_FETCH";

	/**
	 * AUTHORITY TO FETCH VERSION
	 */
	public static final String VERSION_FETCH = "ROLE_VERSION_FETCH";

	/**
	 * AUTHORITY TO CREATE VERSION
	 */
	public static final String VERSION_CREATE = "ROLE_VERSION_CREATE";

	/**
	 * AUTHORITY TO UPDATE VERSION
	 */
	public static final String VERSION_UPDATE = "ROLE_VERSION_UPDATE";

	/**
	 * AUTHORITY TO DELETE VERSION
	 */
	public static final String VERSION_DELETE = "ROLE_VERSION_DELETE";

	/**
	 * AUTHORITY TO FETCH PRODUCT DETAIL
	 */
	public static final String PRODUCT_DETAIL_FETCH = "ROLE_PRODUCT_DETAIL_FETCH";

	/**
	 * AUTHORITY TO CREATE PRODUCT DETAIL
	 */
	public static final String PRODUCT_DETAIL_CREATE = "ROLE_PRODUCT_DETAIL_CREATE";

	/**
	 * AUTHORITY TO UPDATE PRODUCT DETAIL
	 */
	public static final String PRODUCT_DETAIL_UPDATE = "ROLE_PRODUCT_DETAIL_UPDATE";

	/**
	 * AUTHORITY TO DELETE PRODUCT DETAIL
	 */
	public static final String PRODUCT_DETAIL_DELETE = "ROLE_PRODUCT_DETAIL_DELETE";

	/**
	 * AUTHORITY TO FETCH PROJECT PRODUCT
	 */
	public static final String PROJECT_PRODUCT_FETCH = "ROLE_PROJECT_PRODUCT_FETCH";

	/**
	 * AUTHORITY TO CREATE PROJECT PRODUCT
	 */
	public static final String PROJECT_PRODUCT_CREATE = "ROLE_PROJECT_PRODUCT_CREATE";

	/**
	 * AUTHORITY TO UPDATE PROJECT PRODUCT
	 */
	public static final String PROJECT_PRODUCT_UPDATE = "ROLE_PROJECT_PRODUCT_UPDATE";

	/**
	 * AUTHORITY TO REVIEW PROJECT PRODUCT
	 */
	public static final String PROJECT_PRODUCT_REVIEW = "ROLE_PROJECT_PRODUCT_REVIEW";

	/**
	 * AUTHORITY TO APPROVE PROJECT PRODUCT
	 */
	public static final String PROJECT_PRODUCT_APPROVE = "ROLE_PROJECT_PRODUCT_APPROVE";

	/**
	 * AUTHORITY TO SUBMIT PROJECT PRODUCT
	 */
	public static final String PROJECT_PRODUCT_SUBMIT = "ROLE_PROJECT_PRODUCT_SUBMIT";

	/**
	 * AUTHORITY TO RENEW PROJECT PRODUCT
	 */
	public static final String PROJECT_PRODUCT_RENEW = "ROLE_PROJECT_PRODUCT_RENEW";

	/**
	 * AUTHORITY TO DELETE PROJECT PRODUCT
	 */
	public static final String PROJECT_PRODUCT_DELETE = "ROLE_PROJECT_PRODUCT_DELETE";

	/**
	 * AUTHORITY TO FETCH LICENSE
	 */
	public static final String LICENSE_FETCH = "ROLE_LICENSE_FETCH";

	/**
	 * AUTHORITY TO UPDATE LICENSE
	 */
	public static final String LICENSE_UPDATE = "ROLE_LICENSE_UPDATE";

	/**
	 * AUTHORITY TO UPDATE LICENSE TYPE
	 */
	public static final String LICENSE_TYPE_UPDATE = "ROLE_LICENSE_TYPE_UPDATE";

}
