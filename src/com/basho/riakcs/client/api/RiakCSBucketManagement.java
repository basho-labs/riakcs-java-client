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
	public abstract JSONObject listBuckets();

	/**
	 * Create a bucket named with the passed strin
	 * <p>
	 * Bucket names must be unique TODO check what is throw when name is now unique and update doc
	 * 
	 * @param bucketName
	 *            name of the new bucket
	 */
	public abstract void createBucket(String bucketName);

	/**
	 * Check if the current bucket is accessible
	 * 
	 * @param bucketName
	 *            name of bucket to check
	 * @return true if bucket is accessible
	 */
	public abstract boolean isBucketAccessible(String bucketName);

	/**
	 * TODO no idea, ACL is access control list maybe? need to update
	 * <p>
	 * need to add javadoc for ACL
	 * 
	 * @param bucketName
	 *            name of bucket to retrieve ACL information for
	 * @return Json object with ACL information
	 */
	public abstract JSONObject getACLForBucket(String bucketName);

	/**
	 * Delete a bucket, TODO add what happens if bucket doesnt exist or no permissions
	 * 
	 * @param bucketName
	 *            name of bucket to delete
	 */
	public abstract void deleteBucket(String bucketName);

	/**
	 * Remove a bucket and delete all contents TODO how does this differ from delete???
	 * 
	 * @param bucketName
	 *            name of the bucket to remove
	 */
	public abstract void removeBucketAndContent(String bucketName);

}
