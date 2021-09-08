package com.mzlalal.oss;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

/**
 * @author Mzlalal
 * @date 2021/5/16 13:44
 **/
public class SecurityOauthTest {

    @Test
    public void snowIdTest() {
        System.out.println(4 > 4.2d ? "1" : "2");
    }

    @Test
    public void passwordTest() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()));
    }
}
