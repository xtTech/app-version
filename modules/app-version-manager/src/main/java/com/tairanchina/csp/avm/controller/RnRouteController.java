package com.tairanchina.csp.avm.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.RnRouteRequestDTO;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import com.tairanchina.csp.avm.entity.RnRoute;
import com.tairanchina.csp.avm.annotation.OperationRecord;
import com.tairanchina.csp.avm.service.BasicService;
import com.tairanchina.csp.avm.service.RnRouteService;
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
@Api(value = "/route", tags = "RN路由相关接口")
@RestController
@RequestMapping("/route")
public class RnRouteController {

    @Autowired
    private RnRouteService rnRouteService;

    @Autowired
    private BasicService basicService;


    @ApiOperation(
            value = "列出当前选择的APP内所有RN路由（可分页，查询）",
            notes = "列出当前选择的APP内所有RN路由信息（可分页，查询）"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数据条数", defaultValue = "10"),
            @ApiImplicitParam(name = "routeName", value = "路由通用昵称"),
            @ApiImplicitParam(name = "routeKey", value = "被拦截URL（约定）"),
            @ApiImplicitParam(name = "routeValue", value = "目标URL"),
            @ApiImplicitParam(name = "routeStatus", value = "RN路由状态，0:关闭 1:线上开启 2:测试需要"),
    })
    @GetMapping
    public ServiceResult list(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "10") int pageSize,
                              @RequestParam(required = false, defaultValue = "") String routeName,
                              @RequestParam(required = false, defaultValue = "") String routeKey,
                              @RequestParam(required = false, defaultValue = "") String routeValue,
                              @RequestParam(required = false, defaultValue = "") Integer routeStatus) {
        EntityWrapper<RnRoute> wrapper = new EntityWrapper<>();
        wrapper.and().eq("del_flag", 0);
        if (StringUtils.hasLength(routeName)) {
            wrapper.and().like("route_name", "%" + routeName + "%");
        }
        if (StringUtils.hasLength(routeKey)) {
            wrapper.and().like("route_key", "%" + routeKey + "%");
        }

        if (StringUtils.hasLength(routeValue)) {
            wrapper.and().like("route_value", "%" + routeValue + "%");
        }

        if (routeStatus != null) {
            wrapper.and().eq("route_status", routeStatus);
        }
        wrapper.orderBy("created_time", false);
        return rnRouteService.list(page, pageSize, wrapper);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PostMapping
    @OperationRecord(type = OperationRecordLog.OperationType.CREATE, resource = OperationRecordLog.OperationResource.RN_ROUTE, description = OperationRecordLog.OperationDescription.CREATE_RN_ROUTE)
    public ServiceResult create(@RequestBody RnRouteRequestDTO rnRouteRequestDTO) {
        if (StringUtilsExt.hasBlank(
                rnRouteRequestDTO.getRouteName(),
                rnRouteRequestDTO.getRouteKey(),
                rnRouteRequestDTO.getRouteValue()
        )) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        //校验版本区间
        ServiceResult serviceResult = basicService.checkVersion(rnRouteRequestDTO);
        if (serviceResult.getCode() != 200) {
            return serviceResult;
        }
        RnRoute rnRoute = new RnRoute();
        BeanUtils.copyProperties(rnRouteRequestDTO, rnRoute);
        return rnRouteService.create(rnRoute);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PutMapping("/{id}")
    @OperationRecord(type = OperationRecordLog.OperationType.UPDATE, resource = OperationRecordLog.OperationResource.RN_ROUTE, description = OperationRecordLog.OperationDescription.UPDATE_RN_ROUTE)
    public ServiceResult update(@PathVariable int id, @RequestBody RnRouteRequestDTO rnRouteRequestDTO) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        //校验版本区间
        ServiceResult serviceResult = basicService.checkVersion(rnRouteRequestDTO);
        if (serviceResult.getCode() != 200) {
            return serviceResult;
        }
        RnRoute rnRoute = new RnRoute();
        BeanUtils.copyProperties(rnRouteRequestDTO, rnRoute);
        rnRoute.setId(id);
        return rnRouteService.update(rnRoute);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @DeleteMapping("/{id}")
    @OperationRecord(type = OperationRecordLog.OperationType.DELETE, resource = OperationRecordLog.OperationResource.RN_ROUTE, description = OperationRecordLog.OperationDescription.DELETE_RN_ROUTE)
    public ServiceResult delete(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return rnRouteService.delete(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/{id}")
    public ServiceResult find(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return rnRouteService.find(id);
    }
}
