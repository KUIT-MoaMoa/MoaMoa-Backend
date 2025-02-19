//package com.kuit.moamoa.service;
//
//import com.kuit.moamoa.user.domain.User;
//import com.kuit.moamoa.dto.*;
//import com.kuit.moamoa.user.repository.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.sql.Struct;
//
//@Service
//@Slf4j
//public class CustomOAuth2UserService extends DefaultOAuth2UserService  {
//
//    private final UserRepository userRepository;
//
//    public CustomOAuth2UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
//
//        OAuth2User oAuth2User = super.loadUser(userRequest); //DefaultOAuth2UserService생성자 불러서 값 획득(super)
//        String email = (String) oAuth2User.getAttributes().get("email");
//        log.info("소셜 이메일 정보: {}", email);
//
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        OAuth2Response oAuth2Response = null;
//
//        if (registrationId.equals("naver")){
//
//            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
//            log.info("response: {}", oAuth2Response);
//
//        } else if (registrationId.equals("google")) {
//
//            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
//
//        }else {
//            return null;
//        }
//
//        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId(); //뭐지 얜
//        log.info("naver username:{}", username);
//
//        //TODO: nickname과 .getName()은 다름
//        User existData = userRepository.findByNickname(oAuth2Response.getName());
//
//        if(existData == null){
//
//            User newUser=User.builder()
//                    .email(oAuth2Response.getEmail())
//                    .role("ROLE_USER")
//                    .build();
//
//            userRepository.save(newUser);
//
//            UserDTO userDTO = new UserDTO();
//            userDTO.setEmail(newUser.getEmail());
//            userDTO.setRole("ROLE_USER");
//            userDTO.setId(newUser.getId());
//
//            return new CustomOAuth2User(userDTO);
//
//        }else{
//
//            //업데이트
//            //TODO: 필요한 로직이 맞는 지 확인
//            existData.builder()
//                    .nickname(oAuth2Response.getName())
//                    .build();
//
//            userRepository.save(existData);
//
//            UserDTO userDTO = new UserDTO();
//            userDTO.setUsername(existData.getNickname());
//            userDTO.setNickname(oAuth2Response.getName());
//            userDTO.setRole(existData.getRole());
//            //id는 어차피 pk
//
//            return new CustomOAuth2User(userDTO);
//        }
//
//
//    }
//}
package com.kuit.moamoa.join.oauth2.service;

import com.kuit.moamoa.join.oauth2.dto.CustomOAuth2User;
import com.kuit.moamoa.join.oauth2.dto.GoogleResponse;
import com.kuit.moamoa.join.oauth2.dto.NaverResponse;
import com.kuit.moamoa.join.oauth2.dto.OAuth2Response;
import com.kuit.moamoa.join.oauth2.dto.UserDTO;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest); // DefaultOAuth2UserService를 통해 OAuth2 정보 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        log.info("소셜 이메일 정보: {}", email);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if ("naver".equals(registrationId)) {
            oAuth2Response = new NaverResponse(attributes);
            log.info("Naver response: {}", oAuth2Response);

        } else if ("google".equals(registrationId)) {
            oAuth2Response = new GoogleResponse(attributes);
        } else {
            throw new OAuth2AuthenticationException("지원되지 않는 소셜 로그인입니다.");
        }

        // principalName을 설정하여 "principalName cannot be empty" 오류 방지
        String principalName = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
        log.info("OAuth2 Username: {}", principalName);

        User existData = userRepository.findByEmail(oAuth2Response.getEmail());
        log.info("OAuth2 이메일: {}", oAuth2Response.getEmail());
        boolean isNewUser = false;

        UserDTO userDTO = new UserDTO();

        if (existData == null) {
            isNewUser = true;
            User newUser = User.builder()
                    .email(oAuth2Response.getEmail())
                    .role("ROLE_USER")
                    .build();

            userRepository.save(newUser);

            userDTO.setId(newUser.getId());
            userDTO.setEmail(newUser.getEmail());
            userDTO.setRole("ROLE_USER");

        }
//        else {
//            // 기존 유저 정보 업데이트
//            existData.setNickname(oAuth2Response.getName()); // 닉네임 업데이트
//            userRepository.save(existData);
//
//            userDTO.setId(existData.getId());
//            userDTO.setEmail(existData.getEmail());
//            userDTO.setNickname(existData.getNickname());
//            userDTO.setRole(existData.getRole());
//        }

        // ✅ principalName을 CustomOAuth2User에 설정하여 오류 해결
        return new CustomOAuth2User(userDTO, principalName, isNewUser);
    }
}
