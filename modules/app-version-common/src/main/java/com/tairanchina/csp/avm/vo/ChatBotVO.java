package com.tairanchina.csp.avm.vo;

import com.tairanchina.csp.avm.entity.ChatBot;

/**
 * Created by hzlizx on 2018/9/27 0027
 */
public class ChatBotVO {

    private Integer id;
    private Integer appId;
    private String name;
    private String webhook;

    public ChatBotVO(ChatBot chatBot) {
        this.id = chatBot.getId();
        this.appId = chatBot.getAppId();
        this.webhook = chatBot.getWebhook();
        this.name = chatBot.getName();
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

}
