package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.CustomUserDetails;
import com.kuit.moamoa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {

        User foundUser = userRepository.findByNickname(nickname);

        if (foundUser != null){
            return new CustomUserDetails(foundUser);
        }
        return null;
    }
}
