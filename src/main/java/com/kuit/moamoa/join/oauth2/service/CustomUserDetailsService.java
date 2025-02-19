package com.kuit.moamoa.join.oauth2.service;

import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.join.oauth2.dto.CustomUserDetails;
import com.kuit.moamoa.user.repository.UserRepository;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User foundUser = userRepository.findByEmail(email);

        if (foundUser != null){
            return new CustomUserDetails(foundUser);
        }
        return null;
    }
}
