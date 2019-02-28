package com.tairanchina.csp.avm.utils;


import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 分析UA  by:syl
 */
public class UserAgentUtils {

    private static final Logger logger = LoggerFactory.getLogger(UserAgentUtils.class);

    private static String android = "Android";
    private static String iphone = "iPhone";
    private static String ipad = "iPad";
    private static String windows = "Windows";
    private static String noDevice = "未知设备";

    private static String regex = ";\\s?(\\S*?\\s?\\S*?)\\s?(Build)?/";


    //获取用户os信息
    public static String getOS(String ua) {

        if (null == ua) return noDevice;
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        OperatingSystem os = userAgent.getOperatingSystem();
        return os.toString();
    }


    //获取移动用户操作系统
    public static String getMobileOS(String userAgent) {
        if (userAgent.contains(android)) {
            return android;
        } else if (userAgent.contains(iphone)) {
            return iphone;
        } else if (userAgent.contains(ipad)) {
            return ipad;
        } else {
            return "others";
        }
    }


    //获取用户手机型号
    public static String getPhoneModel(String userAgent) {

        if (null == userAgent || "" == userAgent) return noDevice;

        String OS = UserAgentUtils.getMobileOS(userAgent);
        if (OS.equals(android)) {
            String rex = "[()]+";
            String[] str = userAgent.split(rex);
            str = str[1].split("[;]");
            String[] res = str[str.length - 1].split("Build/");
            return res[0];
        } else if (OS.equals(iphone)) {
            String[] str = userAgent.split("[()]+");
            String res = "iphone" + str[1].split("OS")[1].split("like")[0];
            return res;
        } else if (OS.equals(ipad)) {
            return ipad;
        } else {
            return getOS(userAgent);
        }
    }


    public static String getDeviceType(String userAgent) {
        String device = "";
        if (userAgent.contains(iphone)) {
            device = iphone + userAgent.substring(userAgent.indexOf("(") + 7, userAgent.indexOf(";"));
        } else if (userAgent.contains(ipad)) {
            device = ipad;
        } else if (userAgent.contains(windows)) {
            device = windows;
        } else {
            if (userAgent.contains("zh-cn;")) {
                device = userAgent.substring(userAgent.indexOf("zh-cn;") + 7, userAgent.indexOf("Build"));
            } else if (userAgent.contains("zh-CN;")) {
                device = userAgent.substring(userAgent.indexOf("zh-CN;") + 7, userAgent.indexOf("Build"));
            } else if (userAgent.contains("en-us;")) {
                device = userAgent.substring(userAgent.indexOf("en-us;") + 7, userAgent.indexOf("Build"));
            } else if ("".equals(device)) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(userAgent);
                if (matcher.find()) {
                    device = matcher.group(1).trim();
                }
            }
        }
        logger.debug("通过userAgent解析出机型：" + device);
        return device;
    }

    public static String getBrowser(String userAgent) {
        String browser = "";
        //腾讯家浏览器
        if (userAgent.contains(BrowserType.MicroMessenger.browser)) {
            browser = BrowserType.MicroMessenger.name;
        } else if (userAgent.contains(BrowserType.MQQBrowser.browser)) {
            browser = BrowserType.MQQBrowser.name;
        } else if (userAgent.contains(BrowserType.QQBrowser.browser)) {
            browser = BrowserType.QQBrowser.name;
        }
        //userAgent中可能含有谷歌标识的浏览器（谷歌内核）
        else if (userAgent.contains(BrowserType.UCBrowser.browser)) {
            browser = BrowserType.UCBrowser.name;
        } else if (userAgent.contains(BrowserType.LBBROWSER.browser)) {
            browser = BrowserType.LBBROWSER.name;
        } else if (userAgent.contains(BrowserType.Explorer2345.browser)) {
            browser = BrowserType.Explorer2345.name;
        } else if (userAgent.contains(BrowserType.Maxthon.browser)) {
            browser = BrowserType.Maxthon.name;
        } else if (userAgent.contains(BrowserType.MetaSr.browser)) {
            browser = BrowserType.MetaSr.name;
        } else if (userAgent.contains(BrowserType.Firefox.browser)) {
            browser = BrowserType.Firefox.browser;
        } else if (userAgent.contains(BrowserType.MiuiBrowser.browser)) {
            browser = BrowserType.MiuiBrowser.name;
        } else if (userAgent.contains(BrowserType.UBrowser.browser)) {
            browser = BrowserType.UBrowser.name;
        } else if (userAgent.contains(BrowserType.Scrapy.browser)) {
            browser = BrowserType.Scrapy.name;
        } else if (userAgent.contains(BrowserType.Googlebot.browser)) {
            browser = BrowserType.Googlebot.name;
        } else if (userAgent.contains(BrowserType.TheWorld.browser)) {
            browser = BrowserType.TheWorld.name;
        } else if (userAgent.contains(BrowserType.SE360.browser)) {
            browser = BrowserType.SE360.name;
        } else if (userAgent.contains(BrowserType.GreenBrowser.browser)) {
            browser = BrowserType.GreenBrowser.name;
        } else if (userAgent.contains(BrowserType.JuziBrowser.browser)) {
            browser = BrowserType.JuziBrowser.name;
        } else if (userAgent.contains(BrowserType.baiduboxapp.browser)) {
            browser = BrowserType.baiduboxapp.name;
        } else if (userAgent.contains(BrowserType.OPR.browser)) {
            browser = BrowserType.OPR.name;
        } else if (userAgent.contains(BrowserType.Edge.browser)) {
            browser = BrowserType.Edge.name;
        } else if (userAgent.contains(BrowserType.BIDUBrowser.browser)) {
            browser = BrowserType.BIDUBrowser.name;
        } else if (userAgent.contains(BrowserType.SearchCraft.browser)) {
            browser = BrowserType.SearchCraft.name;
        } else if (userAgent.contains(BrowserType.QBWebViewType.browser)) {
            browser = BrowserType.QBWebViewType.name;
        } else if (userAgent.contains(BrowserType.Focus.browser)) {
            browser = BrowserType.Focus.name;
        } else if (userAgent.contains(BrowserType.Quark.browser)) {
            browser = BrowserType.Quark.name;
        }
        //其它有谷歌内核的浏览器
        else if (userAgent.contains(BrowserType.Chrome.browser)) {
            browser = BrowserType.otherChrome.name;
        }
        //userAgent中Safari，Chrome标识并存
        if (StringUtils.isBlank(browser)) {
            if (userAgent.contains(BrowserType.Safari.browser) && !userAgent.contains(BrowserType.Chrome.browser)) {
                browser = BrowserType.Safari.name;
            } else if (userAgent.contains(BrowserType.MobileChrome.browser) && userAgent.contains(BrowserType.Chrome.browser)) {
                browser = BrowserType.MobileChrome.name;
            } else if (userAgent.contains(BrowserType.Safari.browser) && userAgent.contains(BrowserType.Chrome.browser)) {
                browser = BrowserType.Chrome.name;
            } else {
                //如果上面还未识别出来，则交给工具处理
                UserAgent ua = UserAgent.parseUserAgentString(userAgent);
                browser = ua.getBrowser().getName();
            }
        }

        return browser;
    }

}
