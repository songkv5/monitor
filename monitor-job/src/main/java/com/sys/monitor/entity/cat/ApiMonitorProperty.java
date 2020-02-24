package com.sys.monitor.entity.cat;

import lombok.Data;

/**
 * @Author willis
 * @desc 接口监控属性
 * @since 2020年02月18日 15:32
 */
@Data
public class ApiMonitorProperty {
    /**
     * appId
     */
    private String appId;
    /**
     * 接口类型
     */
    private String type;
    /**
     * http: 接口path
     * dubbo： Service名字.方法名
     */
    private String id;
    /**
     * 调用总数
     */
    private Long totalCount;
    /**
     * 失败数量
     */
    private Long failCount;
    /**
     * 失败百分比
     */
    private String failPercent;
    /**
     * 响应时间：最短的
     */
    private Double min;
    /**
     * 响应时间：最长的
     */
    private Double max;
    /**
     * 响应时间：平局值
     */
    private Double avg;
    /**
     * 响应时间：总耗时
     */
    private Double sum;
    /**
     * 响应时间：总耗时
     */
    private Double sum2;
    /**
     * 标准差。标准差越大，说明接口响应时长的时间值波动越大，越不稳定。越小，说明接口响应时长越平稳，相差不大
     */
    private Double std;
    /**
     * tps
     */
    private Double tps;
    /**
     * 50line 响应时长[ms]
     */
    private Double line50Value;
    /**
     * 90line 响应时长[ms]
     */
    private Double line90Value;
    /**
     * 95line 响应时长[ms]
     */
    private Double line95Value;
    /**
     * 99line 响应时长[ms]
     */
    private Double line99Value;
    /**
     * 999line 响应时长[ms]
     */
    private Double line999Value;
    /**
     * 9999line 响应时长[ms]
     */
    private Double line9999Value;
    /**
     * 时间范围
     */
    private String timeRange;

}