package com.tairanchina.csp.avm.service.impl;

import com.tairanchina.csp.avm.dto.JWTSubject;
import org.junit.Test;

/**
 * Created by hzlizx on 2019/2/21
 */
public class TokenServiceImplTest {

    @Test
    public void jwt() {
        JWTSubject jwtSubject = new JWTSubject();
        jwtSubject.setUserId("user111");
        TokenServiceImpl tokenService = new TokenServiceImpl();
        String jwt = tokenService.signJWT(jwtSubject);
        System.out.println(jwt);
        JWTSubject jwtSubject1 = tokenService.validateJWT(jwt);
        if(jwtSubject1!=null){
            System.out.println(jwtSubject1);
        }

    }

}