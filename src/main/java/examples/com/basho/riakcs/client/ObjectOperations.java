/*
 * Copyright (c) 2012 Basho Technologies, Inc. All Rights Reserved. This file is provided to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package examples.com.basho.riakcs.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.basho.riakcs.client.api.RiakCSClient;

public class ObjectOperations {
	public static void runIt(RiakCSClient csClient, boolean enableDebugOutput, String outputFileLocation)
			throws Exception {
		String bucketName = "playground-12345";
		String objectKey = "testfile";

		if (enableDebugOutput)
			csClient.enableDebugOutput();

		runItImpl(csClient, bucketName, objectKey, outputFileLocation);
	}

	private static void runItImpl(RiakCSClient csClient, String bucketName, String objectKey, String outputFilename)
			throws Exception {
		JSONObject result = null;

		// create bucket
		csClient.createBucket(bucketName);

		// upload object
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "text/html");

		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("Title", "see spot run");
		metadata.put("Type", "text/html");

		String webpage = "<html><body>This is a <b>Web Page</b></body></html>";
		InputStream dataInputStream = new ByteArrayInputStream(webpage.getBytes("UTF-8"));

		csClient.createObject(bucketName, objectKey, dataInputStream, headers, metadata);

		// get object info
		result = csClient.getObjectInfo(bucketName, objectKey);
		System.out.println(result.toString(2));

		// get ACL
		result = csClient.getACLForObject(bucketName, objectKey);
		System.out.println(result.toString(2));

		// set ACL
		// if (csClient.endpointIsS3() == false)
		// {
		// // add additional ACL, user has to exist
		// csClient.addAdditionalACLToObject(bucketName, objectKey, "hugo@test.com", RiakCSClient.Permission.READ);
		//
		// // get ACL
		// result= csClient.getACLForObject(bucketName, objectKey);
		// System.out.println(result.toString(2));
		// }

		// set "canned" ACL
		if (csClient.endpointIsS3()) // there is currently an issue with CS
		{
			csClient.setCannedACLForObject(bucketName, objectKey, RiakCSClient.PERM_PUBLIC_READ);

			// get ACL
			result = csClient.getACLForObject(bucketName, objectKey);
			System.out.println(result.toString(2));
		}

		// get object, content comes as part of the JSONObject
		result = csClient.getObject(bucketName, objectKey);
		System.out.println(result.toString(2));

		// get object, write content to file
		File outputFile = new File(outputFilename);
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}
		result = csClient.getObject(bucketName, objectKey, new FileOutputStream(outputFile));
		System.out.println(result.toString(2));

		// list objects
		result = csClient.listObjects(bucketName);
		System.out.println(result.toString(2));

		// delete object
		csClient.deleteObject(bucketName, objectKey);

		// list objects
		result = csClient.listObjects(bucketName);
		System.out.println(result.toString(2));

		// try out some larger file
		// File uploadFile= new File("/tmp/riak-0.14.0-osx-i386.tar");
		// csClient.createObject(bucketName, "someLargeFile", new FileInputStream(uploadFile), null, null);
		// result= csClient.getObject(bucketName, "someLargeFile", new FileOutputStream("/tmp/someLargeFile.tar"));
		// System.out.println(result.toString(2));
		// csClient.deleteObject(bucketName, "someLargeFile");

		// delete bucket
		csClient.deleteBucket(bucketName);

		if (outputFile.exists()) {
			outputFile.delete();
		}

	}
}
