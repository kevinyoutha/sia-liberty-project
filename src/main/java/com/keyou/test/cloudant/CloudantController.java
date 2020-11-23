package com.keyou.test.cloudant;

import com.ibm.cloud.cloudant.v1.model.AllDocsResult;
import com.keyou.test.model.DbData;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/")
public class CloudantController {

  @Inject
  CloudantService manager;

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String createDocument(DbData data) {
    String response = manager.createDocument(data);
    return response;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getAll() {
    String documents = manager.getDocumentsList();
    return documents;
  }
}
