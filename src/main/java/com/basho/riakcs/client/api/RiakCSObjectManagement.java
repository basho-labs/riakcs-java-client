package com.basho.riakcs.client.api;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.basho.riakcs.client.impl.RiakCSClientImpl.Permission;
import com.google.gson.JsonObject;

/**
 * Interface for RIAK CS object operations
 */
public interface RiakCSObjectManagement {

	void createObject(String bucketName, String objectKey,
			InputStream dataInputStream, Map<String, String> headers,
			Map<String, String> metadata);

	JsonObject listObjects(String bucketName);

	JsonObject listObjectNames(String bucketName);

	JsonObject getObject(String bucketName, String objectKey);

	JsonObject getObject(String bucketName, String objectKey,
			OutputStream dataOutputStream);

	JsonObject getObjectInfo(String bucketName, String objectKey);

	void deleteObject(String bucketName, String objectKey);

	JsonObject getACLForObject(String bucketName, String objectKey);

	void setCannedACLForObject(String bucketName, String objectKey,
			String cannedACL);

	void addAdditionalACLToObject(String bucketName, String objectKey,
			String emailAddress, Permission permission);

}
