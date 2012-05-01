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
		
		JSONObject result= csClient.createUser("Hugo Doe", "hugo2@test.com");  // use some non-existing credentials ..
		
		System.out.println(result.toString(2));
	}

}
