package com.basho.riakcs.client.api;

import com.google.gson.JsonObject;

/**
 * Interface for user management operations
 */
public interface RiakCSUserManagement {

	/**
	 * Create a user with specified email address
	 * 
	 * @param fullname
	 *            full name of the user
	 * @param emailAddress
	 *            email of the user
	 * @return {@link JsonObject} with user information
	 */
	JsonObject createUser(String fullname, String emailAddress);

	JsonObject listUsers();

	JsonObject listEnabledUsers();

	JsonObject listDisabledUsers();

	JsonObject getUserInfo(String key_id);

	JsonObject getMyUserInfo();

	void disableUser(String key_id);

	void enableUser(String key_id);

}
