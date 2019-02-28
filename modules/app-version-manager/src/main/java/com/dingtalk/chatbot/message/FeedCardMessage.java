package com.dingtalk.chatbot.message;

import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class FeedCardMessage implements Message {
    private List<FeedCardMessageItem> feedItems;

    public List<FeedCardMessageItem> getFeedItems() {
        return this.feedItems;
    }

    public void setFeedItems(List<FeedCardMessageItem> feedItems) {
        this.feedItems = feedItems;
    }

    public String toJsonString() {
        Map<String, Object> items = new HashMap();
        items.put("msgtype", "feedCard");
        Map<String, Object> feedCard = new HashMap();
        if (this.feedItems != null && !this.feedItems.isEmpty()) {
            Iterator i$ = this.feedItems.iterator();

            FeedCardMessageItem item;
            do {
                if (!i$.hasNext()) {
                    feedCard.put("links", this.feedItems);
                    items.put("feedCard", feedCard);
                    return JSON.toJSONString(items);
                }

                item = (FeedCardMessageItem)i$.next();
                if (StringUtils.isBlank(item.getTitle())) {
                    throw new IllegalArgumentException("title should not be blank");
                }

                if (StringUtils.isBlank(item.getMessageURL())) {
                    throw new IllegalArgumentException("messageURL should not be blank");
                }
            } while(!StringUtils.isBlank(item.getPicURL()));

            throw new IllegalArgumentException("picURL should not be blank");
        } else {
            throw new IllegalArgumentException("feedItems should not be null or empty");
        }
    }
}
