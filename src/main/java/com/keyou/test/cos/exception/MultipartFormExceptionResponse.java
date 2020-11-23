package com.keyou.test.cos.exception;

public class MultipartFormExceptionResponse {

  public String message;

  public MultipartFormExceptionResponse() {}

  public MultipartFormExceptionResponse(String message){
    this.message = message;
  }


  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}