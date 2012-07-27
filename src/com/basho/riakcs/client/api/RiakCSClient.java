/*
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
package com.basho.riakcs.client.api;

import java.io.*;
import java.util.*;

import org.json.*;

import com.basho.riakcs.client.impl.*;

public class RiakCSClient
{
	
	//	Default Constructor for RiakCS
	//	csEndpoint : Hostname:Port of RiakCS installation .. example: localhost:8080

	public RiakCSClient(String csAccessKey, String csSecretKey, String csEndpoint, boolean useSSL)
	{
		csClient= new RiakCSClientImpl(csAccessKey, csSecretKey, csEndpoint, useSSL);
	}

	// Default Constructor for Amazon S3
	//

	public RiakCSClient(String csAccessKey, String csSecretKey)
	{
		csClient= new RiakCSClientImpl(csAccessKey, csSecretKey);
	}

	
	public void enableDebugOutput()
	{
		csClient.enableDebugOutput();
	}


	// User Admin APIs
	//

	public JSONObject createUser(String fullname, String emailAddress) throws RiakCSException
	{
		// requires CS >= 1.2
		return csClient.createUser(fullname, emailAddress);			
	}

	public JSONObject listUsers() throws RiakCSException
	{
		// requires CS >= 1.2
		return csClient.listUsers();
	}

	public JSONObject getUserInfo(String key_id) throws RiakCSException
	{
		// requires CS >= 1.2
		return csClient.getUserInfo(key_id);
	}

	public JSONObject getMyUserInfo() throws RiakCSException
	{
		// requires CS >= 1.2
		return csClient.getMyUserInfo();
	}

	
	// Bucket APIs
	//

	public void createBucket(String bucketName) throws RiakCSException
	{
		csClient.createBucket(bucketName);
	}

	public JSONObject listBuckets() throws RiakCSException
	{
		return csClient.listBuckets();
	}

	public boolean isBucketAccessible(String bucketName) throws RiakCSException
	{
		return csClient.isBucketAccessible(bucketName);
	}
	
	public JSONObject getACLForBucket(String bucketName) throws RiakCSException
	{
		return csClient.getACLForBucket(bucketName);
	}

	public void deleteBucket(String bucketName) throws RiakCSException
	{
		csClient.deleteBucket(bucketName);
	}

	
	// Object APIs
	//

	public void createObject(String bucketName, String objectKey, InputStream dataInputStream, Map<String, String> headers, Map<String, String> metadata) throws RiakCSException
	{
		csClient.createObject(bucketName, objectKey, dataInputStream, headers, metadata);
	}
	
	public JSONObject listObjects(String bucketName) throws RiakCSException
	{
		//Can be slow with large number of objects
		return csClient.listObjects(bucketName);
	}
	
	public JSONObject listObjectNames(String bucketName) throws RiakCSException
	{
		//Can be slow with large number of objects
		return csClient.listObjects(bucketName, false);
	}
	
	public JSONObject getObject(String bucketName, String objectKey) throws RiakCSException
	{
		// Content gets returned as part of the JSONObject
		return csClient.getObject(bucketName, objectKey);
	}
	
	public JSONObject getObject(String bucketName, String objectKey, OutputStream dataOutputStream) throws RiakCSException
	{
		// Content gets written into outputStream
		return csClient.getObject(bucketName, objectKey, dataOutputStream);
	}

	public JSONObject getObjectInfo(String bucketName, String objectKey) throws RiakCSException
	{
		return csClient.getObjectInfo(bucketName, objectKey);
	}
	
	public JSONObject getACLForObject(String bucketName, String objectKey) throws RiakCSException
	{
		return csClient.getACLForObject(bucketName, objectKey);
	}

	public void deleteObject(String bucketName, String objectKey) throws RiakCSException
	{
		csClient.deleteObject(bucketName, objectKey);
	}


	// ACL APIs
	//

	// "Canned" ACLs for buckets and objects
    public static final String PERM_PRIVATE           = "private";
    public static final String PERM_PUBLIC_READ       = "public-read";
    public static final String PERM_PUBLIC_READ_WRITE = "public-read-write";
    public static final String PERM_AUTHENTICATED_READ= "authenticated-read";

	public void setCannedACLForBucket(String bucketName, String cannedACL) throws RiakCSException
	{
		csClient.setCannedACLForBucket(bucketName, cannedACL);
	}

	public void setCannedACLForObject(String bucketName, String objectKey, String cannedACL) throws RiakCSException
	{
		csClient.setCannedACLForObject(bucketName, objectKey, cannedACL);
	}

	
	// "Regular" ACLs for buckets and objects
	public static enum Permission{ READ, WRITE, READ_ACP, WRITE_ACP, FULL_CONTROL };

	public void addAdditionalACLToBucket(String bucketName, String emailAddress, Permission permission) throws RiakCSException
	{
		csClient.addAdditionalACLToBucket(bucketName, emailAddress, permission);
	}
	
	public void addAdditionalACLToObject(String bucketName, String objectKey, String emailAddress, Permission permission) throws RiakCSException
	{
		csClient.addAdditionalACLToObject(bucketName, objectKey, emailAddress, permission);
	}


	// Statistic APIs
	//

	public JSONObject getAccessStatistic(String keyForUser, int howManyHrsBack) throws RiakCSException
	{
		// requires CS >= 1.2
		return csClient.getAccessStatistic(keyForUser, howManyHrsBack);
	}

	public JSONObject getStorageStatistic(String keyForUser, int howManyHrsBack) throws RiakCSException
	{
		// requires CS >= 1.2
		return csClient.getStorageStatistic(keyForUser, howManyHrsBack);
	}

	
	public boolean endpointIsS3()
	{
		return csClient.endpointIsS3();
	}
	

	// Tool APIs
	//
	
	public void removeBucketAndContent(String bucketName) throws RiakCSException
	{
		csClient.removeContentOfBucket(bucketName);
		csClient.deleteBucket(bucketName);
	}

	public void uploadContentOfDirectory(File fromDirectory, String toBucket) throws RiakCSException
	{
		if(csClient.isBucketAccessible(toBucket)) throw new RiakCSException("Bucket already exists, choose different bucket name");

		csClient.createBucket(toBucket);
		csClient.uploadContentOfDirectory(fromDirectory, toBucket);
	}
	

	public static void copyBucketBetweenSystems(RiakCSClient fromSystem, String fromBucket, RiakCSClient toSystem, String toBucket) throws RiakCSException
	{
		if(fromSystem.isBucketAccessible(fromBucket) == false) throw new RiakCSException("Source Bucket doesn't exist");
		if(toSystem.isBucketAccessible(toBucket)) throw new RiakCSException("Bucket already exists, choose different bucket name");

		toSystem.createBucket(toBucket);
		RiakCSClientImpl.copyBucketBetweenSystems(fromSystem, fromBucket, toSystem, toBucket);
	}

	private RiakCSClientImpl csClient= null;

}
