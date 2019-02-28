package com.tairanchina.csp.avm.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.dto.VersionInfo;
import com.tairanchina.csp.avm.entity.*;
import com.tairanchina.csp.avm.mapper.UserMapper;
import com.tairanchina.csp.avm.utils.StringUtilsExt;
import com.tairanchina.csp.avm.utils.VersionCompareUtils;
import com.tairanchina.csp.avm.service.BasicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by hzlizx on 2018/6/11 0011
 */
@Service
public class BasicServiceImpl implements BasicService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void formatCreatedBy(List<? extends BasicEntity> source) {
        source.forEach(this::formatCreatedBy);
    }

    @Override
    public void formatCreatedBy(BasicEntity basicEntity) {
        String createdBy = basicEntity.getCreatedBy();
        if (StringUtils.isNotBlank(createdBy)) {
            User user = new User();
            user.setUserId(createdBy);
            List<User> users = userMapper.selectList(new EntityWrapper<>(user));
            if (!users.isEmpty()) {
                basicEntity.setCreatedBy(users.get(0).getPhone());
            }
        }
    }

    @Override
    public ServiceResult checkVersion(Object o) {
        VersionInfo versionInfo = new VersionInfo();
        BeanUtils.copyProperties(o, versionInfo);
        if (versionInfo.getAndroidEnabled() != null && versionInfo.getAndroidEnabled() == 1) {
            String androidMax = versionInfo.getAndroidMax();
            String androidMin = versionInfo.getAndroidMin();
            if (StringUtilsExt.hasBlank(androidMax, androidMin)) {
                return ServiceResultConstants.NEED_PARAMS;
            }
            if (compareVersion(androidMax, androidMin) <= 0) {
                return ServiceResultConstants.MIN_BIG_THAN_MAX;
            }
        }
        if (versionInfo.getIosEnabled() != null && versionInfo.getIosEnabled() == 1) {
            String iosMin = versionInfo.getIosMin();
            String iosMax = versionInfo.getIosMax();
            if (StringUtilsExt.hasBlank(iosMax, iosMin)) {
                return ServiceResultConstants.NEED_PARAMS;
            }
            if (compareVersion(iosMax, iosMin) <= 0) {
                return ServiceResultConstants.MIN_BIG_THAN_MAX;
            }
        }
        return ServiceResult.ok(null);
    }

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     *
     * @param version1
     * @param version2
     * @return
     */
    @Override
    public int compareVersion(String version1, String version2) {
//        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
//        String[] versionArray2 = version2.split("\\.");
//        int idx = 0;
//        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
//        int diff = 0;
//        while (idx < minLength
//                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
//                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
//            ++idx;
//        }
//        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
//        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
//        if (!VersionCompareUtils.VERSION_REGEX.matcher(version1).matches() || !VersionCompareUtils.VERSION_REGEX.matcher(version1).matches()){
//            logger.error("version information format is incorrect and cannot be compared");
//            return 0;
//        }
        return VersionCompareUtils.compareVersion(version1,version2);
    }

    /**
     * 根据版本信息对查询结果list排序
     * @param source
     * @return
     */
    @Override
    public List<AndroidVersion> sortAndroidVersion(List<AndroidVersion> source, boolean isLowToHigh) {
        Pattern VERSION_REGEX = VersionCompareUtils.VERSION_REGEX;
        List<AndroidVersion> unList = new ArrayList<>();
        List<AndroidVersion> allowList = source.stream().filter(o -> {
            boolean matches = VERSION_REGEX.matcher(o.getAppVersion()).matches();
            if (!matches) unList.add(o);
            return matches;

        }).collect(Collectors.toList());
        List<AndroidVersion> resultList = new ArrayList<>(source.size());
        allowList.sort((o1, o2) -> VersionCompareUtils.compareVersion(o1.getAppVersion(), o2.getAppVersion()));
        if (!isLowToHigh) Collections.reverse(allowList);
        resultList.addAll(allowList);
        resultList.addAll(unList);
        return resultList;
    }

    @Override
    public List<IosVersion> sortIosVersion(List<IosVersion> source, boolean isLowToHigh) {
        Pattern VERSION_REGEX = VersionCompareUtils.VERSION_REGEX;
        List<IosVersion> unList = new ArrayList<>();
        List<IosVersion> allowList = source.stream().filter(o -> {
            boolean matches = VERSION_REGEX.matcher(o.getAppVersion()).matches();
            if (!matches) unList.add(o);
            return matches;

        }).collect(Collectors.toList());
        List<IosVersion> resultList = new ArrayList<>(source.size());
        allowList.sort((o1, o2) -> VersionCompareUtils.compareVersion(o1.getAppVersion(), o2.getAppVersion()));
        if (!isLowToHigh) Collections.reverse(allowList);
        resultList.addAll(allowList);
        resultList.addAll(unList);
        return resultList;
    }

    @Override
    public List<RnPackage> sortRnPackageVersion(List<RnPackage> source, boolean isLowToHigh) {
        Pattern VERSION_REGEX = VersionCompareUtils.VERSION_REGEX;
        List<RnPackage> unList = new ArrayList<>();
        List<RnPackage> allowList = source.stream().filter(o -> {
            boolean matches = VERSION_REGEX.matcher(o.getRnVersion()).matches();
            if (!matches) unList.add(o);
            return matches;

        }).collect(Collectors.toList());
        List<RnPackage> resultList = new ArrayList<>(source.size());
        allowList.sort((o1, o2) -> VersionCompareUtils.compareVersion(o1.getRnVersion(), o2.getRnVersion()));
        if (!isLowToHigh) Collections.reverse(allowList);
        resultList.addAll(allowList);
        resultList.addAll(unList);
        return resultList;
    }

}
