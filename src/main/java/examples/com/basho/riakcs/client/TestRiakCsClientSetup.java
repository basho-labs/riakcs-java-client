/*
 * Copyright (c) 2012 Basho Technologies, Inc. All Rights Reserved. This file is provided to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package examples.com.basho.riakcs.client;

import com.basho.riakcs.client.api.RiakCSClient;
import com.basho.riakcs.client.impl.RiakCSClientImpl;

/**
 * Test the setup of a riak cs client by performing bucket and object requests
 */
public class TestRiakCsClientSetup {

	private static final String RIAK_PROPERTIES = "CSCredentials.Riak.properties";
	static final boolean runAgainstCS = true;
	static final boolean runAgainstS3 = false;
	static final boolean debugEnabled = true;
	static final boolean debugDisabled = false;

	/**
	 * Test using the properties file embedded within this client
	 * 
	 * @param args
	 *            to pass
	 * @throws Exception
	 *             if error
	 */
	public static void main(String[] args) throws Exception {
		boolean debugFlag = debugDisabled;
		// boolean debugFlag= debugEnabled;

		CSCredentials csCredentials = new CSCredentials(CSCredentials.class.getResourceAsStream(RIAK_PROPERTIES));

		RiakCSClient csClient = new RiakCSClientImpl(csCredentials.getCSAccessKey(), csCredentials.getsCSSecretKey(),
				csCredentials.getCSEndPoint(), csCredentials.getUseHttps());

		BucketOperations.runIt(csClient, debugFlag);

		System.out.println("\n\n*** BUCKET OPERATIONS COMPLETED ***\n\n");

		ObjectOperations.runIt(csClient, debugFlag, "target/results.txt");

		System.out.println("\n\n*** OBJECT OPERATIONS COMPLETED ***\n\n");
	}

	/**
	 * Test your cleint setup by running bucket and object operations
	 * 
	 * @param csClient
	 *            to test
	 * @param outputFileLocation
	 *            location to output
	 * @throws Exception
	 *             if error occurs
	 */
	public static void testConnection(RiakCSClient csClient, String outputFileLocation) throws Exception {
		boolean debugFlag = debugDisabled;

		BucketOperations.runIt(csClient, debugFlag);

		System.out.println("\n\n*** BUCKET OPERATIONS COMPLETED ***\n\n");

		ObjectOperations.runIt(csClient, debugFlag, outputFileLocation);

		System.out.println("\n\n*** OBJECT OPERATIONS COMPLETED ***\n\n");
	}
}
