package sk.ba.novak.conf;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) throws IOException {

    	responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "http://localhost:8080");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST");
        
        String reqHeader = requestContext.getHeaderString("Access-Control-Request-Headers");
        if (reqHeader != null && reqHeader != "") {
            responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", reqHeader);
        }
    }
}
