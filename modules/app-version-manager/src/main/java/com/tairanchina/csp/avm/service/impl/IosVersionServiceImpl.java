package com.tairanchina.csp.avm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.entity.IosVersion;
import com.tairanchina.csp.avm.enums.ChatBotEventType;
import com.tairanchina.csp.avm.mapper.IosVersionMapper;
import com.tairanchina.csp.avm.utils.VersionCompareUtils;
import com.tairanchina.csp.avm.wapper.ExtWrapper;
import com.tairanchina.csp.avm.service.BasicService;
import com.tairanchina.csp.avm.service.ChatBotService;
import com.tairanchina.csp.avm.service.IosVersionService;
import com.tairanchina.csp.avm.utils.ThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hzlizx on 2018/6/11 0011
 */
@Service
public class IosVersionServiceImpl implements IosVersionService {

    @Autowired
    private IosVersionMapper iosVersionMapper;

    @Autowired
    private BasicService basicService;

    @Autowired
    private ChatBotService chatBotService;

    @Override
    public ServiceResult create(IosVersion iosVersion) {
        iosVersion.setAppId(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        iosVersion.setCreatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        if (checkVersionExist(iosVersion)) {
            return ServiceResultConstants.VERSION_EXISTS;
        }
        Integer insert = iosVersionMapper.insert(iosVersion);
        if (insert > 0) {
            chatBotService.sendMarkdown(ChatBotEventType.IOS_VERSION_CREATED, "创建新的iOS版本提醒",makeMarkdown(iosVersion));
            return ServiceResult.ok(iosVersion);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult update(IosVersion iosVersion) {
        Integer id = iosVersion.getId();
        IosVersion iosVersionSelected = iosVersionMapper.selectById(id);
        if (iosVersionSelected == null) {
            return ServiceResultConstants.VERSION_NOT_EXISTS;
        }
        if (!iosVersionSelected.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        if (checkVersionExist(iosVersion)) {
            return ServiceResultConstants.VERSION_EXISTS;
        }
        iosVersion.setAppId(null);
        iosVersion.setCreatedBy(null);
        iosVersion.setCreatedTime(null);
        iosVersion.setUpdatedBy(ThreadLocalUtils.USER_THREAD_LOCAL.get().getUserId());
        iosVersion.setUpdatedTime(null);
        iosVersion.setAppId(null);
        iosVersion.setDelFlag(null);
        Integer integer = iosVersionMapper.updateById(iosVersion);
        if (integer > 0) {
            return ServiceResult.ok(iosVersion);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult delete(int id) {
        IosVersion iosVersion = iosVersionMapper.selectById(id);
        if (iosVersion == null) {
            return ServiceResultConstants.VERSION_NOT_EXISTS;
        }
        if (!iosVersion.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        iosVersion.setDelFlag(1);
        Integer integer = iosVersionMapper.updateById(iosVersion);
        if (integer > 0) {
            return ServiceResult.ok(iosVersion);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult list(int page, int pageSize, EntityWrapper<IosVersion> wrapper) {
        Page<IosVersion> versionPage = new Page<>();
        versionPage.setCurrent(page);
        versionPage.setSize(pageSize);
        versionPage.setRecords(iosVersionMapper.selectPage(versionPage, wrapper));
        basicService.formatCreatedBy(versionPage.getRecords());
        return ServiceResult.ok(versionPage);
    }

    @Override
    public ServiceResult listSort(int page, int pageSize, EntityWrapper<IosVersion> wrapper) {
        Page<IosVersion> versionPage = new Page<>(page,pageSize);
        List<IosVersion> iosVersions = iosVersionMapper.selectList(wrapper);
        iosVersions.sort((o1, o2) -> VersionCompareUtils.compareVersion(o2.getAppVersion(),o1.getAppVersion()));
        List<IosVersion> pageList = iosVersions.subList((page-1) * pageSize, pageSize * page >= iosVersions.size() ? iosVersions.size() : pageSize * page);
        versionPage.setRecords(pageList);
        versionPage.setTotal(iosVersions.size());
        basicService.formatCreatedBy(versionPage.getRecords());
        return ServiceResult.ok(versionPage);
    }

    @Override
    public ServiceResult findBetweenVersionList(String version1, String version2, EntityWrapper<IosVersion> wrapper) {
        List<IosVersion> iosVersions = iosVersionMapper.selectList(wrapper);
        String max = VersionCompareUtils.compareVersion(version1,version2) >= 0 ? version1 : version2;
        String min = VersionCompareUtils.compareVersion(version1,version2) <= 0 ? version1 : version2;
        List<IosVersion> versionList = iosVersions.stream().filter(o -> VersionCompareUtils.compareVersion(o.getAppVersion(), min) >= 0 && VersionCompareUtils.compareVersion(max, o.getAppVersion()) >= 0)
                .sorted((o1, o2) -> VersionCompareUtils.compareVersion(o2.getAppVersion(), o1.getAppVersion())).collect(Collectors.toList());
        return ServiceResult.ok(versionList);
    }

    @Override
    public ServiceResult listAllVersion() {
        ExtWrapper<IosVersion> wrapper = new ExtWrapper<>();
        wrapper.and().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        wrapper.and().eq("del_flag", 0);
        wrapper.setVersionSort("app_version",false);
        List<IosVersion> iosVersions = iosVersionMapper.selectList(wrapper);
        List<String> collect = iosVersions.stream().map(IosVersion::getAppVersion).collect(Collectors.toList());
        return ServiceResult.ok(collect);
    }

    @Override
    public ServiceResult delivery(int id) {
        IosVersion iosVersion = iosVersionMapper.selectById(id);
        if (iosVersion == null) {
            return ServiceResultConstants.VERSION_NOT_EXISTS;
        }
        if (!iosVersion.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        iosVersion.setVersionStatus(1);
        Integer integer = iosVersionMapper.updateById(iosVersion);
        if (integer > 0) {
            return ServiceResult.ok(iosVersion);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult undelivery(int id) {
        IosVersion iosVersion = iosVersionMapper.selectById(id);
        if (iosVersion == null) {
            return ServiceResultConstants.VERSION_NOT_EXISTS;
        }
        if (!iosVersion.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        iosVersion.setVersionStatus(0);
        Integer integer = iosVersionMapper.updateById(iosVersion);
        if (integer > 0) {
            return ServiceResult.ok(iosVersion);
        } else {
            return ServiceResultConstants.DB_ERROR;
        }
    }

    @Override
    public ServiceResult get(int id) {
        IosVersion iosVersion = iosVersionMapper.selectById(id);
        if (iosVersion == null) {
            return ServiceResultConstants.VERSION_NOT_EXISTS;
        }
        if (!iosVersion.getAppId().equals(ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId())) {
            return ServiceResultConstants.RESOURCE_NOT_BELONG_APP;
        }
        return ServiceResult.ok(iosVersion);
    }

    private Boolean checkVersionExist(IosVersion iosVersion) {
        EntityWrapper<IosVersion> wrapper = new EntityWrapper<>();
        wrapper.and().eq("del_flag", 0);
        wrapper.and().eq("app_version", iosVersion.getAppVersion());
        wrapper.and().eq("app_id", ThreadLocalUtils.USER_THREAD_LOCAL.get().getAppId());
        List<IosVersion> iosVersions = iosVersionMapper.selectList(wrapper);
        if (!iosVersions.isEmpty()) {
            IosVersion result = iosVersions.get(0);
            if (iosVersion.getId() == null) {
                return true;
            }
            if (iosVersion.getId() != 0 && iosVersion.getId().intValue() != result.getId().intValue()) {
                return true;
            }
        }
        return false;
    }


    private String makeMarkdown(IosVersion iosVersion) {
        StringBuilder sb = new StringBuilder();
        sb.append("#### 发布了新iOS版本[ " + iosVersion.getAppVersion() + " ]\n\n");
        sb.append("> **允许最低版本** ：" + iosVersion.getAllowLowestVersion() + "\n\n");

        Integer customStatus = iosVersion.getUpdateType();
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
        Integer versionStatus = iosVersion.getVersionStatus();
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
        String appStoreUrl = iosVersion.getAppStoreUrl();
        sb.append("> **苹果商店** ：[点击前往]("+ appStoreUrl +")\n\n");
        return sb.toString();
    }
}
