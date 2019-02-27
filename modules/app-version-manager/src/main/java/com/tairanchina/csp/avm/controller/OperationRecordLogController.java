package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import com.tairanchina.csp.avm.service.OperationRecordLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(value = "/log", tags = "操作日志相关接口")
@RestController
@RequestMapping("/log")
public class OperationRecordLogController {

    @Autowired
    OperationRecordLogService operationRecordLogService;


    @ApiOperation(
            value = "根据操作日志ID查询单条操作日志信息",
            notes = "根据操作日志ID查询单条操作日志信息"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
            @ApiImplicitParam(name = "id", value = "操作日志id"),
    })
    @GetMapping("/{id}")
    public ServiceResult getOperationRecordLog(@PathVariable Integer id) {
        return operationRecordLogService.getOperationRecordLogById(id);
    }

    @ApiOperation(
            value = "列出全部操作日志（可分页，查询）",
            notes = "列出全部操作日志（可分页，查询）"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数据条数", defaultValue = "10"),
            @ApiImplicitParam(name = "operationType", value = "操作描述，这里的操作类型实际为操作描述（中文的），是根据操作资源查对应的操作类型查出来的"),
            @ApiImplicitParam(name = "appId", value = "appId，int型的"),
            @ApiImplicitParam(name = "phone", value = "手机号"),
            @ApiImplicitParam(name = "nickName", value = "用户昵称"),
            @ApiImplicitParam(name = "startDate", value = "查询开始时间"),
            @ApiImplicitParam(name = "endDate", value = "查询结束时间"),
    })
    @GetMapping
    public ServiceResult list(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "10") int pageSize,
                              @RequestParam(required = false) String operationType, //这里的操作类型实际为操作描述（中文的），是根据操作资源查对应的操作类型查出来的
                              @RequestParam(required = false) String operationResource,
                              @RequestParam(required = false) Integer appId,
                              @RequestParam(required = false) String phone,
                              @RequestParam(required = false) String nickName,
                              @RequestParam(required = false) String startDate,
                              @RequestParam(required = false) String endDate) {
        return operationRecordLogService.getListByQuery(page, pageSize, phone, nickName, appId, operationResource, operationType, null, startDate, endDate);
    }


    @PutMapping("/delete/{id}")
//    @OperationRecord(type = OperationRecordLog.OperationType.DELETE, resource = OperationRecordLog.OperationResource.OPETATION_RECORD_LOG, description = OperationRecordLog.OperationDescription.DELETE_OPETATION_RECORD_LOG)
    public ServiceResult delete(@PathVariable Integer id) {
        return operationRecordLogService.deleteOperationRecordLog(id);
    }

    @DeleteMapping("/{id}")
//    @OperationRecord(type = OperationRecordLog.OperationType.DELETE_FOREVER, resource = OperationRecordLog.OperationResource.OPETATION_RECORD_LOG, description = OperationRecordLog.OperationDescription.DELETE_FOREVER_OPETATION_RECORD_LOG)
    public ServiceResult deleteForever(@PathVariable Integer id) {
        return operationRecordLogService.deleteOperationRecordLogForever(id);
    }

    @ApiOperation(
            value = "操作类型显示",
            notes = "详细接口说明：https://doc.trc.com/#/module/BB4C6A2C8B654BB096519D35C667585F/service/DEE2FECC554B40D3A50525C9386F39B1 "
    )
    @GetMapping("/type")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    public ServiceResult getOperationType() {
        Map<Enum, String> typeMap = new HashMap<>();
        for (OperationRecordLog.OperationType t : OperationRecordLog.OperationType.values()) {
            typeMap.put(t, t.getName());
        }
        return ServiceResult.ok(typeMap);
    }

    @ApiOperation(
            value = "操作资源显示",
            notes = "详细接口说明：https://doc.trc.com/#/module/BB4C6A2C8B654BB096519D35C667585F/service/3857DF13437A41E08FA1B6B17756574F "
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/resource")
    public ServiceResult getOperationResource() {
        Map<Enum, String> resourceMap = new HashMap<>();
        for (OperationRecordLog.OperationResource r : OperationRecordLog.OperationResource.values()) {
            resourceMap.put(r, r.getName());
        }
        return ServiceResult.ok(resourceMap);
    }

    @ApiOperation(
            value = "根据操作资源显示操作类型",
            notes = "详细接口说明：https://doc.trc.com/#/module/BB4C6A2C8B654BB096519D35C667585F/service/56A3F4A40B094CA3A77D38AF33032E3B "
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/{resource}/type")
    public ServiceResult getOperationResourceType(@PathVariable String resource) {
        Map<Enum, String> resourceMap = new HashMap<>();
        for (OperationRecordLog.OperationDescription r : OperationRecordLog.OperationDescription.values()) {
            if (r.toString().endsWith("_" + resource.toUpperCase())) {
                resourceMap.put(r, r.getDescription());
            }
        }
        return ServiceResult.ok(resourceMap);
    }

}
