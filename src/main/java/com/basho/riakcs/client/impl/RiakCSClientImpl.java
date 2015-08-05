/*
 * Copyright (c) 2012 Basho Technologies, Inc.  All Rights Reserved.
 * 
 * This file is provided to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.basho.riakcs.client.impl;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.basho.riakcs.client.api.RiakCSClient;
import com.basho.riakcs.client.api.RiakCSException;
import com.basho.riakcs.client.impl.RiakCSClientServiceImpl.UserListMode;
import com.google.gson.JsonObject;

public class RiakCSClientImpl implements RiakCSClient {

	private RiakCSClientServiceImpl csClient = null;

	// Default Constructor for RiakCS
	// csEndpoint : Hostname:Port of RiakCS installation .. example:
	// localhost:8080

	public RiakCSClientImpl(String csAccessKey, String csSecretKey,
			String csEndpoint, boolean useSSL) {
		csClient = new RiakCSClientServiceImpl(csAccessKey, csSecretKey,
				csEndpoint, useSSL);
	}

	// Default Constructor for Amazon S3
	//

	public RiakCSClientImpl(String csAccessKey, String csSecretKey) {
		csClient = new RiakCSClientServiceImpl(csAccessKey, csSecretKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basho.riakcs.client.api.RiakCSClient#enableDebugOutput()
	 */
	public void enableDebugOutput() {
		csClient.enableDebugOutput();
	}

	// User Admin APIs
	//

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#createUser(java.lang.String,
	 * java.lang.String)
	 */
	public JsonObject createUser(String fullname, String emailAddress) {
		// requires CS >= 1.2
		return csClient.createUser(fullname, emailAddress);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basho.riakcs.client.api.RiakCSClient#listUsers()
	 */
	public JsonObject listUsers() {
		// requires CS >= 1.2
		return csClient.listUsers(UserListMode.ALL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basho.riakcs.client.api.RiakCSClient#listEnabledUsers()
	 */
	public JsonObject listEnabledUsers() {
		// requires CS >= 1.2
		return csClient.listUsers(UserListMode.ENABLED_ONLY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basho.riakcs.client.api.RiakCSClient#listDisabledUsers()
	 */
	public JsonObject listDisabledUsers() {
		// requires CS >= 1.2
		return csClient.listUsers(UserListMode.DISABLED_ONLY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#getUserInfo(java.lang.String)
	 */
	public JsonObject getUserInfo(String key_id) {
		// requires CS >= 1.2
		return csClient.getUserInfo(key_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basho.riakcs.client.api.RiakCSClient#getMyUserInfo()
	 */
	public JsonObject getMyUserInfo() {
		// requires CS >= 1.2
		return csClient.getMyUserInfo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#disableUser(java.lang.String)
	 */
	public void disableUser(String key_id) {
		// requires CS >= 1.2
		csClient.disableUser(key_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#enableUser(java.lang.String)
	 */
	public void enableUser(String key_id) {
		// requires CS >= 1.2
		csClient.enableUser(key_id);
	}

	// Bucket APIs
	//

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#createBucket(java.lang.String)
	 */
	public void createBucket(String bucketName) {
		csClient.createBucket(bucketName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basho.riakcs.client.api.RiakCSClient#listBuckets()
	 */
	public JsonObject listBuckets() {
		return csClient.listBuckets();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#isBucketAccessible(java.lang
	 * .String)
	 */
	public boolean isBucketAccessible(String bucketName) {
		return csClient.isBucketAccessible(bucketName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#getACLForBucket(java.lang.String
	 * )
	 */
	public JsonObject getACLForBucket(String bucketName) {
		return csClient.getACLForBucket(bucketName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#deleteBucket(java.lang.String)
	 */
	public void deleteBucket(String bucketName) {
		csClient.deleteBucket(bucketName);
	}

	// Object APIs
	//

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#createObject(java.lang.String,
	 * java.lang.String, java.io.InputStream, java.util.Map, java.util.Map)
	 */
	public void createObject(String bucketName, String objectKey,
			InputStream dataInputStream, Map<String, String> headers,
			Map<String, String> metadata) {
		csClient.createObject(bucketName, objectKey, dataInputStream, headers,
				metadata);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#listObjects(java.lang.String)
	 */
	public JsonObject listObjects(String bucketName) {
		// Can be slow with large number of objects
		return csClient.listObjects(bucketName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#listObjectNames(java.lang.String
	 * )
	 */
	public JsonObject listObjectNames(String bucketName) {
		// Can be slow with large number of objects
		return csClient.listObjects(bucketName, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basho.riakcs.client.api.RiakCSClient#getObject(java.lang.String,
	 * java.lang.String)
	 */
	public JsonObject getObject(String bucketName, String objectKey) {
		// Content gets returned as part of the JsonObject
		return csClient.getObject(bucketName, objectKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basho.riakcs.client.api.RiakCSClient#getObject(java.lang.String,
	 * java.lang.String, java.io.OutputStream)
	 */
	public JsonObject getObject(String bucketName, String objectKey,
			OutputStream dataOutputStream) {
		// Content gets written into outputStream
		return csClient.getObject(bucketName, objectKey, dataOutputStream);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#getObjectInfo(java.lang.String,
	 * java.lang.String)
	 */
	public JsonObject getObjectInfo(String bucketName, String objectKey) {
		return csClient.getObjectInfo(bucketName, objectKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#getACLForObject(java.lang.String
	 * , java.lang.String)
	 */
	public JsonObject getACLForObject(String bucketName, String objectKey) {
		return csClient.getACLForObject(bucketName, objectKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#deleteObject(java.lang.String,
	 * java.lang.String)
	 */
	public void deleteObject(String bucketName, String objectKey) {
		csClient.deleteObject(bucketName, objectKey);
	}

	// ACL APIs
	//

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#setCannedACLForBucket(java.lang
	 * .String, java.lang.String)
	 */
	public void setCannedACLForBucket(String bucketName, String cannedACL) {
		csClient.setCannedACLForBucket(bucketName, cannedACL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#setCannedACLForObject(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	public void setCannedACLForObject(String bucketName, String objectKey,
			String cannedACL) {
		csClient.setCannedACLForObject(bucketName, objectKey, cannedACL);
	}

	// "Regular" ACLs for buckets and objects
	public static enum Permission {
		READ, WRITE, READ_ACP, WRITE_ACP, FULL_CONTROL
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#addAdditionalACLToBucket(java
	 * .lang.String, java.lang.String,
	 * com.basho.riakcs.client.api.RiakCSClientImpl.Permission)
	 */
	public void addAdditionalACLToBucket(String bucketName,
			String emailAddress, Permission permission) {
		csClient.addAdditionalACLToBucket(bucketName, emailAddress, permission);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#addAdditionalACLToObject(java
	 * .lang.String, java.lang.String, java.lang.String,
	 * com.basho.riakcs.client.api.RiakCSClientImpl.Permission)
	 */
	public void addAdditionalACLToObject(String bucketName, String objectKey,
			String emailAddress, Permission permission) {
		csClient.addAdditionalACLToObject(bucketName, objectKey, emailAddress,
				permission);
	}

	// Statistic APIs
	//

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#getAccessStatistic(java.lang
	 * .String, int)
	 */
	public JsonObject getAccessStatistic(String keyForUser, int howManyHrsBack) {
		// requires CS >= 1.2
		return csClient.getAccessStatistic(keyForUser, howManyHrsBack);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#getStorageStatistic(java.lang
	 * .String, int)
	 */
	public JsonObject getStorageStatistic(String keyForUser, int howManyHrsBack) {
		// requires CS >= 1.2
		return csClient.getStorageStatistic(keyForUser, howManyHrsBack);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.basho.riakcs.client.api.RiakCSClient#endpointIsS3()
	 */
	public boolean endpointIsS3() {
		return csClient.endpointIsS3();
	}

	// Tool APIs
	//

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#removeBucketAndContent(java.
	 * lang.String)
	 */
	public void removeBucketAndContent(String bucketName) {
		csClient.removeContentOfBucket(bucketName);
		csClient.deleteBucket(bucketName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.basho.riakcs.client.api.RiakCSClient#uploadContentOfDirectory(java
	 * .io.File, java.lang.String)
	 */
	public void uploadContentOfDirectory(File fromDirectory, String toBucket) {
		if (csClient.isBucketAccessible(toBucket))
			throw new RiakCSException(
					"Bucket already exists, choose different bucket name");

		csClient.createBucket(toBucket);
		csClient.uploadContentOfDirectory(fromDirectory, toBucket);
	}

	public static void copyBucketBetweenSystems(RiakCSClient fromSystem,
			String fromBucket, RiakCSClient toSystem, String toBucket) {
		if (fromSystem.isBucketAccessible(fromBucket) == false)
			throw new RiakCSException("Source Bucket doesn't exist");
		if (toSystem.isBucketAccessible(toBucket))
			throw new RiakCSException(
					"Bucket already exists, choose different bucket name");

		toSystem.createBucket(toBucket);
		RiakCSClientServiceImpl.copyBucketBetweenSystems(fromSystem,
				fromBucket, toSystem, toBucket);
	}

}
