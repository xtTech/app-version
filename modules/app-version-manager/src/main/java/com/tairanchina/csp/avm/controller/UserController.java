package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ChangePasswordReq;
import com.tairanchina.csp.avm.dto.LoginReq;
import com.tairanchina.csp.avm.dto.RegisterReq;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import com.tairanchina.csp.avm.annotation.OperationRecord;
import com.tairanchina.csp.avm.service.UserService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by hzlizx on 2018/7/4 0004
 */
@Api(value = "/user", tags = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public ServiceResult login(@RequestBody LoginReq loginReq) {
        return userService.login(loginReq.getPhone(), loginReq.getPassword());
    }


    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public ServiceResult register(@RequestBody RegisterReq registerReq) {
        if (!registerReq.getPassword().trim().equals(registerReq.getPasswordConfirm().trim())) {
            return ServiceResultConstants.PASSWORD_CONFIRM_ERROR;
        }
        int length = registerReq.getPassword().trim().length();
        if (length < 6 || length > 32) {
            return ServiceResultConstants.PASSWORD_ERROR;
        }
        return userService.register(registerReq.getPhone(), registerReq.getPassword());
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/changePassword")
    public ServiceResult register(@RequestBody ChangePasswordReq changePasswordReq) {
        if (!changePasswordReq.getPassword().trim().equals(changePasswordReq.getPasswordConfirm().trim())) {
            return ServiceResultConstants.PASSWORD_CONFIRM_ERROR;
        }
        int length = changePasswordReq.getPassword().trim().length();
        if (length < 6 || length > 32) {
            return ServiceResultConstants.PASSWORD_ERROR;
        }
        return userService.changePassword(changePasswordReq.getOldPassword(), changePasswordReq.getPassword());
    }


    @ApiOperation(
            value = "暂不支持"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/set")
    public ServiceResult set(@RequestParam(required = false, defaultValue = "") String password,
                             @RequestParam(required = false, defaultValue = "") String phone) {
        return ServiceResultConstants.SERVICE_NOT_SUPPORT;
    }

    @ApiOperation(
            value = "暂不支持"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/nick")
    public ServiceResult change(@RequestParam(required = false, defaultValue = "") String password,
                                @RequestParam(required = false, defaultValue = "") String phone,
                                @RequestParam(required = false, defaultValue = "") String nickName) {
        return ServiceResultConstants.SERVICE_NOT_SUPPORT;
    }

    @ApiOperation(
            value = "修改用户昵称（当前登录用户操作）",
            notes = "修改用户昵称"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
            @ApiImplicitParam(name = "nickName", value = "用户昵称", required = true),
    })
    @PutMapping("/update/{nickName}")
    @OperationRecord(type = OperationRecordLog.OperationType.UPDATE, resource = OperationRecordLog.OperationResource.USER, description = OperationRecordLog.OperationDescription.UPDATE_USER)
    public ServiceResult changeNickName(@PathVariable String nickName) {
        String userId = ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId();
        return userService.updateUserNickNameByUserId(userId, nickName);
    }

}
