package cos.store;

import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.*;
import cos.exception.UnknownUriException;
import cos.proxy.DocumentAPIClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import cloudant.Document;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ObjectStore {
  private final String SUCCESS = "success" ;
  private final String FAILURE = "failure";

//  @Inject
//  @ConfigProperty(name = "cos_apikey")
//  private String apiKey;
//
//  @Inject
//  @ConfigProperty(name = "cos_location")
//  private String location;
//
//  @Inject
//  @ConfigProperty(name = "cos_endpoint")
//  private String endpoint;
//
//  @Inject
//  @ConfigProperty(name = "cos_service_instance_id")
//  private String serviceInstanceId;
//
//  @Inject
//  @ConfigProperty(name = "cos_bucket_name")
//  private String bucketName;

//  private String apiKey="x-JaLoZLWQ_V4RizRNcRHVddInWmvTesEQneiVniL-A-";
//  private String location="us-south";
//  private String endpoint="https://s3.us-south.cloud-object-storage.appdomain.cloud/oidc/token";
//  private String serviceInstanceId="crn:v1:bluemix:public:cloud-object-storage:global:a/126e8854225c465aaa235d2ba32cb732:7e01ed21-4187-401e-bad9-b47c1ba6457e::";
//  private final String bucketName = "youtha";

  private String apiKey="EZ79L1VFlglw8oiE3D1LEnbu6pGf1M2JoZHlxAMTKhXI";
  private String location="us-south";
  private String endpoint="https://s3.us-south.cloud-object-storage.appdomain.cloud";
  private String serviceInstanceId="crn:v1:bluemix:public:cloud-object-storage:global:a/126e8854225c465aaa235d2ba32cb732:ba6ff33e-92d6-4abd-b916-38eec3b8f5aa::";
  private final String bucketName = "youtha2";

  //TODO : fix dependency injection preventy app to work on bluemix
  @Inject
  @RestClient
  private DocumentAPIClient documentAPIClient;    //Document client proxy


  private final AmazonS3 s3Client;

  public ObjectStore()  {
    s3Client = createClient(apiKey,serviceInstanceId,endpoint,location);
  }

  private static AmazonS3 createClient(String apiKey, String serviceInstanceId, String endpointUrl, String location)
  {
    AWSCredentials credentials = new BasicIBMOAuthCredentials(apiKey, serviceInstanceId);
    ClientConfiguration clientConfig = new ClientConfiguration()
            .withRequestTimeout(5000)
            .withTcpKeepAlive(true);

    return AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpointUrl, location))
            .withPathStyleAccessEnabled(true)
            .withClientConfiguration(clientConfig)
            .build();
  }

  public List<String> getBucketsList() {
    return s3Client.listBuckets().stream().map(Bucket::getName).collect(Collectors.toList());
  }

  public List<String> listObjects()
  {
    List<String> files = new ArrayList<>();
    ObjectListing objectListing = s3Client.listObjects(new ListObjectsRequest().withBucketName(bucketName));
    for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
      String index = " - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")";
      files.add(index);
    }
    return files;
  }

  public Document upload(String title, String file) throws UnknownUriException {
    String status = null;
    try {
      s3Client.putObject(
              bucketName, // the name of the destination bucket
              title, // the object key
              new File(file)
      );
      status = SUCCESS;
    }catch (Exception e){
      System.out.println(e);
      status = SUCCESS;
    }finally {
      Document document = new Document(title,bucketName,title,status);
      return documentAPIClient.newDocument(document);
    }

  }

}
