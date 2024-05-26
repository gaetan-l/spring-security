package com.training.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final AuthJwtTokenFilter authJwtTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception {
        return authConf.getAuthenticationManager(); // Celui par défaut de Spring, on peut le changer
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Chaîne de filtres de sécurité qui intercepte toute requête vers un
     * controller.
     *
     * @param  httpSecurity  objet de configuration de sécurité
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        /*
         * Cross-Origin Resource Sharing. À désactiver dans le cas d'une API
         * publique par exemple.
         */
        httpSecurity.cors(AbstractHttpConfigurer::disable);

        /*
         * Cross-Site Request Forgery pour empêcher les exploitations par
         * modification de formulaire. Pas de formulaires en mode API donc à
         * désactiver.
         */
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        /*
         * Gestion de la session. Dans une API (stateless) il n'y a pas de
         * session.
         */
        httpSecurity.sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /* Authentication et Authorization */
        httpSecurity.authorizeHttpRequests(requests ->
            requests
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers(antMatcher(HttpMethod.POST,   "/tutorial/**")).hasAuthority("ROLE_ADMIN") // Ou .hasRole("ADMIN")
                    .requestMatchers(antMatcher(HttpMethod.PUT,    "/tutorial/**")).hasAuthority("ROLE_ADMIN")
                    .requestMatchers(antMatcher(HttpMethod.DELETE, "/tutorial/**")).hasAuthority("ROLE_ADMIN")
                    .anyRequest().authenticated());

        /*
         * On ajoute un filtre custom pour l'authentification par token JWT
         * avant le filtre user/password.
         */
        httpSecurity.addFilterBefore(authJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }
}