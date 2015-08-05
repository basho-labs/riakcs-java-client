package com.basho.riakcs.client.api;

import com.basho.riakcs.client.impl.RiakCSClientImpl.Permission;
import com.google.gson.JsonObject;

/**
 * RIAK CS client operating's for bucket management
 */
public interface RiakCSBucketManagement {

	/**
	 * Create a bucket named with the passed string
	 * <p>
	 * Bucket names must be unique TODO check what is throw when name is now
	 * unique and update doc
	 * 
	 * @param bucketName
	 *            name of the new bucket
	 */
	void createBucket(String bucketName);

	/**
	 * List all buckets
	 * 
	 * @return {@link JsonObject} with buck list
	 */
	JsonObject listBuckets();

	/**
	 * Delete a bucket, TODO add what happens if bucket doesn't exist or no
	 * permissions
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
	 * @return {@link JsonObject} with ACL information
	 */
	JsonObject getACLForBucket(String bucketName);

	/**
	 * Set the canned ACL permissions on the bucket
	 * 
	 * @param bucketName
	 *            name of bucket to retrieve ACL information for
	 * @param cannedACL
	 *            ACL permission to set
	 */
	void setCannedACLForBucket(String bucketName, String cannedACL);

	/**
	 * Add a addition ACL to the bucket
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param emailAddress
	 *            email to set with the ACL
	 * @param permission
	 *            {@link Permission} to set on the bucket
	 */
	void addAdditionalACLToBucket(String bucketName, String emailAddress,
			Permission permission);

}
