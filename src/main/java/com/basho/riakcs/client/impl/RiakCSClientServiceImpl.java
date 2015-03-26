/*
 * Copyright (c) 2012 Basho Technologies, Inc. All Rights Reserved. This file is provided to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.basho.riakcs.client.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SimpleTimeZone;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONRuntimeException;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.basho.riakcs.client.api.RiakCSClient;
import com.basho.riakcs.client.api.RiakCSException;
import com.basho.riakcs.client.impl.RiakCSClientImpl.Permission;

public class RiakCSClientServiceImpl {
	private final static Map<String, String> EMPTY_STRING_MAP = new HashMap<String, String>();

	private static final SimpleDateFormat iso8601DateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");

	private String csAccessKey = null;
	private String csSecretKey = null;

	private String csEndpoint = "s3.amazonaws.com";
	private boolean useSSL = true;

	private boolean debugModeEnabled = false;

	public RiakCSClientServiceImpl(String csAccessKey, String csSecretKey, String csEndpoint, boolean useSSL) {
		this.csAccessKey = csAccessKey;
		this.csSecretKey = csSecretKey;
		this.csEndpoint = csEndpoint;
		this.useSSL = useSSL;
	}

	public RiakCSClientServiceImpl(String csAccessKey, String csSecretKey) {
		this.csAccessKey = csAccessKey;
		this.csSecretKey = csSecretKey;
	}

	public void enableDebugOutput() {
		debugModeEnabled = true;
	}

	public boolean endpointIsS3() {
		return csEndpoint.contains("s3.amazonaws.com");
	}

	public JSONObject createUser(String fullname, String emailAddress) {
		if (endpointIsS3())
			throw new RiakCSException("Not Supported on AWS S3");

		JSONObject result = null;

		try {
			JSONObject postData = new JSONObject();
			postData.put("email", emailAddress);
			postData.put("name", fullname);

			InputStream dataInputStream = new ByteArrayInputStream(postData.toString().getBytes("UTF-8"));

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json");
			headers.put("Content-Length", String.valueOf(dataInputStream.available()));

			CommunicationLayer comLayer = getCommunicationLayer();

			URL url = comLayer.generateCSUrl("", "user", EMPTY_STRING_MAP);
			HttpURLConnection connection = comLayer.makeCall(CommunicationLayer.HttpMethod.POST, url, dataInputStream,
					headers);
			InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");

			result = new JSONObject(new JSONTokener(inputStreamReader));

		} catch (Exception e) {
			throw new RiakCSException(e);
		}

		return result;
	}

	public static enum UserListMode {
		ALL, ENABLED_ONLY, DISABLED_ONLY
	};

	public JSONObject listUsers(UserListMode listMode) {
		if (endpointIsS3())
			throw new RiakCSException("Not Supported on AWS S3");

		JSONObject result = null;

		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Accept", "application/json");

			String filterArgument = "";
			if (listMode == UserListMode.ENABLED_ONLY)
				filterArgument = "?status=enabled";
			else if (listMode == UserListMode.DISABLED_ONLY)
				filterArgument = "?status=disabled";

			CommunicationLayer comLayer = getCommunicationLayer();

			URL url = comLayer.generateCSUrl("/riak-cs/users" + filterArgument);
			HttpURLConnection connection = comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url, null, headers);
			InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");

			JSONArray userList = new JSONArray();

			BufferedReader reader = new BufferedReader(inputStreamReader);
			for (String line; (line = reader.readLine()) != null;) {
				if (line.startsWith("[")) {
					JSONArray aUserlist = new JSONArray(new JSONTokener(line));
					for (int pt = 0; pt < aUserlist.length(); pt++)
						userList.put(aUserlist.getJSONObject(pt));
				}
			}

			result = new JSONObject();
			result.put("userlist", userList);

		} catch (Exception e) {
			throw new RiakCSException(e);
		}

		return result;
	}

	public JSONObject getUserInfo(String key_id) {
		if (endpointIsS3())
			throw new RiakCSException("Not Supported on AWS S3");

		JSONObject result = null;

		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Accept", "application/json");

			CommunicationLayer comLayer = getCommunicationLayer();

			URL url = comLayer.generateCSUrl("/riak-cs/user/" + key_id);
			HttpURLConnection connection = comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url, null, headers);
			InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");

			result = new JSONObject(new JSONTokener(inputStreamReader));

		} catch (Exception e) {
			throw new RiakCSException(e);
		}

		return result;
	}

	public JSONObject getMyUserInfo() {
		if (endpointIsS3())
			throw new RiakCSException("Not Supported on AWS S3");

		JSONObject result = null;

		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Accept", "application/json");

			CommunicationLayer comLayer = getCommunicationLayer();

			URL url = comLayer.generateCSUrl("/riak-cs/user");
			HttpURLConnection connection = comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url, null, headers);
			InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");

			result = new JSONObject(new JSONTokener(inputStreamReader));

		} catch (Exception e) {
			throw new RiakCSException(e);
		}

		return result;
	}

	public void enableUser(String key_id) {
		enableDisableUser(key_id, true);
	}

	public void disableUser(String key_id) {
		enableDisableUser(key_id, false);
	}

	private void enableDisableUser(String key_id, boolean enable) {
		if (endpointIsS3())
			throw new RiakCSException("Not Supported on AWS S3");

		try {
			JSONObject postData = new JSONObject();
			if (enable)
				postData.put("status", "enabled");
			else
				postData.put("status", "disabled");

			InputStream dataInputStream = new ByteArrayInputStream(postData.toString().getBytes("UTF-8"));

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json");
			headers.put("Content-Length", String.valueOf(dataInputStream.available()));

			CommunicationLayer comLayer = getCommunicationLayer();

			URL url = comLayer.generateCSUrl("/riak-cs/user/" + key_id);
			comLayer.makeCall(CommunicationLayer.HttpMethod.PUT, url, dataInputStream, headers);

		} catch (Exception e) {
			throw new RiakCSException(e);
		}
	}

	public void createBucket(String bucketName) {
		CommunicationLayer comLayer = getCommunicationLayer();

		try {
			URL url = comLayer.generateCSUrl(bucketName, "", EMPTY_STRING_MAP);
			comLayer.makeCall(CommunicationLayer.HttpMethod.PUT, url);

		} catch (Exception e) {
			throw new RiakCSException(e);
		}
	}

	public JSONObject listBuckets() {
		CommunicationLayer comLayer = getCommunicationLayer();

		JSONObject bucketList = null;

		try {
			URL url = comLayer.generateCSUrl("", "", EMPTY_STRING_MAP);
			HttpURLConnection conn = comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url);
			Document xmlDoc = XMLUtils.parseToDocument(conn.getInputStream(), debugModeEnabled);

			bucketList = new JSONObject();

			for (Node node : XMLUtils.xpathToNodeList("//Buckets/Bucket", xmlDoc)) {
				JSONObject bucket = new JSONObject();
				bucket.put("name", XMLUtils.xpathToContent("Name", node));
				bucket.put("creationDate", XMLUtils.xpathToContent("CreationDate", node));

				bucketList.append("bucketList", bucket);
			}

			JSONObject owner = new JSONObject();
			owner.put("id", XMLUtils.xpathToContent("//Owner/ID", xmlDoc));
			owner.put("displayName", XMLUtils.xpathToContent("//Owner/DisplayName", xmlDoc));

			bucketList.put("owner", owner);

		} catch (Exception e) {
			throw new RiakCSException(e);
		}

		return bucketList;
	}

	public boolean isBucketAccessible(String bucketName) {
		boolean result = false;

		try {
			CommunicationLayer comLayer = getCommunicationLayer();

			URL url = comLayer.generateCSUrl(bucketName, "", EMPTY_STRING_MAP);
			HttpURLConnection conn = comLayer.makeCall(CommunicationLayer.HttpMethod.HEAD, url);

			for (String headerName : conn.getHeaderFields().keySet()) {
				// .. just check for something ..
				if (headerName != null && headerName.equals("Server"))
					result = true;
			}

		} catch (Exception e) {
			if (e.getMessage().contains("404"))
				return false;
			if (e.getMessage().contains("403"))
				return false;

			throw new RiakCSException(e);
		}

		return result;
	}

	public void deleteBucket(String bucketName) {
		try {
			CommunicationLayer comLayer = getCommunicationLayer();

			URL url = comLayer.generateCSUrl(bucketName, "", EMPTY_STRING_MAP);
			comLayer.makeCall(CommunicationLayer.HttpMethod.DELETE, url);

		} catch (Exception e) {
			throw new RiakCSException(e);
		}
	}

	public void createObject(String bucketName, String objectKey, InputStream dataInputStream,
			Map<String, String> headers, Map<String, String> metadata) {
		createObject(bucketName, objectKey, dataInputStream, headers, metadata, null);
	}

	public void createObject(String bucketName, String objectKey, InputStream dataInputStream,
			Map<String, String> headers, Map<String, String> metadata, String policy) {
		try {
			if (headers == null)
				headers = new HashMap<String, String>();

			if (!headers.containsKey("Content-Length"))
				headers.put("Content-Length", String.valueOf(dataInputStream.available()));
			if (!headers.containsKey("Content-Type"))
				headers.put("Content-Type", "application/octet-stream");

			// Add user-meta info
			if (metadata != null) {
				for (Map.Entry<String, String> metadataHeader : metadata.entrySet()) {
					headers.put("x-amz-meta-" + metadataHeader.getKey(), metadataHeader.getValue());
				}
			}

			CommunicationLayer comLayer = getCommunicationLayer();

			URL url = comLayer.generateCSUrl(bucketName, objectKey, EMPTY_STRING_MAP);
			comLayer.makeCall(CommunicationLayer.HttpMethod.PUT, url, dataInputStream, headers);

		} catch (Exception e) {
			throw new RiakCSException(e);
		}
	}

	public JSONObject getObject(String bucketName, String objectKey) {
		return getObject(bucketName, objectKey, null);
	}

	public JSONObject getObject(String bucketName, String objectKey, OutputStream dataOutputStream) {
		JSONObject object = null;

		try {
			CommunicationLayer comLayer = getCommunicationLayer();

			URL url = comLayer.generateCSUrl(bucketName, objectKey, EMPTY_STRING_MAP);
			HttpURLConnection conn = comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url, null, EMPTY_STRING_MAP);

			object = extractMetaInfoForObject(objectKey, conn);

			// Download data
			if (dataOutputStream != null) {
				InputStream inputStream = conn.getInputStream();
				byte[] buffer = new byte[8192];
				int count = -1;
				while ((count = inputStream.read(buffer)) != -1) {
					dataOutputStream.write(buffer, 0, count);
				}
				dataOutputStream.close();
				inputStream.close();
			} else {
				object.put("body", getInputStreamAsString(conn.getInputStream()));
			}

		} catch (Exception e) {
			throw new RiakCSException(e);
		}

		return object;
	}

	public JSONObject getObjectInfo(String bucketName, String objectKey) {
		JSONObject object = null;

		try {
			CommunicationLayer comLayer = getCommunicationLayer();

			URL url = comLayer.generateCSUrl(bucketName, objectKey, EMPTY_STRING_MAP);
			HttpURLConnection conn = comLayer.makeCall(CommunicationLayer.HttpMethod.HEAD, url, null, EMPTY_STRING_MAP);

			object = extractMetaInfoForObject(objectKey, conn);

		} catch (Exception e) {
			throw new RiakCSException(e);
		}

		return object;
	}

	private JSONObject extractMetaInfoForObject(String objectKey, HttpURLConnection conn) {
		Map<String, String> responseHeaders = new HashMap<String, String>();
		JSONObject metadata = new JSONObject();

		for (String headerName : conn.getHeaderFields().keySet()) {
			if (headerName == null)
				continue;

			if (headerName.toLowerCase().startsWith("x-amz-meta")) {
				metadata.put(headerName.substring(11), conn.getHeaderFields().get(headerName).get(0));
			} else {
				responseHeaders.put(headerName, conn.getHeaderFields().get(headerName).get(0));
			}
		}

		JSONObject object = new JSONObject();
		object.put("key", objectKey);
		object.put("etag", responseHeaders.get("ETag"));
		object.put("lastModified", responseHeaders.get("Last-Modified"));
		object.put("size", responseHeaders.get("Content-Length"));
		object.put("contenttype", responseHeaders.get("Content-Type"));
		object.put("metadata", metadata);
		return object;
	}

	public JSONObject listObjects(String bucketName) {
		return listObjects(bucketName, true);
	}

	public JSONObject listObjects(String bucketName, boolean extendedList) {
		// TODO switch to more streaming type of mode
		JSONObject result = new JSONObject();

		try {
			result.put("bucketName", bucketName);

			boolean isTruncated = true;
			while (isTruncated) {
				CommunicationLayer comLayer = getCommunicationLayer();

				URL url = comLayer.generateCSUrl(bucketName, "", EMPTY_STRING_MAP);
				HttpURLConnection conn = comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url);

				result.put("objectList", new JSONArray());

				Document xmlDoc = XMLUtils.parseToDocument(conn.getInputStream(), debugModeEnabled);
				List<Node> nodeList = XMLUtils.xpathToNodeList("//Contents", xmlDoc);
				for (Node node : nodeList) {
					JSONObject object = new JSONObject();
					object.put("key", XMLUtils.xpathToContent("Key", node));
					if (extendedList) {
						object.put("size", XMLUtils.xpathToContent("Size", node));
						object.put("lastModified", XMLUtils.xpathToContent("LastModified", node));
						object.put("etag", XMLUtils.xpathToContent("ETag", node));

						JSONObject owner = new JSONObject();
						owner.put("id", XMLUtils.xpathToContent("Owner/ID", node));
						owner.put("displayName", XMLUtils.xpathToContent("Owner/DisplayName", node));
						object.put("owner", owner);
					}

					result.append("objectList", object);
				}

				isTruncated = "true".equals(XMLUtils.xpathToContent("//IsTruncated", xmlDoc));
			}

		} catch (Exception e) {
			throw new RiakCSException(e);
		}

		return result;
	}

	public void deleteObject(String bucketName, String objectKey) {
		try {
			CommunicationLayer comLayer = getCommunicationLayer();

			URL url = comLayer.generateCSUrl(bucketName, objectKey, EMPTY_STRING_MAP);
			comLayer.makeCall(CommunicationLayer.HttpMethod.DELETE, url);

		} catch (Exception e) {
			throw new RiakCSException(e);
		}
	}

	public void setCannedACLForBucket(String bucketName, String cannedAcl) {
		setCannedACLImplt(bucketName, "", cannedAcl);
	}

	public void setCannedACLForObject(String bucketName, String objectKey, String cannedAcl) {
		setCannedACLImplt(bucketName, objectKey, cannedAcl);
	}

	public void setCannedACLImplt(String bucketName, String objectKey, String cannedAcl) {
		try {
			CommunicationLayer comLayer = getCommunicationLayer();

			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("acl", null);
			URL url = comLayer.generateCSUrl(bucketName, objectKey, parameters);

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("x-amz-acl", cannedAcl);
			comLayer.makeCall(CommunicationLayer.HttpMethod.PUT, url, null, headers);

		} catch (Exception e) {
			throw new RiakCSException(e);
		}
	}

	public void addAdditionalACLToBucket(String bucketName, String emailAddress, Permission permission) {
		try {
			JSONObject oldACL = getACLForBucket(bucketName);

			JSONObject newGrant = new JSONObject();
			JSONObject grantee = new JSONObject();
			grantee.put("emailAddress", emailAddress);
			newGrant.put("grantee", grantee);
			newGrant.put("permission", permission);

			oldACL.append("grantsList", newGrant);

			addAdditionalACLImpl(bucketName, "", oldACL);

		} catch (Exception e) {
			throw new RiakCSException(e);
		}
	}

	public void addAdditionalACLToObject(String bucketName, String objectKey, String emailAddress, Permission permission) {
		try {
			JSONObject oldACL = getACLForObject(bucketName, objectKey);

			JSONObject newGrant = new JSONObject();
			JSONObject grantee = new JSONObject();
			grantee.put("emailAddress", emailAddress);
			newGrant.put("grantee", grantee);
			newGrant.put("permission", permission);

			oldACL.append("grantsList", newGrant);

			addAdditionalACLImpl(bucketName, objectKey, oldACL);

		} catch (Exception e) {
			throw new RiakCSException(e);
		}
	}

	private void addAdditionalACLImpl(String bucketName, String objectKey, JSONObject acl) throws Exception {
		StringBuffer aclText = new StringBuffer();

		aclText.append("<AccessControlPolicy>");
		aclText.append("<Owner><ID>" + acl.getJSONObject("owner").getString("id") + "</ID>");
		aclText.append("<DisplayName>" + acl.getJSONObject("owner").getString("displayName") + "</DisplayName></Owner>");
		aclText.append("<AccessControlList>");

		JSONArray grantsList = acl.getJSONArray("grantsList");
		for (int pt = 0; pt < grantsList.length(); pt++) {
			JSONObject grant = grantsList.getJSONObject(pt);

			aclText.append("<Grant><Permission>" + grant.getString("permission") + "</Permission>");
			aclText.append("<Grantee");
			aclText.append(" ").append("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");

			if (grant.getJSONObject("grantee").has("id")) {
				aclText.append(" xsi:type='CanonicalUser'>");
				aclText.append("<ID>" + grant.getJSONObject("grantee").getString("id") + "</ID>");
			} else {
				aclText.append(" xsi:type='AmazonCustomerByEmail'>");
				aclText.append("<EmailAddress>" + grant.getJSONObject("grantee").getString("emailAddress")
						+ "</EmailAddress>");
			}

			aclText.append("</Grantee>");
			aclText.append("</Grant>");
		}
		aclText.append("</AccessControlList>");
		aclText.append("</AccessControlPolicy>");

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("acl", null);

		InputStream dataInputStream = new ByteArrayInputStream(aclText.toString().getBytes("UTF-8"));

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/xml");

		CommunicationLayer comLayer = getCommunicationLayer();

		URL url = comLayer.generateCSUrl(bucketName, objectKey, parameters);
		comLayer.makeCall(CommunicationLayer.HttpMethod.PUT, url, dataInputStream, headers);
	}

	public JSONObject getACLForBucket(String bucketName) {
		JSONObject result = null;

		try {
			result = getAclImpl(bucketName, "");

		} catch (Exception e) {
			throw new RiakCSException(e);
		}

		return result;
	}

	public JSONObject getACLForObject(String bucketName, String objectKey) {
		JSONObject result = null;

		try {
			result = getAclImpl(bucketName, objectKey);

		} catch (Exception e) {
			throw new RiakCSException(e);
		}

		return result;
	}

	private JSONObject getAclImpl(String bucketName, String objectKey) throws Exception {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("acl", null);

		CommunicationLayer comLayer = getCommunicationLayer();

		URL url = comLayer.generateCSUrl(bucketName, objectKey, parameters);
		HttpURLConnection conn = comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url);

		Document xmlDoc = XMLUtils.parseToDocument(conn.getInputStream(), debugModeEnabled);
		JSONObject acl = new JSONObject();

		for (Node grantNode : XMLUtils.xpathToNodeList("//Grant", xmlDoc)) {
			JSONObject grant = new JSONObject();
			grant.put("permission", XMLUtils.xpathToContent("Permission", grantNode));

			String type = XMLUtils.xpathToContent("Grantee/@type", grantNode);

			JSONObject grantee = new JSONObject();
			grantee.put("type", type);

			if (type.equals("Group")) {
				grantee.put("uri", XMLUtils.xpathToContent("Grantee/URI", grantNode));
			} else {
				grantee.put("id", XMLUtils.xpathToContent("Grantee/ID", grantNode));
				grantee.put("displayName", XMLUtils.xpathToContent("Grantee/DisplayName", grantNode));
			}
			grant.put("grantee", grantee);
			acl.append("grantsList", grant);
		}

		JSONObject owner = new JSONObject();
		owner.put("id", XMLUtils.xpathToContent("//Owner/ID", xmlDoc));
		owner.put("displayName", XMLUtils.xpathToContent("//Owner/DisplayName", xmlDoc));
		acl.put("owner", owner);
		return acl;
	}

	public JSONObject getAccessStatistic(String keyForUser, int howManyHrsBack) {
		if (endpointIsS3())
			throw new RiakCSException("Not supported by AWS S3");

		JSONObject result = null;

		try {
			iso8601DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));

			Date endTime = new Date(System.currentTimeMillis());
			Date startTime = new Date(System.currentTimeMillis() - (howManyHrsBack * 60 * 60 * 1000));

			StringBuffer path = new StringBuffer();
			path.append("/usage");
			path.append("/");
			path.append(keyForUser);
			path.append("/aj");
			path.append("/");
			path.append(iso8601DateFormat.format(startTime));
			path.append("/");
			path.append(iso8601DateFormat.format(endTime));

			CommunicationLayer comLayer = getCommunicationLayer();

			URL url = comLayer.generateCSUrl(path.toString());
			HttpURLConnection connection = comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url, null,
					EMPTY_STRING_MAP);

			InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");
			result = new JSONObject(new JSONTokener(inputStreamReader));
			result = result.getJSONObject("Access");

		} catch (Exception e) {
			throw new RiakCSException(e);
		}

		return result;
	}

	public JSONObject getStorageStatistic(String keyForUser, int howManyHrsBack) {
		if (endpointIsS3())
			throw new RiakCSException("Not supported by AWS S3");

		JSONObject result = null;

		try {
			iso8601DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));

			Date endTime = new Date(System.currentTimeMillis());
			Date startTime = new Date(System.currentTimeMillis() - (howManyHrsBack * 60 * 60 * 1000));

			StringBuffer path = new StringBuffer();
			path.append("/usage");
			path.append("/");
			path.append(keyForUser);
			path.append("/bj");
			path.append("/");
			path.append(iso8601DateFormat.format(startTime));
			path.append("/");
			path.append(iso8601DateFormat.format(endTime));

			CommunicationLayer comLayer = getCommunicationLayer();

			URL url = comLayer.generateCSUrl(path.toString());
			HttpURLConnection connection = comLayer.makeCall(CommunicationLayer.HttpMethod.GET, url, null,
					EMPTY_STRING_MAP);

			InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");
			result = new JSONObject(new JSONTokener(inputStreamReader));
			result = result.getJSONObject("Storage");

		} catch (Exception e) {
			throw new RiakCSException(e);
		}

		return result;
	}

	public void removeContentOfBucket(String bucketName) {
		try {
			JSONObject response = listObjects(bucketName, false);
			JSONArray resultList = response.getJSONArray("objectList");

			// if (debugModeEnabled) System.out.println("Number of Objects to delete: "+ resultList.length() + "\n");
			System.out.println("Number of Objects to delete: " + resultList.length() + "\n");

			for (int pt = 0; pt < resultList.length(); pt++) {
				String key = resultList.getJSONObject(pt).getString("key");
				deleteObject(bucketName, key);
			}

		} catch (JSONRuntimeException e) {
			throw new RiakCSException(e);
		}

	}

	public void uploadContentOfDirectory(File fromDirectory, String toBucket) {
		Set<String> objectNames = new HashSet<String>();

		try {
			uploadContentOfDirectoryImpl(fromDirectory, toBucket, "", objectNames);
			System.out.println("Number of Objects uploaded: " + objectNames.size());

		} catch (Exception e) {
			throw new RiakCSException(e);
		}
	}

	private void uploadContentOfDirectoryImpl(File fromDirectory, String toBucket, String subFolderPath,
			Set<String> objectNames) throws Exception {
		if (fromDirectory.isDirectory() == false)
			throw new RiakCSException(fromDirectory + " is not a valid directory");

		File[] folderContent = fromDirectory.listFiles();

		for (File item : folderContent) {
			if (item.isHidden())
				continue;

			if (item.isDirectory()) {
				uploadContentOfDirectoryImpl(item, toBucket, subFolderPath + fixName(item.getName()) + '/', objectNames);
			} else {
				String objectName = subFolderPath + fixName(item.getName());

				if (objectNames.contains(objectName))
					throw new RiakCSException("ObjectName/Key conflict: " + objectName);
				objectNames.add(objectName);

				FileInputStream inputStream = new FileInputStream(item);
				createObject(toBucket, objectName, inputStream, null, null);
				inputStream.close();
			}
		}

	}

	private String fixName(String name) {
		// There are certain naming restrictions for S3 objects
		StringBuffer result = new StringBuffer();

		for (int pt = 0; pt < name.length(); pt++) {
			char chr = name.charAt(pt);
			if (Character.isLetter(chr) || Character.isDigit(chr) || chr == '.' || chr == '-' || chr == '_'
					|| chr == '/')
				result.append(chr);
		}

		if (result.length() == 0)
			throw new RiakCSException("Funny name: " + name);

		return result.toString();
	}

	public static void copyBucketBetweenSystems(RiakCSClient fromSystem, String fromBucket, RiakCSClient toSystem,
			String toBucket) {
		try {
			JSONObject response = fromSystem.listObjectNames(fromBucket);
			JSONArray resultList = response.getJSONArray("objectList");

			System.out.println("Number of Objects to transfer: " + resultList.length() + "\n");

			for (int pt = 0; pt < resultList.length(); pt++) {
				String key = resultList.getJSONObject(pt).getString("key");
				File tempFile = File.createTempFile("cscopy-", ".bin");

				// Retrieve Object
				FileOutputStream outputStream = new FileOutputStream(tempFile);
				JSONObject objectData = fromSystem.getObject(fromBucket, key, outputStream);
				outputStream.close();

				// Upload Object
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", objectData.getString("contenttype"));

				Map<String, String> metadata = null;
				if (objectData.has("metadata") && objectData.getJSONObject("metadata").length() > 0) {
					metadata = new HashMap<String, String>();

					JSONObject metadataRaw = objectData.getJSONObject("metadata");
					String[] metaKeys = JSONObject.getNames(metadataRaw);
					for (String metaKey : metaKeys) {
						metadata.put(metaKey, metadataRaw.getString(metaKey));
					}
				}

				FileInputStream inputStream = new FileInputStream(tempFile);
				toSystem.createObject(toBucket, key, inputStream, headers, metadata);
				inputStream.close();

				tempFile.delete();
			}

		} catch (Exception e) {
			throw new RiakCSException(e);
		}
	}

	private CommunicationLayer getCommunicationLayer() {
		return new CommunicationLayer(csAccessKey, csSecretKey, csEndpoint, useSSL, debugModeEnabled);
	}

	private String getInputStreamAsString(InputStream is) throws IOException {
		StringBuffer responseBody = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = reader.readLine()) != null) {
			responseBody.append(line + "\n");
		}
		reader.close();

		if (debugModeEnabled)
			System.out.println("Body:\n" + responseBody + "\n");

		return responseBody.toString();
	}

}
