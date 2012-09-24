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
package examples.com.basho.riakcs.client;

import org.json.*;

import com.basho.riakcs.client.api.*;

public class BucketOperations
{
	public static void runIt(boolean runAgainstRiakCS, boolean enableDebugOutput) throws Exception
	{
		RiakCSClient csClient= null;

		String bucketName= "playground-123";

		if (runAgainstRiakCS)
		{
			CSCredentials csCredentials= new CSCredentials(CSCredentials.class.getResourceAsStream("CSCredentials.Riak.properties"));			
			csClient= new RiakCSClient(csCredentials.getCSAccessKey(), csCredentials.getsCSSecretKey(), csCredentials.getCSEndPoint(), false);

		} else {
			CSCredentials s3Credentials= new CSCredentials(CSCredentials.class.getResourceAsStream("CSCredentials.AWS.properties"));
			csClient= new RiakCSClient(s3Credentials.getCSAccessKey(), s3Credentials.getsCSSecretKey());
			
		}

		if (enableDebugOutput) csClient.enableDebugOutput();

		runItImpl(csClient, bucketName);
	}

	private static void runItImpl(RiakCSClient csClient, String bucketName) throws Exception
	{
		JSONObject result= null;
		
		// list buckets
		result= csClient.listBuckets();
		System.out.println(result.toString(2));

		// create bucket
		csClient.createBucket(bucketName);

		// bucket accessible
		if (csClient.isBucketAccessible(bucketName) == false)
			throw new RiakCSException("Bucket is not accessible: " + bucketName);

		// list buckets
		result= csClient.listBuckets();
		System.out.println(result.toString(2));

		// get ACL
		result= csClient.getACLForBucket(bucketName);
		System.out.println(result.toString(2));

//		if (csClient.endpointIsS3() == false) // just run it against RiakCS
//		{
//			// add additional ACL, user has to exist
//			csClient.addAdditionalACLToBucket(bucketName, "hugo@test.com", RiakCSClient.Permission.WRITE);
//
//			// get ACL
//			result= csClient.getACLForBucket(bucketName);
//			System.out.println(result.toString(2));
//		}

		// set "canned" ACL
		csClient.setCannedACLForBucket(bucketName, RiakCSClient.PERM_AUTHENTICATED_READ);

		// get ACL
		result= csClient.getACLForBucket(bucketName);
		System.out.println(result.toString(2));

		// delete bucket
		csClient.deleteBucket(bucketName);
		
		// list buckets
		result= csClient.listBuckets();
		System.out.println(result.toString(2));

	}

}
