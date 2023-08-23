package com.green.smartgradever2.settings.security.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserEntity entity = mapper.getByUid(username);
//        return MyUserDetails.builder()
//                .uid(entity.getUid())
//                .iuser(entity.getIuser())
//                .upw(entity.getUpw())
//                .roles(Collections.singletonList(entity.getRole()))
//                .build();
        return null;
    }
}
