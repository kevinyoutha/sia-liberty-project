// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
// tag::manager[]
package io.openliberty.guides.cos;

import java.net.ConnectException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.ProcessingException;

import com.ibm.cloud.objectstorage.AmazonClientException;
import com.ibm.cloud.objectstorage.AmazonServiceException;
import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.cloud.objectstorage.services.s3.model.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.openliberty.guides.cos.client.CloudantClient;
import io.openliberty.guides.cos.client.UnknownUriException;
import io.openliberty.guides.cos.client.UnknownUriExceptionMapper;
import io.openliberty.guides.cos.model.DbData;

// tag::ApplicationScoped[]
@ApplicationScoped
// end::ApplicationScoped[]
public class CosService {
  private final String SUCCESS = "success" ;
  private final String FAILURE = "failure";
  private final String BUCKETNAME = "Documents";

//  @Inject
//  @ConfigProperty(name = "default.http.port")
//  String DEFAULT_PORT;

  @Inject
  String DEFAULT_PORT;


  @Inject
  @RestClient
  private CloudantClient cloudantClient;

  AmazonS3 s3Client;

  private CosService() throws NamingException {

    Object apiKeyConstant = new InitialContext().lookup("cos/api-key");
    String apiKey = (String) apiKeyConstant;

    Object locationConstant = new InitialContext().lookup("cos/location");
    String location = (String) locationConstant;

    Object endpointConstant = new InitialContext().lookup("cos/endpoint");
    String endpoint = (String) endpointConstant;

    Object serviceInstanceIdKeyConstant = new InitialContext().lookup("cos/service-instance-id");
    String serviceInstanceId = (String) serviceInstanceIdKeyConstant;

    s3Client = createClient(apiKey,serviceInstanceId,endpoint,location);
  }

  public static AmazonS3 createClient(String apiKey, String serviceInstanceId, String endpointUrl, String location)
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

  public static List<String> listObjects(AmazonS3 cosClient, String bucketName)
  {
    List<String> files = new ArrayList<>();
    System.out.println("Listing objects in bucket " + bucketName);
    ObjectListing objectListing = cosClient.listObjects(new ListObjectsRequest().withBucketName(bucketName));
    for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
      String index = " - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")";
      files.add(index);
    }
    return files;
  }

  public DbData upload(DbData data){
    try{
      if(!s3Client.doesBucketExist(BUCKETNAME))
        s3Client.createBucket(BUCKETNAME);
//      ObjectMetadata metadata = new ObjectMetadata();
//      metadata.setContentLength(fileText.length());
//      s3Client.putObject(data.getTitle(),data.getCosBucketName(),inputStream,metadata);
      data.setStatus(SUCCESS);
    }catch (AmazonServiceException e){
      data.setStatus(FAILURE);
    }catch (AmazonClientException e){
      data.setStatus(FAILURE);
    }
    return data;
  }


  public String get(String hostname, DbData dbData) {
    String  response = null;
    if (hostname.equals("localhost")) {
      response = persistWithDefaultHostName(dbData);
    } else {
      response = persistWithGivenHostName(hostname,dbData);
    }

    return response;
  }

  // tag::getPropertiesWithDefaultHostName[]
  private String  persistWithDefaultHostName(DbData dbData) {
    try {
      // tag::defaultRCGetProperties[]
      return cloudantClient.persist(dbData);
      // end::defaultRCGetProperties[]
    } catch (UnknownUriException e) {
      System.err.println("The given URI is not formatted correctly.");
    } catch (ProcessingException ex) {
      handleProcessingException(ex);
    }
    return null;
  }
  // end::getPropertiesWithDefaultHostName[]

  // tag::getPropertiesWithGivenHostName[]
  private String persistWithGivenHostName(String hostname, DbData dbData) {
    String customURIString = "http://" + hostname + ":" + DEFAULT_PORT + "/cloudant";
    URI customURI = null;
    try {
      customURI = URI.create(customURIString);
      // tag::customRestClientBuilder[]
      CloudantClient customRestClient = RestClientBuilder.newBuilder()
                                        .baseUri(customURI)
                                        .register(UnknownUriExceptionMapper.class)
                                        .build(CloudantClient.class);
      // end::customRestClientBuilder[]
      // tag::customRCGetProperties[]
      return customRestClient.persist(dbData);
      // end::customRCGetProperties[]
    } catch (ProcessingException ex) {
      handleProcessingException(ex);
    } catch (UnknownUriException e) {
      System.err.println("The given URI is unreachable.");
    }
    return null;
  }
  // end::getPropertiesWithGivenHostName[]

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
}
// end::manager[]
