package com.kuit.moamoa.jwt;

import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.CustomUserDetails;
import com.kuit.moamoa.repository.AttendanceRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
//@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final AttendanceRepository attendanceRepository;

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("email");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String email = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = customUserDetails.getUsername();
        User user = customUserDetails.getUser();

//        String nickname = customUserDetails.getUsername();
        Long userId = customUserDetails.getUser().getId();
        boolean hasNotAttended = attendanceRepository.hasNotAttendedInLastTwoWeeks(user, LocalDateTime.now());

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(userId, role);
        log.info("여기서 토큰: {}", token);

        response.addHeader("Authorization", "Bearer " + token);
        response.setHeader("Recent-activity", String.valueOf(!hasNotAttended));

    }





}
