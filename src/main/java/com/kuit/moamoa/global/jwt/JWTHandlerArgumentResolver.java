package com.kuit.moamoa.global.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
@Component
public class JWTHandlerArgumentResolver implements HandlerMethodArgumentResolver {
    private final JWTUtil jwtUtil;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(Jwt.class);
        boolean hasType = Long.class.isAssignableFrom(parameter.getParameterType());
        log.info("hasAnnotation={}, hasType={}, hasAnnotation && hasType={}", hasAnnotation, hasType, hasAnnotation&&hasType);
        return hasAnnotation && hasType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String authorization = extractTokenFromCookies(request);
        if(authorization == null) {
            authorization = request.getHeader("Authorization");
        }

        String token = authorization.split(" ")[1]; // Bearer 다음에 오는 토큰 반환
        log.info("userId={}", jwtUtil.getUserId(token));
        return jwtUtil.getUserId(token);
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
}
