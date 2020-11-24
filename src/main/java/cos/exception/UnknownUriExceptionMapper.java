package cos.exception;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class UnknownUriExceptionMapper
    implements ResponseExceptionMapper<UnknownUriException> {

  @Override
  public boolean handles(int status, MultivaluedMap<String, Object> headers) {
    return status == 404;
  }

  @Override
  public UnknownUriException toThrowable(Response response) {
    return new UnknownUriException();
  }
}
