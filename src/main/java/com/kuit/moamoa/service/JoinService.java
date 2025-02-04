package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.JoinDTO;
import com.kuit.moamoa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public void JoinProcess(JoinDTO joinDTO) {

        String nickname=joinDTO.getNickname();
        String password=joinDTO.getPassword();

        Boolean IsExist = userRepository.existsByNickname(nickname);

        if(IsExist){

            return;
        }

        User newUser=User.builder()
                .nickname(nickname)
                .password(bCryptPasswordEncoder.encode(password))
                .role("ROLE_ADMIN")
                .build();

        userRepository.save(newUser);
    }
}
