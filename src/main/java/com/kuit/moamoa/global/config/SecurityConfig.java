package com.kuit.moamoa.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.web.servlet.function.RequestPredicates.headers;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CSRF, 폼 로그인, HTTP 기본 인증 비활성화
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
//                .headers().frameOptions().disable()
                .httpBasic(httpBasic -> httpBasic.disable());

        // 경로 기반 권한 부여
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/", "/join/**", "/join", "/swagger", "/swagger-ui.html", "/swagger-ui/**",
                                "/api-docs", "/api-docs/**", "/v3/api-docs/**",
                                "/h2-console/**", "/h2/**").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated())
                        .headers(headers -> headers.frameOptions(frameOptions ->frameOptions.sameOrigin())); //h2 db 콘솔확인을 위한 설정

        // 세션 관리
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
