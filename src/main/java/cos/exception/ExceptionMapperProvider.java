package cos.exception;

import org.apache.http.HttpStatus;

import javax.ejb.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Singleton
public class ExceptionMapperProvider implements ExceptionMapper<Exception>
{

    @Override
    public Response toResponse(final Exception exception)
    {
        return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(new BasicResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, exception.getMessage(),exception)).type(MediaType.APPLICATION_JSON).build();
    }
}

class BasicResponse {

    public int internalStatus;
    public String message;
    public StackTraceElement[] stackTrace;
    public String cause;


    public BasicResponse() {}

    public BasicResponse(int internalStatus, String message, Exception e){
        this.internalStatus = internalStatus;
        this.message = message;
        this.stackTrace = e.getStackTrace();
        try{
            this.cause = e.getCause().toString();
        }catch (Exception ex){
            cause = "";
        }

    }
}