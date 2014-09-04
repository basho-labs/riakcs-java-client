package com.basho.riakcs.client.api;

import org.json.JSONObject;

public interface RiakCSBucketManagement {

	public abstract JSONObject listBuckets();

	public abstract void createBucket(String bucketName);

	public abstract boolean isBucketAccessible(String bucketName);

	public abstract JSONObject getACLForBucket(String bucketName);

	public abstract void deleteBucket(String bucketName);

	public abstract void removeBucketAndContent(String bucketName);

}
