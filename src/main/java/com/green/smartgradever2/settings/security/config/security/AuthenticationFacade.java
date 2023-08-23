package com.green.smartgradever2.settings.security.config.security;


import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// 클라이언트가 요청한 값을
@Component
@Configuration
public class AuthenticationFacade {

    public MyUserDetails getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        return userDetails;
    }

    public Long getLoginUserPk () {
        return getLoginUser().getIuser();
    }
}
