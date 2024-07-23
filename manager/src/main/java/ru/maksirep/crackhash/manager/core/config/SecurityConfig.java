package ru.maksirep.crackhash.manager.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import ru.maksirep.crackhash.manager.api.ApiPaths;

@Configuration
public class SecurityConfig {

    private static final String[] WHITE_LIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            ApiPaths.CRACK,
            ApiPaths.STATUS
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        aut -> aut
                                .requestMatchers(WHITE_LIST).permitAll()
                                .requestMatchers(new DockerRequest()).permitAll()
                                .anyRequest().denyAll()
                ).build();
    }
}