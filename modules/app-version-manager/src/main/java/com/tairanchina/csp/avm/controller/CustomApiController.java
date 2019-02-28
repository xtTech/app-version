package com.tairanchina.csp.avm.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.CustomApiRequestDTO;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.CustomApi;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import com.tairanchina.csp.avm.annotation.OperationRecord;
import com.tairanchina.csp.avm.service.BasicService;
import com.tairanchina.csp.avm.service.CustomApiService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "/capi", tags = "自定义接口相关")
@RestController
@RequestMapping("/capi")
public class CustomApiController {

    @Autowired
    private BasicService basicService;

    @Autowired
    private CustomApiService customApiService;

    @ApiOperation(
            value = "列出当前选择的APP内所有RN包（可分页，查询）",
            notes = "列出当前选择的APP内所有RN包信息（可分页，查询）"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数据条数", defaultValue = "10"),
            @ApiImplicitParam(name = "osType", value = "适用终端， ios / android"),
            @ApiImplicitParam(name = "customName", value = "自定义接口名称"),
    })
    @GetMapping
    public ServiceResult list(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "10") int pageSize,
                              @RequestParam(required = false) String osType,
                              @RequestParam(required = false) String customName) {
        EntityWrapper<CustomApi> wrapper = new EntityWrapper<>();
        wrapper.and().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());

        if ("android".equals(osType)) {
            wrapper.andNew().eq("android_enabled", 1);
        } else if ("ios".equals(osType)) {
            wrapper.andNew().eq("ios_enabled", 1);
        }
        if (StringUtils.isNotBlank(customName)) {
            wrapper.andNew().like("custom_name", "%" + customName + "%");
        }
        wrapper.andNew().eq("del_flag", 0);
        wrapper.orderBy("created_time", false);
        return customApiService.list(page, pageSize, wrapper);
    }

    @ApiOperation(
            value = "根据自定义接口ID查找对应的自定义接口信息",
            notes = "根据ID查找对应的自定义接口信息"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/{id}")
    public ServiceResult findCustomApi(@PathVariable Integer id) {
        CustomApi customApi = new CustomApi();
        customApi.setId(id);
        return customApiService.getCustomApiByOne(customApi);
    }

    @ApiOperation(
            value = "添加自定义接口",
            notes = "添加自定义接口"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PostMapping("/add")
    @OperationRecord(type = OperationRecordLog.OperationType.CREATE, resource = OperationRecordLog.OperationResource.CUSTOM_API, description = OperationRecordLog.OperationDescription.CREATE_CUSTOM_API)
    public ServiceResult addCustomApi(@Valid @RequestBody CustomApiRequestDTO customApiRequestDTO) {
        if (StringUtils.isBlank(customApiRequestDTO.getCustomKey())) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        //校验版本区间
        ServiceResult serviceResult = basicService.checkVersion(customApiRequestDTO);
        if (serviceResult.getCode() != 200) {
            return serviceResult;
        }
        CustomApi customApi = new CustomApi();
        BeanUtils.copyProperties(customApiRequestDTO, customApi);
        customApi.setId(null);
        customApi.setDelFlag(null);
        return customApiService.createCustomApi(customApi);
    }

    @ApiOperation(
            value = "编辑自定义接口",
            notes = "修改自定义接口"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PutMapping("/update/{id}")
    @OperationRecord(type = OperationRecordLog.OperationType.UPDATE, resource = OperationRecordLog.OperationResource.CUSTOM_API, description = OperationRecordLog.OperationDescription.UPDATE_CUSTOM_API)
    public ServiceResult updateCustomApi(@PathVariable Integer id, @Valid @RequestBody CustomApiRequestDTO customApiRequestDTO) {
        if (1 > id) {
            return ServiceResult.failed(40001, "id不正确");
        }
        //校验版本区间
        ServiceResult serviceResult = basicService.checkVersion(customApiRequestDTO);
        if (serviceResult.getCode() != 200) {
            return serviceResult;
        }

        CustomApi customApi = new CustomApi();
        BeanUtils.copyProperties(customApiRequestDTO, customApi);
        customApi.setId(id);
        return customApiService.updateCustomApi(customApi);
    }

    /**
     * 硬删
     *
     * @param id
     * @return
     */
    @ApiOperation(
            value = "删除自定义接口（硬删）",
            notes = "删除自定义接口"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @DeleteMapping("/{id}")
    @OperationRecord(type = OperationRecordLog.OperationType.DELETE_FOREVER, resource = OperationRecordLog.OperationResource.CUSTOM_API, description = OperationRecordLog.OperationDescription.DELETE_FOREVER_CUSTOM_API)
    public ServiceResult deleteCustomApiForver(@PathVariable Integer id) {
        return customApiService.deleteCustomApiForver(id);
    }

    /**
     * 软删
     *
     * @param id
     * @return
     */

    @ApiOperation(
            value = "删除自定义接口（软删）",
            notes = "删除自定义接口"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PutMapping("/{id}")
    @OperationRecord(type = OperationRecordLog.OperationType.DELETE, resource = OperationRecordLog.OperationResource.CUSTOM_API, description = OperationRecordLog.OperationDescription.DELETE_CUSTOM_API)
    public ServiceResult deleteCustomApi(@PathVariable Integer id) {
        return customApiService.deleteCustomApi(id);
    }


}
