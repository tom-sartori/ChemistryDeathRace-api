package exception;

import io.quarkus.logging.Log;
import io.quarkus.security.UnauthorizedException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Arrays;

public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

    @Override
    public Response toResponse(UnauthorizedException exception) {
        Log.error(Arrays.toString(exception.getStackTrace()));
        return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN).entity(exception.getMessage()).build();
    }
}
