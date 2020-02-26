package com.sys.monitor.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ApiType {
    DUBBO("DUBBO", "DubboService"),
    REST("URL", "URL"),
    UNKNOWN("UNKNOWN"),
    ;
    private String code;
    private String catName;

    ApiType(String code) {
        this.code = code;
    }

    ApiType(String code, String catName) {
        this.code = code;
        this.catName = catName;
    }

    public String getCode() {
        return code;
    }

    public String getCatName() {
        return catName;
    }

    public static ApiType getByCode(String code) {
        Optional<ApiType> any = Arrays.stream(ApiType.values()).filter(arg -> arg.getCode().equalsIgnoreCase(code)).findAny();
        if (any.isPresent()) {
            return any.get();
        }
        return UNKNOWN;
    }

}
