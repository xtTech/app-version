
package com.dingtalk.chatbot.message;

import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class LinkMessage implements Message {
    private String title;
    private String text;
    private String picUrl;
    private String messageUrl;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMessageUrl() {
        return this.messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }

    public String toJsonString() {
        Map<String, Object> items = new HashMap();
        items.put("msgtype", "link");
        Map<String, String> linkContent = new HashMap();
        if (StringUtils.isBlank(this.title)) {
            throw new IllegalArgumentException("title should not be blank");
        } else {
            linkContent.put("title", this.title);
            if (StringUtils.isBlank(this.messageUrl)) {
                throw new IllegalArgumentException("messageUrl should not be blank");
            } else {
                linkContent.put("messageUrl", this.messageUrl);
                if (StringUtils.isBlank(this.text)) {
                    throw new IllegalArgumentException("text should not be blank");
                } else {
                    linkContent.put("text", this.text);
                    if (StringUtils.isNotBlank(this.picUrl)) {
                        linkContent.put("picUrl", this.picUrl);
                    }

                    items.put("link", linkContent);
                    return JSON.toJSONString(items);
                }
            }
        }
    }
}
