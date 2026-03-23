package com.jdcolorado.gestions.eventos.api.security.config;

import com.jdcolorado.gestions.eventos.api.security.jwt.JwtAuthEntryPoint;
import com.jdcolorado.gestions.eventos.api.security.jwt.JwtAuthenticationFilter;
import com.jdcolorado.gestions.eventos.api.security.util.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final Environment environment;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http){
        http
                .csrf(AbstractHttpConfigurer::disable )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                            auth
                                    .requestMatchers("/api/v1/auth/**").permitAll();

                            if (environment.acceptsProfiles(Profiles.of("dev"))){
                                auth
//                                        .requestMatchers("/h2-console/**").permitAll()
                                        .requestMatchers(
                                        "/swagger-ui/**", "/swagger-ui.html",
                                        "/v3/api-docs/**", "/v3/api-docs.yaml",
                                        "/swagger-resources/**",
                                        "/webjars/**"
                                ).permitAll();
                            }
                            auth.anyRequest().authenticated();
                        }
                ).headers(AbstractHttpConfigurer::disable);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(exception -> exception
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
        );
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));

        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));

        corsConfiguration.setExposedHeaders(List.of("Authorization"));

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
