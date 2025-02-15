package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.Status;
import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.request.UserAuthRequest;
import com.kuit.moamoa.dto.request.NicknameRequest;
import com.kuit.moamoa.dto.response.UserAuthResponse;
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

    public void resetPassword(UserAuthRequest request){

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

    public UserAuthResponse joinProcess(UserAuthRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();
//        Status status = request.getStatus();

        boolean isExist = userRepository.existsByEmail(email);
        User user = userRepository.findByEmail(email);
        Status status = user.getStatus();

        if(isExist && status.equals(Status.ACTIVE)){

            throw new DuplicateKeyException("이미 가입된 이메일입니다.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        return UserAuthResponse.from(user);
    }

    public void setNickname(NicknameRequest request) {

        String email = request.getEmail();
        String nickname = request.getNickname(); //새로운 닉네임

        boolean isExist = userRepository.existsByEmail(email);
        User user = userRepository.findByEmail(email);


        if(!isExist){
            throw new IllegalStateException("가입되지 않은 유저입니다.");
        }
        user.setNickname(nickname);
        userRepository.save(user);
    }
}
