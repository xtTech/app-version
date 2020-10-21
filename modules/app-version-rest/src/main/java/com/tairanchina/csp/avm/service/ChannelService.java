package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.Channel;

/**
 * 渠道管理
 * Created by hzlizx on 2018/6/6 0006
 */
public interface ChannelService {

    /****
     * 根据appId和channelCode找到Channel
     * @param appId：appId
     * @param channelId: 渠道ID
     * @return
     ****/
     Channel findChannelByChannelId(Integer appId,Integer channelId);

}
