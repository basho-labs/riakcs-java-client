package com.basho.riakcs.client.impl;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import org.json.*;
import org.w3c.dom.*;

import com.basho.riakcs.client.api.*;
import com.basho.riakcs.client.api.RiakCSClient.Permission;


public class RiakCSClientImpl
{
    private final static Map<String, String> EMPTY_STRING_MAP= new HashMap<String, String>();

    private static final SimpleDateFormat iso8601DateFormat= new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
    
    private String csAccessKey= null;
	private String csSecretKey= null;
	
	private String  csEndpoint= "s3.amazonaws.com";
	private boolean useSSL    = true;

	private boolean debugModeEnabled= false;
	

	public RiakCSClientImpl(String csAccessKey, String csSecretKey, String csEndpoint, boolean useSSL)
	{
		this.csAccessKey= csAccessKey;
		this.csSecretKey= csSecretKey;
		this.csEndpoint = csEndpoint;
		this.useSSL     = useSSL;
	}

	
	public RiakCSClientImpl(String csAccessKey, String csSecretKey)
	{
		this.csAccessKey= csAccessKey;
		this.csSecretKey= csSecretKey;
	}

	
	public void enableDebugOutput()
	{
		debugModeEnabled= true;
	}


	public boolean endpointIsS3()
	{
		return csEndpoint.contains("s3.amazonaws.com");
	}


	public JSONObject createUser(String fullname, String emailAddress) throws Exception
	{
		if(endpointIsS3()) throw new RiakCSException("Can't create User on AWS S3");

		StringBuffer postData= new StringBuffer();
		postData.append("email=");
		postData.append(URLEncoder.encode(emailAddress, "UTF-8").replace("+", "%20"));
		postData.append("&name=");
		postData.append(URLEncoder.encode(fullname, "UTF-8").replace("+", "%20"));
		InputStream dataInputStream= new ByteArrayInputStream(postData.toString().getBytes("UTF-8"));

		Map<String, String> headers= new HashMap<String, String>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("Content-Length", String.valueOf(dataInputStream.available()));
		
		CommunicationLayer comLayer= getCommunicationLayer();
		
		URL url= comLayer.generateCSUrl("", "user", EMPTY_STRING_MAP);		
		HttpURLConnection connection= comLayer.makeUnsignedCall(CommunicationLayer.HttpMethod.POST, url, dataInputStream, headers);
		InputStreamReader inputStreamReader= new InputStreamReader(connection.getInputStream(), "UTF-8");
		
		JSONObject result= new JSONObject(new JSONTokener(inputStreamReader));
		
		return result;
	}


	public void createBucket(String bucketName) throws Exception
	{
		CommunicationLayer comLayer= getCommunicationLayer();

		URL url= comLayer.generateCSUrl(bucketName, "", EMPTY_STRING_MAP);
		comLayer.makeCall(CommunicationLayer.HttpMethod.PUT, url);
	}


	public JSONObject listBuckets() throws Exception
	{
		CommunicationLayer comLayer= getCommunicationLayer();

		URL url= comLayer.generateCSUrl("", "", EMPTY_STRING_MAP);
		HttpURLConnection conn= comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url);
		Document xmlDoc= XMLUtils.parseToDocument(conn.getInputStream(), debugModeEnabled);

		JSONObject bucketList= new JSONObject();

		for (Node node : XMLUtils.xpathToNodeList("//Buckets/Bucket", xmlDoc))
		{
			JSONObject bucket= new JSONObject();
			bucket.put("name", XMLUtils.xpathToContent("Name", node));
			bucket.put("creationDate", XMLUtils.xpathToContent("CreationDate", node));

			bucketList.append("bucketList", bucket);
		}

		JSONObject owner= new JSONObject();
		owner.put("id", XMLUtils.xpathToContent("//Owner/ID", xmlDoc));
		owner.put("displayName", XMLUtils.xpathToContent("//Owner/DisplayName", xmlDoc));

		bucketList.put("owner", owner);

