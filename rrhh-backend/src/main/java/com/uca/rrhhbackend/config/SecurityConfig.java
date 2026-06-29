package com.uca.rrhhbackend.config;

import com.uca.rrhhbackend.security.CustomUserDetailsService;
import com.uca.rrhhbackend.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        /*
                         * ENDPOINTS PÚBLICOS
                         * No necesitan token.
                         */
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()

                        /*
                         * DOCUMENTACIÓN
                         * No necesita token.
                         */
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        /*
                         * PERFIL DEL CANDIDATO
                         * Necesita token de CANDIDATE.
                         */
                        .requestMatchers(
                                "/api/candidates/profile/**",
                                "/api/candidates/education/**",
                                "/api/candidates/experience/**",
                                "/api/candidates/skills/**"
                        ).hasRole("CANDIDATE")

                        /*
                         * EMPRESAS
                         * Consultar: cualquier rol autenticado.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/companies",
                                "/api/companies/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER",
                                "CANDIDATE"
                        )

                        /*
                         * Crear, actualizar o eliminar empresas:
                         * ADMIN o RECRUITER.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/companies",
                                "/api/companies/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/companies/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/companies/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        /*
                         * VACANTES
                         * Consultar: todos los roles autenticados.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/job-offers",
                                "/api/job-offers/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER",
                                "CANDIDATE"
                        )

                        /*
                         * Crear, actualizar, publicar, cerrar o eliminar:
                         * ADMIN o RECRUITER.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/job-offers",
                                "/api/job-offers/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/job-offers/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/job-offers/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/job-offers/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        /*
                         * POSTULACIONES
                         * Crear una postulación:
                         * solo CANDIDATE.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/applications"
                        ).hasRole("CANDIDATE")

                        /*
                         * Consultar el historial propio:
                         * solo CANDIDATE.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/applications/me",
                                "/api/applications/me/**"
                        ).hasRole("CANDIDATE")

                        /*
                         * Consultar todas las postulaciones:
                         * solo ADMIN o RECRUITER.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/applications"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        /*
                         * Consultar postulaciones por candidato:
                         * solo ADMIN o RECRUITER.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/applications/candidate/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        /*
                         * Consultar postulaciones por vacante:
                         * solo ADMIN o RECRUITER.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/applications/job-offer/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        /*
                         * Consultar una postulación concreta:
                         * solo ADMIN o RECRUITER.
                         *
                         * El candidato debe usar /api/applications/me/{id}
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/applications/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        /*
                         * Cambiar el estado de una postulación:
                         * solo ADMIN o RECRUITER.
                         */
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/applications/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        /*
                         * ENTREVISTAS
                         * Por ahora solo ADMIN o RECRUITER.
                         */
                        .requestMatchers(
                                "/api/interviews/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        /*
                         * COMENTARIOS PRIVADOS
                         * Solo ADMIN o RECRUITER.
                         */
                        .requestMatchers(
                                "/api/recruiter-comments/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/technical-tests"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        /*
                         * Revisar prueba:
                         * ADMIN o RECRUITER.
                         */
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/technical-tests/*/review"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        /*
                         * Enviar prueba:
                         * solo CANDIDATE.
                         */
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/technical-tests/*/submit"
                        ).hasRole("CANDIDATE")

                        /*
                         * Consultar pruebas:
                         * cualquier rol autenticado.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/technical-tests/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER",
                                "CANDIDATE"
                        )

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/notifications"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/notifications/me",
                                "/api/notifications/me/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER",
                                "CANDIDATE"
                        )

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/notifications/*/read"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER",
                                "CANDIDATE"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/notifications/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                        .requestMatchers(
                                "/api/scoring/**"
                        ).hasAnyRole(
                                "ADMIN",
                                "RECRUITER"
                        )

                                /*
                                 * RECLUTADORES
                                 *
                                 * Solo ADMIN puede crear, modificar o desactivar reclutadores.
                                 */
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/recruiters"
                                ).hasRole("ADMIN")

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/recruiters/**"
                                ).hasRole("ADMIN")

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/recruiters/**"
                                ).hasRole("ADMIN")

                               /*
                               * ADMIN y RECRUITER pueden consultar perfiles.
                               */
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/recruiters",
                                        "/api/recruiters/**"
                                ).hasAnyRole(
                                        "ADMIN",
                                        "RECRUITER"
                                )

                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}