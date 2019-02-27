package com.tairanchina.csp.avm.mq;

import com.ecfront.dew.common.$;
import com.tairanchina.csp.avm.enums.ChatBotEventType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hzlizx on 2018/9/28 0028
 */
public class ChatBotMQEvent implements Serializable {

    private ChatBotEventType chatBotEventType;
    private String title;
    private String message;
    private String type;
    private String webhook;
    private Integer appId;
    private Date eventTime;
    private String triggeredBy;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public ChatBotEventType getChatBotEventType() {
        return chatBotEventType;
    }

    public void setChatBotEventType(ChatBotEventType chatBotEventType) {
        this.chatBotEventType = chatBotEventType;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    @Override
    public String toString() {
        return $.json.toJsonString(this);
    }
}
