package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.Channel;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


public class ChannelServiceTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ChannelServiceTest.class);

    @Autowired
    private ChannelService channelService;

    @Test
    public void createChannel() throws Exception {
        Channel channel = new Channel();
        channel.setChannelName("name");
        channel.setChannelCode("code");
        channel.setChannelType(1);
        ServiceResult result = channelService.createChannel(channel.getChannelName(), channel.getChannelCode(), channel.getChannelType());
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        channel.setChannelCode("fghfsghfshgfs");
        result = channelService.createChannel(channel.getChannelName(), channel.getChannelCode(), channel.getChannelType());
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void deleteChannel() throws Exception {
        Integer id = 24;
        ServiceResult result = channelService.deleteChannel(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void deleteChannelForever() throws Exception {
        Integer id = 24;
        ServiceResult result = channelService.deleteChannelForever(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void scrapChannel() throws Exception {
        Integer id = 22;
        ServiceResult result = channelService.scrapChannel(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 44444;
        result = channelService.scrapChannel(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 34;
        result = channelService.scrapChannel(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void openChannel() throws Exception {
        Integer id = 31;
        ServiceResult result = channelService.openChannel(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 444444;
        result = channelService.openChannel(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 28;
        result = channelService.openChannel(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void list() throws Exception {
        String channelName = "";
        String channelCode = "";
        Integer channelStatus = 1;
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
        wrapper.orderBy("created_time", false);
        ServiceResult result = channelService.list(1, 10, wrapper);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void findChannel() throws Exception {
        Integer id = 24;
        ServiceResult result = channelService.findChannel(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        id = 44444;
        result = channelService.findChannel(id);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

    @Test
    public void findByChannelCode() throws Exception {
        String channelCode = "tttt";
        ServiceResult result = channelService.findByChannelCode(channelCode);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }

        channelCode = "xxxx";
        result = channelService.findByChannelCode(channelCode);
        if (result.getData() != null) {
            logger.info(result.getData().toString());
        }
    }

}