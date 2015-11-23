package introsde.dsantoro.a2.resources.logger;

import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<Exception>{
	
	@Context
    private HttpHeaders headers;
	
	@Override
    public Response toResponse(Exception e) {
        e.printStackTrace();
        return Response.status(404).entity("resource not found").type( getAcceptType()).build();
    }
	
	private String getAcceptType(){
        List<MediaType> accepts = headers.getAcceptableMediaTypes();
        if (accepts!=null && accepts.size() > 0) {
        	return "Application/json";
            //return ((MediaType)(accepts.get(0))).getType();
        }else {
            return "Application/json";
        }
   }
}