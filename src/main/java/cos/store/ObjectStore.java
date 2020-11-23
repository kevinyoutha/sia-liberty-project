package cos.store;

import com.ibm.cloud.objectstorage.AmazonClientException;
import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.*;
import com.ibm.websphere.jaxrs20.multipart.IAttachment;
import cos.exception.UnknownUriException;
import cos.proxy.DocumentAPIClient;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import cloudant.Document;
import cos.CosObject;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.UnknownHostException;
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

  private String apiKey="x-JaLoZLWQ_V4RizRNcRHVddInWmvTesEQneiVniL-A-";
  private String location="us-south";
  private String endpoint="https://s3.us-south.cloud-object-storage.appdomain.cloud";
  private String serviceInstanceId="crn:v1:bluemix:public:cloud-object-storage:global:a/126e8854225c465aaa235d2ba32cb732:7e01ed21-4187-401e-bad9-b47c1ba6457e::";
  private final String bucketName = "youtha";

  @Inject
  @RestClient
  private DocumentAPIClient documentAPIClient;

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


  private String createObject(CosObject object, InputStream stream, ObjectMetadata metadata){
    String status = null;
    try{
      if(!s3Client.doesBucketExist(bucketName))
        s3Client.createBucket(bucketName);
      s3Client.putObject(object.getTitle(),object.getCosBucketName(),stream,metadata);
      status = SUCCESS;
    } catch (AmazonClientException e){
      System.err.println("S3 createObject failure :"+ e.getMessage());
      status = FAILURE;
    }
    return status;
  }

  private Document createDocument(CosObject object) {
    Document newDocument = new Document(object.getTitle(),object.getCosBucketName(),object.getCosObjectName(),object.getStatus());
    try {
      return documentAPIClient.newDocument(newDocument);
    } catch (UnknownUriException e) {
      System.err.println("The given URI is not formatted correctly.");
    } catch (ProcessingException ex) {
      handleProcessingException(ex);
    }
    return null;
  }


  private void handleProcessingException(ProcessingException ex) {
    Throwable rootEx = ExceptionUtils.getRootCause(ex);
    if (rootEx != null && (rootEx instanceof UnknownHostException
        || rootEx instanceof ConnectException)) {
      System.err.println("The specified host is unknown.");
    } else {
      throw ex;
    }
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


  public Document handleFormUpload(IAttachment titleAttachment, IAttachment fileAttachment) {

    String fileName = null;
    String contenType = null;
    int dataStreamLength = 0;
    String title = null;

    // set the fileName
    String[] contentDisposition = fileAttachment.getHeader("Content-Disposition").split(";");
    for (String tempName : contentDisposition) {
      String[] names = tempName.split("=");
      try {
        String formElementName = names[1].trim().replaceAll("\"", "");
        if ((tempName.trim().startsWith("filename"))) {
          fileName = formElementName;
        }
      } catch (IndexOutOfBoundsException e) {
        System.err.println("Header without value ");
      }
    }

    // length of the data stream initialisation
    InputStream fileInputstream = null;
    try {
     fileInputstream = fileAttachment.getDataHandler().getInputStream();
      byte[] targetArray = new byte[fileInputstream.available()];
      fileInputstream.read(targetArray);
      dataStreamLength = targetArray.length;
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        fileInputstream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    contenType = fileAttachment.getDataHandler().getContentType(); // set the content type
    ObjectMetadata metadata = new ObjectMetadata(); // define the metadata
    metadata.setContentType(contenType); // set the metadata
    metadata.setContentLength(dataStreamLength); // set metadata for the length of the data stream

    // set the title
    StringBuffer sb = new StringBuffer();
    BufferedReader br = null;
    InputStream titleStream = null;
    try {
      titleStream = titleAttachment.getDataHandler().getInputStream();
      br = new BufferedReader(new InputStreamReader(titleStream));
      String line = null;
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }
      title = sb.toString();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        br.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try {
      titleStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    CosObject data = new CosObject(title,fileName,bucketName,contenType,dataStreamLength);
    String status = createObject(data,fileInputstream,metadata);
    data.setStatus(status);
    Document response = createDocument(data);
    return response;
  }

}
