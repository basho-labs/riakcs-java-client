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

	/**
	 * Create a new object
	 * 
	 * @param bucketName
	 *            name of the bucket to store data
	 * @param objectKey
	 *            unique key of the object
	 * @param dataInputStream
	 *            InputStream for the file to store
	 * @param headers
	 *            map of headers to set on request
	 * @param metadata
	 *            meta data to store with the object
	 */
	void createObject(String bucketName, String objectKey,
			InputStream dataInputStream, Map<String, String> headers,
			Map<String, String> metadata);

	/**
	 * list all objects in a bucket
	 * 
	 * @param bucketName
	 *            name of the bucket to list
	 * @return {@link JsonObject} with list of objects
	 */
	JsonObject listObjects(String bucketName);

	/**
	 * list all object names
	 * 
	 * @param bucketName
	 *            name of the bucket to list
	 * @return {@link JsonObject} with list of objects names
	 */
	JsonObject listObjectNames(String bucketName);

	/**
	 * retrieve a object
	 * <P>
	 * TODO update the response information
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param objectKey
	 *            key of the object
	 * @return {@link JsonObject} containing the object
	 */
	JsonObject getObject(String bucketName, String objectKey);

	/**
	 * retrieve a object and stream data to provided output stream
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param objectKey
	 *            key of the object
	 * @param dataOutputStream
	 *            output stream for data
	 * @return {@link JsonObject} containing the object
	 */
	JsonObject getObject(String bucketName, String objectKey,
			OutputStream dataOutputStream);

	/**
	 * Get the object information
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param objectKey
	 *            key of the object
	 * @return {@link JsonObject} containing the object
	 */
	JsonObject getObjectInfo(String bucketName, String objectKey);

	/**
	 * Delete a object
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param objectKey
	 *            key of the object
	 */
	void deleteObject(String bucketName, String objectKey);

	/**
	 * Get the Acls for a object
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param objectKey
	 *            key of the object
	 * @return {@link JsonObject} containing the object's ACL
	 */
	JsonObject getACLForObject(String bucketName, String objectKey);

	/**
	 * Set a canned ACL for a object
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param objectKey
	 *            key of the object
	 * @param cannedACL
	 *            canned ACL to set
	 */
	void setCannedACLForObject(String bucketName, String objectKey,
			String cannedACL);

	/**
	 * Add another ACL to a object
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param objectKey
	 *            key of the object
	 * @param emailAddress
	 *            email to set with ACL
	 * @param permission
	 *            {@link Permission} to set
	 */
	void addAdditionalACLToObject(String bucketName, String objectKey,
			String emailAddress, Permission permission);

}
