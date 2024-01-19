package choorai.excuseme.global.config;

import choorai.excuseme.global.security.AccessDeniedHandlerImpl;
import choorai.excuseme.global.security.AuthenticationEntryPointImpl;
import choorai.excuseme.global.security.JwtAuthenticationFilter;
import choorai.excuseme.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(configurer -> configurer.disable());
        http.csrf(configurer -> configurer.disable());
        http.formLogin(configurer -> configurer.disable());
        http.cors(configurer -> {
            configurer.configurationSource(request -> {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(List.of("*"));
                return configuration;
            });
        });
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(handlerConfigurer -> {
            handlerConfigurer.accessDeniedHandler(accessDeniedHandler());
            handlerConfigurer.authenticationEntryPoint(authenticationEntryPoint());
        });

        http.authorizeHttpRequests(configurer ->
                configurer.requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/members/register", "/members/login").permitAll()
                        .anyRequest().authenticated()
        );
        return http.build();
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointImpl();
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }
}
