package com.tairanchina.csp.avm.service;

import com.tairanchina.csp.avm.dto.ServiceResult;

public interface UserService {


    ServiceResult getUser(String userId);

    ServiceResult addUser(String phone,String userName,String password,String nickName);

    ServiceResult editUser(String phone,String userName,String nickName,String id);

    ServiceResult restPwd(String password, String userId);

    ServiceResult delUser(String userId);

    ServiceResult register(String phone, String password);

    ServiceResult login(String phone, String password);

    ServiceResult validate(String jwt);

    ServiceResult changePassword(String oldPassword, String password);

    ServiceResult updateUserNickNameByUserId(String userId, String nickname);

}
