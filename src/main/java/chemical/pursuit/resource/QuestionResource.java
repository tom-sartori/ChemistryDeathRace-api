package chemical.pursuit.resource;

import chemical.pursuit.collection.question.Question;
import chemical.pursuit.constant.Paths;
import chemical.pursuit.repository.QuestionRepository;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(Paths.QUESTION)
@ApplicationScoped
public class QuestionResource {

    @Inject
    QuestionRepository questionRepository;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Question question) {
        questionRepository.persist(question);
        return Response
                .status(Response.Status.CREATED)
                .entity(question)
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response read() {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.listAll())
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Response read(@PathParam("id") ObjectId id) {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findById(id))
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category")
    public Response readCategories() {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAllCategories())
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/{category}")
    public Response read(@PathParam("category") String category) {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findByCategory(category))
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Response update(@PathParam("id") ObjectId id, Question question) {
        questionRepository.update(question);
        return Response
                .status(Response.Status.OK)
                .entity(question)
                .build();
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/{category}")
    public Response updateAll(@PathParam("category") String oldCategoryValue, String newCategoryValue) {
        questionRepository.updateAllCategories(oldCategoryValue, newCategoryValue);
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAllCategories())
                .build();
    }

    @DELETE
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.WILDCARD)
    @Path("/id/{id}")
    public Response delete(@PathParam("id") ObjectId id) {
        questionRepository.deleteById(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}
