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

public class DbData {

  private  String title;
  private  String cosObjectName;
  private  String cosBucketName;
  private  String contentType;
  private  int dataStreamLength;
  private  String status;

  public DbData() {
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

}
