package innovatech.apigateway.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            // 1. Le decimos a Spring Security que active y use nuestra configuración CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable()) // Desactivar CSRF para APIs
            .authorizeExchange(exchanges -> exchanges
                .anyExchange().permitAll() // Permitimos todo para que nuestro FILTRO gestione el token
            )
            .build();
    }
    //Configuracion del CORS que habilita el consumo de nuestro microservicio en el frontend
    //
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        //Se elimino la "/" al final de localhost:5173 para que coincida exactamente con el navegador
        //Aca al subir el frontend a vercel agregar el URL para que permita interactuar con el API
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000", "https://innovatech-front-end.vercel.app/")); 
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        //Permitimos el Token de Firebase y datos en JSON
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-User-UID"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return source;
    }

}