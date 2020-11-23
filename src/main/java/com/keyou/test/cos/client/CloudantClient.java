package com.keyou.test.cos.client;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.keyou.test.cos.exception.UnknownUriException;
import com.keyou.test.cos.exception.UnknownUriExceptionMapper;
import com.keyou.test.model.DbData;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "cloudantClient", baseUri = "http://localhost:9080/cloudant")
@RegisterProvider(UnknownUriExceptionMapper.class)
@Path("/")
public interface CloudantClient extends AutoCloseable {

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public String createDocument(DbData data) throws UnknownUriException, ProcessingException;
}

