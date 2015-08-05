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

import java.io.*;
import java.util.*;

import com.basho.riakcs.client.api.*;
import com.basho.riakcs.client.impl.RiakCSClientImpl;
import com.google.gson.JsonObject;

public class CopyOperations
{
	public static void runIt(boolean enableDebugOutput) throws Exception
	{
		CSCredentials csCredentials= new CSCredentials(CSCredentials.class.getResourceAsStream("CSCredentials.Riak.properties"));			
		RiakCSClient csClient= new RiakCSClientImpl(csCredentials.getCSAccessKey(), csCredentials.getsCSSecretKey(), csCredentials.getCSEndPoint(), csCredentials.getUseHttps());

		CSCredentials s3Credentials= new CSCredentials(CSCredentials.class.getResourceAsStream("CSCredentials.AWS.properties"));
		RiakCSClient s3Client= new RiakCSClientImpl(s3Credentials.getCSAccessKey(), s3Credentials.getsCSSecretKey());
			
		if (enableDebugOutput) csClient.enableDebugOutput();
		if (enableDebugOutput) s3Client.enableDebugOutput();


		String bucketName= "copy_test_10";

		// upload docs to s3 and copy them to c2
		s3Client.createBucket(bucketName);

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "text/html");

		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("Title","see spot run");
		metadata.put("Type","text/html");
		
		String webpage= "<html><body>This is a <b>Web Page</b></body></html>";
		InputStream dataInputStream= new ByteArrayInputStream(webpage.getBytes("UTF-8"));
		
		s3Client.createObject(bucketName, "test1.txt", dataInputStream, headers, metadata);

		
		headers = new HashMap<String, String>();
		headers.put("Content-Type", "text/html");

		metadata = new HashMap<String, String>();
		metadata.put("Title","see riak run");
		
		webpage= "<html><body>This is a 2nd <b>Web Page</b></body></html>";
		dataInputStream= new ByteArrayInputStream(webpage.getBytes("UTF-8"));
		
		s3Client.createObject(bucketName, "/test/test1.txt", dataInputStream, headers, metadata);

		
		// copy from S3 to CS
		RiakCSClientImpl.copyBucketBetweenSystems(s3Client, bucketName, csClient, bucketName);

		
		// retrieve data
		JsonObject result= s3Client.listObjects(bucketName);
		System.out.println(result);
		
		result= csClient.listObjects(bucketName);
		System.out.println(result);

		result= csClient.getObject(bucketName, "test1.txt");
		System.out.println(result);
		
		result= csClient.getObject(bucketName, "/test/test1.txt");
		System.out.println(result);
		
		// cleanup
		s3Client.removeBucketAndContent(bucketName);
		csClient.removeBucketAndContent(bucketName);
	}

}
