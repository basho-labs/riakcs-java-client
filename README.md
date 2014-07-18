Lightweight Java library to interact with a CF Riak CS instance.

## Pre-requisites

* Maven
* JDK version >= 1.6

## Operation

* Update the contents of `src/examples/com/basho/riakcs/client/CSCredentials.Riak.properties` to include the key/id from `VCAP_SERVICES`, and set the hostname to `p-riakcs.yourhostname`
* `mvn clean install`
* `java -jar target/riak-cs-client-0.0.1-SNAPSHOT-jar-with-dependencies.jar`

### Bucket APIs

    void       createBucket(String bucketName)
    JSONObject listBuckets()
    boolean    isBucketAccessible(String bucketName)
    JSONObject getACLForBucket(String bucketName)
    void       deleteBucket(String bucketName)

### Object APIs

    void       createObject(String bucketName, String objectKey, InputStream dataInputStream, Map<String, String> headers, Map<String, String> metadata)
    JSONObject listObjects(String bucketName)
    JSONObject listObjectNames(String bucketName)
    JSONObject getObject(String bucketName, String objectKey)
    JSONObject getObject(String bucketName, String objectKey, OutputStream dataOutputStream)
    JSONObject getObjectInfo(String bucketName, String objectKey)
    JSONObject getACLForObject(String bucketName, String objectKey)
    void       deleteObject(String bucketName, String objectKey)

License forthcoming.
