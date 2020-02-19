package com.sys.monitor.entity.resp;

import lombok.Data;

/**
 * @Author willis
 * @desc
 * @since 2020年02月18日 17:40
 */
@Data
public class HttpResponse<T> {
    private T data;
    private int code = 200;
    private String msg;
    public static <T> HttpResponse<T> success(T data) {
        HttpResponse<T> result = new HttpResponse<>();
        result.setData(data);
        return result;
    }
}
