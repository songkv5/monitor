package com.sys.monitor.enums;

public enum ApiType {
    DUBBO("DubboService"),
    REST("URL"),
    ;
    private String code;

    ApiType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


}
