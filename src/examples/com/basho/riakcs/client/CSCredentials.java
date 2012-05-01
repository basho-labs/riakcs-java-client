package examples.com.basho.riakcs.client;

import java.io.*;
import java.util.*;


public class CSCredentials
{
	private String accessKey= null;
	private String secretKey= null;
	private String endPoint = null;

	public CSCredentials(InputStream inputStream) throws IOException
	{
		Properties csProperties= new Properties();

		csProperties.load(inputStream);
		inputStream.close();

		accessKey= csProperties.getProperty("accessKey");
		secretKey= csProperties.getProperty("secretKey");
		endPoint = csProperties.getProperty("endPoint");
	}

	public String getCSAccessKey()
	{
		return accessKey;
	}

	public String getsCSSecretKey()
	{
		return secretKey;
	}

	public String getCSEndPoint()
	{
		return endPoint;
	}

}
