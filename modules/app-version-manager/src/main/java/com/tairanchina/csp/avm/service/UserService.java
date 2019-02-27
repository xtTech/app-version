package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;

public interface UserService {

    ServiceResult register(String phone, String password);

    ServiceResult login(String phone, String password);

    ServiceResult validate(String jwt);

    ServiceResult changePassword(String oldPassword, String password);

    ServiceResult updateUserNickNameByUserId(String userId, String nickname);

}
