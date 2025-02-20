package com.kuit.moamoa.join.service;

import com.kuit.moamoa.global.Status;
import com.kuit.moamoa.join.dto.ResetPasswordRequest;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.join.dto.UserAuthRequest;
import com.kuit.moamoa.join.dto.NicknameRequest;
import com.kuit.moamoa.join.oauth2.dto.UserAuthResponse;
import com.kuit.moamoa.configuration.exception.ErrorCode;
import com.kuit.moamoa.configuration.exception.GlobalException;
import com.kuit.moamoa.user.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
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

    public Long login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if(Objects.equals(user.getPassword(), bCryptPasswordEncoder.encode(password))) {
            return user.getId();
        }

        return 0L;
    }

    public void resetPassword(Long userId, ResetPasswordRequest request) throws Exception {

        User findUser = userRepository.findById(userId).orElseThrow(Exception::new);
        String password = request.getPassword();
        findUser.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(findUser);

    }

    public UserAuthResponse joinProcess(UserAuthRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();

        boolean isExist = userRepository.existsByEmail(email);

        User user = userRepository.findByEmail(email);
        Status status = user.getStatus();

        if(isExist && user.getPassword()!=null){
            throw new DuplicateKeyException("이미 가입된 이메일입니다.");
        } else if(isExist && status.equals(Status.INACTIVE)) {
            throw new GlobalException(ErrorCode.UNVERIFIED_USER, "인증되지 않은 유저 이메일입니다.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return UserAuthResponse.from(user);
    }

    public void setNickname(NicknameRequest request) {

        String email = request.getEmail();
        String nickname = request.getNickname(); //새로운 닉네임

        boolean isExist = userRepository.existsByEmail(email);
        User user = userRepository.findByEmail(email);


        if(!isExist){
            throw new GlobalException(ErrorCode.USER_NOT_FOUND, "해당 유저를 찾을 수 없습니다.");
        }
        user.setNickname(nickname);
        userRepository.save(user);
    }
}
