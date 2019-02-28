package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.RnPackageRequestDTO;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import com.tairanchina.csp.avm.entity.RnPackage;
import com.tairanchina.csp.avm.wapper.ExtWrapper;
import com.tairanchina.csp.avm.annotation.OperationRecord;
import com.tairanchina.csp.avm.service.BasicService;
import com.tairanchina.csp.avm.service.RnPackageService;
import com.tairanchina.csp.avm.utils.StringUtilsExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hzlizx on 2018/6/20 0020
 */
@Api(value = "/package", tags = "RN包相关接口")
@RestController
@RequestMapping("/package")
public class RnPackageController {

    @Autowired
    private BasicService basicService;

    @Autowired
    private RnPackageService rnPackageService;

    @ApiOperation(
            value = "列出当前选择的APP内所有RN包（可分页，查询）",
            notes = "列出当前选择的APP内所有RN包信息（可分页，查询）"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数据条数", defaultValue = "10"),
            @ApiImplicitParam(name = "rnName", value = "模块名称（约定）"),
            @ApiImplicitParam(name = "rnNickName", value = "RN包模块通用昵称"),
            @ApiImplicitParam(name = "rnStatus", value = "RN包状态，0:关闭 1:线上开启 2:测试需要"),
    })
    @GetMapping
    public ServiceResult list(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "10") int pageSize,
                              @RequestParam(required = false, defaultValue = "") String rnName,
                              @RequestParam(required = false, defaultValue = "") String rnNickName,
                              @RequestParam(required = false, defaultValue = "") Integer rnStatus) {
        ExtWrapper<RnPackage> wrapper = new ExtWrapper<>();
        wrapper.and().eq("del_flag", 0);
        if (StringUtils.hasLength(rnName)) {
            wrapper.and().like("rn_name", "%" + rnName + "%");
        }
        if (StringUtils.hasLength(rnNickName)) {
            wrapper.and().like("rn_nick_name", "%" + rnNickName + "%");
        }
        if (rnStatus != null) {
            wrapper.and().eq("rn_status", rnStatus);
        }
        wrapper.setVersionSort("rn_version", false);
        return rnPackageService.listSort(page, pageSize, wrapper);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PostMapping
    @OperationRecord(type = OperationRecordLog.OperationType.CREATE, resource = OperationRecordLog.OperationResource.RN_PACKAGE, description = OperationRecordLog.OperationDescription.CREATE_RN_PACKAGE)
    public ServiceResult create(@RequestBody RnPackageRequestDTO rnPackageRequestDTO) {
        if (StringUtilsExt.hasEmpty(
                rnPackageRequestDTO.getRnName(),
                rnPackageRequestDTO.getRnNickName(),
                rnPackageRequestDTO.getResourceUrl(),
                rnPackageRequestDTO.getRnVersion(),
                rnPackageRequestDTO.getRnUpdateLog()
        )) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        //校验版本区间
        if (StringUtilsExt.hasNotBlank(rnPackageRequestDTO.getVersionMin(), rnPackageRequestDTO.getVersionMax())) {
            if (basicService.compareVersion(rnPackageRequestDTO.getVersionMax(), rnPackageRequestDTO.getVersionMin()) <= 0) {
                return ServiceResultConstants.MIN_BIG_THAN_MAX;
            }
        }

        RnPackage rnPackage = new RnPackage();
        BeanUtils.copyProperties(rnPackageRequestDTO, rnPackage);
        return rnPackageService.create(rnPackage);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PutMapping("/{id}")
    @OperationRecord(type = OperationRecordLog.OperationType.UPDATE, resource = OperationRecordLog.OperationResource.RN_PACKAGE, description = OperationRecordLog.OperationDescription.UPDATE_RN_PACKAGE)
    public ServiceResult update(@PathVariable int id, @RequestBody RnPackageRequestDTO rnPackageRequestDTO) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        //校验版本区间
        if (StringUtilsExt.hasNotBlank(rnPackageRequestDTO.getVersionMin(), rnPackageRequestDTO.getVersionMax()) && basicService.compareVersion(rnPackageRequestDTO.getVersionMax(), rnPackageRequestDTO.getVersionMin()) <= 0) {
            return ServiceResultConstants.MIN_BIG_THAN_MAX;
        }
        RnPackage rnPackage = new RnPackage();
        BeanUtils.copyProperties(rnPackageRequestDTO, rnPackage);
        rnPackage.setId(id);
        return rnPackageService.update(rnPackage);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @DeleteMapping("/{id}")
    @OperationRecord(type = OperationRecordLog.OperationType.DELETE, resource = OperationRecordLog.OperationResource.RN_PACKAGE, description = OperationRecordLog.OperationDescription.DELETE_RN_PACKAGE)
    public ServiceResult delete(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return rnPackageService.delete(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/{id}")
    public ServiceResult find(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return rnPackageService.find(id);
    }
}
