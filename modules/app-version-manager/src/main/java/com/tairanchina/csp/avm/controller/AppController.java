package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.dto.AppRequestDTO;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import com.tairanchina.csp.avm.annotation.OperationRecord;
import com.tairanchina.csp.avm.service.AppService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hzlizx on 2018/6/8 0008
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private AppService appService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping
    public ServiceResult app(@RequestParam(required = false, defaultValue = "1") int page,
                             @RequestParam(required = false, defaultValue = "10") int pageSize){
        return appService.getAppListWithUserId(page, pageSize, ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
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
    @OperationRecord(type = OperationRecordLog.OperationType.CREATE, resource = OperationRecordLog.OperationResource.APP, description = OperationRecordLog.OperationDescription.CREATE_APP)
    @PostMapping
    public ServiceResult createApp(@RequestBody AppRequestDTO appRequestDTO){
        return appService.createApp(appRequestDTO.getAppName(), appRequestDTO.getTenantAppId());
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/bind")
    public ServiceResult myApp(){
        return appService.getMyApp();
    }

}
