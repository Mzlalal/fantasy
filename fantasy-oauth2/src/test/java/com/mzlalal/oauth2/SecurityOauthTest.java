package com.mzlalal.oauth2;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

/**
 * @author Mzlalal
 * @date 2021/5/16 13:44
 **/
public class SecurityOauthTest {

    @Test
    public void passwordTest() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        System.out.println(bCryptPasswordEncoder.encode(uuid));
    }
}
