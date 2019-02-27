package com.tairanchina.csp.avm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by hzlizx on 2018/9/27 0027
 */
@ApiModel(description = "钉钉机器人请求信息")
public class ChatBotReq {

    @ApiModelProperty("机器人名称")
    private String name;

    @ApiModelProperty("WebHook")
    private String webhook;

    @ApiModelProperty("应用ID")
    private Integer appId;

    @ApiModelProperty("事件列表")
    private List<String> events;

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

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }
}
