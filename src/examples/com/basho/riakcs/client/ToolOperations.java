/*
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

import com.basho.riakcs.client.api.*;

public class ToolOperations
{
	public static void runIt(boolean enableDebugOutput) throws Exception
	{
		CSCredentials csCredentials= new CSCredentials(CSCredentials.class.getResourceAsStream("CSCredentials.Riak.properties"));			

		RiakCSClient csClient= new RiakCSClient(csCredentials.getCSAccessKey(), csCredentials.getsCSSecretKey(), csCredentials.getCSEndPoint(), false);
		if (enableDebugOutput) csClient.enableDebugOutput();

		// delete bucket plus all of its content
//		csClient.removeBucketAndContent("test_bench");

	}

}
