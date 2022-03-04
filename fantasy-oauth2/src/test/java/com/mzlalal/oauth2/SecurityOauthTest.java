package com.mzlalal.oauth2;


import com.alibaba.fastjson.JSON;
import com.mzlalal.base.common.GlobalResult;
import com.mzlalal.base.entity.global.Result;
import com.mzlalal.base.entity.oauth2.dto.UserEntity;
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

    @Test
    public void fastJsonValidateTest() {
        System.out.println(JSON.parse("12312"));
        System.out.println(JSON.toJSONString(GlobalResult.REPEAT_SUBMIT.result()));
        System.out.println(JSON.parseObject("{\"msg\":\"正在加快处理中，请稍候...\",\"state\":3004}", Result.class));
    }

    @Test
    public void equalsTest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("1");
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setId("1");
        System.out.println(userEntity.hashCode());
        System.out.println(userEntity2.hashCode());
        System.out.println(userEntity.equals(userEntity2));
    }
}
