package com.kuit.moamoa.jwt;

import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.Cookie;
import java.io.IOException;


@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 우선 쿠키에서 토큰을 찾음
        String authorization = extractTokenFromCookies(request);
        log.info("token: {}", authorization);

        // 쿠키에서 토큰을 찾지 못한 경우, Authorization 헤더에서 토큰을 찾음
        if (authorization == null) {
            authorization = extractTokenFromHeader(request);
        }

        // 토큰 검증
        if (authorization == null || jwtUtil.isExpired(authorization)) {
            log.info("Token is either null or expired.");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 사용자 정보 추출
        String nickname = jwtUtil.getNickname(authorization);
        String role = jwtUtil.getRole(authorization);

        User user = User.builder()
                .nickname(nickname)
                .role(role)
                .password("temppassword")
                .build();
        log.info("user_name: {}", user.getNickname());
        log.info("user_role: {}", user.getRole());

        // 사용자 정보를 바탕으로 UserDetails 객체 생성
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 스프링 시큐리티 인증 토큰 생성 및 세션에 등록
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.split(" ")[1]; // Bearer 다음에 오는 토큰 반환
        }
        return null;
    }
}
