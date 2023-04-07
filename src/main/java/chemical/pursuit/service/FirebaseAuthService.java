package chemical.pursuit.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import io.quarkus.security.UnauthorizedException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;

@ApplicationScoped
public class FirebaseAuthService {

    @Inject
    FirebaseApp firebaseApp;

    public void verifyIdToken(HttpHeaders httpHeaders) {
        String authorizationHeader = httpHeaders.getHeaderString("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization header must be provided.");
        }

        String idToken = authorizationHeader.substring(7);
        try {
            FirebaseAuth.getInstance(firebaseApp).verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            throw new UnauthorizedException("Invalid ID token.");
        }
    }
}
