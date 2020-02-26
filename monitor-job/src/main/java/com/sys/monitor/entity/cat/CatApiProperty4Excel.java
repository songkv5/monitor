package com.sys.monitor.entity.cat;

import com.sys.monitor.annotation.FieldQualifier;
import lombok.Data;

/**
 * @Author willis
 * @desc
 * @since 2020年02月26日 18:19
 */
@Data
public class CatApiProperty4Excel {
    /**
     * appId
     */
    @FieldQualifier(alias = "应用ID", sequence = 1, exclude = true, autoSize = true)
    private String appId;
    /**
     * 接口类型
     */
    @FieldQualifier(alias = "接口类型", sequence = 3, autoSize = true)
    private String type;
    /**
     * http: 接口path
     * dubbo： Service名字.方法名
     */
    @FieldQualifier(alias = "接口", sequence = 2, autoSize = true)
    private String id;
    /**
     * 调用总数
     */
    @FieldQualifier(alias = "调用总数", sequence = 4, autoSize = true)
    private String totalCount;
    /**
     * 失败数量
     */
    @FieldQualifier(alias = "失败总数", sequence = 5, autoSize = true)
    private String failCount;
    /**
     * 失败百分比
     */
    @FieldQualifier(alias = "失败占比", sequence = 6, exclude = true, autoSize = true)
    private String failPercent;
    /**
     * 响应时间：最短的
     */
    @FieldQualifier(alias = "最快", sequence = 7, autoSize = true)
    private String min;
    /**
     * 响应时间：最长的
     */
    @FieldQualifier(alias = "最慢", sequence = 8, autoSize = true)
    private String max;
    /**
     * 响应时间：平局值
     */
    @FieldQualifier(alias = "平均", sequence = 9, autoSize = true)
    private String avg;
    /**
     * 响应时间：总耗时
     */
    @FieldQualifier(alias = "时间总和", sequence = 10, exclude = true, autoSize = true)
    private String sum;
    /**
     * 响应时间：总耗时
     */
    @FieldQualifier(exclude = true, autoSize = true)
    private String sum2;
    /**
     * 标准差。标准差越大，说明接口响应时长的时间值波动越大，越不稳定。越小，说明接口响应时长越平稳，相差不大
     */
    @FieldQualifier(alias = "标准差", sequence = 11, autoSize = true)
    private String std;
    /**
     * tps
     */
    @FieldQualifier(alias = "tps", sequence = 12, autoSize = true)
    private String tps;
    /**
     * 50line 响应时长[ms]
     */
    @FieldQualifier(alias = "50line", sequence = 13, exclude = true, autoSize = true)
    private String line50Value;
    /**
     * 90line 响应时长[ms]
     */
    @FieldQualifier(alias = "90line", sequence = 14, exclude = true, autoSize = true)
    private String line90Value;
    /**
     * 95line 响应时长[ms]
     */
    @FieldQualifier(alias = "95line", sequence = 15, autoSize = true)
    private String line95Value;
}
