package com.tairanchina.csp.avm.dto;

import com.tairanchina.csp.avm.entity.Apk;

/**
 * Created by hzlizx on 2018/6/14 0014
 */
public class ApkExt extends Apk{

    private String channelCode;

    private String version;

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
