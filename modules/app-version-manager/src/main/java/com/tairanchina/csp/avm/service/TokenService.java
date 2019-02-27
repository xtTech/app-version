package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.JWTSubject;

/**
 * Created by hzlizx on 2019/2/21
 */
public interface TokenService {

    String signJWT(JWTSubject json);

    JWTSubject validateJWT(String jwt);
}
