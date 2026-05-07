package innovatech.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable()) // Desactivar CSRF para facilitar el consumo de la API
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll() // Se permite el paso para que el AuthenticationFilter gestione la lógica del token
                )
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // Permite solicitudes desde el origen del frontend (Vite)
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        
        // Define los métodos HTTP permitidos para las operaciones del sistema
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Permite todos los encabezados, necesario para enviar el token de Authorization
        corsConfig.addAllowedHeader("*");
        
        // Tiempo de vida de la respuesta de pre-vuelo (CORS cache)
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}