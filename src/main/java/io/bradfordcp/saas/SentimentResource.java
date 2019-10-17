package io.bradfordcp.saas;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/sentiment")
public class SentimentResource {
    @Inject
    SentimentService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/calculate/{input}")
    public String calculate(@PathParam("input") String sentence) {
        Float score = service.calculate(sentence);

        return score.toString();
    }
}
