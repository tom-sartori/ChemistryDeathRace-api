package chemical.pursuit.resource;

import chemical.pursuit.collection.question.Question;
import chemical.pursuit.constant.Paths;
import chemical.pursuit.constant.Roles;
import chemical.pursuit.repository.QuestionRepository;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(Paths.QUESTION)
@ApplicationScoped
@SecurityScheme(
        securitySchemeName = "jwt",
        scheme = "Bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
public class QuestionResource {

    @Inject
    QuestionRepository questionRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.ADMIN, Roles.CONTRIBUTOR})
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
    @PermitAll
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
    @PermitAll
    public Response read(@PathParam("id") ObjectId id) {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findById(id))
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/difficulty/{difficulty}")
    @PermitAll
    public Response readCategoriesByDifficulty(@PathParam("difficulty") String difficulty) {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAllCategoriesByDifficulty(difficulty))
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/difficulty/{difficulty}/category/{category}")
    @PermitAll
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
    @PermitAll
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
    @PermitAll
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
    @RolesAllowed({Roles.ADMIN, Roles.CONTRIBUTOR})
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
    @Path("/difficulty/{difficulty}/category/{category}")
    @RolesAllowed(Roles.ADMIN)
    public Response updateAllCategory(@PathParam("difficulty") String difficulty, @PathParam("category") String oldCategoryValue, String newCategoryValue) {
        questionRepository.updateAllCategories(difficulty, oldCategoryValue, newCategoryValue);
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAllCategories())
                .build();
    }

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/difficulty/{difficulty}")
    @RolesAllowed(Roles.ADMIN)
    public Response updateAllDifficulty(@PathParam("difficulty") String oldDifficultyValue, String newDifficultyValue) {
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
    @RolesAllowed({Roles.ADMIN, Roles.CONTRIBUTOR})
    public Response delete(@PathParam("id") ObjectId id) {
        questionRepository.deleteById(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}
