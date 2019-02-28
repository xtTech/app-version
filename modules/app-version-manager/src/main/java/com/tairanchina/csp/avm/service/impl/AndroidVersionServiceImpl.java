package com.tairanchina.csp.avm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.AndroidVersion;
import com.tairanchina.csp.avm.entity.Apk;
import com.tairanchina.csp.avm.enums.ChatBotEventType;
import com.tairanchina.csp.avm.mapper.AndroidVersionMapper;
import com.tairanchina.csp.avm.mapper.ApkMapper;
import com.tairanchina.csp.avm.mapper.ChannelMapper;
import com.tairanchina.csp.avm.utils.VersionCompareUtils;
import com.tairanchina.csp.avm.wapper.ExtWrapper;
import com.tairanchina.csp.avm.service.AndroidVersionService;
import com.tairanchina.csp.avm.service.BasicService;
import com.tairanchina.csp.avm.service.ChatBotService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 安卓版本管理实现
 * Created by hzlizx on 2018/6/6 0006
 */
@Service
public class AndroidVersionServiceImpl implements AndroidVersionService {

    @Autowired
    private AndroidVersionMapper androidVersionMapper;

    @Autowired
    private ApkMapper apkMapper;

    @Autowired
    private BasicService basicService;

    @Autowired
    private ChatBotService chatBotService;

