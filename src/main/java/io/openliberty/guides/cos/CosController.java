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
package io.openliberty.guides.cos;

import com.ibm.websphere.jaxrs20.multipart.IAttachment;
import com.ibm.websphere.jaxrs20.multipart.IMultipartBody;
import io.openliberty.guides.cos.exception.InvalidArgumentSizeException;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/upload")
public class CosController {

  @Inject
  CosService manager;

  @POST
  @Consumes("multipart/form-data")
  @Produces("multipart/form-data")
  public Response upload(IMultipartBody multipartBody) throws InvalidArgumentSizeException {

    List<IAttachment> attachments = multipartBody.getAllAttachments();

    if (attachments.size() != 2) {
      throw new InvalidArgumentSizeException();
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
