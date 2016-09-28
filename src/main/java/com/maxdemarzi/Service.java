package com.maxdemarzi;

import org.codehaus.jackson.map.ObjectMapper;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

@Path("/service")
public class Service {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Path("/helloworld")
    public Response helloWorld() throws IOException {
        Map<String, String> results = new HashMap<String, String>() {{
            put("hello", "world");
        }};
        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
    }
}
