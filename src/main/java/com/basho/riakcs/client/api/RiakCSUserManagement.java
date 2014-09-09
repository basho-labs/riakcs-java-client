package com.basho.riakcs.client.api;

import org.json.JSONObject;

public interface RiakCSUserManagement {

	public abstract void disableUser(String key_id);

	public abstract void enableUser(String key_id);

	public abstract JSONObject createUser(String fullname, String emailAddress);

	public abstract JSONObject listUsers();

	public abstract JSONObject listEnabledUsers();

	public abstract JSONObject listDisabledUsers();

	public abstract JSONObject getUserInfo(String key_id);

	public abstract JSONObject getMyUserInfo();

}
