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

import java.io.*;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.MultivaluedMap;

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
import com.ibm.websphere.jaxrs20.multipart.IMultipartBody;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.openliberty.guides.cos.client.CloudantClient;
import io.openliberty.guides.cos.client.UnknownUriException;
import io.openliberty.guides.cos.model.DbData;

// tag::ApplicationScoped[]
@RequestScoped
// end::ApplicationScoped[]
public class CosService {
  private final String SUCCESS = "success" ;
  private final String FAILURE = "failure";
  private final String BUCKETNAME = "Documents";

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

  private String apiKey="2T02CI7dyGBXx01_Iwpv8qJaR5f4nSnvBFrOhEIzHaOq";
  private String location="us";
  private String endpoint="https://s3-api.us-geo.objectstorage.softlayer.net";
  private String serviceInstanceId="crn:v1:bluemix:public:cloud-object-storage:global:a/126e8854225c465aaa235d2ba32cb732:7e01ed21-4187-401e-bad9-b47c1ba6457e::";


  @Inject
  @RestClient
  private CloudantClient cloudantClient;

  private final AmazonS3 s3Client;

  public CosService()  {
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

  public List<String> listObjects(AmazonS3 cosClient, String bucketName)
  {
    List<String> files = new ArrayList<>();
    ObjectListing objectListing = cosClient.listObjects(new ListObjectsRequest().withBucketName(bucketName));
    for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
      String index = " - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")";
      files.add(index);
    }
    return files;
  }

  private DbData handleFormSubmission(IMultipartBody multipartBody){

    List <IAttachment> attachments = multipartBody.getAllAttachments();
    String formElementValue = null;
    InputStream stream = null;
    for (Iterator<IAttachment> it = attachments.iterator(); it.hasNext();) {
      IAttachment attachment = it.next();
      if (attachment == null) {
        continue;
      }
      DataHandler dataHandler = attachment.getDataHandler();
      try {
        stream = dataHandler.getInputStream();
      } catch (IOException e) {
        e.printStackTrace();
      }
      MultivaluedMap<String, String> map = attachment.getHeaders();
      String formElementName = null;
      String[] contentDisposition = map.getFirst("Content-Disposition").split(";");
      String fileName = null;
      for (String tempName : contentDisposition) {
        String[] names = tempName.split("=");
        try {
          formElementName = names[1].trim().replaceAll("\"", "");
          if ((tempName.trim().startsWith("filename"))) {
            fileName = formElementName;
          }
        }catch (Exception e){
          e.printStackTrace();
        }
      }
      if (fileName == null) {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        try {
          while ((line = br.readLine()) != null) {
            sb.append(line);
          }
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          if (br != null) {
            try {
              br.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
        formElementValue = sb.toString();
        System.out.println(formElementName + ":" + formElementValue);
      } else {
        //handle the file as you want
        File tempFile = new File(fileName);

      }
    }
    if (stream != null) {
      try {
        stream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    //      ObjectMetadata metadata = new ObjectMetadata();
//      metadata.setContentLength(fileText.length());


    return null;

  }


  public DbData createObject(DbData data){
    try{
      if(!s3Client.doesBucketExist(BUCKETNAME))
        s3Client.createBucket(BUCKETNAME);

//      s3Client.putObject(data.getTitle(),data.getCosBucketName(),data.getInputStream(),metadata);
      data.setStatus(SUCCESS);
    } catch (AmazonClientException e){
      data.setStatus(FAILURE);
    }
    return data;
  }

  public String createDocument(DbData dbData) {
    try {
      return cloudantClient.createDocument(dbData);
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

  public String store(DbData data) {
//    DbData test = new DbData("tast.php","Documents/tast.php","Documents","failure");
    String document = null;
    try {
      document = cloudantClient.createDocument(data);
    } catch (UnknownUriException e) {
      e.printStackTrace();
    }
    return document;
  }
}
// end::manager[]
