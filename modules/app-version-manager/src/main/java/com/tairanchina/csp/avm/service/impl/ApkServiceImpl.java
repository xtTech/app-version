package com.tairanchina.csp.avm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ecfront.dew.common.$;
import com.tairanchina.csp.avm.constants.RedisKey;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ApkExt;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.AndroidVersion;
import com.tairanchina.csp.avm.entity.Apk;
import com.tairanchina.csp.avm.entity.Channel;
import com.tairanchina.csp.avm.mapper.AndroidVersionMapper;
import com.tairanchina.csp.avm.mapper.ApkMapper;
import com.tairanchina.csp.avm.mapper.ChannelMapper;
import com.tairanchina.csp.avm.service.ApkService;
import com.tairanchina.csp.avm.service.BasicService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.cluster.ClusterLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hzlizx on 2018/6/14 0014
 */
@Service
public class ApkServiceImpl implements ApkService {
    private static final Logger logger = LoggerFactory.getLogger(ApkServiceImpl.class);

    @Autowired
    private ApkMapper apkMapper;

    @Autowired
    private BasicService basicService;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    AndroidVersionMapper androidVersionMapper;

    @Override
    public ServiceResult create(Apk apk) {
        apk.setAppId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        apk.setId(null);
        apk.setCreatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());

        EntityWrapper<Apk> apkEntityWrapper = new EntityWrapper<>();
        apkEntityWrapper.and().eq("channel_id", apk.getChannelId());
        apkEntityWrapper.and().eq("app_id", apk.getAppId());
        apkEntityWrapper.and().eq("version_id", apk.getVersionId());
        apkEntityWrapper.and().eq("del_flag", 0);

        ClusterLock lock = Dew.cluster.lock.instance(RedisKey.APP_LOCK + ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        try {
            if (lock.tryLock(5000)) {
                if (!apkMapper.selectList(apkEntityWrapper).isEmpty()) {
                    return ServiceResultConstants.APK_EXISTS;
                }

                Integer insert = apkMapper.insert(apk);
                if (insert > 0) {
                    return ServiceResult.ok(apk);
                } else {
                    return ServiceResultConstants.DB_ERROR;
                }
            }
            return ServiceResultConstants.ERROR_500;
        } catch (InterruptedException e) {
            logger.error("加锁失败", e);
            Thread.currentThread().interrupt();
            return ServiceResultConstants.ERROR_500;
        } finally {
            lock.unLock();
        }
    }

    @Override
    public ServiceResult list(int page, int pageSize, EntityWrapper<Apk> wrapper, int versionId) {
        AndroidVersion androidVersion = androidVersionMapper.selectById(versionId);
        if (androidVersion == null) {
            return ServiceResultConstants.VERSION_NOT_EXISTS;
        }
        Page<Apk> apkPage = new Page<>();
        apkPage.setCurrent(page);
        apkPage.setSize(pageSize);
        apkPage.setRecords(apkMapper.selectPage(apkPage, wrapper));
        basicService.formatCreatedBy(apkPage.getRecords());
        List<ApkExt> collect = apkPage.getRecords().stream().map(mapper -> {
            ApkExt ext = new ApkExt();
            try {
                $.bean.copyProperties(ext, mapper);
                Channel channel = channelMapper.selectById(mapper.getChannelId());
                if (channel != null) {
                    ext.setChannelCode(channel.getChannelCode());
                }
                ext.setVersion(androidVersion.getAppVersion());
                return ext;
            } catch (InvocationTargetException | IllegalAccessException e) {
                logger.error("Apk参数转换出错", e);
            }
            return null;
        }).collect(Collectors.toList());

        Page<ApkExt> apkExtPage = new Page<>();
        try {
            $.bean.copyProperties(apkExtPage, apkPage);
            apkExtPage.setRecords(collect);
            return ServiceResult.ok(apkExtPage);
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.error("Apk参数转换出错", e);
        }
        return ServiceResultConstants.DB_ERROR;
    }

    @Override
    public ServiceResult delivery(int apkId) {
        Apk apk = apkMapper.selectById(apkId);
        if (apk == null) {
            return ServiceResultConstants.APK_NOT_EXISTS;
        }

        if (!apk.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        apk.setDeliveryStatus(1);
        Integer integer = apkMapper.updateById(apk);
        if (integer > 0) {
            return ServiceResult.ok(apk);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult undelivery(int apkId) {
        Apk apk = apkMapper.selectById(apkId);
        if (apk == null) {
            return ServiceResultConstants.APK_NOT_EXISTS;
        }

        if (!apk.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        apk.setDeliveryStatus(0);
        Integer integer = apkMapper.updateById(apk);
        if (integer > 0) {
            return ServiceResult.ok(apk);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult delete(int apkId) {
        Apk apk = apkMapper.selectById(apkId);
        if (apk == null) {
            return ServiceResultConstants.APK_NOT_EXISTS;
        }
        if (!apk.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        apk.setDelFlag(1);
        Integer integer = apkMapper.updateById(apk);
        if (integer > 0) {
            return ServiceResult.ok(apk);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult exists(String channelCode, int versionId) {
        Channel channel = new Channel();
        channel.setChannelCode(channelCode);
        channel.setAppId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        channel.setDelFlag(0);

        EntityWrapper<Channel> wrapper = new EntityWrapper<>(channel);

        wrapper.last("limit 0,1");
        List<Channel> channels = channelMapper.selectList(wrapper);
        if (channels.isEmpty()) {
            return ServiceResultConstants.CHANNEL_NOT_EXISTS;
        }
        Channel channelSelected = channels.get(0);
        if (channelSelected.getChannelStatus() != 1) {
            return ServiceResultConstants.CHANNEL_STATUS_2_3;
        }
        Apk apk = new Apk();
        apk.setChannelId(channelSelected.getId());
        apk.setVersionId(versionId);
        apk.setDelFlag(0);
        apk.setAppId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        EntityWrapper<Apk> apkEntityWrapper = new EntityWrapper<>(apk);
        apkEntityWrapper.last("limit 0,1");
        List<Apk> apks = apkMapper.selectList(apkEntityWrapper);
        HashMap<String, Object> map = new HashMap<>();
        String message = "APK不存在";
        if (apks.isEmpty()) {
            map.put("exists", false);
        } else {
            message = "该渠道下APK已经上传包，请勿重复上传";
            map.put("exists", true);
        }
        ServiceResult ok = ServiceResult.ok(map);
        ok.setMessage(message);
        return ok;
    }

    @Override
    public ServiceResult getApkPageWithChannelCode(int page, int pageSize, Integer versionId, String channelCode, String md5, Integer deliveryStatus) {
        AndroidVersion androidVersion = androidVersionMapper.selectById(versionId);
        if (androidVersion == null) {
            return ServiceResultConstants.VERSION_NOT_EXISTS;
        }
        Page<ApkExt> apkPage = new Page<>();
        apkPage.setCurrent(page);
        apkPage.setSize(pageSize);
        apkPage.setRecords(apkMapper.selectApkWithChannelCode(apkPage, versionId, channelCode, md5, deliveryStatus));
        basicService.formatCreatedBy(apkPage.getRecords());
        return ServiceResult.ok(apkPage);
    }
}
