package com.kuit.moamoa.join.oauth2;

import com.kuit.moamoa.attendance.repository.AttendanceRepository;
import com.kuit.moamoa.attendance.service.AttendanceService;
import com.kuit.moamoa.join.oauth2.dto.CustomOAuth2User;
import com.kuit.moamoa.global.jwt.JWTUtil;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final AttendanceService attendanceService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();


        Long userId = customUserDetails.getId();
        User user = getUserById(userId);
        attendanceService.recordAttendance(userId);
        // 2주 이상 미접속 여부 확인
        boolean hasNotAttended = attendanceService.hasAttendedRecently(userId);
        String role = user.getRole();
        String token = jwtUtil.createJwt(userId, role);

        response.addCookie(createCookie("Authorization", token));

        log.info("{}", token);

        // 2주 이상 미접속 여부를 쿠키에 추가
        response.addCookie(createCookie("Recent-activity", String.valueOf(hasNotAttended)));
        response.sendRedirect("https://moa-moa-frontend-individual.vercel.app/login?token=" + token);
    }

    //토큰 전달을 쿠키방식으로
    private Cookie createCookie(String key, String value){

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;

    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

}
