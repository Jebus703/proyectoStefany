package com.saas.workspaces.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Permitir origenes (frontend)
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",  // Angular dev server
                "http://localhost:3000",  // React dev server (por si acaso)
                "http://127.0.0.1:4200",
                "http://127.0.0.1:3000"
        ));

        // Permitir metodos HTTP
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Permitir headers
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));

        // Permitir credenciales
        config.setAllowCredentials(true);

        // Exponer el header Authorization en las respuestas
        config.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
