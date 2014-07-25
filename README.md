Lightweight Java library to interact with a CF Riak CS instance.

## Pre-requisites

* Maven
* JDK version >= 1.6

## Run the Smoketest

Executing the compiled jar will run two suites of tests for the Bucket and Object APIs. This test creates a bucket, writes and read, and deletes the bucket.

1. Update the contents of `src/examples/com/basho/riakcs/client/CSCredentials.Riak.properties` with credentials from `VCAP_SERVICES`

        #Credentials for RiakCS Installation
        accessKey=access_key_id
        secretKey=secret_access_key
        
        #hostname:port for your RiakCS installation
        endPoint=hostname_from_uri #eg. p-riakcs.system-domain.com
        useHttps=false

1. `$ mvn clean install`
1. `$ java -jar target/riak-cs-client-0.0.1-SNAPSHOT-jar-with-dependencies.jar`

### SSL

If you set `useHttps=true` in `CSCredentials.Riak.properties` and your Riak CS deployment uses a self-signed certificate, executing the jar above will fail. On OSX you can work around this by adding the self-signed cert to your Java keystore:

1. Display the certificate

        $ openssl s_client -connect p-riakcs.system-domain.com:443 -showcerts
1. Copy from "-----BEGIN CER....................." to ".....................END CERTIFICATE-----" inclusively.
1. Paste this text into a new file eg. `p-riakcs.cer`
1. Save the cert to java keystore

        $ sudo keytool -importcert -alias dev -file p-riakcs.cer -keystore /Library/Java/Home/lib/security/cacerts

When asked for the keystore password, try `changeit`. Executing the jar should succeed now.

## Bucket APIs

    void       createBucket(String bucketName)
    JSONObject listBuckets()
    boolean    isBucketAccessible(String bucketName)
    JSONObject getACLForBucket(String bucketName)
    void       deleteBucket(String bucketName)

## Object APIs

    void       createObject(String bucketName, String objectKey, InputStream dataInputStream, Map<String, String> headers, Map<String, String> metadata)
    JSONObject listObjects(String bucketName)
    JSONObject listObjectNames(String bucketName)
    JSONObject getObject(String bucketName, String objectKey)
    JSONObject getObject(String bucketName, String objectKey, OutputStream dataOutputStream)
    JSONObject getObjectInfo(String bucketName, String objectKey)
    JSONObject getACLForObject(String bucketName, String objectKey)
    void       deleteObject(String bucketName, String objectKey)

License forthcoming.
