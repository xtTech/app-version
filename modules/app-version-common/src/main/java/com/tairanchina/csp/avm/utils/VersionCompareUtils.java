package com.tairanchina.csp.avm.utils;

import com.tairanchina.csp.avm.exception.RegexMatchIllException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 描述:
 *
 * @author hzds
 * @Create 2018-09 : 11 14:07
 */
public class VersionCompareUtils {

    private static final Logger logger = LoggerFactory.getLogger(VersionCompareUtils.class);
    //匹配四位纯自然数版本字符串 ps: 在前端的正则中限制了每位版本号的长度，后端未做处理
    public static final Pattern VERSION_REGEX = Pattern.compile("^([0]|[1-9][0-9]*)\\.([0]|[1-9][0-9]*)\\.([0]|[1-9][0-9]*)\\.([0]|[1-9][0-9]*)$");
    //匹配四位语义化版本字符串
    private static final Pattern SEMANTIC_VERSION_REGEX = Pattern.compile("^([0]|[1-9][0-9]*)(\\.)([0]|[1-9][0-9]*)(\\.)([0]|[1-9][0-9]*)(\\.)([0]|[0][A-Za-z-][0-9A-Za-z]*|[1-9A-Za-z-][0-9A-Za-z-]*)(|[+][0-9A-Za-z.]*)$");
    /**
     * 简单版本比较处理（必须符合VERSION_REGEX正则表达式）
     *
     * @param firstVersion
     * @param secondVersion
     * @return
     */
    public static int compareVersion(String firstVersion, String secondVersion) {
        //针对历史数据只存在一位的自然数，特殊处理
        Pattern regex = Pattern.compile("^([0]|[1-9]\\d*)$");
        if (regex.matcher(firstVersion).matches() || regex.matcher(secondVersion).matches()) {
            int v1 = regex.matcher(secondVersion).matches() ? 1 : 0;
            int v2 = regex.matcher(firstVersion).matches() ? 1 : 0;
            return v1-v2 == 0 ? compareOthersVersion(firstVersion, secondVersion) : v1 - v2;
        }
        if (VERSION_REGEX.matcher(firstVersion).matches() && VERSION_REGEX.matcher(secondVersion).matches()) {
            String[] splits1 = firstVersion.split("\\.");
            String[] splits2 = secondVersion.split("\\.");
            int minLength = Math.min(splits1.length, splits2.length);
            int idx = 0;
            int diff = 0;
            while (idx < minLength && diff == 0) {
                diff = Integer.parseInt(splits1[idx]) - Integer.parseInt(splits2[idx]);
                idx++;
            }
            return diff;
        } else if (SEMANTIC_VERSION_REGEX.matcher(firstVersion).matches() && SEMANTIC_VERSION_REGEX.matcher(secondVersion).matches()) {
            return compareSemanticVersion(firstVersion, secondVersion);
        } else {
            return compareOthersVersion(firstVersion, secondVersion);
        }
    }

