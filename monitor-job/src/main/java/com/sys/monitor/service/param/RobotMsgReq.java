package com.sys.monitor.service.param;

import com.sys.monitor.entity.ding.RobotMsg;
import com.sys.monitor.enums.DingRobotMsgTypeEnum;

/**
 * @Author willis
 * @desc
 * @since 2020年02月19日 14:34
 */
public class RobotMsgReq {
    private DingRobotMsgTypeEnum msgType;
    private String robotToken;
    private long timestamp;
    private String sign;
    private RobotMsg msg;

    public DingRobotMsgTypeEnum getMsgType() {
        return msgType;
    }

    public String getRobotToken() {
        return robotToken;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSign() {
        return sign;
    }

    public RobotMsg getMsg() {
        return msg;
    }

    private RobotMsgReq() {
    }
    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder{
        private RobotMsgReq rmr;
        private Builder() {
            rmr = new RobotMsgReq();
        }
        public Builder msgType(DingRobotMsgTypeEnum msgType) {
            this.rmr.msgType = msgType;
            return this;
        }
        public Builder robotToken(String robotToken) {
            this.rmr.robotToken = robotToken;
            return this;
        }
        public Builder sign(String sign) {
            this.rmr.sign = sign;
            return this;
        }
        public Builder timestamp(long timestamp) {
            this.rmr.timestamp = timestamp;
            return this;
        }
        public Builder msg(RobotMsg msg) {
            this.rmr.msg = msg;
            return this;
        }
        public RobotMsgReq build() {
            return this.rmr;
        }
    }
}
