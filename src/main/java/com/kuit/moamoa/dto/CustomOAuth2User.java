package com.kuit.moamoa.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;
    private final String principalName;
    private final boolean isNewUser;


    @Override
    public Map<String, Object> getAttributes() {
        //소셜 별 데이터 형태가 다른데 획일화가 힘듦
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return principalName;
    }

    public String getUsername(){
        return userDTO.getUsername();
    }

    public Long getId(){
        return userDTO.getId();
    }

    public boolean isNewUser() {
        return isNewUser;
    }
}
