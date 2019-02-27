package com.tairanchina.csp.avm.utils;


public enum BrowserType {


    QQBrowser("QQ浏览器", "QQBrowser"),
    MQQBrowser("手机QQ浏览器", "MQQBrowser"),
    MicroMessenger("微信内嵌浏览器", "MicroMessenger"),

    UCBrowser("UC浏览器", "UCBrowser"),
    LBBROWSER("猎豹浏览器", "LBBROWSER"),
    Explorer2345("2345浏览器", "2345Explorer"),
    Maxthon("傲游浏览器", "Maxthon"),
    MetaSr("搜狗浏览器", "MetaSr"),
    Safari("Safari", "Safari"),
    Firefox("Firefox", "Firefox"),

    MiuiBrowser("小米浏览器", "MiuiBrowser"),
    UBrowser("三维浏览器", "UBrowser"),

    TheWorld("世界之窗", "The World"),
    SE360("360浏览器", "360SE"),
    GreenBrowser("绿色浏览器", "Green Browser"),
    JuziBrowser("桔子浏览器", "JuziBrowser"),
    baiduboxapp("手机百度浏览器", "baiduboxapp"),
    OPR("欧朋浏览器", "OPR"),
    Edge("Edge浏览器", "Edge"),
    BIDUBrowser("百度浏览器", "BIDUBrowser"),
    SearchCraft("百度简单搜素", "SearchCraft"),
    QBWebViewType("QQ内置浏览器", "QBWebViewType"),
    Focus("Focus", "Focus"),
    Quark("Quark", "Quark"),

    Scrapy("Scrapy", "Scrapy"),
    Googlebot("Googlebot", "Googlebot"),

    Chrome("谷歌浏览器", "Chrome"),
    MobileChrome("手机谷歌浏览器", "Mobile Safari"),
    otherChrome("Chrome内核", "Chrome");


    String name;

    String browser;

    private BrowserType(String name) {
        this.name = name();
    }

    private BrowserType(String name, String browser) {
        this.name = name;
        this.browser = browser;
    }

    public String getName() {
        return name;
    }

    public String getBrowser() {
        return browser;
    }
}
