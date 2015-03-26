package com.basho.riakcs.client.api;

import java.io.File;

import org.json.JSONObject;

import com.basho.riakcs.client.impl.RiakCSClientImpl.Permission;

/**
 * {@link RiakCSClient} is a Lightweight Java library to interact with a CF Riak CS instance.
 */
public interface RiakCSClient extends RiakCSUserManagement, RiakCSBucketManagement, RiakCSObjectManagement {

	// "Canned" ACLs for buckets and objects
	String PERM_PRIVATE = "private";
	String PERM_PUBLIC_READ = "public-read";
	String PERM_PUBLIC_READ_WRITE = "public-read-write";
	String PERM_AUTHENTICATED_READ = "authenticated-read";

	/**
	 * Enable debug information
	 */
	void enableDebugOutput();

	void setCannedACLForBucket(String bucketName, String cannedACL);

	void setCannedACLForObject(String bucketName, String objectKey, String cannedACL);

	void addAdditionalACLToBucket(String bucketName, String emailAddress, Permission permission);

	void addAdditionalACLToObject(String bucketName, String objectKey, String emailAddress, Permission permission);

	JSONObject getAccessStatistic(String keyForUser, int howManyHrsBack);

	JSONObject getStorageStatistic(String keyForUser, int howManyHrsBack);

	boolean endpointIsS3();

	void uploadContentOfDirectory(File fromDirectory, String toBucket);

}
