package com.basho.riakcs.client.api;

import java.io.File;

import org.json.JSONObject;

import com.basho.riakcs.client.impl.RiakCSClientImpl.Permission;

/**
 * {@link RiakCSClient} is a Lightweight Java library to interact with a CF Riak CS instance.
 */
public interface RiakCSClient extends RiakCSUserManagement, RiakCSBucketManagement, RiakCSObjectManagement {

	// "Canned" ACLs for buckets and objects
	public static final String PERM_PRIVATE = "private";
	public static final String PERM_PUBLIC_READ = "public-read";
	public static final String PERM_PUBLIC_READ_WRITE = "public-read-write";
	public static final String PERM_AUTHENTICATED_READ = "authenticated-read";

	public abstract void enableDebugOutput();

	public abstract void setCannedACLForBucket(String bucketName, String cannedACL);

	public abstract void setCannedACLForObject(String bucketName, String objectKey, String cannedACL);

	public abstract void addAdditionalACLToBucket(String bucketName, String emailAddress, Permission permission);

	public abstract void addAdditionalACLToObject(String bucketName, String objectKey, String emailAddress,
			Permission permission);

	public abstract JSONObject getAccessStatistic(String keyForUser, int howManyHrsBack);

	public abstract JSONObject getStorageStatistic(String keyForUser, int howManyHrsBack);

	public abstract boolean endpointIsS3();

	public abstract void uploadContentOfDirectory(File fromDirectory, String toBucket);

}
