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

public class Playground
{
	static final boolean runAgainstCS = true;
	static final boolean runAgainstS3 = false;
	static final boolean debugEnabled = true;
	static final boolean debugDisabled= false;


	public static void main(String[] args) throws Exception
	{
		boolean debugFlag= debugDisabled;
//		boolean debugFlag= debugEnabled;

		UserOperations.runIt(debugFlag);

		BucketOperations.runIt(runAgainstCS, debugFlag);
		BucketOperations.runIt(runAgainstS3, debugFlag);

		ObjectOperations.runIt(runAgainstCS, debugFlag);
		ObjectOperations.runIt(runAgainstS3, debugFlag);

		ToolOperations.runIt(runAgainstCS, debugFlag);
		ToolOperations.runIt(runAgainstS3, debugFlag);

		CopyOperations.runIt(debugFlag);

		StatisticOperations.runIt(debugFlag);
	
	}

}