    /**
     * 版本比较处理（严格按照语义化版本文档比较）
     *
     * @param firstVersion
     * @param secondVersion
     * @return
     */
    private static int compareSemanticVersion(String firstVersion, String secondVersion) {
        String[] versionArray1;
        String[] versionArray2;
        try {
            versionArray1 = convertArray(firstVersion);
            versionArray2 = convertArray(secondVersion);
        } catch (RegexMatchIllException e) {
            //e.printStackTrace();
            return -1;
        }
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);
        int diff = 0;
        while (idx < minLength) {
            if (idx < 3) {
                diff = Integer.parseInt(versionArray1[idx]) - Integer.parseInt(versionArray2[idx]);
            }
            if (idx == 3) {
                if (StringUtils.isEmpty(versionArray1[idx]) || StringUtils.isEmpty(versionArray2[idx])) {
                    diff = (StringUtils.isEmpty(versionArray1[idx]) ? 1 : 0) - (StringUtils.isEmpty(versionArray2[idx]) ? 1 : 0);
                } else {
                    String[] arrays3 = versionArray1[idx].split("\\.");
                    String[] arrays4 = versionArray2[idx].split("\\.");
                    int minx = Math.min(arrays3.length, arrays4.length);
                    int index = 0;
                    while (index < minx) {
                        try {
                            diff = Integer.parseInt(arrays3[index]) - Integer.parseInt(arrays4[index]);
                        } catch (Exception e) {
                            diff = arrays3[index].compareTo(arrays4[index]);
                        }
                        index++;
                        if (diff != 0) {
                            index = minx;
                        }
                    }
                }
            }
            if (diff != 0) break;
            idx++;
        }
        diff = (diff != 0) ? diff : secondVersion.length() - firstVersion.length();
        return diff;
    }

    /**
     * 兼容历史数据版本比较
     *
     * @param firstVersion
     * @param secondVersion
     * @return
     */
    private static int compareOthersVersion(String firstVersion, String secondVersion) {
        String compare1 = firstVersion.split("-")[0];
        String compare2 = secondVersion.split("-")[0];
        String[] versionArray1 = compare1.split("\\.");
        String[] versionArray2 = compare2.split("\\.");
        int minLength = Math.min(versionArray1.length, versionArray2.length);
        int idx = 0;
        int diff = 0;
        while (idx < minLength && diff == 0) {
            int exceptionNum1 = 0;
            int exceptionNum2 = 0;
            int aheadNum = 0;
            int laterNum = 0;
            try {
                aheadNum = Integer.parseInt(versionArray1[idx]);
            } catch (Exception e) {
                exceptionNum1 = 1;
            }
            try {
                laterNum = Integer.parseInt(versionArray2[idx]);
            } catch (Exception e) {
                exceptionNum2 = 1;
            }
            if (exceptionNum1 == 0 && exceptionNum2 == 0) {
                diff = aheadNum - laterNum;
            } else {
                if (exceptionNum1 == 1 && exceptionNum2 == 0) diff = -1;
                if (exceptionNum1 == 0) diff = 1;
                if (exceptionNum1 == 1 && exceptionNum2 == 1) {
                    diff = String.valueOf(versionArray1[idx]).compareTo(String.valueOf(versionArray2[idx]));
                }
            }
            idx++;
        }
        if (diff == 0) {
            if (firstVersion.split("-").length > 1 && secondVersion.split("-").length > 1) {
                diff = firstVersion.split("-")[1].compareTo(secondVersion.split("-")[1]);
            } else {
                if (firstVersion.split("-").length > 1) return -1;
                if (secondVersion.split("-").length > 1) return 1;
            }
        }
        return diff;
    }

    private static String[] convertArray(String appVersion) throws RegexMatchIllException {
        if (!SEMANTIC_VERSION_REGEX.matcher(appVersion).matches() || appVersion.contains("..") || appVersion.contains("--")) {
            logger.error("版本信息 [" + appVersion + "] 格式有误");
            throw new RegexMatchIllException("Incorrect version information [" + appVersion + "] format");
        }
        String[] arrays = new String[5];
        String str = appVersion;//"1.0.2-bata.3s4.13lso";
        arrays[0] = str.substring(0, str.indexOf("."));
        str = str.substring(str.indexOf(".") + 1);
        arrays[1] = str.substring(0, str.indexOf("."));
        str = str.substring(str.indexOf(".") + 1);
        arrays[2] = str.substring(0, str.indexOf("."));
        str = str.substring(str.indexOf(".") + 1);
        String left = str.contains("+") ? str.substring(0, str.indexOf("+")) : str;
        if (left.contains("-")) {
            arrays[3] = left.substring(0, left.indexOf("-"));
            arrays[4] = left.substring(left.indexOf("-") + 1);
        } else {
            arrays[3] = left;
        }
        List<String> temArray = new ArrayList<>();
        for (String array : arrays) {
            if(!StringUtils.isBlank(array)) temArray.add(array);
        }
        String[] arr = new String[temArray.size()];
        for (int i = 0; i < temArray.size(); i++) {
            arr[i] = temArray.get(i);
        }
        return arr;

    }

//    public static void main(String[] args) {
//        String version1 = "1.0.0";
//        String version2 = "1.0.1";
//        int diff = VersionCompareUtils.compareVersion(version1, version2);
//        boolean result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "1.0.0";
//        version2 = "1.0.1";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "1.0.0.0";
//        version2 = "1.0.0.1";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "9.0.0";
//        version2 = "10.0.0";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "9.0.0.0";
//        version2 = "10.0.0.0";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "9.0.0-beta";
//        version2 = "10.0.0-beta";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "9.0.0.0-beta";
//        version2 = "10.0.0.0-beta";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "10.0.0-beta";
//        version2 = "10.0.0-alpha";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "9.0.0.0-beta";
//        version2 = "9.0.0.0-beta";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "9.0.0-beta";
//        version2 = "9.0.0-rc";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "10.0.0.0-beta";
//        version2 = "10.0.0.0-alpha";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//
//        version1 = "10.0.0.0";
//        version2 = "10.0.0.0-alpha";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//
//        version1 = "1.0.0";
//        version2 = "10.0.0";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "1.0.0";
//        version2 = "10.0.1";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "1.10.0";
//        version2 = "1.0.11";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "11.0.0";
//        version2 = "1.0.1";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//
//        version1 = "1.0.0";
//        version2 = "1.0.1";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "1.0.0";
//        version2 = "1.0.1";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "1.0.0.0";
//        version2 = "1.0.0.1";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "9.0.0";
//        version2 = "10.0.0";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "9.0.0.0";
//        version2 = "10.0.0.0";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "9.0.0-beta";
//        version2 = "10.0.0-beta";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "9.0.0.0-beta";
//        version2 = "10.0.0.0-beta";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "10.0.0-beta";
//        version2 = "10.0.0-alpha";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "9.0.0.0-beta";
//        version2 = "9.0.0.0-beta";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "9.0.0-beta";
//        version2 = "9.0.0-rc";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "10.0.0.0-beta";
//        version2 = "10.0.0.0-alpha";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//
//        version1 = "10.0.0.0";
//        version2 = "10.0.0.0-alpha";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//
//        version1 = "1.0.0";
//        version2 = "10.0.0";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "1.0.0";
//        version2 = "10.0.1";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "1.10.0";
//        version2 = "1.0.11";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "11.0.0";
//        version2 = "1.0.1";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "1.0.0.a";
//        version2 = "1.0.0.a";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "100";
//        version2 = "1.0.0.a";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "100";
//        version2 = "12q1";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//        version1 = "100";
//        version2 = "99";
//        diff = VersionCompareUtils.compareVersion(version1, version2);
//        result = diff > 0;
//        logger.info(version1 + " 比 " + version2 + "大 : " + result + " ,相差值：" + diff);
//
//    }
}
