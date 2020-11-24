
package cos.rest;

import cloudant.Document;
import com.google.gson.Gson;
import com.ibm.websphere.jaxrs20.multipart.IAttachment;
import com.ibm.websphere.jaxrs20.multipart.IMultipartBody;
import cos.exception.ExceptionMapperProvider;
import cos.store.ObjectStore;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.NoSuchElementException;

@RegisterProvider(ExceptionMapperProvider.class)
@ApplicationScoped
@ApplicationPath("cos")
@Path("/")
public class CosAPI extends Application {

  //TODO : fix dependency injection on deployment in bluemix
  @Inject
  ObjectStore store;

  // TODO: For bluemix and to remove after enabling CDI on bluemix
//  ObjectStore store = new ObjectStore();     //Our cloud object storage store

  /**
   * Creates a new Document.
   *
   * REST API example:
   * <code>
   * POST http://localhost:9080/cos/upload
   * <code>
   * POST Body:
   * <code>
   * {
   *   "name":"Bob"
   * }
   * </code>
   * Response:
   * <code>
   * {
   *   "id":"123",
   *   "name":"Bob"
   * }
   * </code>
   * @param multipartBody The new object to create.
   * @return The Document after it has been passed stored.  This will include a unique ID for the Visitor.
   */
  @POST
  @Path("/upload")
  @Consumes("multipart/form-data")
  @Produces(MediaType.APPLICATION_JSON)
  public Response upload(IMultipartBody multipartBody) {

    List<IAttachment> attachments = multipartBody.getAllAttachments();

    if (attachments.size() != 2) {
      return Response.status(Response.Status.BAD_REQUEST)
              .entity("{ \"error\" : \"Missing arguments !"
                      + " Please provide file and title arguments "
                      + " \" }")
              .build();
    }

    try {
      // Looking for file Attachment
      IAttachment fileAttachment = attachments.stream()
              .filter(iAttachment -> iAttachment.getContentType().getType().contains("application"))
              .findFirst().get();

      // Looking for plain text title Attachment
      IAttachment titleAttachment = attachments.stream()
              .filter(iAttachment -> iAttachment.getContentType().getType().contains("text"))
              .findFirst().get();
      Document response = store.handleFormUpload(titleAttachment, fileAttachment);
      return Response.ok(response).build();

    }catch (NoSuchElementException e) {
      return Response.status(Response.Status.BAD_REQUEST)
              .entity("{ \"error\" : \"Missing arguments !"
                      + " Please provide file and title arguments "
                      + " \" }")
              .build();
    }
  }

  @GET
  @Path("/test")
  @Produces(MediaType.APPLICATION_JSON)
  public String test() {
    return "test";
  }

  /**
   * Gets all buckets.
   * REST API example:
   * <code>
   * GET http://localhost:9080/cos/buckets
   * </code>
   *
   * Response:
   * <code>
   * [ "Bob", "Jane" ]
   * </code>
   * @return A collection of all the buckets
   */
  @GET
  @Path("/buckets")
  @Produces(MediaType.APPLICATION_JSON)
  public String getBuckets() {
    List<String> bucketsList = store.getBucketsList();
    return new Gson().toJson(bucketsList);
  }

  /**
   * Gets all files.
   * REST API example:
   * <code>
   * GET http://localhost:9080/cos/objects
   * </code>
   *
   * Response:
   * <code>
   * [{
   *
   * }]
   * </code>
   * @return A collection of all objects
   */
  @GET
  @Path("/objects")
  @Produces(MediaType.APPLICATION_JSON)
  public String getObjects() {
    List<String> objects = store.listObjects();
    return new Gson().toJson(objects);
  }
}
