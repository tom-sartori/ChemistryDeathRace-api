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

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/played")
    @RolesAllowed({Roles.ADMIN})
    public Response countGamesPlayed() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.count())
                .build();
    }

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

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    @RolesAllowed({Roles.ADMIN})
    public Response read(@PathParam("id") ObjectId id) {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.findById(id))
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/percentage/question")
    @RolesAllowed({Roles.ADMIN})
    public Response getPercentageByQuestion() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.getPercentageByQuestion())
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/percentage")
    @RolesAllowed({Roles.ADMIN})
    public Response getPercentage() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.getPercentage())
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/average/time")
    @RolesAllowed({Roles.ADMIN})
    public Response getAverageTime() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.getAverageTime())
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/average/dicesize")
    @RolesAllowed({Roles.ADMIN})
    public Response getAverageDiceSize() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.getAverageDiceSize())
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/average/players")
    @RolesAllowed({Roles.ADMIN})
    public Response getAveragePlayersNumber() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.getAveragePlayersNumber())
                .build();
    }

    @GET
    @Consumes(MediaType.MEDIA_TYPE_WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/most/played/difficulty")
    @RolesAllowed({Roles.ADMIN})
    public Response getMostPlayedDifficulty() {
        return Response
                .status(Response.Status.OK)
                .entity(gameRepository.getMostPlayedDifficulty())
                .build();
    }


}
