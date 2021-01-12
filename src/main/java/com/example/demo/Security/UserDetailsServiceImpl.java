package com.example.demo.Security;

import com.example.demo.entity.User;
import com.example.demo.entity.UserDetailsVO;
import com.example.demo.entity.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@AllArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetailsVO loadUserByUsername(String userid) {
        User user = userRepository.findByUserid(userid);
        UserDetailsVO uservo = new UserDetailsVO(user, Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())));
        return uservo;
    }

}