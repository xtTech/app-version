package com.tairanchina.csp.avm.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.Channel;

/**
 * 渠道管理
 * Created by hzlizx on 2018/6/6 0006
 */
public interface ChannelService {

    /**
     * 创建渠道
     * @param channelName   渠道名称
     * @param channelCode   渠道号
     * @return              是否成功
     */
    ServiceResult createChannel(String channelName, String channelCode, Integer channelType);

    /**
     * 删除渠道（软删除）
     * @param channelId     渠道ID
     * @return              是否成功
     */
    ServiceResult deleteChannel(int channelId);

    /**
     * 彻底删除渠道
     * @param channelId     渠道ID
     * @return              是否成功
     */
    ServiceResult deleteChannelForever(int channelId);

    /**
     * 废弃一个渠道
     * 该渠道还会存在，以前发的APK也都存在，但是不能新发版，以后新的版本也不会显示该渠道的发包框。
     * @param channelId     渠道ID
     * @return              是否成功
     */
    ServiceResult scrapChannel(int channelId);

    /**
     * 打开一个被废弃的渠道
     * @param channelId     渠道ID
     * @return              是否成功
     */
    ServiceResult openChannel(int channelId);

    /**
     * 列出所有的渠道
     * @param page          页码
     * @param pageSize      每页大小
     * @param wrapper       查询条件
     * @return              列表
     */
    ServiceResult list(int page, int pageSize, EntityWrapper<Channel> wrapper);

    /**
     * 找到一个渠道
     * @param channelId     渠道ID
     * @return              渠道
     */
    ServiceResult findChannel(int channelId);

    /**
     * 根据channelCode找到channel
     * @param channelCode   渠道码
     * @return              渠道信息
     */
    ServiceResult findByChannelCode(String channelCode);
}
