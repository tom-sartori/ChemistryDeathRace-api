package chemical.pursuit.resource;

import chemical.pursuit.collection.question.Question;
import chemical.pursuit.constant.Paths;
import chemical.pursuit.repository.QuestionRepository;
import chemical.pursuit.service.FirebaseAuthService;
import org.bson.types.ObjectId;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(Paths.QUESTION)
@ApplicationScoped
public class QuestionResource {

    @Inject
    QuestionRepository questionRepository;

    @Inject
    FirebaseAuthService authService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Question question, @Context HttpHeaders httpHeaders) {
        authService.verifyIdToken(httpHeaders);
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
    @Path("/category/difficulty/{difficulty}")
    public Response readCategoriesByDifficulty(@PathParam("difficulty") String difficulty) {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAllCategoriesByDifficulty(difficulty))
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/{category}")
    public Response readCategory(@PathParam("category") String category) {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findByCategory(category))
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/difficulty/{difficulty}/category/{category}")
    public Response readCategory(@PathParam("difficulty") String difficulty, @PathParam("category") String category) {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.listAll(difficulty, category))
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/difficulty")
    public Response readDifficulties() {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAllDifficulties())
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/difficulty/{difficulty}")
    public Response readDifficulty(@PathParam("difficulty") String difficulty) {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findByDifficulty(difficulty))
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Response update(@PathParam("id") ObjectId id, Question question, @Context HttpHeaders httpHeaders) {
        authService.verifyIdToken(httpHeaders);
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
    public Response updateAllCategory(@PathParam("category") String oldCategoryValue, String newCategoryValue, @Context HttpHeaders httpHeaders) {
        authService.verifyIdToken(httpHeaders);
        questionRepository.updateAllCategories(oldCategoryValue, newCategoryValue);
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAllCategories())
                .build();
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/difficulty/{difficulty}")
    public Response updateAllDifficulty(@PathParam("difficulty") String oldDifficultyValue, String newDifficultyValue, @Context HttpHeaders httpHeaders) {
        authService.verifyIdToken(httpHeaders);
        questionRepository.updateAllDifficulties(oldDifficultyValue, newDifficultyValue);
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAllDifficulties())
                .build();
    }

    @DELETE
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.WILDCARD)
    @Path("/id/{id}")
    public Response delete(@PathParam("id") ObjectId id, @Context HttpHeaders httpHeaders) {
        authService.verifyIdToken(httpHeaders);
        questionRepository.deleteById(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}