		return bucketList;
	}
    
	public boolean isBucketAccessible(String bucketName) throws Exception
	{
		boolean result= false;

		CommunicationLayer comLayer= getCommunicationLayer();

		URL url= comLayer.generateCSUrl(bucketName, "", EMPTY_STRING_MAP);
		HttpURLConnection conn= comLayer.makeCall(CommunicationLayer.HttpMethod.HEAD, url);
		
		for (String headerName : conn.getHeaderFields().keySet())
		{
			// .. just check for something ..
			if(headerName != null && headerName.equals("Server"))
				result= true;
		}
		
		return result;
	}

	public void deleteBucket(String bucketName) throws Exception
	{
		CommunicationLayer comLayer= getCommunicationLayer();

		URL url= comLayer.generateCSUrl(bucketName, "", EMPTY_STRING_MAP);
		comLayer.makeCall(CommunicationLayer.HttpMethod.DELETE, url);
	}


	public void createObject(String bucketName, String objectKey, InputStream dataInputStream, Map<String, String> headers, Map<String, String> metadata) throws Exception
	{
		createObject(bucketName, objectKey, dataInputStream, headers, metadata, null);
	}

	
	public void createObject(String bucketName, String objectKey, InputStream dataInputStream, Map<String, String> headers, Map<String, String> metadata, String policy) throws Exception
	{
		if (headers == null)  headers= new HashMap<String, String>();
		
		if (!headers.containsKey("Content-Length")) headers.put("Content-Length", String.valueOf(dataInputStream.available()));
		if (!headers.containsKey("Content-Type"))   headers.put("Content-Type", "application/octet-stream");

		// Add user-meta info
		if (metadata != null)
		{
			for (Map.Entry<String, String> metadataHeader : metadata.entrySet())
			{
				headers.put("x-amz-meta-" + metadataHeader.getKey(), metadataHeader.getValue());
			}
		}

		CommunicationLayer comLayer= getCommunicationLayer();
		
		URL url= comLayer.generateCSUrl(bucketName, objectKey, EMPTY_STRING_MAP);
		comLayer.makeCall(CommunicationLayer.HttpMethod.PUT, url, dataInputStream, headers);
	}

	
	public JSONObject getObject(String bucketName, String objectKey) throws Exception
	{
		return getObject(bucketName, objectKey, null);
	}

	public JSONObject getObject(String bucketName, String objectKey, OutputStream dataOutputStream) throws Exception
	{
		CommunicationLayer comLayer= getCommunicationLayer();
		
		URL url= comLayer.generateCSUrl(bucketName, objectKey, EMPTY_STRING_MAP);
		HttpURLConnection conn= comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url, null, EMPTY_STRING_MAP);

		JSONObject object= extractMetaInfoForObject(objectKey, conn);

		// Download data
		if (dataOutputStream != null)
		{
			InputStream inputStream= conn.getInputStream();
			byte[] buffer= new byte[8192];
			int count= -1;
			while ((count= inputStream.read(buffer)) != -1)
			{
				dataOutputStream.write(buffer, 0, count);
			}
			dataOutputStream.close();
			inputStream.close();
		} else
		{
			object.put("body", getInputStreamAsString(conn.getInputStream()));
		}

		return object;
	}


	public JSONObject getObjectInfo(String bucketName, String objectKey) throws Exception
	{
		CommunicationLayer comLayer= getCommunicationLayer();
		
		URL url= comLayer.generateCSUrl(bucketName, objectKey, EMPTY_STRING_MAP);
		HttpURLConnection conn= comLayer.makeCall(CommunicationLayer.HttpMethod.HEAD, url, null, EMPTY_STRING_MAP);

		JSONObject object= extractMetaInfoForObject(objectKey, conn);

		return object;
	}


	private JSONObject extractMetaInfoForObject(String objectKey, HttpURLConnection conn) throws JSONException
	{
		Map<String, String> responseHeaders= new HashMap<String, String>();
		JSONObject metadata= new JSONObject();

		for (String headerName : conn.getHeaderFields().keySet())
		{
			if (headerName == null)
				continue;

			if (headerName.startsWith("x-amz-meta"))
			{
				metadata.put(headerName.substring(11), conn.getHeaderFields().get(headerName).get(0));
			} else {
				responseHeaders.put(headerName, conn.getHeaderFields().get(headerName).get(0));
			}
		}

		JSONObject object= new JSONObject();
		object.put("key", objectKey);
		object.put("etag", responseHeaders.get("ETag"));
		object.put("lastModified", responseHeaders.get("Last-Modified"));
		object.put("size", responseHeaders.get("Content-Length"));
		object.put("metadata", metadata);
		return object;
	}


	public JSONObject listObjects(String bucketName) throws Exception
	{
		JSONObject result= new JSONObject();
		result.put("bucketName", bucketName);
		
		boolean isTruncated= true;
		while (isTruncated)
		{
			CommunicationLayer comLayer= getCommunicationLayer();
			
			URL url= comLayer.generateCSUrl(bucketName, "", EMPTY_STRING_MAP);
			HttpURLConnection conn= comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url);
			
			Document xmlDoc= XMLUtils.parseToDocument(conn.getInputStream(), debugModeEnabled);
			List<Node> nodeList= XMLUtils.xpathToNodeList("//Contents", xmlDoc);
			for (Node node : nodeList)
			{
				JSONObject object = new JSONObject();
				object.put("key", XMLUtils.xpathToContent("Key", node));
				object.put("size", XMLUtils.xpathToContent("Size", node));
				object.put("lastModified", XMLUtils.xpathToContent("LastModified", node));
				object.put("etag", XMLUtils.xpathToContent("ETag", node));
				
				JSONObject owner= new JSONObject();
				owner.put("id", XMLUtils.xpathToContent("Owner/ID", node));
				owner.put("displayName", XMLUtils.xpathToContent("Owner/DisplayName", node));
				object.put("owner", owner);
			
				result.append("objectList", object);
			}

//			for (Node node : xpathToNodeList("//CommonPrefixes", xmlDoc))
//			{
//				objectList.prefixes.add(node.getTextContent());
//			}
            
			// Determine whether listing is truncated
			isTruncated= "true".equals(XMLUtils.xpathToContent("//IsTruncated", xmlDoc));
			
			// Set the marker parameter to the NextMarker if possible,
			// otherwise set it to the last key name in the listing
//			if (xpathToContent("//NextMarker", xmlDoc) != null)
//			{
//				parameters.put("marker", xpathToContent("//NextMarker", xmlDoc));
//			} else if (xpathToContent("//Contents/Key", xmlDoc) != null) {
//				// Java's XPath implementation doesn't support the 'last()'
//				// function, so we must manually find the last Key node.
//				List<Node> keys = xpathToNodeList("//Contents/Key", xmlDoc);
//				Node lastNode = keys.get(keys.size() - 1);
//				parameters.put("marker", lastNode.getTextContent());
//			} else {
//				parameters.put("marker", "");
//			}
		}

		return result;
	}

	public void deleteObject(String bucketName, String objectKey) throws Exception
	{
		CommunicationLayer comLayer= getCommunicationLayer();
		
        URL url= comLayer.generateCSUrl(bucketName, objectKey, EMPTY_STRING_MAP);
        comLayer.makeCall(CommunicationLayer.HttpMethod.DELETE, url);
	}

	public void setCannedACLForBucket(String bucketName, String cannedAcl) throws Exception
	{
		setCannedACLImplt(bucketName, "", cannedAcl);
	}

	public void setCannedACLForObject(String bucketName, String objectKey, String cannedAcl) throws Exception
	{
		setCannedACLImplt(bucketName, objectKey, cannedAcl);
	}

	public void setCannedACLImplt(String bucketName, String objectKey, String cannedAcl) throws Exception
	{
		CommunicationLayer comLayer= getCommunicationLayer();

		Map<String, String> parameters= new HashMap<String, String>();
		parameters.put("acl", null);
		URL url= comLayer.generateCSUrl(bucketName, objectKey, parameters);

		Map<String, String> headers= new HashMap<String, String>();
		headers.put("x-amz-acl", cannedAcl);
		comLayer.makeCall(CommunicationLayer.HttpMethod.PUT, url, null, headers);
	}

	public void addAdditionalACLToBucket(String bucketName, String emailAddress, Permission permission) throws Exception
	{
		JSONObject oldACL= getACLForBucket(bucketName);
		
		JSONObject newGrant= new JSONObject();
		JSONObject grantee= new JSONObject();
		grantee.put("emailAddress", emailAddress);
		newGrant.put("grantee", grantee);
		newGrant.put("permission", permission);

		oldACL.append("grantsList", newGrant);

		addAdditionalACLImpl(bucketName, "", oldACL);
	}

	public void addAdditionalACLToObject(String bucketName, String objectKey, String emailAddress, Permission permission) throws Exception
	{
		JSONObject oldACL= getACLForObject(bucketName, objectKey);
		
		JSONObject newGrant= new JSONObject();
		JSONObject grantee= new JSONObject();
		grantee.put("emailAddress", emailAddress);
		newGrant.put("grantee", grantee);
		newGrant.put("permission", permission);

		oldACL.append("grantsList", newGrant);

		addAdditionalACLImpl(bucketName, objectKey, oldACL);
	}
	
	private void addAdditionalACLImpl(String bucketName, String objectKey, JSONObject acl) throws Exception
	{
		StringBuffer aclText= new StringBuffer();

		aclText.append("<AccessControlPolicy>");
		aclText.append("<Owner><ID>" + acl.getJSONObject("owner").getString("id") + "</ID>");
		aclText.append("<DisplayName>" + acl.getJSONObject("owner").getString("displayName") + "</DisplayName></Owner>");
		aclText.append("<AccessControlList>");

		JSONArray grantsList= acl.getJSONArray("grantsList");
		for (int pt= 0; pt < grantsList.length(); pt++)
		{
			JSONObject grant= grantsList.getJSONObject(pt);

			aclText.append("<Grant><Permission>" + grant.getString("permission") + "</Permission>");
			aclText.append("<Grantee");
			aclText.append(" ").append("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
			
			if (grant.getJSONObject("grantee").has("id"))
			{
				aclText.append(" xsi:type='CanonicalUser'>");
				aclText.append("<ID>" + grant.getJSONObject("grantee").getString("id") + "</ID>");
			} else
			{
				aclText.append(" xsi:type='AmazonCustomerByEmail'>");
				aclText.append("<EmailAddress>" + grant.getJSONObject("grantee").getString("emailAddress") + "</EmailAddress>");
			}
			
			aclText.append("</Grantee>");
			aclText.append("</Grant>");
		}
		aclText.append("</AccessControlList>");
		aclText.append("</AccessControlPolicy>");

		Map<String, String> parameters= new HashMap<String, String>();
		parameters.put("acl", null);

		InputStream dataInputStream= new ByteArrayInputStream(aclText.toString().getBytes("UTF-8"));

		Map<String, String> headers= new HashMap<String, String>();
		headers.put("Content-Type", "application/xml");
		
		CommunicationLayer comLayer= getCommunicationLayer();
		
        URL url= comLayer.generateCSUrl(bucketName, objectKey, parameters);
        comLayer.makeCall(CommunicationLayer.HttpMethod.PUT, url, dataInputStream, headers);
	}

	public JSONObject getACLForBucket(String bucketName) throws Exception
	{
		return getAclImpl(bucketName, "");
	}

	public JSONObject getACLForObject(String bucketName, String objectKey) throws Exception
	{
		return getAclImpl(bucketName, objectKey);
	}

	private JSONObject getAclImpl(String bucketName, String objectKey) throws Exception
	{
		Map<String, String> parameters= new HashMap<String, String>();
		parameters.put("acl", null);

		CommunicationLayer comLayer= getCommunicationLayer();

		URL url= comLayer.generateCSUrl(bucketName, objectKey, parameters);
		HttpURLConnection conn= comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url);

		Document xmlDoc= XMLUtils.parseToDocument(conn.getInputStream(), debugModeEnabled);
		JSONObject acl= new JSONObject();

		for (Node grantNode : XMLUtils.xpathToNodeList("//Grant", xmlDoc))
		{
			JSONObject grant= new JSONObject();
			grant.put("permission", XMLUtils.xpathToContent("Permission", grantNode));

			String type= XMLUtils.xpathToContent("Grantee/@type", grantNode);

			JSONObject grantee= new JSONObject();
			grantee.put("type", type);

			if (type.equals("Group"))
			{
				grantee.put("uri", XMLUtils.xpathToContent("Grantee/URI", grantNode));
			} else
			{
				grantee.put("id",XMLUtils.xpathToContent("Grantee/ID", grantNode));
				grantee.put("displayName", XMLUtils.xpathToContent("Grantee/DisplayName", grantNode));
			}
			grant.put("grantee", grantee);
			acl.append("grantsList", grant);
		}
		
		JSONObject owner= new JSONObject();
		owner.put("id", XMLUtils.xpathToContent("//Owner/ID", xmlDoc));
		owner.put("displayName", XMLUtils.xpathToContent("//Owner/DisplayName", xmlDoc));
		acl.put("owner", owner);
		return acl;
	}

	public JSONArray getAccessStatistic(String keyForUser, int howManyHrsBack) throws Exception
	{		
		if(endpointIsS3()) throw new RiakCSException("Not supported by AWS S3");

		iso8601DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
		
		Date endTime  = new Date(System.currentTimeMillis());
		Date startTime= new Date(System.currentTimeMillis()-(howManyHrsBack*60*60*1000));

		StringBuffer path= new StringBuffer();
		path.append("/usage");
		path.append("/");
		path.append(keyForUser);
		path.append("/aj");
		path.append("/");
		path.append(iso8601DateFormat.format(startTime));
		path.append("/");
		path.append(iso8601DateFormat.format(endTime));

		CommunicationLayer comLayer= getCommunicationLayer();

		URL url= comLayer.generateCSUrl(path.toString());
		HttpURLConnection connection= comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url, null, EMPTY_STRING_MAP);

		InputStreamReader inputStreamReader= new InputStreamReader(connection.getInputStream(), "UTF-8");
		JSONObject result= new JSONObject(new JSONTokener(inputStreamReader));

		return result.getJSONArray("Access");
	}

	public JSONArray getStorageStatistic(String keyForUser, int howManyHrsBack) throws Exception
	{
		if(endpointIsS3()) throw new RiakCSException("Not supported by AWS S3");

		iso8601DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
		
		Date endTime  = new Date(System.currentTimeMillis());
		Date startTime= new Date(System.currentTimeMillis()-(howManyHrsBack*60*60*1000));

		StringBuffer path= new StringBuffer();
		path.append("/usage");
		path.append("/");
		path.append(keyForUser);
		path.append("/bj");
		path.append("/");
		path.append(iso8601DateFormat.format(startTime));
		path.append("/");
		path.append(iso8601DateFormat.format(endTime));

		CommunicationLayer comLayer= getCommunicationLayer();

		URL url= comLayer.generateCSUrl(path.toString());
		HttpURLConnection connection= comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url, null, EMPTY_STRING_MAP);

		InputStreamReader inputStreamReader= new InputStreamReader(connection.getInputStream(), "UTF-8");
		JSONObject result= new JSONObject(new JSONTokener(inputStreamReader));

		return result.getJSONArray("Storage");
	}



	private CommunicationLayer getCommunicationLayer()
	{
		return new CommunicationLayer(csAccessKey, csSecretKey, csEndpoint, useSSL, debugModeEnabled);
	}

	private String getInputStreamAsString(InputStream is) throws IOException
	{
		StringBuffer responseBody= new StringBuffer();
		BufferedReader reader= new BufferedReader(new InputStreamReader(is));
		String line= null;
		while ((line= reader.readLine()) != null)
		{
			responseBody.append(line + "\n");
		}
		reader.close();

		if (debugModeEnabled) System.out.println("Body:\n" + responseBody + "\n");

		return responseBody.toString();
	}



}
