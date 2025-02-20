package com.kuit.moamoa.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.moamoa.configuration.exception.ErrorCode;
import com.kuit.moamoa.configuration.exception.GlobalException;
import com.kuit.moamoa.configuration.response.ErrorResponse;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.join.oauth2.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/h2-console", "/swagger-ui", "/swagger-resources",
            "/v3/api-docs", "/login", "/join", "/verify-email",
            "/ws-stomp", "/chat", "/nickname", "/send", "/check",
            "/reset-password", "/result"
    );

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDE_URLS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);

            if (token != null) {
                validateAndSetAuthentication(token);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            handleAuthenticationError(response, e);
        }
    }

    private String extractToken(HttpServletRequest request) {
        // 쿠키에서 토큰 추출 시도
        String token = extractTokenFromHeader(request);

        // 쿠키에 없다면 헤더에서 추출 시도
//        if (token == null) {
            token = extractTokenFromHeader(request);
//        }

        return token;
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> "Authorization".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

    private void validateAndSetAuthentication(String token) {
        if (jwtUtil.isExpired(token)) {
            throw new GlobalException(ErrorCode.TOKEN_IS_EXPIRED,
                    "토큰이 만료되었습니다");
        }

        String role = jwtUtil.getRole(token);

        User user = User.builder()
                .role(role)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void handleAuthenticationError(HttpServletResponse response, Exception e) throws IOException {
        log.error("Authentication error occurred", e);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = new ErrorResponse(
                500,
                e.getMessage()
        );

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}