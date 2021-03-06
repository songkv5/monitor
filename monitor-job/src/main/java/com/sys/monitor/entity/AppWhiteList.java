package com.sys.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 白名单，不管接口慢成傻德行，都不管
 * </p>
 *
 * @author willis
 * @since 2020-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AppWhiteList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 白名单ID
     */
    @TableId(value = "white_api_id", type = IdType.AUTO)
    private Long whiteApiId;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 接口
     */
    private String api;

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


    public static final String WHITE_API_ID = "white_api_id";

    public static final String APP_ID = "app_id";

    public static final String API = "api";

    public static final String IS_DELETED = "is_deleted";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

}
