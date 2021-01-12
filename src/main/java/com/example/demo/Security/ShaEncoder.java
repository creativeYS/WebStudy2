package com.example.demo.Security;

import javax.annotation.Resource;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class ShaEncoder {
    @Resource(name="passwordEncoder")
    private StandardPasswordEncoder encoder;
}
