package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.JoinDTO;
import com.kuit.moamoa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void JoinProcess(JoinDTO joinDTO) {

        String nickname=joinDTO.getNickname();
        String password=joinDTO.getPassword();

        Boolean IsExist = userRepository.existByNickname(nickname);

        if(IsExist){

            return;
        }

        User newUser=User.builder()
                .nickname(nickname)
                .password(bCryptPasswordEncoder.encode(password))
                .build();

        userRepository.save(newUser);
    }
}
