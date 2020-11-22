// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018, 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
// tag::client[]
package io.openliberty.guides.cos.client;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import io.openliberty.guides.cos.model.DbData;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

// tag::RegisterRestClient[]
@RegisterRestClient(configKey = "cloudantClient", baseUri = "http://localhost:9080/cloudant")
// end::RegisterRestClient[]
// tag::RegisterProvider[]
@RegisterProvider(UnknownUriExceptionMapper.class)
// end::RegisterProvider[]
@Path("/")
// tag::SystemClient[]
// tag::AutoCloseable[]
public interface CloudantClient extends AutoCloseable {
// end::AutoCloseable[]

  @POST
  // tag::Produces[]
  @Produces(MediaType.APPLICATION_JSON)
  // end::Produces[]
  // tag::getProperties[]
  public String createDocument(DbData data) throws UnknownUriException, ProcessingException;
  // end::getProperties[]
}
// end::SystemClient[]
// end::client[]
