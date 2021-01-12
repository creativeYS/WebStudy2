package com.example.demo.Security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserAccountChangedEvenetListener implements ApplicationListener<UserAccountChangedEvent> {

    @Resource(name = "userDetailsService")
    private UserDetailsService userDetailsService;

    @Override
    public void onApplicationEvent(UserAccountChangedEvent e)
    {

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails newPrincipal = userDetailsService.loadUserByUsername(e.getUserId());

        UsernamePasswordAuthenticationToken newAuth =
                new UsernamePasswordAuthenticationToken(newPrincipal,
                        currentAuth.getCredentials(),
                        newPrincipal.getAuthorities());

        newAuth.setDetails(currentAuth.getDetails());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
