package com.sys.monitor.test.service;

import com.sys.monitor.entity.ding.RobotMsg;
import com.sys.monitor.enums.DingRobotMsgTypeEnum;
import com.sys.monitor.service.DingService;
import com.sys.monitor.service.param.RobotMsgReq;
import com.sys.monitor.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * @Author willis
 * @desc
 * @since 2020年02月19日 14:32
 */
public class DingServiceTest extends BaseTest {
    @Autowired
    private DingService dingService;

    @Test
    public void sendRobotMsgTest() {
        long ts = System.currentTimeMillis();
        String secret = "SEC49739c3e71ff717bcf727b099f3a96dd35004f3d68454f94900008de1d30524c";
        String token = "7c86750dc48c0e07809e7f7cc137dc5400dc538cffd66dc7dc6ab117d5c765ea";
        //https://oapi.dingtalk.com/robot/send?access_token=7c86750dc48c0e07809e7f7cc137dc5400dc538cffd66dc7dc6ab117d5c765ea
        String sign = dingService.sign4Robot(secret, ts);
        RobotMsgReq.Builder builder = RobotMsgReq.newBuilder();
        RobotMsg msg = RobotMsg.Builder.builder().atUserMobiles(Arrays.asList("17520487299"))
                .content("hello")
                .title("保重身体")
                .build();

        RobotMsgReq rmr = builder.msgType(DingRobotMsgTypeEnum.TEXT)
                .sign(sign)
                .timestamp(ts)
                .robotToken(token)
                .msg(msg)
                .build();
        dingService.sendRobotMsg(rmr);
    }
}
