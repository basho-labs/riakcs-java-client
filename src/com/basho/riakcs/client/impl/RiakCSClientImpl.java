/*
 * Copyright (c) 2012 Basho Technologies, Inc. All Rights Reserved. This file is provided to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.basho.riakcs.client.impl;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.json.JSONObject;

import com.basho.riakcs.client.api.RiakCSClient;
import com.basho.riakcs.client.api.RiakCSException;
import com.basho.riakcs.client.impl.RiakCSClientServiceImpl.UserListMode;

public class RiakCSClientImpl implements RiakCSClient {

	// Default Constructor for RiakCS
	// csEndpoint : Hostname:Port of RiakCS installation .. example: localhost:8080

	public RiakCSClientImpl(String csAccessKey, String csSecretKey, String csEndpoint, boolean useSSL) {
		csClient = new RiakCSClientServiceImpl(csAccessKey, csSecretKey, csEndpoint, useSSL);
	}

	// Default Constructor for Amazon S3
	//

	public RiakCSClientImpl(String csAccessKey, String csSecretKey) {
		csClient = new RiakCSClientServiceImpl(csAccessKey, csSecretKey);
	}

	/**
	 * {@inheritDoc}
	 */
	public void enableDebugOutput() {
		csClient.enableDebugOutput();
	}

	// User Admin APIs
	//

	/**
	 * {@inheritDoc}
	 */
	public JSONObject createUser(String fullname, String emailAddress) {
		// requires CS >= 1.2
		return csClient.createUser(fullname, emailAddress);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject listUsers() {
		// requires CS >= 1.2
		return csClient.listUsers(UserListMode.ALL);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject listEnabledUsers() {
		// requires CS >= 1.2
		return csClient.listUsers(UserListMode.ENABLED_ONLY);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject listDisabledUsers() {
		// requires CS >= 1.2
		return csClient.listUsers(UserListMode.DISABLED_ONLY);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject getUserInfo(String key_id) {
		// requires CS >= 1.2
		return csClient.getUserInfo(key_id);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject getMyUserInfo() {
		// requires CS >= 1.2
		return csClient.getMyUserInfo();
	}

	/**
	 * {@inheritDoc}
	 */
	public void disableUser(String key_id) {
		// requires CS >= 1.2
		csClient.disableUser(key_id);
	}

	/**
	 * {@inheritDoc}
	 */
	public void enableUser(String key_id) {
		// requires CS >= 1.2
		csClient.enableUser(key_id);
	}

	// Bucket APIs
	//

	/**
	 * {@inheritDoc}
	 */
	public void createBucket(String bucketName) {
		csClient.createBucket(bucketName);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject listBuckets() {
		return csClient.listBuckets();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isBucketAccessible(String bucketName) {
		return csClient.isBucketAccessible(bucketName);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject getACLForBucket(String bucketName) {
		return csClient.getACLForBucket(bucketName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteBucket(String bucketName) {
		csClient.deleteBucket(bucketName);
	}

	// Object APIs
	//

	/**
	 * {@inheritDoc}
	 */
	public void createObject(String bucketName, String objectKey, InputStream dataInputStream,
			Map<String, String> headers, Map<String, String> metadata) {
		csClient.createObject(bucketName, objectKey, dataInputStream, headers, metadata);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject listObjects(String bucketName) {
		// Can be slow with large number of objects
		return csClient.listObjects(bucketName);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject listObjectNames(String bucketName) {
		// Can be slow with large number of objects
		return csClient.listObjects(bucketName, false);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject getObject(String bucketName, String objectKey) {
		// Content gets returned as part of the JSONObject
		return csClient.getObject(bucketName, objectKey);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject getObject(String bucketName, String objectKey, OutputStream dataOutputStream) {
		// Content gets written into outputStream
		return csClient.getObject(bucketName, objectKey, dataOutputStream);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject getObjectInfo(String bucketName, String objectKey) {
		return csClient.getObjectInfo(bucketName, objectKey);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject getACLForObject(String bucketName, String objectKey) {
		return csClient.getACLForObject(bucketName, objectKey);
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteObject(String bucketName, String objectKey) {
		csClient.deleteObject(bucketName, objectKey);
	}

	// ACL APIs
	//

	/**
	 * {@inheritDoc}
	 */
	public void setCannedACLForBucket(String bucketName, String cannedACL) {
		csClient.setCannedACLForBucket(bucketName, cannedACL);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCannedACLForObject(String bucketName, String objectKey, String cannedACL) {
		csClient.setCannedACLForObject(bucketName, objectKey, cannedACL);
	}

	// "Regular" ACLs for buckets and objects
	public static enum Permission {
		READ, WRITE, READ_ACP, WRITE_ACP, FULL_CONTROL
	};

	/**
	 * {@inheritDoc}
	 */
	public void addAdditionalACLToBucket(String bucketName, String emailAddress, Permission permission) {
		csClient.addAdditionalACLToBucket(bucketName, emailAddress, permission);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addAdditionalACLToObject(String bucketName, String objectKey, String emailAddress, Permission permission) {
		csClient.addAdditionalACLToObject(bucketName, objectKey, emailAddress, permission);
	}

	// Statistic APIs
	//

	/**
	 * {@inheritDoc}
	 */
	public JSONObject getAccessStatistic(String keyForUser, int howManyHrsBack) {
		// requires CS >= 1.2
		return csClient.getAccessStatistic(keyForUser, howManyHrsBack);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSONObject getStorageStatistic(String keyForUser, int howManyHrsBack) {
		// requires CS >= 1.2
		return csClient.getStorageStatistic(keyForUser, howManyHrsBack);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean endpointIsS3() {
		return csClient.endpointIsS3();
	}

	// Tool APIs
	//

	/**
	 * {@inheritDoc}
	 */
	public void removeBucketAndContent(String bucketName) {
		csClient.removeContentOfBucket(bucketName);
		csClient.deleteBucket(bucketName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadContentOfDirectory(File fromDirectory, String toBucket) {
		if (csClient.isBucketAccessible(toBucket))
			throw new RiakCSException("Bucket already exists, choose different bucket name");

		csClient.createBucket(toBucket);
		csClient.uploadContentOfDirectory(fromDirectory, toBucket);
	}

	public static void copyBucketBetweenSystems(RiakCSClient fromSystem, String fromBucket, RiakCSClient toSystem,
			String toBucket) {
		if (fromSystem.isBucketAccessible(fromBucket) == false)
			throw new RiakCSException("Source Bucket doesn't exist");
		if (toSystem.isBucketAccessible(toBucket))
			throw new RiakCSException("Bucket already exists, choose different bucket name");

		toSystem.createBucket(toBucket);
		RiakCSClientServiceImpl.copyBucketBetweenSystems(fromSystem, fromBucket, toSystem, toBucket);
	}

	private RiakCSClientServiceImpl csClient = null;

}
