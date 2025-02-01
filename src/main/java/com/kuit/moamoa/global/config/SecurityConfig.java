package com.kuit.moamoa.global.config;

import com.kuit.moamoa.jwt.JWTFilter;
import com.kuit.moamoa.jwt.JWTUtil;
import com.kuit.moamoa.jwt.LoginFilter;
import com.kuit.moamoa.oauth2.CustomSuccessHandler;
import com.kuit.moamoa.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Dev 브랜치의 CORS 설정과 feat/auth의 추가 헤더 설정을 합침
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 필요한 경우 특정 도메인만 허용하도록 수정하세요.
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        // feat/auth에서 노출한 헤더 설정 (여러 헤더를 노출하려면 리스트에 모두 추가)
        configuration.setExposedHeaders(List.of("Set-Cookie", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 로그인 시 JWT 발급을 위한 LoginFilter 생성 (사용자명 파라미터를 "nickname"으로 설정)
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setUsernameParameter("nickname");

        http
            // CORS 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // CSRF, 폼 로그인, HTTP 기본 인증 비활성화
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            // H2 콘솔 등 iframe 접근을 위한 헤더 설정 (필요시)
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
            // OAuth2 로그인 설정
            .oauth2Login(oauth2 -> oauth2
                    .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService))
                    .successHandler(customSuccessHandler)
            )
            // URL 기반 권한 설정
            .authorizeHttpRequests(auth -> auth
                    // feat/auth 브랜치에서 허용한 URL
                    .requestMatchers(
                            "/login", "/", "/join/**", "/join",
                            "/swagger", "/swagger-ui.html", "/swagger-ui/**",
                            "/api-docs", "/api-docs/**", "/v3/api-docs/**",
                            "/h2-console/**", "/h2/**"
                    ).permitAll()
                    // dev 브랜치에서 허용한 WebSocket 및 채팅 관련 URL
                    .requestMatchers("/ws-stomp/**", "/chat/**").permitAll()
                    // 예시: /admin URL은 ADMIN 권한 필요
                    .requestMatchers("/admin").hasRole("ADMIN")
                    // 나머지 요청은 인증 필요
                    .anyRequest().authenticated()
            )
            // 세션을 사용하지 않고 JWT 기반 인증을 위해 상태 없는(stateless) 세션 관리 설정
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 필터 추가  
        // LoginFilter를 UsernamePasswordAuthenticationFilter 위치에 추가하고, 그 앞에 JWTFilter를 추가
        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        return http.build();
    }
}