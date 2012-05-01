package examples.com.basho.riakcs.client;

import org.json.*;

import com.basho.riakcs.client.api.*;

public class StatisticOperations
{
	public static void runIt(boolean enableDebugOutput) throws Exception
	{
		CSCredentials csCredentials= new CSCredentials(CSCredentials.class.getResourceAsStream("CSCredentials.Riak.properties"));			

		RiakCSClient csClient= new RiakCSClient(csCredentials.getCSAccessKey(), csCredentials.getsCSSecretKey(), csCredentials.getCSEndPoint(), false);
		if (enableDebugOutput) csClient.enableDebugOutput();

		JSONArray result= null;

		// get access stats
		int lastTwelveHrs= 12;
		result= csClient.getAccessStatistic(csCredentials.getCSAccessKey(), lastTwelveHrs);
		
		System.out.println(result.toString(2));
		
		// get storage stats
		int lastTwentyFourHrs= 24;
		result= csClient.getStorageStatistic(csCredentials.getCSAccessKey(), lastTwentyFourHrs);
		
		System.out.println(result.toString(2));
		
	}

}
