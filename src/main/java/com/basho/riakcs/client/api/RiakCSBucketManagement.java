package com.basho.riakcs.client.api;

import org.json.JSONObject;

/**
 * RIAK CS client operating's for bucket management
 */
public interface RiakCSBucketManagement {

	/**
	 * List all buckets
	 * 
	 * @return JSON object with buck list
	 */
	JSONObject listBuckets();

	/**
	 * Create a bucket named with the passed strin
	 * <p>
	 * Bucket names must be unique TODO check what is throw when name is now unique and update doc
	 * 
	 * @param bucketName
	 *            name of the new bucket
	 */
	void createBucket(String bucketName);

	/**
	 * Check if the current bucket is accessible
	 * 
	 * @param bucketName
	 *            name of bucket to check
	 * @return true if bucket is accessible
	 */
	boolean isBucketAccessible(String bucketName);

	/**
	 * Retrieve the access control list for this bucket
	 * 
	 * @param bucketName
	 *            name of bucket to retrieve ACL information for
	 * @return Json object with ACL information
	 */
	JSONObject getACLForBucket(String bucketName);

	/**
	 * Delete a bucket, TODO add what happens if bucket doesnt exist or no permissions
	 * 
	 * @param bucketName
	 *            name of bucket to delete
	 */
	void deleteBucket(String bucketName);

	/**
	 * Remove a bucket and delete all contents
	 * 
	 * @param bucketName
	 *            name of the bucket to remove
	 */
	void removeBucketAndContent(String bucketName);

}
