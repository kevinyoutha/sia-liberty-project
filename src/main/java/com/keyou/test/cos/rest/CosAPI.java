
package com.keyou.test.cos;

import com.ibm.websphere.jaxrs20.multipart.IAttachment;
import com.ibm.websphere.jaxrs20.multipart.IMultipartBody;
import com.keyou.test.cos.exception.MultipartFormExceptionMapper;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterProvider(MultipartFormExceptionMapper.class)

@RequestScoped
@Path("/upload")
public class CosController {

  @Inject
  CosService manager;

  @POST
  @Consumes("multipart/form-data")
  @Produces(MediaType.APPLICATION_JSON)
  public Response upload(IMultipartBody multipartBody) throws Exception {

    List<IAttachment> attachments = multipartBody.getAllAttachments();

    if (attachments.size() != 2) {
      throw new Exception("Missing form arguments");
    }

    // Looking for file Attachment
    IAttachment fileAttachment = attachments.stream()
            .filter(iAttachment -> iAttachment.getContentType().getType().contains("application"))
            .findFirst().get();

    // Looking for plain text title Attachment
    IAttachment titleAttachment = attachments.stream()
            .filter(iAttachment -> iAttachment.getContentType().getType().contains("text"))
            .findFirst().get();

    String response = manager.handleFormUpload(titleAttachment, fileAttachment);
    return Response.ok(response).build();

  }


  @GET
  @Path("/buckets")
  @Produces(MediaType.APPLICATION_JSON)
  public List<String> listBuckets() {
    return manager.getBucketsList();
  }

  @GET
  @Path("/files")
  @Produces(MediaType.APPLICATION_JSON)
  public List<String> listObjects() {
    return manager.listObjects();
  }
}
