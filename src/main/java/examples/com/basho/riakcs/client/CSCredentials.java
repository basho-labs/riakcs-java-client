/*
 * Copyright (c) 2012 Basho Technologies, Inc. All Rights Reserved. This file is provided to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package examples.com.basho.riakcs.client;

import java.io.*;
import java.util.*;

public class CSCredentials {
	private String accessKey = null;
	private String secretKey = null;
	private String endPoint = null;
	private String useHttps = null;

	public CSCredentials(InputStream inputStream) throws IOException {
		Properties csProperties = new Properties();

		csProperties.load(inputStream);
		inputStream.close();

		accessKey = csProperties.getProperty("accessKey");
		secretKey = csProperties.getProperty("secretKey");
		endPoint = csProperties.getProperty("endPoint");
		useHttps = csProperties.getProperty("useHttps", "false");
	}

	public String getCSAccessKey() {
		return accessKey;
	}

	public String getsCSSecretKey() {
		return secretKey;
	}

	public String getCSEndPoint() {
		return endPoint;
	}

	public boolean getUseHttps() {
		return new Boolean(useHttps);
	}

}
