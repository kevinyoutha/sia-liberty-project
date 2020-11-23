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
package com.keyou.test.model;

public class DbData {

  private  String title;
  private  String cosObjectName;
  private  String cosBucketName;
  private  String contentType;
  private  int dataStreamLength;
  private  String status;


  public DbData() {
  }

  public DbData(String title, String cosObjectName, String cosBucketName, String contentType, int dataStreamLength, String status) {
    this.title = title;
    this.cosObjectName = cosObjectName;
    this.cosBucketName = cosBucketName;
    this.contentType = contentType;
    this.dataStreamLength = dataStreamLength;
    this.status = status;
  }



  public DbData(String title, String fileName, String cosBucketName, String contentType, int dataStreamLength) {
    this.title = title;
    this.cosObjectName = fileName;
    this.cosBucketName = cosBucketName;
    this.contentType = contentType;
    this.dataStreamLength = dataStreamLength;
  }

  @Override
  public String toString() {
    return "DbData{" +
            "title='" + title + '\'' +
            ", cosObjectName='" + cosObjectName + '\'' +
            ", cosBucketName='" + cosBucketName + '\'' +
            ", contentType='" + contentType + '\'' +
            ", dataStreamLength=" + dataStreamLength +
            ", status='" + status + '\'' +
            '}';
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCosObjectName() {
    return cosObjectName;
  }

  public void setCosObjectName(String cosObjectName) {
    this.cosObjectName = cosObjectName;
  }

  public String getCosBucketName() {
    return cosBucketName;
  }

  public void setCosBucketName(String cosBucketName) {
    this.cosBucketName = cosBucketName;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public int getDataStreamLength() {
    return dataStreamLength;
  }

  public void setDataStreamLength(int dataStreamLength) {
    this.dataStreamLength = dataStreamLength;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
