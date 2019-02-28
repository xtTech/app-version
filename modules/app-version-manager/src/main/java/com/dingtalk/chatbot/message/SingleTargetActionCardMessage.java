
package com.dingtalk.chatbot.message;

import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class SingleTargetActionCardMessage implements Message {
    private String title;
    private String bannerUrl;
    private String briefTitle;
    private String briefText;
    private String singleTitle;
    private String singleURL;
    private boolean hideAvatar;

    public boolean isHideAvatar() {
        return this.hideAvatar;
    }

    public void setHideAvatar(boolean hideAvatar) {
        this.hideAvatar = hideAvatar;
    }

    public String getBriefTitle() {
        return this.briefTitle;
    }

    public void setBriefTitle(String briefTitle) {
        this.briefTitle = briefTitle;
    }

    public String getBannerUrl() {
        return this.bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBriefText() {
        return this.briefText;
    }

    public void setBriefText(String briefText) {
        this.briefText = briefText;
    }

    public String getSingleTitle() {
        return this.singleTitle;
    }

    public void setSingleTitle(String singleTitle) {
        this.singleTitle = singleTitle;
    }

    public String getSingleURL() {
        return this.singleURL;
    }

    public void setSingleURL(String singleURL) {
        this.singleURL = singleURL;
    }

    public String toJsonString() {
        Map<String, Object> items = new HashMap();
        items.put("msgtype", "actionCard");
        Map<String, Object> actionCardContent = new HashMap();
        actionCardContent.put("title", this.title);
        StringBuffer text = new StringBuffer();
        if (StringUtils.isNotBlank(this.bannerUrl)) {
            text.append(MarkdownMessage.getImageText(this.bannerUrl) + "\n");
        }

        if (StringUtils.isNotBlank(this.briefTitle)) {
            text.append(MarkdownMessage.getHeaderText(3, this.briefTitle) + "\n");
        }

        if (StringUtils.isNotBlank(this.briefText)) {
            text.append(this.briefText + "\n");
        }

        actionCardContent.put("text", text.toString());
        if (this.hideAvatar) {
            actionCardContent.put("hideAvatar", "1");
        }

        if (StringUtils.isBlank(this.singleTitle)) {
            throw new IllegalArgumentException("singleTitle should not be blank");
        } else if (StringUtils.isBlank(this.singleURL)) {
            throw new IllegalArgumentException("singleURL should not be blank");
        } else {
            actionCardContent.put("singleTitle", this.singleTitle);
            actionCardContent.put("singleURL", this.singleURL);
            items.put("actionCard", actionCardContent);
            return JSON.toJSONString(items);
        }
    }
}
