package com.tairanchina.csp.avm.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.Channel;
import com.tairanchina.csp.avm.entity.OperationRecordLog;
import com.tairanchina.csp.avm.annotation.OperationRecord;
import com.tairanchina.csp.avm.service.ChannelService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hzlizx on 2018/6/8 0008
 */
@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping
    public ServiceResult list(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "10") int pageSize,
                              @RequestParam(required = false, defaultValue = "") String channelName,
                              @RequestParam(required = false, defaultValue = "") String channelCode,
                              @RequestParam(required = false, defaultValue = "") Integer channelStatus) {
        EntityWrapper<Channel> wrapper = new EntityWrapper<>();
        wrapper.and().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        if (StringUtils.hasLength(channelName)) {
            wrapper.and().like("channel_name", "%" + channelName + "%");
        }
        if (StringUtils.hasLength(channelCode)) {
            wrapper.and().like("channel_code", "%" + channelCode + "%");
        }
        if (channelStatus != null && (channelStatus == 1 || channelStatus == 2 || channelStatus == 3)) {
            wrapper.and().eq("channel_status", channelStatus);
        }
        wrapper.and().eq("del_flag",0);
        wrapper.orderBy("created_time", false);
        return channelService.list(page, pageSize, wrapper);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PostMapping
    @OperationRecord(type = OperationRecordLog.OperationType.CREATE, resource = OperationRecordLog.OperationResource.CHANNEL, description = OperationRecordLog.OperationDescription.CREATE_CHANNEL)
    public ServiceResult create(@RequestBody Channel channel) {
        if (StringUtils.isEmpty(channel.getChannelName()) || StringUtils.isEmpty(channel.getChannelCode())) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        if(channel.getChannelName().length()>32){
            return ServiceResultConstants.CHANNEL_NAME_TOO_LONG;
        }
        if(channel.getChannelCode().length()>32){
            return ServiceResultConstants.CHANNEL_CODE_TOO_LONG;
        }
        return channelService.createChannel(channel.getChannelName(), channel.getChannelCode(), channel.getChannelType());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @DeleteMapping("/{id}")
    @OperationRecord(type = OperationRecordLog.OperationType.DELETE, resource = OperationRecordLog.OperationResource.CHANNEL, description = OperationRecordLog.OperationDescription.DELETE_CHANNEL)
    public ServiceResult delete(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return channelService.deleteChannel(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PutMapping("/{id}/scrap")
    @OperationRecord(type = OperationRecordLog.OperationType.SCRAP, resource = OperationRecordLog.OperationResource.CHANNEL, description = OperationRecordLog.OperationDescription.SCRAP_CHANNEL)
    public ServiceResult scrap(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return channelService.scrapChannel(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PutMapping("/{id}/open")
    @OperationRecord(type = OperationRecordLog.OperationType.OPEN, resource = OperationRecordLog.OperationResource.CHANNEL, description = OperationRecordLog.OperationDescription.OPEN_CHANNEL)
    public ServiceResult open(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return channelService.openChannel(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @PutMapping("/{id}/edit")
    @OperationRecord(type = OperationRecordLog.OperationType.UPDATE, resource = OperationRecordLog.OperationResource.CHANNEL, description = OperationRecordLog.OperationDescription.UPDATE_CHANNEL)
    public ServiceResult edit(@PathVariable int id, @RequestBody Channel channel) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return ServiceResultConstants.SERVICE_NOT_SUPPORT;
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户登录凭证", paramType = "header", dataType = "string", defaultValue = "Bearer ", required = true),
    })
    @GetMapping("/{id}")
    public ServiceResult find(@PathVariable int id) {
        if (id < 1) {
            return ServiceResultConstants.NEED_PARAMS;
        }
        return channelService.findChannel(id);
    }

}
