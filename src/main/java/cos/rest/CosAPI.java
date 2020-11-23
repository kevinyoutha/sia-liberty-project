
package cos.rest;

import cloudant.Document;
import com.ibm.websphere.jaxrs20.multipart.IAttachment;
import com.ibm.websphere.jaxrs20.multipart.IMultipartBody;
import cos.store.ObjectStore;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

//@RegisterProvider(MultipartFormExceptionMapper.class)

@Path("/")
public class CosAPI {


  @Inject
  ObjectStore store;     //Our cloud object storage store

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
//      throw new Exception("Missing form arguments");
    }

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
  public List<String> getBuckets() {
    return store.getBucketsList();
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
  public List<String> getObjects() {
    return store.listObjects();
  }
}
