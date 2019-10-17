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
        // Get the argmax of the class predictions.
        // The predicted classes can be an arbitrary set of non-negative integer classes,
        // but in our current sentiment models, the values used are on a 5-point
        // scale of 0 = very negative, 1 = negative, 2 = neutral, 3 = positive,
        // and 4 = very positive.
        Float score = service.calculate(sentence);

        return score.toString();
    }
}
