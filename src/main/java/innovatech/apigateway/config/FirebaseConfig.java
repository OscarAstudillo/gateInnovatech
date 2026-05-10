package innovatech.apigateway.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    // Esta variable leerá el valor de la sección "Environment"
    // Agregar .env con el codigo alfanumerico para probar el funcionamiento
    @Value("${FIREBASE_CONFIG_BASE64}")
    private String firebaseConfigBase64;

    @PostConstruct
    public void init() throws IOException {
        if (firebaseConfigBase64 == null || firebaseConfigBase64.isEmpty()) {
            throw new RuntimeException("Error: Variable FIREBASE_CONFIG_BASE64 no encontrada.");
        }

        // Decodificamos la cadena Base64
        byte[] decodedBytes = Base64.getDecoder().decode(firebaseConfigBase64);
        
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(decodedBytes)))
                .build();
        //Un log para guiarnos si firebase inicio perfectamente
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase inicializado exitosamente desde el entorno.");
        }
    }
}
