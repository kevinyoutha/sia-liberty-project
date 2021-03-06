
package cos.rest;

import cloudant.Document;
import com.google.gson.Gson;
import cos.exception.ExceptionMapperProvider;
import cos.exception.UnknownUriException;
import cos.store.ObjectStore;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
   *   "file":@"/Users/klim/Downloads/download.jpeg",
   *   "title":"Some JPEG File"
   * }
   * </code>
   * Response:
   * <code>
   * {
   *   "id":"123",
   *   "name":"Bob"
   * }
   * </code>
   * @param title The object name to create.
   * @param file The filepath of the file to create.
   * @return The Document after it has been passed stored.  This will include a unique ID for the Visitor.
   */
  @POST
  @Path("/upload")
  @Consumes("multipart/form-data")
  @Produces(MediaType.APPLICATION_JSON)
  public String uploadFile(@NotEmpty @FormParam(value="title") String title, @NotEmpty @FormParam("file") String file ) throws UnknownUriException {
    Document document = store.upload(title, file);
    return new Gson().toJson(document);
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
