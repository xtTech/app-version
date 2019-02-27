
package com.dingtalk.chatbot.message;

import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MarkdownMessage implements Message {
    private String title;
    private List<String> items = new ArrayList();

    public MarkdownMessage() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void add(String text) {
        this.items.add(text);
    }

    public static String getBoldText(String text) {
        return "**" + text + "**";
    }

    public static String getItalicText(String text) {
        return "*" + text + "*";
    }

    public static String getLinkText(String text, String href) {
        return "[" + text + "](" + href + ")";
    }

    public static String getImageText(String imageUrl) {
        return "![image](" + imageUrl + ")";
    }

    public static String getHeaderText(int headerType, String text) {
        if (headerType >= 1 && headerType <= 6) {
            StringBuffer numbers = new StringBuffer();

            for(int i = 0; i < headerType; ++i) {
                numbers.append("#");
            }

            return numbers + " " + text;
        } else {
            throw new IllegalArgumentException("headerType should be in [1, 6]");
        }
    }

    public static String getReferenceText(String text) {
        return "> " + text;
    }

    public static String getOrderListText(List<String> orderItem) {
        if (orderItem.isEmpty()) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();

            for(int i = 1; i <= orderItem.size() - 1; ++i) {
                sb.append(i + ". " + (String)orderItem.get(i - 1) + "\n");
            }

            sb.append(orderItem.size() + ". " + (String)orderItem.get(orderItem.size() - 1));
            return sb.toString();
        }
    }

    public static String getUnorderListText(List<String> unorderItem) {
        if (unorderItem.isEmpty()) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < unorderItem.size() - 1; ++i) {
                sb.append("- " + (String)unorderItem.get(i) + "\n");
            }

            sb.append("- " + (String)unorderItem.get(unorderItem.size() - 1));
            return sb.toString();
        }
    }

    public String toJsonString() {
        Map<String, Object> result = new HashMap();
        result.put("msgtype", "markdown");
        Map<String, Object> markdown = new HashMap();
        markdown.put("title", this.title);
        StringBuffer markdownText = new StringBuffer();
        Iterator i$ = this.items.iterator();

        while(i$.hasNext()) {
            String item = (String)i$.next();
            markdownText.append(item + "\n");
        }

        markdown.put("text", markdownText.toString());
        result.put("markdown", markdown);
        return JSON.toJSONString(result);
    }
}
