package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.AndroidVersionRequestDTO;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.AndroidVersion;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import com.tairanchina.csp.avm.utils.StringUtilsExt;
import com.tairanchina.csp.avm.wapper.ExtWrapper;
import com.tairanchina.csp.avm.annotation.OperationRecord;
import com.tairanchina.csp.avm.service.AndroidVersionService;
import com.tairanchina.csp.avm.service.BasicService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hzlizx on 2018/6/11 0011
 */
@Api(value = "/android", tags = "安卓版本相关接口")
@RestController
@RequestMapping("/android")
public class AndroidController {

    @Autowired
    private AndroidVersionService androidVersionService;

    @Autowired
    private BasicService basicService;

    @ApiOperation(
            value = "版本列表查询和分页",
            notes = "版本列表查询和分页"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数据条数", defaultValue = "10"),
            @ApiImplicitParam(name = "appVersion", value = "版本号"),
            @ApiImplicitParam(name = "updateType", value = "更新类型，0：强制更新 1：一般更新 2：静默更新 3：可忽略更新 4：静默可忽略更新"),
            @ApiImplicitParam(name = "versionStatus", value = "上架状态，0-未上架；1-已上架"),
    })
    @GetMapping
    public ServiceResult list(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "10") int pageSize,
                              @RequestParam(required = false, defaultValue = "") String appVersion,
                              @RequestParam(required = false, defaultValue = "") Integer updateType,
                              @RequestParam(required = false, defaultValue = "") Integer versionStatus) {
        ExtWrapper<AndroidVersion> wrapper = new ExtWrapper<>();
        wrapper.and().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        wrapper.and().eq("del_flag", 0);
        wrapper.setVersionSort("app_version", false);
        if (StringUtils.hasLength(appVersion)) {
            wrapper.and().like("app_version", "%" + appVersion + "%");
        }
        if (updateType != null) {
            wrapper.and().eq("update_type", updateType);
        }
        if (versionStatus != null) {
            wrapper.and().eq("version_status", versionStatus);
        }
        return androidVersionService.listSort(page, pageSize, wrapper);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PostMapping
    @OperationRecord(type = OperationRecordLog.OperationType.CREATE, resource = OperationRecordLog.OperationResource.ANDROID_VERSION, description = OperationRecordLog.OperationDescription.CREATE_ANDROID_VERSION)
    public ServiceResult create(@RequestBody AndroidVersionRequestDTO androidVersionRequestDTO) {
        String appVersion = androidVersionRequestDTO.getAppVersion();
        String allowLowestVersion = androidVersionRequestDTO.getAllowLowestVersion();
        String versionDescription = androidVersionRequestDTO.getVersionDescription();
        Integer updateType = androidVersionRequestDTO.getUpdateType();
        if (StringUtilsExt.hasEmpty(appVersion, allowLowestVersion, versionDescription) || updateType == null) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        if (appVersion.length() > 32) {
            return ServiceResultConstants.VERSION_TOO_LONG;
        }
        if (allowLowestVersion.length() > 32) {
            return ServiceResultConstants.VERSION_TOO_LONG;
        }
        //校验版本区间
        if (StringUtilsExt.hasNotBlank(allowLowestVersion, appVersion)) {
            if (basicService.compareVersion(appVersion, allowLowestVersion) < 0) {
                return ServiceResultConstants.ALLOWLOWESTVERSION_BIG_THAN_APPVERSION;
            }
        }
        AndroidVersion androidVersion = new AndroidVersion();
        BeanUtils.copyProperties(androidVersionRequestDTO, androidVersion);
        androidVersion.setId(null); //使用数据库自增ID
        return androidVersionService.createAndroidVersion(androidVersion);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PutMapping("/{id}")
    @OperationRecord(type = OperationRecordLog.OperationType.UPDATE, resource = OperationRecordLog.OperationResource.ANDROID_VERSION, description = OperationRecordLog.OperationDescription.UPDATE_ANDROID_VERSION)
    public ServiceResult update(@RequestBody AndroidVersionRequestDTO androidVersionRequestDTO, @PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }

        String appVersion = androidVersionRequestDTO.getAppVersion();
        String allowLowestVersion = androidVersionRequestDTO.getAllowLowestVersion();
        //校验版本区间
        if (StringUtilsExt.hasNotBlank(allowLowestVersion, appVersion) && basicService.compareVersion(appVersion, allowLowestVersion) < 0) {
            return ServiceResultConstants.ALLOWLOWESTVERSION_BIG_THAN_APPVERSION;
        }
        AndroidVersion androidVersion = new AndroidVersion();
        BeanUtils.copyProperties(androidVersionRequestDTO, androidVersion);
        androidVersion.setId(id);
        return androidVersionService.updateAndroidVersion(androidVersion);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @DeleteMapping("/{id}")
    @OperationRecord(type = OperationRecordLog.OperationType.DELETE, resource = OperationRecordLog.OperationResource.ANDROID_VERSION, description = OperationRecordLog.OperationDescription.DELETE_ANDROID_VERSION)
    public ServiceResult delete(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return androidVersionService.deleteAndroidVersion(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/versions")
    public ServiceResult versions() {
        return androidVersionService.listAllVersion();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/{id}")
    public ServiceResult get(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return androidVersionService.findById(id);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PutMapping("/{id}/delivery")
    @OperationRecord(type = OperationRecordLog.OperationType.DELIVERY, resource = OperationRecordLog.OperationResource.ANDROID_VERSION, description = OperationRecordLog.OperationDescription.DELIVERY_ANDROID_VERSION)
    public ServiceResult delivery(@PathVariable int id) {
        return androidVersionService.delivery(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PutMapping("/{id}/undelivery")
    @OperationRecord(type = OperationRecordLog.OperationType.UNDELIVERY, resource = OperationRecordLog.OperationResource.ANDROID_VERSION, description = OperationRecordLog.OperationDescription.UNDELIVERY_ANDROID_VERSION)
    public ServiceResult undelivery(@PathVariable int id) {
        return androidVersionService.undelivery(id);
    }
}
