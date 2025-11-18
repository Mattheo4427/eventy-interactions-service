package com.polytech.interactionservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permet d'utiliser @PreAuthorize si besoin dans le futur
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Désactiver CSRF
            // Inutile pour les API REST stateless qui utilisent des tokens (comme JWT)
            .csrf(AbstractHttpConfigurer::disable)

            // 2. Gestion de session Stateless
            // Spring ne créera pas de session HTTP (pas de cookie JSESSIONID).
            // Chaque requête doit apporter son propre token.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 3. Règles d'autorisation des requêtes
            .authorizeHttpRequests(auth -> auth
                // Autoriser l'accès public aux endpoints techniques (santé, métriques)
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/error").permitAll()
                
                // Optionnel : Si vous avez une documentation Swagger/OpenAPI
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // TOUT le reste nécessite d'être authentifié (avoir un token valide)
                .anyRequest().authenticated()
            )

            // 4. Configurer le serveur de ressource OAuth2 (Validation JWT)
            // Cela vérifie la signature du token avec Keycloak
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}