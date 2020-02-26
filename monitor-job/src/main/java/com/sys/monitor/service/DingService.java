package com.sys.monitor.service;

import com.sys.monitor.entity.ding.RobotMsg;
import com.sys.monitor.enums.DingRobotMsgTypeEnum;
import com.sys.monitor.service.param.RobotMsgReq;
import com.sys.monitor.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author willis
 * @chapter 用于发送钉钉消息
 * @section
 * @since 2020年02月18日 17:05
 */
@Service
@Slf4j
public class DingService {
    private static final String DING_TALK_BASE_URL = "https://oapi.dingtalk.com/robot/send";

    /**
     * 发送机器人消息提醒
     * @param robotMsgReq
     * @link https://open-doc.dingtalk.com/microapp/serverapi2/qf2nxq#-4
     */
    public void sendRobotMsg(RobotMsgReq robotMsgReq) {
        RobotMsg msg = robotMsgReq.getMsg();
        DingRobotMsgTypeEnum msgType = robotMsgReq.getMsgType();
        String robotToken = robotMsgReq.getRobotToken();
        String sign = robotMsgReq.getSign();
        long timestamp = robotMsgReq.getTimestamp();

        DingRobotMsgTypeEnum msgTypeEnum = msgType;
        String token = StringUtils.trimToNull(robotToken);
        //默认使用文本方案
        if (msgTypeEnum == null) {
            msgTypeEnum = DingRobotMsgTypeEnum.TEXT;
        }
        if (token == null) {
            log.warn("未提供机器人token！");
            return;
        }
        if (msg == null) {
            log.warn("未提供消息实体，不发送消息");
            return;
        }
        StringBuilder sb = new StringBuilder();

        sb.append(DING_TALK_BASE_URL).append("?access_token=").append(robotToken);
        if (timestamp > 0 || sign != null) {
            sb.append("&timestamp=").append(timestamp).append("&sign=").append(sign);
        }
        String url = sb.toString();
        Map entity = null;
        switch (msgTypeEnum) {
            case TEXT: {
                entity = buildTextEntity(msg);
                break;
            }
            case LINK: {
                entity = buildLinkEntity(msg);
                break;
            }
            case MARKDOWN: {
                entity = buildMarkdownEntity(msg);
                break;
            }
            default: {
                break;
            }
        }
        if (entity != null) {
            byte[] bytes = HttpUtil.sendPostJsonRequest(url, null, null, entity);

        }
    }

    private Map buildTextEntity(RobotMsg msg) {
        /**
         * 实体格式
         * {
         *     "msgtype": "text",
         *     "text": {
         *         "content": "我就是我, 是不一样的烟火@156xxxx8827"
         *     },
         *     "at": {
         *         "atMobiles": [
         *             "156xxxx8827",
         *             "189xxxx8325"
         *         ],
         *         "isAtAll": false
         *     }
         * }
         */
        Map<Object, Object> entity = new HashMap<>();
        entity.put("msgtype", "text");
        Map<Object, Object> contentObj = new HashMap<>();
        contentObj.put("content", msg.getContent());
        entity.put("text", contentObj);

        Map<Object, Object> atObj = new HashMap<>();
        atObj.put("isAtAll", msg.getIsAtAll());
        atObj.put("atMobiles", msg.getAtUserMobiles());
        entity.put("at", atObj);
        return entity;
    }
    private Map buildMarkdownEntity(RobotMsg msg) {
        /**
         * 消息格式
         * {
         *      "msgtype": "markdown",
         *      "markdown": {
         *          "title":"杭州天气",
         *          "text": "#### 杭州天气 @156xxxx8827\n" +
         *                  "> 9度，西北风1级，空气良89，相对温度73%\n\n" +
         *                  "> ![screenshot](https://gw.alipayobjects.com/zos/skylark-tools/public/files/84111bbeba74743d2771ed4f062d1f25.png)\n"  +
         *                  "> ###### 10点20分发布 [天气](http://www.thinkpage.cn/) \n"
         *      },
         *     "at": {
         *         "atMobiles": [
         *             "156xxxx8827",
         *             "189xxxx8325"
         *         ],
         *         "isAtAll": false
         *     }
         *  }
         */
        Map<Object, Object> entity = new HashMap<>();
        entity.put("msgtype", "markdown");

        Map<Object, Object> mdObj = new HashMap<>();
        mdObj.put("title", msg.getTitle());
        mdObj.put("text", msg.getContent());

        entity.put("markdown", mdObj);

        Map<Object, Object> atObj = new HashMap<>();
        atObj.put("isAtAll", msg.getIsAtAll());
        atObj.put("atMobiles", msg.getAtUserMobiles());
        entity.put("at", atObj);
        return entity;
    }
    private Map buildLinkEntity(RobotMsg msg) {
        /**
         * 消息格式
         * {
         *     "msgtype": "link",
         *     "link": {
         *         "text": "这个即将发布的新版本，创始人陈航（花名“无招”）称它为“红树林”。
         * 而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是“红树林”？",
         *         "title": "时代的火车向前开",
         *         "picUrl": "",
         *         "messageUrl": "https://www.dingtalk.com/s?__biz=MzA4NjMwMTA2Ng==&mid=2650316842&idx=1&sn=60da3ea2b29f1dcc43a7c8e4a7c97a16&scene=2&srcid=09189AnRJEdIiWVaKltFzNTw&from=timeline&isappinstalled=0&key=&ascene=2&uin=&devicetype=android-23&version=26031933&nettype=WIFI"
         *     }
         * }
         */

        Map<Object, Object> entity = new HashMap<>();
        entity.put("msgtype", "link");
        Map<Object, Object> linkObj = new HashMap<>();
        linkObj.put("text", msg.getContent());
        linkObj.put("title", msg.getTitle());
        linkObj.put("messageUrl", msg.getLinkUrl());
//        linkObj.put("picUrl", msg.getTitle());
        entity.put("link", linkObj);
        return entity;
    }

    /**
     * 生成钉钉签名
     * @param secret
     * @param timestamp
     * @return
     */
    public String sign4Robot(String secret, Long timestamp) {
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            return URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
        } catch (Exception e) {
            log.error("钉钉签名生成失败，secret={}", secret);
        }
        return null;
    }

}