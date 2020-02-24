package com.sys.monitor.entity;

import com.sys.monitor.enums.ApiType;
import lombok.Data;

import java.util.List;

/**
 * @Author willis
 * @desc 监控配置 from monitor.xml
 * @since 2020年02月20日 15:05
 */
@Data
public class MonitorItem {
    /**
     * appId，公司内部标记系统的唯一标识
     */
    private String appId;
    /**
     * 机器人token
     */
    private String dingRobotToken;
    /**
     * 机器人秘钥
     */
    private String dingRobotSecret;
    /**
     * 接受通知的手机号
     */
    private List<String> receivers;
    /**
     * 接口类型
     */
    private List<ApiType> types;
    /**
     * 白名单
     */
    private List<String> whiteApis;
    /**
     * 接口耗时标准，默认走全局配置
     */
    private Long dubboLine95;
    /**
     * 接口耗时标准
     */
    private Long urlLine95;

    /**
     * 监控系统的url
     */
    private String monitorUrl;
}
