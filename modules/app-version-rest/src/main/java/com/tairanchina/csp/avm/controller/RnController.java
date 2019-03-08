package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.utils.StringUtilsExt;
import com.tairanchina.csp.avm.service.RnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hzlizx on 2018/6/22 0022
 */

@Api(value = "/", description = "RN相关接口")
@RestController
public class RnController {

    @Autowired
    private RnService rnService;

    @ApiOperation(value = "查询RN路由", notes = "根据版本号、应用ID、平台、状态获取RN路由信息，根据[更新时间]倒序")
    @GetMapping("/route/{version}-{tenantAppId}-{platform}-{status}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", required = true),
            @ApiImplicitParam(name = "tenantAppId", value = "应用appId", dataType = "string", defaultValue = "uc28ec7f8870a6e785", required = true),
            @ApiImplicitParam(name = "platform", value = "平台，值应为 ios 或 android", required = true),
            @ApiImplicitParam(name = "status", value = "RN路由状态，0:关闭 1:线上开启 2:测试需要", required = true),
    })
    public ServiceResult route(@PathVariable String version,
                               @PathVariable String tenantAppId,
                               @PathVariable String platform,
                               @PathVariable Integer status) {
        if (StringUtilsExt.hasEmpty(version, tenantAppId, platform) || status == null) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        if (status != 1 && status != 2) {
            return ServiceResultConstants.RN_STATUS_ERROR;
        }
        return rnService.route(version, tenantAppId, platform, status);
    }

    @ApiOperation(value = "查询RN包", notes = "根据版本号、应用ID、平台、状态获取RN包信息，根据[更新时间]倒序")
    @GetMapping("/bundles/{version}-{tenantAppId}-{platform}-{status}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", required = true),
            @ApiImplicitParam(name = "tenantAppId", value = "应用appId", dataType = "string", defaultValue = "uc28ec7f8870a6e785", required = true),
            @ApiImplicitParam(name = "platform", value = "平台，值应为 ios 或 android", required = true),
            @ApiImplicitParam(name = "status", value = "RN包状态，0:关闭 1:线上开启 2:测试需要", required = true),
    })
    public ServiceResult bundles(@PathVariable String version,
                                 @PathVariable String tenantAppId,
                                 @PathVariable String platform,
                                 @PathVariable Integer status) {
        if (StringUtilsExt.hasEmpty(version, tenantAppId, platform) || status == null) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        if (status != 1 && status != 2) {
            return ServiceResultConstants.RN_STATUS_ERROR;
        }
        return rnService.bundles(version, tenantAppId, platform, status);
    }
}
