package com.tairanchina.csp.avm.controller;

import com.tairanchina.csp.avm.constants.ServiceResultConstants;
import com.tairanchina.csp.avm.dto.ChatBotReq;
import com.tairanchina.csp.avm.dto.ServiceResult;
import com.tairanchina.csp.avm.enums.ChatBotEventType;
import com.tairanchina.csp.avm.service.ChatBotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hzlizx on 2018/9/27 0027
 */
@Api(value = "/chatbot", tags = "钉钉机器人管理")
@RestController
@RequestMapping("/chatbot")
public class ChatBotController {
    private static final Logger logger = LoggerFactory.getLogger(ChatBotController.class);

    @Autowired
    private ChatBotService chatBotService;

    @ApiOperation(value = "根据AppId找到绑定的机器人", notes = "根据AppId找到绑定的机器人")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true)
    })
    @GetMapping("/getByAppId/{appId}")
    public ServiceResult getByAppId(@PathVariable Integer appId){
        if(appId==null || appId==0){
            return ServiceResultConstants.NEED_PARAMS;
        }
        return chatBotService.getByAppId(appId);
    }

    @ApiOperation(value = "获取所有可以绑定的事件", notes = "获取所有可以绑定的事件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true)
    })
    @GetMapping("/event")
    public ServiceResult listEvent() {
        List<HashMap<String, String>> collect = Arrays.stream(ChatBotEventType.values()).map(chatBotEventType -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("event", chatBotEventType.name());
            hashMap.put("message", chatBotEventType.getMessage());
            return hashMap;
        }).collect(Collectors.toList());
        return ServiceResult.ok(collect);
    }


    @ApiOperation(value = "绑定一个钉钉机器人到应用", notes = "绑定一个钉钉机器人到应用，https://open-doc.dingtalk.com/docs/doc.htm?spm=a219a.7629140.0.0.21364a972AacKR&treeId=257&articleId=105735&docType=1")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true)
    })
    @PostMapping
    public ServiceResult create(@RequestBody ChatBotReq chatBot) {
        List<ChatBotEventType> events = this.eventTypeFormat(chatBot.getEvents());
        if (events.size() == 0) {
            return ServiceResultConstants.CHAT_BOT_EVENT_NOT_EXIST;
        }
        return chatBotService.createChatBot(chatBot.getAppId(), chatBot.getWebhook(), chatBot.getName(), events);
    }

    @ApiOperation(value = "修改一个应用的机器人信息", notes = "修改一个应用的机器人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true)
    })
    @PutMapping
    public ServiceResult edit(@RequestBody ChatBotReq chatBot) {
        List<ChatBotEventType> events = this.eventTypeFormat(chatBot.getEvents());
        if (events.size() == 0) {
            return ServiceResultConstants.CHAT_BOT_EVENT_NOT_EXIST;
        }
        return chatBotService.editChatBot(chatBot.getAppId(), chatBot.getWebhook(), chatBot.getName(), events);
    }

    @ApiOperation(value = "删除一个应用的机器人信息", notes = "删除一个应用的机器人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "用户凭证", required = true)
    })
    @DeleteMapping("/{appId}")
    public ServiceResult delete(@PathVariable Integer appId) {
        return chatBotService.deleteChatBot(appId);
    }

    private List<ChatBotEventType> eventTypeFormat(List<String> events){
        return events.stream().map(event -> {
            try {
                return ChatBotEventType.valueOf(event);
            } catch (Exception e) {
                logger.error("渲染事件枚举类[ChatBotEventType]失败:" + event, e);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
