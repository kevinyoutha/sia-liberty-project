package com.keyou.test.cos.exception;

import org.apache.http.HttpStatus;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MultipartFormExceptionMapper
    implements ExceptionMapper<Exception> {

  public MultipartFormExceptionMapper() {
  }

  @Override
  public Response toResponse(Exception e) {
    return Response.status(HttpStatus.SC_BAD_REQUEST).entity(new MultipartFormExceptionResponse(e.getMessage())).type(MediaType.APPLICATION_JSON).build();

  }
}
