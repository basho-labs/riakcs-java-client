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

import org.json.*;

import com.basho.riakcs.client.api.*;

public class UserOperations
{
	public static void runIt(boolean enableDebugOutput) throws Exception
	{
		CSCredentials csCredentials= new CSCredentials(CSCredentials.class.getResourceAsStream("CSCredentials.Riak.properties"));			

		RiakCSClient csClient= new RiakCSClient(csCredentials.getCSAccessKey(), csCredentials.getsCSSecretKey(), csCredentials.getCSEndPoint(), false);
		if (enableDebugOutput) csClient.enableDebugOutput();

		//create new user, and get info using newly created key_id
//		JSONObject result= csClient.createUser("Hugo Doe", "hugo_doe@test.com");		
//		System.out.println(result.toString(2));
//		String key_id= result.getString("key_id");
//		result= csClient.getUserInfo(key_id);
//		System.out.println(result.toString(2));
		
		JSONObject userInfo= csClient.getMyUserInfo();
		System.out.println(userInfo.toString(2));

//		JSONObject userInfo= csClient.getUserInfo("GFUPU2YDTCICR4PMOGRP"); // use key_id from existing user
//		System.out.println(userInfo.toString(2));

//		csClient.disableUser("GFUPU2YDTCICR4PMOGRP"); // use key_id from existing user
//		JSONObject userList= csClient.listDisabledUsers();
//		System.out.println(userList.toString(2));
//
//		csClient.enableUser("TK9KAEBX201ERVTPCNB5"); // use key_id from existing user
		
		JSONObject userList= csClient.listUsers();
		System.out.println(userList.toString(2));

		userList= csClient.listEnabledUsers();
		System.out.println(userList.toString(2));

		userList= csClient.listDisabledUsers();
		System.out.println(userList.toString(2));
	}

}
