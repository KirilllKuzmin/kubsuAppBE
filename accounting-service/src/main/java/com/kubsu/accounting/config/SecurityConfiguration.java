package com.kubsu.accounting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/accounting/lecturers/courses").hasAnyRole("LECTURER")
                        .requestMatchers("/api/v1/accounting/lecturers/courses/{courseId}/groups").hasAnyRole("LECTURER", "MANAGER")
                        .requestMatchers("/api/v1/accounting/groups/{groupId}/students").hasAnyRole("LECTURER", "MANAGER")
                        .requestMatchers("/api/v1/accounting/lecturers/courses/{courseId}/groups/{groupId}/dates").hasAnyRole("STUDENT", "LECTURER", "MANAGER", "ADMIN")
                        .requestMatchers("/api/v1/accounting/lecturers/absences").hasAnyRole("LECTURER", "MANAGER")
                        .requestMatchers("/api/v1/accounting/lecturers/absences/courses/{courseId}/groups/{groupId}").hasAnyRole("ADMIN")
                        .requestMatchers("/api/v1/accounting/lecturers/courses/{courseId}/groups/{groupId}/works").hasAnyRole("LECTURER", "MANAGER")
                        .requestMatchers("/api/v1/accounting/workTypes").hasAnyRole("STUDENT", "LECTURER", "MANAGER", "ADMIN")
                        .requestMatchers("/api/v1/accounting/lecturers/courses/{courseId}/groups/{groupId}/dates/{workDate}/works").hasAnyRole("LECTURER", "MANAGER", "ADMIN")
                        .requestMatchers("/api/v1/accounting/lecturers/evaluations/systems").hasAnyRole("LECTURER", "MANAGER")
                        .requestMatchers("/api/v1/accounting/lecturers/evaluations").hasAnyRole("LECTURER", "MANAGER", "ADMIN")
                        .requestMatchers("/api/v1/accounting/lecturers/evaluations/courses/{courseId}/groups/{groupId}").hasAnyRole("LECTURER", "MANAGER")
                        .requestMatchers("/api/v1/timetables/number-time-classes-held").hasAnyRole("STUDENT", "LECTURER", "MANAGER", "ADMIN")
                        .requestMatchers("/api/v1/timetables").hasAnyRole("STUDENT", "LECTURER", "MANAGER", "ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new JwtAuthenticationConverter();
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        converter.setPrincipalClaimName("preferred_username");
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            var roles = (List<String>) jwt.getClaimAsMap("realm_access").get("roles");

            return Stream.concat(authorities.stream(),
                            roles.stream()
                                    .filter(role -> role.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast))
                    .toList();
        });

        return converter;
    }
}