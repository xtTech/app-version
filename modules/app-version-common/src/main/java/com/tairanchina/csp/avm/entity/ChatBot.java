package com.tairanchina.csp.avm.entity;

/**
 * Created by hzlizx on 2018/9/27 0027
 */
public class ChatBot extends BasicEntity{

    private Integer id;
    private String name;
    private String webhook;
    private Integer appId;
    private Integer delFlag;
    private String activeEvent;

    public String getActiveEvent() {
        return activeEvent;
    }

    public void setActiveEvent(String activeEvent) {
        this.activeEvent = activeEvent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
