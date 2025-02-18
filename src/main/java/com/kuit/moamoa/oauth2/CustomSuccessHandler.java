package com.kuit.moamoa.oauth2;

import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.CustomOAuth2User;
import com.kuit.moamoa.jwt.JWTUtil;
import com.kuit.moamoa.repository.UserRepository;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();


        Long userId = customUserDetails.getId();
//        boolean isNewUser = customUserDetails.isNewUser();
        User user = userRepository.findById(userId).orElse(null);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();


        String token = jwtUtil.createJwt(userId, role);
        log.info("token-here: {}", token);

        response.addCookie(createCookie("Authorization", token));
        response.sendRedirect("http://localhost:5173");//홈화면

//
//        if (isNewUser) { //TODO: 경로 수정 필요
//            log.info("새로운 유저");
//            response.sendRedirect("http://localhost:5173/join/joinprocess");//낙네임 설정
//        }else{
//            log.info("이미 가입된 유저");
//            response.sendRedirect("http://localhost:5173");//홈화면
//        }

    }

    //토큰 전달을 쿠키방식으로
    private Cookie createCookie(String key, String value){

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;

    }

}
