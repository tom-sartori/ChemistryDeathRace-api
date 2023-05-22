package chemistry.death.race.resource;

import chemistry.death.race.collection.game.Answer;
import chemistry.death.race.collection.game.Game;
import chemistry.death.race.constant.Paths;
import chemistry.death.race.constant.Roles;
import chemistry.death.race.repository.GameRepository;
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

/**
 * Game ressource used for statistics.
 */
@Path(Paths.GAME)
@ApplicationScoped
@SecurityScheme(
        securitySchemeName = "jwt",
        scheme = "Bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
public class GameResource {

    @Inject
    GameRepository gameRepository;

    /**
     * Creates a new game used for statistics.
     *
     * @param game the game to create.
     * @return the created game.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response newGame(Game game) {
        gameRepository.addGame(game);
        return Response
                .status(Response.Status.CREATED)
                .entity(game)
                .build();
    }

    /**
     * Adds an answer to a game.
     *
     * @param id the id of the game.
     * @param answer the answer to add.
     * @return a 201 response if the answer was added.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/answer/{id}")
    @PermitAll
    public Response addAnswer(@PathParam("id") ObjectId id, Answer answer) {
        gameRepository.addAnswer(id, answer);
        return Response
                .status(Response.Status.CREATED)
                .entity(answer)
                .build();
    }

    /**
     * Closes a game.
     *
     * @param id the id of the game to close.
     * @return a 201 response if the game was closed.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/close/{id}")
    @PermitAll
    public Response closeGame(@PathParam("id") ObjectId id) {
        gameRepository.closeGame(id);
        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    /**
     * Counts the number of games played.
     *
     * @return the number of games played.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/played")
    @RolesAllowed({Roles.ADMIN, Roles.CONTRIBUTOR})
    public Response countGamesPlayed() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.count())
                .build();
    }

    /**
     * Get all the games.
     *
     * @return all the games.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response read() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.listAll())
                .build();
    }

    /**
     * Get a game by its id.
     *
     * @param id the id of the game to get.
     * @return the game with the given id.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    @RolesAllowed({Roles.ADMIN, Roles.CONTRIBUTOR})
    public Response read(@PathParam("id") ObjectId id) {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.findById(id))
                .build();
    }

    /**
     * Get the percentage of correct answers by question.
     *
     * @return the percentage of correct answers by question.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/percentage/question")
    @RolesAllowed({Roles.ADMIN, Roles.CONTRIBUTOR})
    public Response getPercentageByQuestion() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.getPercentageByQuestion())
                .build();
    }

    /**
     * Get the global percentage of correct answers.
     *
     * @return the percentage of correct answers.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/percentage")
    @RolesAllowed({Roles.ADMIN, Roles.CONTRIBUTOR})
    public Response getPercentage() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.getPercentage())
                .build();
    }

    /**
     * Get the average duration of a game.
     *
     * @return the average duration of a game.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/average/time")
    @RolesAllowed({Roles.ADMIN, Roles.CONTRIBUTOR})
    public Response getAverageTime() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.getAverageTime())
                .build();
    }

    /**
     * Get the average dice size chosen by players.
     *
     * @return the average dice size chosen by players.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/average/dicesize")
    @RolesAllowed({Roles.ADMIN, Roles.CONTRIBUTOR})
    public Response getAverageDiceSize() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.getAverageDiceSize())
                .build();
    }

    /**
     * Get the average number of players per game.
     *
     * @return the average number of players per game.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/average/players")
    @RolesAllowed({Roles.ADMIN, Roles.CONTRIBUTOR})
    public Response getAveragePlayersNumber() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.getAveragePlayersNumber())
                .build();
    }

    /**
     * Get the most played difficulty.
     *
     * @return the most played difficulty.
     */
    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/most/played/difficulty")
    @RolesAllowed({Roles.ADMIN, Roles.CONTRIBUTOR})
    public Response getMostPlayedDifficulty() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.getMostPlayedDifficulty())
                .build();
    }
}
