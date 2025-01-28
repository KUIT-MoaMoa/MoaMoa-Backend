package com.kuit.moamoa.global.config;

import com.kuit.moamoa.jwt.JWTFilter;
import com.kuit.moamoa.jwt.JWTUtil;
import com.kuit.moamoa.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.web.servlet.function.RequestPredicates.headers;

@Configuration
@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil  jwtUtil){
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setUsernameParameter("nickname");


        // CSRF, 폼 로그인, HTTP 기본 인증 비활성화
        http
                .csrf(csrf -> csrf.disable());
        http
                .formLogin(form -> form.disable());
        http
                .httpBasic(httpBasic -> httpBasic.disable());
//                .headers().frameOptions().disable()

        // 경로 기반 권한 부여
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join/**", "/join", "/swagger", "/swagger-ui.html", "/swagger-ui/**",
                                "/api-docs", "/api-docs/**", "/v3/api-docs/**",
                                "/h2-console/**", "/h2/**").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated())
                        .headers(headers -> headers.frameOptions(frameOptions ->frameOptions.sameOrigin())); //h2 db 콘솔확인을 위한 설정
        http
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        // 세션 관리
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
