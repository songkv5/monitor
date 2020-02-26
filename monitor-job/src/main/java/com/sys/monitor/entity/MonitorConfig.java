package com.sys.monitor.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监控配置表
 * </p>
 *
 * @author willis
 * @since 2020-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MonitorConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 监控配置ID
     */
    @TableId(value = "monitor_id", type = IdType.AUTO)
    private Long monitorId;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 95line标准
     */
    private BigDecimal line95;

    /**
     * 接口类型。URL=http接口；DUBBO=dubbo接口
     */
    private String apiType;

    /**
     * 钉钉机器人token
     */
    private String dingRobotToken;

    /**
     * 钉钉机器人secret
     */
    private String dingRobotSecret;

    /**
     * 是否删除: 0=否,1=是
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    public static final String MONITOR_ID = "monitor_id";

    public static final String APP_ID = "app_id";

    public static final String LINE95 = "line95";

    public static final String API_TYPE = "api_type";

    public static final String DING_ROBOT_TOKEN = "ding_robot_token";

    public static final String DING_ROBOT_SECRET = "ding_robot_secret";

    public static final String IS_DELETED = "is_deleted";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

}
