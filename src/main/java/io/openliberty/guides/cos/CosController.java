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

import com.ibm.websphere.jaxrs20.multipart.IAttachment;
import com.ibm.websphere.jaxrs20.multipart.IMultipartBody;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/upload")
public class CosController {

  @Inject
  CosService manager;

  @POST
  @Consumes("multipart/form-data")
  @Produces(MediaType.APPLICATION_JSON)
  public Response postFormData(IMultipartBody multipartBody) {
    List <IAttachment> attachments = multipartBody.getAllAttachments();
    String formElementValue = null;
    InputStream stream = null;
    for (Iterator<IAttachment> it = attachments.iterator(); it.hasNext();) {
      IAttachment attachment = it.next();
      if (attachment == null) {
        continue;
      }
      DataHandler dataHandler = attachment.getDataHandler();
      try {
        stream = dataHandler.getInputStream();
      } catch (IOException e) {
        e.printStackTrace();
      }
      MultivaluedMap<String, String> map = attachment.getHeaders();
      String fileName = null;
      String formElementName = null;
      String[] contentDisposition = map.getFirst("Content-Disposition").split(";");
      for (String tempName : contentDisposition) {
        String[] names = tempName.split("=");
        try {
          formElementName = names[1].trim().replaceAll("\"", "");
          if ((tempName.trim().startsWith("filename"))) {
            fileName = formElementName;
          }
        }catch (Exception e){
          e.printStackTrace();
        }
      }
      if (fileName == null) {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line = null;
        try {
          while ((line = br.readLine()) != null) {
            sb.append(line);
          }
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          if (br != null) {
            try {
              br.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
        formElementValue = sb.toString();
        System.out.println(formElementName + ":" + formElementValue);
      } else {
        //handle the file as you want
        File tempFile = new File(fileName);

      }
    }
    if (stream != null) {
      try {
        stream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return Response.ok("test").build();
  }


  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<String> listBuckets() {
    return manager.getBucketsList();
  }
}
