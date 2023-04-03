package chemical.pursuit.resource;

import chemical.pursuit.constant.Paths;
import chemical.pursuit.repository.QuestionRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(Paths.QUESTION_CATEGORY)
@ApplicationScoped
public class CategoryResource {

    @Inject
    QuestionRepository questionRepository;


    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response read() {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAllCategories())
                .build();
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{category}")
    public Response updateAll(@PathParam("category") String oldCategoryValue, String newCategoryValue) {
        questionRepository.updateAllCategories(oldCategoryValue, newCategoryValue);
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAllCategories())
                .build();
    }
}
