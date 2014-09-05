package com.basho.riakcs.client.api;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.json.JSONObject;

/**
 * Interface for RIAK CS object operations
 * <p>
 * TODO fill out rest of java docs when i know what in the JSON object
 */
public interface RiakCSObjectManagement {

	/**
	 * Save a object to RIAK CS
	 * 
	 * @param bucketName
	 *            name of the bucket to save to
	 * @param objectKey
	 *            unique key for resource
	 * @param dataInputStream
	 *            input stream of the object to store
	 * @param headers
	 *            Map of headers to save
	 * @param metadata
	 *            meta data to save with the object
	 */
	public abstract void createObject(String bucketName, String objectKey, InputStream dataInputStream,
			Map<String, String> headers, Map<String, String> metadata);

	public abstract JSONObject listObjects(String bucketName);

	public abstract JSONObject listObjectNames(String bucketName);

	public abstract JSONObject getObject(String bucketName, String objectKey);

	public abstract JSONObject getObject(String bucketName, String objectKey, OutputStream dataOutputStream);

	public abstract JSONObject getObjectInfo(String bucketName, String objectKey);

	public abstract JSONObject getACLForObject(String bucketName, String objectKey);

	public abstract void deleteObject(String bucketName, String objectKey);

}
