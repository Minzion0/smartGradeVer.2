package com.green.smartgradever2.settings.security.config.security.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class MyUserDetails implements UserDetails {
    private Long iuser;
    private String userNum;
    private String uid;
    private String upw;

    @Builder.Default // 권한을 담아준다.
    private List<String> roles = new ArrayList<>();


    @Override // 권한을 리턴하는 부분 ROLL_USER
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() { return this.upw; }

    @Override
    public String getUsername() { return this.uid; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
