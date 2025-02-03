package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.request.JoinRequest;
import com.kuit.moamoa.dto.request.LoginRequest;
import com.kuit.moamoa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    public void loginProcess(LoginRequest request) {
//
//        String password=request.getPassword();
//        String email = request.getEmail();
//
//        boolean IsExist = userRepository.existsByEmail(email);
//
//        if(!IsExist){
//
//            throw new UsernameNotFoundException("가입되지 않은 이메일입니다.");
//        }
//
//    }

    public void resetPassword(LoginRequest request){

        String password = request.getPassword();
        String email = request.getEmail();

        User findUser = userRepository.findByEmail(email);
        if (findUser != null) {

            findUser.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(findUser);
        } else {

            throw new UsernameNotFoundException("해당 닉네임의 사용자를 찾을 수 없습니다.");
        }


    }

    public User joinProcess(JoinRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();
        String nickname = request.getNickname();

        boolean isExist = userRepository.existsByEmail(email);

        if(isExist){

            throw new DuplicateKeyException("이미 가입된 이메일입니다.");
        }

        User newUser=User.builder()
                .nickname(nickname)
                .password(bCryptPasswordEncoder.encode(password))
                .email(email)
                .role("ROLE_USER")
                .build();

        userRepository.save(newUser);
        return newUser;
    }
}
