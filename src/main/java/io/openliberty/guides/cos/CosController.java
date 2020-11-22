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

import com.ibm.websphere.jaxrs20.multipart.IMultipartBody;
import io.openliberty.guides.cos.model.DbData;

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

//  @POST
//  @Consumes("multipart/form-data")
//  @Produces(MediaType.APPLICATION_JSON)
//  public Response postFormData(IMultipartBody multipartBody) {
////    String response = manager.createObject(multipartBody);
////    String response = manager.store(multipartBody);
//    String response = null;
//    return Response.ok(response).build();
//  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response postFormData(DbData formData) {
//    String response = manager.createObject(multipartBody);

    String response = manager.store(formData);
    return Response.ok(response).build();
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<String> listBuckets() {
    return manager.getBucketsList();
  }
}