    @Override
    public ServiceResult createAndroidVersion(AndroidVersion androidVersion) {
        //校验安卓版本是否已存在
        if (checkVersionExist(androidVersion)){
            return ServiceResultConstants.VERSION_EXISTS;
        }
        androidVersion.setCreatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        androidVersion.setAppId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        Integer insert = androidVersionMapper.insert(androidVersion);
        if (insert > 0) {
            chatBotService.sendMarkdown(ChatBotEventType.ANDROID_VERSION_CREATED, "创建新的Android版本提醒",makeMarkdown(androidVersion));
            return ServiceResult.ok(androidVersion);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult deleteAndroidVersion(int versionId) {
        AndroidVersion androidVersion = androidVersionMapper.selectById(versionId);
        if (androidVersion == null) {
            return ServiceResultConstants.VERSION_NOT_EXISTS;
        }
        if (!androidVersion.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        androidVersion.setDelFlag(1);
        Integer integer = androidVersionMapper.updateById(androidVersion);
        if (integer > 0) {
            EntityWrapper<Apk> wrapper = new EntityWrapper<>();
            wrapper.and().eq("version_id",versionId);
            wrapper.and().eq("del_flag",0);
            Apk apk = new Apk();
            apk.setDelFlag(1);
            apkMapper.update(apk,wrapper);
            return ServiceResult.ok(androidVersion);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult deleteAndroidVersionForever(int versionId) {
        AndroidVersion androidVersion = androidVersionMapper.selectById(versionId);
        if (androidVersion == null) {
            return ServiceResultConstants.VERSION_NOT_EXISTS;
        }
        if (!androidVersion.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        if (androidVersion.getDelFlag() != 1) {
            return ServiceResultConstants.VERSION_NOT_SOFT_DELETE;
        }
        Integer integer = androidVersionMapper.deleteById(versionId);
        if (integer > 0) {
            return ServiceResult.ok(androidVersion);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult updateAndroidVersion(AndroidVersion androidVersion) {

        AndroidVersion androidVersionSelected = androidVersionMapper.selectById(androidVersion.getId());
        if (androidVersionSelected == null) {
            return ServiceResultConstants.VERSION_NOT_EXISTS;
        }
        if (!androidVersionSelected.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        //校验安卓版本是否已存在
        if (checkVersionExist(androidVersion)){
            return ServiceResultConstants.VERSION_EXISTS;
        }
        androidVersion.setDelFlag(0);
        androidVersion.setCreatedBy(null);
        androidVersion.setCreatedTime(null);
        androidVersion.setUpdatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        androidVersion.setUpdatedTime(null);
        Integer integer = androidVersionMapper.updateById(androidVersion);
        if (integer > 0) {
            return ServiceResult.ok(androidVersion);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult list(int page, int pageSize, EntityWrapper<AndroidVersion> wrapper) {
        Page<AndroidVersion> pageEntity = new Page<>(page, pageSize);
        pageEntity.setRecords(androidVersionMapper.selectPage(pageEntity, wrapper));
        basicService.formatCreatedBy(pageEntity.getRecords());
        return ServiceResult.ok(pageEntity);
    }

    @Override
    public ServiceResult listSort(int page, int pageSize, EntityWrapper<AndroidVersion> wrapper) {
        Page<AndroidVersion> pageEntity = new Page<>(page, pageSize);
        List<AndroidVersion> androidVersions = androidVersionMapper.selectList(wrapper);
        androidVersions.sort((o1, o2) -> VersionCompareUtils.compareVersion(o2.getAppVersion(),o1.getAppVersion()));
        List<AndroidVersion> pageList = androidVersions.subList((page-1) * pageSize, pageSize * page >= androidVersions.size() ? androidVersions.size() : pageSize * page);
        pageEntity.setRecords(pageList);
        pageEntity.setTotal(androidVersions.size());
        basicService.formatCreatedBy(pageEntity.getRecords());
        return ServiceResult.ok(pageEntity);
    }

    @Override
    public ServiceResult findBetweenVersionList(String version1, String version2, EntityWrapper<AndroidVersion> wrapper) {
        List<AndroidVersion> androidVersions = androidVersionMapper.selectList(wrapper);
        String max = VersionCompareUtils.compareVersion(version1,version2) >= 0 ? version1 : version2;
        String min = VersionCompareUtils.compareVersion(version1,version2) <= 0 ? version1 : version2;
        List<AndroidVersion> versionList = androidVersions.stream().filter(o -> VersionCompareUtils.compareVersion(o.getAppVersion(), min) >= 0 && VersionCompareUtils.compareVersion(max, o.getAppVersion()) >= 0)
                .sorted((o1, o2) -> VersionCompareUtils.compareVersion(o2.getAppVersion(), o1.getAppVersion())).collect(Collectors.toList());
        return ServiceResult.ok(versionList);
    }

    @Override
    public ServiceResult findById(int id) {
        AndroidVersion androidVersion = androidVersionMapper.selectById(id);
        if (androidVersion == null) {
            return ServiceResultConstants.VERSION_NOT_EXISTS;
        } else {
            if (!androidVersion.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
                return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
            }
            return ServiceResult.ok(androidVersion);
        }
    }

    @Override
    public ServiceResult listAllVersion() {
        ExtWrapper<AndroidVersion> wrapper = new ExtWrapper<>();
        wrapper.and().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        wrapper.and().eq("del_flag", 0);
        wrapper.setVersionSort("app_version",false);
        List<AndroidVersion> iosVersions = androidVersionMapper.selectList(wrapper);
        List<String> collect = iosVersions.stream().map(AndroidVersion::getAppVersion).collect(Collectors.toList());
        return ServiceResult.ok(collect);
    }

    @Override
    public ServiceResult delivery(int id) {
        AndroidVersion androidVersion = androidVersionMapper.selectById(id);
        if (androidVersion == null) {
            return ServiceResultConstants.VERSION_NOT_EXISTS;
        }
        if (!androidVersion.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
//        //只有官方渠道official上架了，才能上架
//        Channel channel = new Channel();
//        channel.setChannelCode("official");
//        channel.setAppId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
//        channel.setDelFlag(0);
//        List<Channel> channels = channelMapper.selectList(new EntityWrapper<>(channel));
//        if (channels.size() == 0) {
//            return ServiceResultConstants.CHANNEL_OFFICIAL_NOT_EXISTS;
//        }
//
//        Channel channelSelected = channels.get(0);
//        Apk apk = new Apk();
//        apk.setAppId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
//        apk.setVersionId(androidVersion.getId());
//        apk.setChannelId(channelSelected.getId());
//        apk.setDelFlag(0);
//        List<Apk> apks = apkMapper.selectList(new EntityWrapper<>(apk));
//        if (apks.size() == 0) {
//            return ServiceResultConstants.CHANNEL_OFFICIAL_NOT_UPLOADED;
//        }
//        Apk apkSelected = apks.get(0);
//        if (apkSelected.getDeliveryStatus() == 0) {
//            return ServiceResultConstants.CHANNEL_OFFICIAL_NOT_UPLOADED;
//        }
        androidVersion.setVersionStatus(1);
        Integer integer = androidVersionMapper.updateById(androidVersion);
        if (integer > 0) {
            return ServiceResult.ok(androidVersion);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult undelivery(int id) {
        AndroidVersion androidVersion = androidVersionMapper.selectById(id);
        if (androidVersion == null) {
            return ServiceResultConstants.VERSION_NOT_EXISTS;
        }
        if (!androidVersion.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        androidVersion.setVersionStatus(0);
        Integer integer = androidVersionMapper.updateById(androidVersion);
        if (integer > 0) {
            return ServiceResult.ok(androidVersion);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    private Boolean checkVersionExist(AndroidVersion androidVersion) {
        String appVersion = androidVersion.getAppVersion();
        EntityWrapper<AndroidVersion> wrapper = new EntityWrapper<>();
        wrapper.and().eq("del_flag", 0);
        wrapper.and().eq("app_version", appVersion);
        wrapper.and().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        List<AndroidVersion> androidVersions = androidVersionMapper.selectList(wrapper);
        if (!androidVersions.isEmpty()) {
            AndroidVersion result = androidVersions.get(0);
            if (androidVersion.getId() == null) {
                return true;
            }
            if (androidVersion.getId() != 0 && androidVersion.getId().intValue() != result.getId().intValue()) {
                return true;
            }
        }
        return false;
    }

    private String makeMarkdown(AndroidVersion androidVersion) {
        StringBuilder sb = new StringBuilder();
        sb.append("#### 发布了新Android版本[ " + androidVersion.getAppVersion() + " ]\n\n");
        sb.append("> **允许最低版本** ：" + androidVersion.getAllowLowestVersion() + "\n\n");

        Integer customStatus = androidVersion.getUpdateType();
        //0：强制更新 1：一般更新 2：静默更新 3：可忽略更新 4：静默可忽略更新
        switch (customStatus) {
            case 0:
                sb.append("> **更新类型** ：强制更新\n\n");
                break;
            case 1:
                sb.append("> **更新类型** ：一般更新\n\n");
                break;
            case 2:
                sb.append("> **更新类型** ：静默更新\n\n");
                break;
            case 3:
                sb.append("> **更新类型** ：可忽略更新\n\n");
                break;
            case 4:
                sb.append("> **更新类型** ：静默可忽略更新\n\n");
                break;
            default:
                break;
        }
        Integer versionStatus = androidVersion.getVersionStatus();
        if(versionStatus!=null){
            switch (versionStatus) {
                case 0:
                    sb.append("> **发布状态** ：未上架\n\n");
                    break;
                case 1:
                    sb.append("> **发布状态** ：已上架\n\n");
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    }
}
