## Riak CS Java Client

Lightweight Java library for Amazon S3 and Riak CS. No external dependencies.

Based on code from the book: "Programming Amazon Web Services"
http://shop.oreilly.com/product/9780596515812.do

Example code can be found in: examples.com.basho.riakcs.client

User Management (Riak CS only)

    JSONObject createUser(String fullname, String emailAddress)
    JSONObject listUsers()
    JSONObject getUserInfo(String key_id)
    JSONObject getMyUserInfo()

Bucket APIs

    void createBucket(String bucketName)
    JSONObject listBuckets()
    boolean isBucketAccessible(String bucketName)
    JSONObject getACLForBucket(String bucketName)
    void deleteBucket(String bucketName)

Object APIs

    void createObject(String bucketName, String objectKey, InputStream dataInputStream, Map<String, String> headers, Map<String, String> metadata)
    JSONObject listObjects(String bucketName)
    JSONObject listObjectNames(String bucketName)
    JSONObject getObject(String bucketName, String objectKey)
    JSONObject getObject(String bucketName, String objectKey, OutputStream dataOutputStream)
    JSONObject getObjectInfo(String bucketName, String objectKey)
    JSONObject getACLForObject(String bucketName, String objectKey)
    void deleteObject(String bucketName, String objectKey)

ACL APIs: "Canned" ACLs for buckets and objects

    PERM_PRIVATE, PERM__READ, PERM__READ_WRITE, PERM_AUTHENTICATED_READ

    void setCannedACLForBucket(String bucketName, String cannedACL)
    void setCannedACLForObject(String bucketName, String objectKey, String cannedACL)

	
ACL APIs: "Regular" ACLs for buckets and objects

    READ, WRITE, READ_ACP, WRITE_ACP, FULL_CONTROL

    void addAdditionalACLToBucket(String bucketName, String emailAddress, Permission permission)
    void addAdditionalACLToObject(String bucketName, String objectKey, String emailAddress, Permission permission)

Statistic APIs (Riak CS only)

    JSONObject getAccessStatistic(String keyForUser, int howManyHrsBack)
    JSONObject getStorageStatistic(String keyForUser, int howManyHrsBack)

Tool APIs

    void removeBucketAndContent(String bucketName)
    void uploadContentOfDirectory(File fromDirectory, String toBucket)
    void copyBucketBetweenSystems(RiakCSClient fromSystem, String fromBucket, RiakCSClient toSystem, String toBucket)