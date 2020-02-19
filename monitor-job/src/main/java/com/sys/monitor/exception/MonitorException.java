package com.sys.monitor.exception;

/**
 * @Author willis
 * @desc
 * @since 2020年02月18日 17:38
 */
public class MonitorException extends RuntimeException {
    private int code;
    private String msg;

    public MonitorException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
