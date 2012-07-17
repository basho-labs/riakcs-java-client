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
package com.basho.riakcs.client.api;

import java.io.*;
import java.util.*;

import org.json.*;

import com.basho.riakcs.client.impl.*;

public class RiakCSClient
{
	
	//	Default Constructor for RiakCS
	//	csEndpoint : Hostname:Port of RiakCS installation .. example: localhost:8080

	public RiakCSClient(String csAccessKey, String csSecretKey, String csEndpoint, boolean useSSL)
	{
		csClient= new RiakCSClientImpl(csAccessKey, csSecretKey, csEndpoint, useSSL);
	}

	// Default Constructor for Amazon S3
	
	public RiakCSClient(String csAccessKey, String csSecretKey)
	{
		csClient= new RiakCSClientImpl(csAccessKey, csSecretKey);
	}

	
	public void enableDebugOutput()
	{
		csClient.enableDebugOutput();
	}

	
	public JSONObject createUser(String fullname, String emailAddress) throws RiakCSException
	{
		try
		{
			return csClient.createUser(fullname, emailAddress);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}

	public void createBucket(String bucketName) throws RiakCSException
	{
		try
		{
			csClient.createBucket(bucketName);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}

	public JSONObject listBuckets() throws RiakCSException
	{
		try
		{
			return csClient.listBuckets();
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}

	public boolean isBucketAccessible(String bucketName) throws RiakCSException
	{
		try
		{
			return csClient.isBucketAccessible(bucketName);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}
	
	public JSONObject getACLForBucket(String bucketName) throws RiakCSException
	{
		try
		{
			return csClient.getACLForBucket(bucketName);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}

	public void deleteBucket(String bucketName) throws RiakCSException
	{
		try
		{
			csClient.deleteBucket(bucketName);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}

	public void createObject(String bucketName, String objectKey, InputStream dataInputStream, Map<String, String> headers, Map<String, String> metadata) throws RiakCSException
	{
		try
		{
			csClient.createObject(bucketName, objectKey, dataInputStream, headers, metadata);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}
	
	public JSONObject listObjects(String bucketName) throws RiakCSException
	{
		try
		{
			return csClient.listObjects(bucketName);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}
	
	public JSONObject getObject(String bucketName, String objectKey) throws RiakCSException
	{
		// Content gets returned as part of the JSONObject
		try
		{
			return csClient.getObject(bucketName, objectKey);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}
	
	public JSONObject getObject(String bucketName, String objectKey, OutputStream dataOutputStream) throws RiakCSException
	{
		// Content gets written into outputStream
		try
		{
			return csClient.getObject(bucketName, objectKey, dataOutputStream);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}

	public JSONObject getObjectInfo(String bucketName, String objectKey) throws RiakCSException
	{
		try
		{
			return csClient.getObjectInfo(bucketName, objectKey);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}
	
	public JSONObject getACLForObject(String bucketName, String objectKey) throws RiakCSException
	{
		try
		{
			return csClient.getACLForObject(bucketName, objectKey);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}

	public void deleteObject(String bucketName, String objectKey) throws RiakCSException
	{
		try
		{
			csClient.deleteObject(bucketName, objectKey);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}


	// "Canned" ACLs for buckets and objects
    public static final String PERM_PRIVATE           = "private";
    public static final String PERM_PUBLIC_READ       = "public-read";
    public static final String PERM_PUBLIC_READ_WRITE = "public-read-write";
    public static final String PERM_AUTHENTICATED_READ= "authenticated-read";

	public void setCannedACLForBucket(String bucketName, String cannedACL) throws RiakCSException
	{
		try
		{
			csClient.setCannedACLForBucket(bucketName, cannedACL);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}

	public void setCannedACLForObject(String bucketName, String objectKey, String cannedACL) throws RiakCSException
	{
		try
		{
			csClient.setCannedACLForObject(bucketName, objectKey, cannedACL);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}

	
	// "Regular" ACLs for buckets and objects
	public static enum Permission{ READ, WRITE, READ_ACP, WRITE_ACP, FULL_CONTROL };

	public void addAdditionalACLToBucket(String bucketName, String emailAddress, Permission permission) throws RiakCSException
	{
		try
		{
			csClient.addAdditionalACLToBucket(bucketName, emailAddress, permission);

		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}
	
	public void addAdditionalACLToObject(String bucketName, String objectKey, String emailAddress, Permission permission) throws RiakCSException
	{
		try
		{
			csClient.addAdditionalACLToObject(bucketName, objectKey, emailAddress, permission);

		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}


	public JSONArray getAccessStatistic(String keyForUser, int howManyHrsBack) throws RiakCSException
	{
		try
		{
			return csClient.getAccessStatistic(keyForUser, howManyHrsBack);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}

	public JSONArray getStorageStatistic(String keyForUser, int howManyHrsBack) throws RiakCSException
	{
		try
		{
			return csClient.getStorageStatistic(keyForUser, howManyHrsBack);
			
		} catch(Exception e)
		{
			throw new RiakCSException(e);
		}
	}

	
	public boolean endpointIsS3()
	{
		return csClient.endpointIsS3();
	}
	


	private RiakCSClientImpl csClient= null;

}
