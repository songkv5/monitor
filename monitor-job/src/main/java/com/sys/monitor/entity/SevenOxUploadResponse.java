package com.sys.monitor.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author willis
 * @desc
 * @since 2020年02月27日 16:56
 */
@Data
public class SevenOxUploadResponse implements Serializable {
    /**
     * 域名
     */
    private String domain;
    /**
     * 相对路径
     */
    private String key;
    /**
     * 媒体类型
     */
    private String mime;
    /**
     * 结果
     */
    private String result;
    /**
     * 访问链接
     */
    private String url;
}
