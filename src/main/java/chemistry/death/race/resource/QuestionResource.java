package chemistry.death.race.resource;

import chemistry.death.race.collection.question.Question;
import chemistry.death.race.constant.Paths;
import chemistry.death.race.constant.Roles;
import chemistry.death.race.repository.QuestionRepository;
import io.quarkus.panache.common.Sort;
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

    /**
     * Create a new question.
     *
     * @param question The question to create.
     * @return 201 Created if the question was created successfully.
     */
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

    /**
     * Read all questions ordered by name.
     *
     * @return 200 OK if the questions were read successfully.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response read() {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.listAll(Sort.ascending("name")))
                .build();
    }

    /**
     * Read a question by id.
     *
     * @param id The id of the question to read.
     * @return 200 OK if the question was read successfully.
     */
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

    /**
     * Real all categories for a given difficulty.
     *
     * @param difficulty The difficulty to read categories for.
     * @return 200 OK if the categories were read successfully.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/category/difficulty/{difficulty}")
    @PermitAll
    public Response readCategoriesByDifficulty(@PathParam("difficulty") String difficulty) {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAllCategoriesByDifficulty(urlDecodeParameter(difficulty)))
                .build();
    }

    /**
     * Read all questions for a given difficulty and category.
     *
     * @param difficulty the difficulty to read questions for.
     * @param category the category to read questions for.
     * @return 200 OK if the questions were read successfully.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/difficulty/{difficulty}/category/{category}")
    @PermitAll
    public Response readCategory(@PathParam("difficulty") String difficulty, @PathParam("category") String category) {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.listAll(urlDecodeParameter(difficulty), urlDecodeParameter(category)))
                .build();
    }

    /**
     * Read all difficulties.
     *
     * @return 200 OK if the difficulties were read successfully.
     */
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

    /**
     * Read all available difficulties. A difficulty is available if it has at least 6 categories.
     *
     * @return 200 OK if the available difficulties were read successfully.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/difficulty/available")
    @PermitAll
    public Response readAvailableDifficulties() {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAvailableDifficulties())
                .build();
    }

    /**
     * Read all questions for a given difficulty.
     *
     * @param difficulty The difficulty to read questions for.
     * @return 200 OK if the questions were read successfully.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/difficulty/{difficulty}")
    @PermitAll
    public Response readDifficulty(@PathParam("difficulty") String difficulty) {
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findByDifficulty(urlDecodeParameter(difficulty)))
                .build();
    }

    /**
     * Update a question.
     *
     * @param id The id of the question to update.
     * @param question The updated question.
     * @return 200 OK if the question was updated successfully.
     */
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

    /**
     * Update a category.
     *
     * @param difficulty The difficulty of the category to update.
     * @param oldCategoryValue The old category value.
     * @param newCategoryValue The new category value.
     * @return 200 OK if the category was updated successfully.
     */
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/difficulty/{difficulty}/category/{category}")
    @RolesAllowed(Roles.ADMIN)
    public Response updateAllCategory(@PathParam("difficulty") String difficulty, @PathParam("category") String oldCategoryValue, String newCategoryValue) {
        questionRepository.updateAllCategories(urlDecodeParameter(difficulty), urlDecodeParameter(oldCategoryValue), newCategoryValue);
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAllCategories())
                .build();
    }

    /**
     * Update a difficulty.
     *
     * @param oldDifficultyValue The old difficulty value.
     * @param newDifficultyValue The new difficulty value.
     * @return 200 OK if the difficulty was updated successfully.
     */
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/difficulty/{difficulty}")
    @RolesAllowed(Roles.ADMIN)
    public Response updateAllDifficulty(@PathParam("difficulty") String oldDifficultyValue, String newDifficultyValue) {
        questionRepository.updateAllDifficulties(urlDecodeParameter(oldDifficultyValue), newDifficultyValue);
        return Response
                .status(Response.Status.OK)
                .entity(questionRepository.findAllDifficulties())
                .build();
    }

    /**
     * Delete a question by id.
     *
     * @return a 204 No Content response if the question was deleted successfully.
     */
    @DELETE
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.WILDCARD)
    @Path("/id/{id}")
    @RolesAllowed({Roles.ADMIN, Roles.CONTRIBUTOR})
    public Response delete(@PathParam("id") ObjectId id) {
        questionRepository.deleteQuestionById(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }

    private String urlDecodeParameter(String parameter) {
        return parameter.replace("_", " ");
    }
}
