package com.sys.monitor.entity.ding;

import java.io.Serializable;
import java.util.List;

/**
 * @author willis
 * @chapter 钉钉机器人消息发送实体
 * @section
 * @since 2020年02月18日 17:39
 */
public class RobotMsg implements Serializable {
    /**
     * 标题,markdown类型的有效
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 连接类型消息的地址
     */
    private String linkUrl;
    /**
     * @ 的用户
     */
    private List<String> atUserMobiles;

    /**
     * 是否@ 所有人
     */
    private boolean isAtAll;

    public static class Builder{
        private RobotMsg msg;

        private Builder() {
            this.msg = new RobotMsg();
        }

        public static Builder builder(){
            return new Builder();
        }

        public Builder title(String title){
            this.msg.title = title;
            return this;
        }

        public Builder content(String content){
            this.msg.content = content;
            return this;
        }

        public Builder linkUrl(String linkUrl) {
            this.msg.linkUrl = linkUrl;
            return this;
        }

        public Builder atUserMobiles (List<String> atUserMobiles) {
            this.msg.atUserMobiles = atUserMobiles;
            return this;
        }
        public Builder isAtAll(boolean isAtAll) {
            this.msg.isAtAll = isAtAll;
            return this;
        }
        public RobotMsg build() {
            return this.msg;
        }
    }

    private RobotMsg() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public List<String> getAtUserMobiles() {
        return atUserMobiles;
    }

    public void setAtUserMobiles(List<String> atUserMobiles) {
        this.atUserMobiles = atUserMobiles;
    }

    public boolean getIsAtAll() {
        return isAtAll;
    }

    public void setIsAtAll(boolean isAtAll) {
        this.isAtAll = isAtAll;
    }
}