package com.basho.riakcs.client.api;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.json.JSONObject;

/**
 * Interface for RIAK CS object operations
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
	void createObject(String bucketName, String objectKey, InputStream dataInputStream, Map<String, String> headers,
			Map<String, String> metadata);

	/**
	 * List all objects in a bucket
	 * 
	 * @param bucketName
	 *            name of bucket to list objects
	 * @return JSON object with all objects belonging to the bucket
	 */
	JSONObject listObjects(String bucketName);

	/**
	 * List the names of the objects in a bucket
	 * 
	 * @param bucketName
	 *            name of bucket to list all object names
	 * @return JSON object with all objects names belonging to the bucket
	 */
	JSONObject listObjectNames(String bucketName);

	/**
	 * Get a object by ID
	 * 
	 * @param bucketName
	 *            name of the bucket object resides in
	 * @param objectKey
	 *            ID of the object to retrieve
	 * @return JSON object representing the object
	 */
	JSONObject getObject(String bucketName, String objectKey);

	/**
	 * Get a object by ID
	 * 
	 * @param bucketName
	 *            name of the bucket object resides in
	 * @param objectKey
	 *            ID of the object to retrieve
	 * @param dataOutputStream
	 *            The OutputStream to stream the file to
	 * @return JSON object representing the object
	 */
	JSONObject getObject(String bucketName, String objectKey, OutputStream dataOutputStream);

	/**
	 * Get a objects information by ID
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param objectKey
	 *            ID of the object
	 * @return JSON object representing the object information
	 */
	JSONObject getObjectInfo(String bucketName, String objectKey);

	/**
	 * Get the ACL for a specific object
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param objectKey
	 *            ID of the object
	 * @return JSON object representing the ACL information
	 */
	JSONObject getACLForObject(String bucketName, String objectKey);

	/**
	 * Delete a object
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param objectKey
	 *            ID of the object to delete
	 */
	void deleteObject(String bucketName, String objectKey);

}
