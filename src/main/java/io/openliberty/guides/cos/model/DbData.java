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
package io.openliberty.guides.cos.model;

import java.util.Properties;

public class DbData {

  private  String title;
  private  String cosObjectName;
  private  String cosBucketName;
  private  String fileType;
  private  String fileSize;
  private   String status;

  public DbData() {
  }

  public DbData(String title, String cosObjectName, String cosBucketName, String status) {
    this.title = title;
    this.cosObjectName = cosObjectName;
    this.cosBucketName = cosObjectName;
    this.status = status;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setCosObjectName(String cosObjectName) {
    this.cosObjectName = cosObjectName;
  }

  public void setCosBucketName(String cosBucketName) {
    this.cosBucketName = cosBucketName;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public String getFileSize() {
    return fileSize;
  }

  public void setFileSize(String fileSize) {
    this.fileSize = fileSize;
  }

  public String getTitle() {
    return title;
  }

  public String getCosObjectName() {
    return cosObjectName;
  }

  public String getCosBucketName() {
    return cosBucketName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
//
//  @Override
//  public boolean equals(Object host) {
//    if (host instanceof DdData) {
//      return hostname.equals(((DdData) host).getHostname());
//    }
//    return false;
//  }
}
