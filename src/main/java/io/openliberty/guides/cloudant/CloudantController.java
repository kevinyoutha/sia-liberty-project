// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.cloudant;

import com.ibm.cloud.cloudant.v1.model.AllDocsResult;
import com.ibm.cloud.cloudant.v1.model.DocumentResult;
import io.openliberty.guides.cos.model.DbData;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/")
public class CloudantController {

  @Inject
  CloudantService manager;

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public String createDocument(DbData data) {
    DocumentResult document = manager.createDocument(data);
    return document.toString();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public AllDocsResult getAll() {
    AllDocsResult documents = manager.getDocumentsList();
    return documents;
  }
}
