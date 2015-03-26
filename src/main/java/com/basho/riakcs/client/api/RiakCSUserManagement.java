package com.basho.riakcs.client.api;

import org.json.JSONObject;

/**
 * Interface for user management operations
 */
public interface RiakCSUserManagement {

	/**
	 * Disable a user
	 * 
	 * @param key_id
	 *            ID of the user to disable
	 */
	void disableUser(String key_id);

	/**
	 * Enable a user
	 * 
	 * @param key_id
	 *            ID of the user to enable
	 */
	void enableUser(String key_id);

	/**
	 * Create a new user
	 * 
	 * @param fullname
	 *            Full name of the user
	 * @param emailAddress
	 *            email of the user
	 * @return JSON object of the created user
	 */
	JSONObject createUser(String fullname, String emailAddress);

	/**
	 * List all users
	 * 
	 * @return JSON object with all users
	 */
	JSONObject listUsers();

	/**
	 * List all enabled users
	 * 
	 * @return JSON object with all enabled users
	 */
	JSONObject listEnabledUsers();

	/**
	 * List all disabled users
	 * 
	 * @return JSON object with all disabled users
	 */
	JSONObject listDisabledUsers();

	/**
	 * Get a users information by id
	 * 
	 * @param key_id
	 *            id of the user
	 * @return ID of user to get information
	 */
	JSONObject getUserInfo(String key_id);

	/**
	 * Get the current user information
	 * 
	 * @return JSON object with current user information
	 */
	JSONObject getMyUserInfo();

}
