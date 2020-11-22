// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
// tag::mapper[]
package io.openliberty.guides.cos.exception;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class MissingArgumentExceptionMapper
    implements ResponseExceptionMapper<MissingArgumentException> {
  Logger LOG = Logger.getLogger(MissingArgumentExceptionMapper.class.getName());

  @Override
  // tag::handles[]
  public boolean handles(int status, MultivaluedMap<String, Object> headers) {
    LOG.info("MissingArgumentException status = " + status);
    return status == 404;
  }
  // end::handles[]

  @Override
  // tag::toThrowable[]
  public MissingArgumentException toThrowable(Response response) {
    return new MissingArgumentException();
  }
  // end::toThrowable[]
}
// end::mapper[]
