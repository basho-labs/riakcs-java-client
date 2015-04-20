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
package com.basho.riakcs.client.api;

import java.io.File;

import com.google.gson.JsonObject;

/**
 * {@link RiakCSClient} is a Lightweight Java library to interact with a CF Riak
 * CS instance.
 */
public interface RiakCSClient extends RiakCSUserManagement,
		RiakCSBucketManagement, RiakCSObjectManagement {

	/**
	 * "Canned" ACLs for buckets and objects
	 * <p>
	 * Owner gets FULL_CONTROL
	 */
	public static final String PERM_PRIVATE = "private";

	/**
	 * "Canned" ACLs for buckets and objects
	 * <p>
	 * Owner gets FULL_CONTROL. The AllUsers group gets READ access.
	 */
	public static final String PERM_PUBLIC_READ = "public-read";

	/**
	 * "Canned" ACLs for buckets and objects
	 * <p>
	 * Owner gets FULL_CONTROL. The AllUsers group gets READ and WRITE access.
	 * Granting this on a bucket is generally not recommended.
	 */
	public static final String PERM_PUBLIC_READ_WRITE = "public-read-write";
	
	/**
	 * "Canned" ACLs for buckets and objects
	 * <p>
	 * Owner gets FULL_CONTROL. The AuthenticatedUsers group gets READ access.
	 */
	public static final String PERM_AUTHENTICATED_READ = "authenticated-read";

	/**
	 * Enable debug information
	 */
	void enableDebugOutput();

	/**
	 * Is the configured end point a S3 instance
	 * 
	 * @return true is S3
	 */
	boolean endpointIsS3();

	/**
	 * Upload the contents of a directory to a bucket
	 * 
	 * @param fromDirectory
	 *            directory to upload
	 * @param toBucket
	 *            bucket name to upload files to
	 */
	void uploadContentOfDirectory(File fromDirectory, String toBucket);

	/**
	 * Get the access statistics
	 * 
	 * @param keyForUser
	 *            user key
	 * @param howManyHrsBack
	 *            hours to generate statics for
	 * @return {@link JsonObject} with statistics information
	 */
	JsonObject getAccessStatistic(String keyForUser, int howManyHrsBack);

	/**
	 * Get the storage statistics
	 * 
	 * @param keyForUser
	 *            user key
	 * @param howManyHrsBack
	 *            hours to generate statics for
	 * @return {@link JsonObject} with statistics information
	 */
	JsonObject getStorageStatistic(String keyForUser, int howManyHrsBack);

}