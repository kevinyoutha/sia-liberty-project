package cos.proxy;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import cloudant.Document;
import cos.exception.UnknownUriException;
import cos.exception.UnknownUriExceptionMapper;

import javax.ws.rs.*;

@RegisterRestClient(configKey = "documentAPIClient", baseUri = "http://localhost:9080/cloudant")
@RegisterProvider(UnknownUriExceptionMapper.class)
@Path("/")
public interface DocumentAPIClient extends AutoCloseable {

  @POST
  @Produces("application/json")
  @Consumes("application/json")
  public Document newDocument(Document document) throws UnknownUriException, ProcessingException;
}

