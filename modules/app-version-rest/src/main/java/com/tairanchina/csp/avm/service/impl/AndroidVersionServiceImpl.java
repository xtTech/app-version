package com.tairanchina.csp.avm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ecfront.dew.common.$;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.AndroidVersion;
import com.tairanchina.csp.avm.entity.Apk;
import com.tairanchina.csp.avm.entity.App;
import com.tairanchina.csp.avm.entity.Channel;
import com.tairanchina.csp.avm.mapper.AndroidVersionMapper;
import com.tairanchina.csp.avm.mapper.ApkMapper;
import com.tairanchina.csp.avm.mapper.ChannelMapper;
import com.tairanchina.csp.avm.utils.VersionCompareUtils;
import com.tairanchina.csp.avm.wapper.ExtWrapper;
import com.tairanchina.csp.avm.service.AndroidVersionService;
import com.tairanchina.csp.avm.service.AppService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hzlizx on 2018/6/21 0021
 */
@Service
public class AndroidVersionServiceImpl implements AndroidVersionService {
    private static final Logger logger = LoggerFactory.getLogger(AndroidVersionServiceImpl.class);

    @Autowired
    private AndroidVersionMapper androidVersionMapper;

    @Autowired
    private AppService appService;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private ApkMapper apkMapper;

    @Override
    public ServiceResult findNewestVersion(String tenantAppId, String version, String channelCode) {
        logger.debug("查询tenantAppId为{}的应用...", tenantAppId);
        App appSelected = appService.findAppByTenantAppId(tenantAppId);
        if (appSelected == null) {
            return ServiceResultConstants.APP_NOT_EXISTS;
        }
        logger.debug("找到应用:{}", appSelected.getAppName());

        ExtWrapper<AndroidVersion> androidVersionEntityWrapper = new ExtWrapper<>();
        androidVersionEntityWrapper.and().eq("app_id", appSelected.getId());
        androidVersionEntityWrapper.and().eq("del_flag", 0);
        androidVersionEntityWrapper.and().eq("version_status", 1);
        androidVersionEntityWrapper.setVersionSort("app_version", false);
        List<AndroidVersion> androidVersions = androidVersionMapper.selectList(androidVersionEntityWrapper);
        if (androidVersions.isEmpty()) {
            logger.debug("查询不到新版本或者新版本未上架，当前版本为最新");
            return ServiceResultConstants.NO_NEW_VERSION;
        }
        // 将查询结果再次进行筛选，选出大于传入version的安卓版本，然后再找出最新版本
        List<AndroidVersion> androidVersionsResult = new LinkedList<>();
        androidVersionsResult.addAll(androidVersions);
        if (VersionCompareUtils.compareVersion(version, androidVersions.get(androidVersions.size() - 1).getAppVersion()) > 0) {
            Collections.reverse(androidVersions);
            for (AndroidVersion a : androidVersions) {
                String androidVersionMin = a.getAppVersion();
                if (VersionCompareUtils.compareVersion(version, androidVersionMin) >= 0) {
                    androidVersionsResult.remove(a);
                }
            }
        }
        if (androidVersionsResult.isEmpty()) {
            logger.debug("查询不到新版本或者新版本未上架，当前版本为最新");
            return ServiceResultConstants.NO_NEW_VERSION;
        }
        logger.debug("查询到的筛选过的版本：");
        androidVersionsResult.forEach(androidVersion -> logger.debug(androidVersion.getAppVersion()));

        //查找指定渠道
        logger.debug("查询channelCode为{}的渠道...", channelCode);
        Channel channel = new Channel();
        channel.setAppId(appSelected.getId());
        channel.setChannelCode(channelCode);
        channel.setDelFlag(0);
        channel.setChannelStatus(1);
        List<Channel> channels = channelMapper.selectList(new EntityWrapper<>(channel));
        Channel channelSelected = null;
        if (!channels.isEmpty()) {
            channelSelected = channels.get(0);
            logger.debug("找到指定渠道：{}", channelSelected.toString());
        }

        //查找官方渠道
        Channel channelOfficial = new Channel();
        channelOfficial.setAppId(appSelected.getId());
        channelOfficial.setChannelCode("official");
        channelOfficial.setDelFlag(0);
        channelOfficial.setChannelStatus(1);
        List<Channel> officialChannels = channelMapper.selectList(new EntityWrapper<>(channelOfficial));
        Channel officialChannel = null;
        if (!officialChannels.isEmpty()) {
            officialChannel = officialChannels.get(0);
            logger.debug("找到官方渠道：{}", officialChannel.toString());
        }

        for (AndroidVersion androidVersion : androidVersionsResult) {
            if (channelSelected != null) {
                if (channelSelected.getChannelStatus() == 1) {
                    HashMap<String, Object> apk = this.findApk(androidVersion, appSelected.getId(), channelSelected.getId());
                    if (apk != null) {
                        logger.debug("结果：{}", $.json.toJsonString(apk));
                        return ServiceResult.ok(apk);
                    }
                }
            }
            if (officialChannel != null) {
                HashMap<String, Object> apk = this.findApk(androidVersion, appSelected.getId(), officialChannel.getId());
                if (apk != null) {
                    logger.debug("结果：{}", $.json.toJsonString(apk));
                    return ServiceResult.ok(apk);
                }
            }
        }
        return ServiceResultConstants.APK_NOT_EXISTS;
    }

