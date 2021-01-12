package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@Getter
public class UserDetailsVO implements UserDetails {

    @Delegate
    private User userVO;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userVO.getPw();
    }

    @Override
    public String getUsername() {
        return userVO.getUserid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
        //return userVO.getIsEnable();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
        //return userVO.getIsEnable();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
        //return userVO.getIsEnable();
    }

    @Override
    public boolean isEnabled() {
        return true;
        //return userVO.getIsEnable();
    }

    public String GetName() { return userVO.getName(); }
}