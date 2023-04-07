package chemical.pursuit;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class FirebaseInitializer {

    private FirebaseApp firebaseApp;

    @Produces
    @ApplicationScoped
    public FirebaseApp createFirebaseApp() {
        if (firebaseApp == null) {
            try {
                InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");

                assert serviceAccount != null;

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                firebaseApp = FirebaseApp.initializeApp(options);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de l'initialisation de FirebaseApp", e);
            }
        }

        return firebaseApp;
    }
}
