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

public class StatisticOperations
{
	public static void runIt(boolean enableDebugOutput) throws Exception
	{
		CSCredentials csCredentials= new CSCredentials(CSCredentials.class.getResourceAsStream("CSCredentials.Riak.properties"));			

		RiakCSClient csClient= new RiakCSClient(csCredentials.getCSAccessKey(), csCredentials.getsCSSecretKey(), csCredentials.getCSEndPoint(), csCredentials.getUseHttps());
		if (enableDebugOutput) csClient.enableDebugOutput();

		JSONObject accessStats= null;

		// get access stats
		int lastTwelveHrs= 12;
		accessStats= csClient.getAccessStatistic(csCredentials.getCSAccessKey(), lastTwelveHrs);
		
		System.out.println(accessStats.toString(2));
		
		// get storage stats
		int lastTwentyFourHrs= 24;
		JSONObject storageStats= csClient.getStorageStatistic(csCredentials.getCSAccessKey(), lastTwentyFourHrs);
		
		System.out.println(storageStats.toString(2));
		
	}

}
