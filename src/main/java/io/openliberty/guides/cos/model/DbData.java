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
  private final String title;
  private final String cosObjectName;
  private final String cosBucketName;
  private String status;

  public DbData(String title, String cosObjectName, String cosBucketName) {
    this.title = title;
    this.cosObjectName = cosObjectName;
    this.cosBucketName = cosBucketName;
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
