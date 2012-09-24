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

import com.basho.riakcs.client.api.*;

public class ToolOperations
{
	public static void runIt(boolean runAgainstRiakCS, boolean enableDebugOutput) throws Exception
	{
		RiakCSClient csClient= null;

		if (runAgainstRiakCS)
		{
			CSCredentials csCredentials= new CSCredentials(CSCredentials.class.getResourceAsStream("CSCredentials.Riak.properties"));			
			csClient= new RiakCSClient(csCredentials.getCSAccessKey(), csCredentials.getsCSSecretKey(), csCredentials.getCSEndPoint(), false);

		} else {
			CSCredentials s3Credentials= new CSCredentials(CSCredentials.class.getResourceAsStream("CSCredentials.AWS.properties"));
			csClient= new RiakCSClient(s3Credentials.getCSAccessKey(), s3Credentials.getsCSSecretKey());
			
		}

		if (enableDebugOutput) csClient.enableDebugOutput();

		runItImpl(csClient);
	}
	
	private static void runItImpl(RiakCSClient csClient) throws Exception
	{
		// delete bucket plus all of its content
//		csClient.removeBucketAndContent("basho_bench");

		// upload folder/file structure
//		long startTime= System.currentTimeMillis();		
//		csClient.uploadContentOfDirectory(new File("/Users/Shared/playground/riak-1.1.1"), "riak_1.1.1");
//		System.out.println("Upload time in seconds: " + (Math.abs(System.currentTimeMillis() - startTime)/1000));
	}

}
