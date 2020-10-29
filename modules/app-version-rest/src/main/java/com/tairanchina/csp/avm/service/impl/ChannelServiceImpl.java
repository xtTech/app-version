package com.tairanchina.csp.avm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.entity.Channel;
import com.tairanchina.csp.avm.mapper.ChannelMapper;
import com.tairanchina.csp.avm.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 渠道管理实现
 * Created by hzlizx on 2018/6/6 0006
 */
@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private ChannelMapper channelMapper;

    @Override
    public Channel findChannelByChannelId(Integer appId,Integer channelId) {
        Channel channel = new Channel();
        channel.setAppId(appId);
        channel.setId(channelId);
        EntityWrapper<Channel> wrapper = new EntityWrapper<>(channel);
        wrapper.last("limit 0,1");
        List<Channel> channels = channelMapper.selectList(wrapper);
        Channel getChannel=null;
        if (!channels.isEmpty()) {
            getChannel = channels.get(0);
        }
        return getChannel;
    }
}
