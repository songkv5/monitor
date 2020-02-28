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
        String secret = "b7f6ffaa57027bc3a9f703f9ef91be77cb77b07075a590bf2c4710a32fdd6962";
        String token = "b7f6ffaa57027bc3a9f703f9ef91be77cb77b07075a590bf2c4710a32fdd6962";
        //https://oapi.dingtalk.com/robot/send?access_token=7c86750dc48c0e07809e7f7cc137dc5400dc538cffd66dc7dc6ab117d5c765ea
        String sign = dingService.sign4Robot(secret, ts);
        RobotMsgReq.Builder builder = RobotMsgReq.newBuilder();
        RobotMsg msg = RobotMsg.Builder.builder().atUserMobiles(Arrays.asList("15099909560"))
                .content("hello @15099909560")
                .title("保重身体")
                .build();

        RobotMsgReq rmr = builder.msgType(DingRobotMsgTypeEnum.TEXT)
//                .sign(sign)
//                .timestamp(ts)
                .robotToken(token)
                .msg(msg)
                .build();
        dingService.sendRobotMsg(rmr);
    }
}