    @Override
    public ServiceResult getDownloadUrl(int apkId) {
        Apk apk = apkMapper.selectById(apkId);
        if (apk == null) {
            return ServiceResultConstants.APK_NOT_EXISTS;
        }
        return ServiceResult.ok(apk);
    }


    @Override
    public ServiceResult findNewestApk(String tenantAppId, String channelCode) {
        logger.debug("查询tenantAppId为{}的应用...", tenantAppId);
        App appSelected = appService.findAppByTenantAppId(tenantAppId);
        if (appSelected == null) {
            return ServiceResultConstants.APP_NOT_EXISTS;
        }
        logger.debug("找到应用:{}", appSelected.getAppName());

        //查出最新版本
        ExtWrapper<AndroidVersion> androidVersionEntityWrapper = new ExtWrapper<>();
        androidVersionEntityWrapper.and().eq("app_id", appSelected.getId());
        androidVersionEntityWrapper.and().eq("del_flag", 0);
        androidVersionEntityWrapper.and().eq("version_status", 1);
        androidVersionEntityWrapper.setVersionSort("app_version", false);
        List<AndroidVersion> androidVersions = androidVersionMapper.selectList(androidVersionEntityWrapper);
        logger.debug("查询到的版本：");
        androidVersions.forEach(androidVersion -> logger.debug(androidVersion.getAppVersion()));
        if (androidVersions.isEmpty()) {
            logger.debug("查询不到新版本或者新版本未上架，当前版本为最新");
            return ServiceResultConstants.NO_NEW_VERSION;
        }

        //查找渠道，默认为查询官方渠道，如果channelCode有值，则查询指定渠道
        logger.debug("查询channelCode为{}的渠道...", channelCode);
        Channel channel = new Channel();
        channel.setAppId(appSelected.getId());
        if (StringUtils.isBlank(channelCode)) {
            channel.setChannelCode("official");
        } else {
            channel.setChannelCode(channelCode);
        }
        channel.setDelFlag(0);
        channel.setChannelStatus(1);
        List<Channel> channels = channelMapper.selectList(new EntityWrapper<>(channel));
        if (channels.isEmpty()) {
            return ServiceResultConstants.APK_CHANNEL_NOT_EXISTS;
        }

        for (AndroidVersion androidVersion : androidVersions) {
            Apk apk = new Apk();
            apk.setAppId(appSelected.getId());
            apk.setChannelId(channels.get(0).getId());
            apk.setVersionId(androidVersion.getId());
            apk.setDelFlag(0);
            apk.setDeliveryStatus(1);
            List<Apk> apks = apkMapper.selectList(new EntityWrapper<>(apk));
            if (!apks.isEmpty()) {
                try {
                    HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                    response.sendRedirect(apks.get(0).getOssUrl());
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("下载出错");
                }
            }
        }

        return ServiceResultConstants.APK_NOT_EXISTS;

    }

    private ServiceResult loopSelectAndroidVersions(List<AndroidVersion> list, int appId, int channelId) {
        for (AndroidVersion androidVersion : list) {
            int versionId = androidVersion.getId();
            Apk apk = new Apk();
            apk.setAppId(appId);
            apk.setChannelId(channelId);
            apk.setVersionId(versionId);
            apk.setDelFlag(0);
            apk.setDeliveryStatus(1);
            List<Apk> apks = apkMapper.selectList(new EntityWrapper<>(apk));

            if (!apks.isEmpty()) {
                Apk apkSelected = apks.get(0);
                logger.debug("找到APK，开始组装返回值...");
                HashMap<String, Object> map = new HashMap<>();
                map.put("allowLowestVersion", androidVersion.getAllowLowestVersion());
                map.put("downloadUrl", "/v/download/" + apkSelected.getId());
                map.put("description", androidVersion.getVersionDescription());
                map.put("forceUpdate", androidVersion.getUpdateType());
                map.put("version", androidVersion.getAppVersion());
                return ServiceResult.ok(map);
            }
        }
        return null;
    }

    private HashMap<String, Object> findApk(AndroidVersion androidVersion, int appId, int channelId) {
        int versionId = androidVersion.getId();
        Apk apk = new Apk();
        apk.setAppId(appId);
        apk.setChannelId(channelId);
        apk.setVersionId(versionId);
        apk.setDelFlag(0);
        apk.setDeliveryStatus(1);
        List<Apk> apks = apkMapper.selectList(new EntityWrapper<>(apk));
        if (!apks.isEmpty()) {
            Apk apkSelected = apks.get(0);
            logger.debug("找到APK，开始组装返回值...");
            HashMap<String, Object> map = new HashMap<>();
            map.put("allowLowestVersion", androidVersion.getAllowLowestVersion());
            map.put("downloadUrl", "/v/download/" + apkSelected.getId());
            map.put("description", androidVersion.getVersionDescription());
            map.put("forceUpdate", androidVersion.getUpdateType());
            map.put("version", androidVersion.getAppVersion());
            return map;
        }
        return null;
    }
}
