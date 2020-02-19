package com.sys.monitor.enums;

/**
 * @author willis
 * @chapter
 * @section
 * @since 2019年05月10日 14:36
 */
public enum DingRobotMsgTypeEnum {
    TEXT(1, "text","文本类型"),
    LINK(2, "link", "连接类型"),
    MARKDOWN(3, "markdown", "MD富文本类型")
    ;
    private int code;
    private String key;
    private String name;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    DingRobotMsgTypeEnum(int code, String key, String name) {
        this.code = code;
        this.key = key;
        this.name = name;
    }

    DingRobotMsgTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
