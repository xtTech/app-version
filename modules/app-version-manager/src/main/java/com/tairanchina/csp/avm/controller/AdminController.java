package com.tairanchina.csp.avm.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.AppRequestDTO;
import com.tairanchina.csp.avm.entity.App;
import com.tairanchina.csp.avm.entity.LoginInfo;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import com.tairanchina.csp.avm.entity.User;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.utils.StringUtilsExt;
import com.tairanchina.csp.avm.annotation.OperationRecord;
import com.tairanchina.csp.avm.dto.AdminUpdateNickNameRequestDTO;
import com.tairanchina.csp.avm.service.AdminService;
import com.tairanchina.csp.avm.service.UserService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;


/**
 * 管理员操作
 */
@Api(value = "/admin", tags = "管理员操作相关接口")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @ApiOperation(
            value = "判断当前登录用户是否是管理员",
            notes = "判断当前登录用户是否是管理员"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/isAdmin")
    public ServiceResult isAdmin() {
        LoginInfo loginInfo = ThreadLocalUtils.USER_THREAD_LOCAL.get();
        return adminService.isAdmin(loginInfo.getUserId());
    }

    /**
     * 列出所有用户
     *
     * @return 用户列表
     */
    @ApiOperation(
            value = "列出所有用户（可分页，查询）",
            notes = "APP版本管理系统的全部用户列表"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数据条数", defaultValue = "10"),
            @ApiImplicitParam(name = "admin", value = "是否是管理员，0：管理员，1：管理员"),
            @ApiImplicitParam(name = "phone", value = "手机号"),
    })
    @GetMapping("/user/list")
    public ServiceResult listUser(@RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "10") int pageSize,
                                  @RequestParam(required = false, defaultValue = "0") int admin,
                                  @RequestParam(required = false, defaultValue = "") String phone) {
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        if (admin == 1) {
            wrapper.and().eq("is_admin", 1);
        }
        if (StringUtils.isNotBlank(phone)) {
            wrapper.and().like("phone", "%" + phone + "%");
        }
        wrapper.isNotNull("first_login_time");
        wrapper.orderBy("first_login_time", false);
        return adminService.listUser(page, pageSize, wrapper);
    }

    /**
     * 列出所有应用
     *
     * @return 应用列表
     */
    @ApiOperation(
            value = "列出所有应用（可分页，查询）",
            notes = "应用列表"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数据条数", defaultValue = "10"),
            @ApiImplicitParam(name = "appName", value = "应用名称"),
            @ApiImplicitParam(name = "isAll", value = "是否查询全部应用(包括已软删的)，1是0否"),
    })
    @GetMapping("/app/list")
    public ServiceResult listApp(@RequestParam(required = false, defaultValue = "1") int page,
                                 @RequestParam(required = false, defaultValue = "10") int pageSize,
                                 @RequestParam(required = false, defaultValue = "") String appName,
                                 @RequestParam(required = false, defaultValue = "false") Boolean isAll) {
        EntityWrapper<App> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(appName)) {
            wrapper.andNew().like("app_name", "%" + appName + "%");
        }
        //是否查询全部的数据（已删和未删的）
        if (!isAll) {
            wrapper.and().eq("del_flag", 0);
        }
        return adminService.listApp(page, pageSize, wrapper);
    }

    /**
     * 根据应用id（int）获取应用信息
     *
     * @return App
     */
    @ApiOperation(
            value = "根据应用id（int）获取应用信息",
            notes = "根据应用id（int）获取应用信息"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true),
            @ApiImplicitParam(name = "id", value = "应用ID", required = true),
    })
    @GetMapping("/app/{id}")
    public ServiceResult app(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return adminService.getApp(id);
    }

    /**
     * 列出所有应用（带绑定信息）
     *
     * @return 应用列表
     */
    @ApiOperation(
            value = "列出用户与所有应用的绑定关系（带绑定信息）",
            notes = "列出所有应用（带绑定信息）"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数据条数", defaultValue = "10"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true),
    })
    @GetMapping("/app/list/bind")
    public ServiceResult listAppWithBindInfo(@RequestParam(required = false, defaultValue = "1") int page,
                                             @RequestParam(required = false, defaultValue = "10") int pageSize,
                                             @RequestParam String userId) {
        EntityWrapper<App> wrapper = new EntityWrapper<>();
        if (StringUtils.isEmpty(userId)) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        wrapper.and().eq("del_flag", 0);
        return adminService.listAppWithBindInfo(page, pageSize, wrapper, userId);
    }

    /**
     * 创建APP
     *
     * @param appRequestDTO 应用实体类（参数，主要是appName）
     * @return 是否成功
     */
    @ApiOperation(
            value = "创建APP",
            notes = "创建APP"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PostMapping("/app")
    @OperationRecord(type = OperationRecordLog.OperationType.CREATE, resource = OperationRecordLog.OperationResource.APP, description = OperationRecordLog.OperationDescription.CREATE_APP)
    public ServiceResult createApp(@RequestBody AppRequestDTO appRequestDTO) {
        if (appRequestDTO == null || StringUtils.isBlank(appRequestDTO.getAppName()) || StringUtils.isBlank(appRequestDTO.getTenantAppId())) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        if (appRequestDTO.getAppName().length() > 32) {
            return ServiceResultConstants.APP_NAME_TOO_LONG;
        }
        if (appRequestDTO.getTenantAppId().length() > 32) {
            return ServiceResultConstants.TENANT_APP_ID_TOO_LONG;
        }

        App app = new App();
        app.setTenantAppId(appRequestDTO.getTenantAppId());
        app.setAppName(appRequestDTO.getAppName());
        return adminService.createApp(app.getAppName(), app.getTenantAppId());
    }

    /**
     * 修改APP
     *
     * @param appRequestDTO 应用实体类（参数，主要是appId和appName）
     * @return 是否成功
     */
    @ApiOperation(
            value = "修改APP",
            notes = "修改APP"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true),
            @ApiImplicitParam(name = "id", value = "appId(int型)", required = true),
    })
    @PutMapping("/app/{id}")
    @OperationRecord(type = OperationRecordLog.OperationType.UPDATE, resource = OperationRecordLog.OperationResource.APP, description = OperationRecordLog.OperationDescription.UPDATE_APP)
    public ServiceResult editApp(@PathVariable int id, @RequestBody AppRequestDTO appRequestDTO) {
        if (appRequestDTO == null || StringUtils.isBlank(appRequestDTO.getAppName()) || StringUtils.isBlank(appRequestDTO.getTenantAppId())) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        if (appRequestDTO.getAppName().length() > 32) {
            return ServiceResultConstants.APP_NAME_TOO_LONG;
        }
        if (appRequestDTO.getTenantAppId().length() > 32) {
            return ServiceResultConstants.TENANT_APP_ID_TOO_LONG;
        }
        App app = new App();
        app.setTenantAppId(appRequestDTO.getTenantAppId());
        app.setAppName(appRequestDTO.getAppName());
        app.setId(id);
        return adminService.editApp(app.getId(), app.getAppName(), app.getTenantAppId());
    }

    /**
     * 删除某个APP
     *
     * @param appId 应用ID
     * @return 是否成功
     */
    @ApiOperation(
            value = "删除某个APP",
            notes = "删除某个APP"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true),
            @ApiImplicitParam(name = "appId", value = "appid(int型)", required = true),
    })
    @DeleteMapping("/app/{appId}")
    @OperationRecord(type = OperationRecordLog.OperationType.DELETE, resource = OperationRecordLog.OperationResource.APP, description = OperationRecordLog.OperationDescription.DELETE_APP)
    public ServiceResult deleteApp(@PathVariable int appId) {
        if (appId < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return adminService.deleteApp(appId);
    }

    /**
     * 绑定某个用户和APP
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 是否成功
     */
    @ApiOperation(
            value = "绑定某个用户和APP",
            notes = "绑定某个用户和APP"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(name = "appId", value = "appId，应用ID(int型)", required = true),
    })
    @PutMapping("/{userId}/{appId}/bind")
    @OperationRecord(type = OperationRecordLog.OperationType.CREATE, resource = OperationRecordLog.OperationResource.USER_APP_REL, description = OperationRecordLog.OperationDescription.CREATE_USER_APP_REL)
    public ServiceResult bind(@PathVariable String userId, @PathVariable int appId) {
        if (StringUtils.isEmpty(userId) || appId < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return adminService.bindUserAndApp(userId, appId);
    }


    /**
     * 列出所有绑定应用
     *
     * @return 应用列表
     */
    @ApiOperation(
            value = "列出用户所有绑定应用",
            notes = "列出所有绑定应用"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true),
    })
    @GetMapping("/app/list/only/bind")
    public ServiceResult listBindApp(@RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return adminService.listBindApp(userId);
    }


    /**
     * 解绑某个用户和APP
     *
     * @param userId 用户ID
     * @param appId  应用ID
     * @return 是否成功
     */
    @ApiOperation(
            value = "解绑某个用户和APP",
            notes = "解绑某个用户和APP"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(name = "appId", value = "appId，应用ID(int型)", required = true),
    })
    @PutMapping("/{userId}/{appId}/unBind")
    @OperationRecord(type = OperationRecordLog.OperationType.DELETE_FOREVER, resource = OperationRecordLog.OperationResource.USER_APP_REL, description = OperationRecordLog.OperationDescription.DELETE_FOREVER_USER_APP_REL)
    public ServiceResult unBind(@PathVariable String userId, @PathVariable int appId) {
        if (StringUtils.isEmpty(userId) || appId < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return adminService.unbindUserAndApp(userId, appId);
    }

    /**
     * 管理员
     * 根据userId修改用户昵称
     *
     * @return
     */
    @ApiOperation(
            value = "根据userId修改用户昵称（仅管理员才可操作）",
            notes = "根据userId修改用户昵称"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true),
    })
    @PutMapping("/user")
    @OperationRecord(type = OperationRecordLog.OperationType.UPDATE, resource = OperationRecordLog.OperationResource.USER, description = OperationRecordLog.OperationDescription.UPDATE_USER)
    public ServiceResult changeNickName(@RequestBody AdminUpdateNickNameRequestDTO user) {
        String userId = user.getUserId();
        String nickName = user.getNickName();
        if (StringUtilsExt.hasBlank(userId, nickName)) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return userService.updateUserNickNameByUserId(userId, nickName);
    }
}
