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
 * app负责人表
 * </p>
 *
 * @author willis
 * @since 2020-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AppOwner implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 负责人ID
     */
    @TableId(value = "owner_id", type = IdType.AUTO)
    private Long ownerId;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 负责人姓名
     */
    private String ownerName;

    /**
     * 负责人电话
     */
    private String ownerMobile;

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


    public static final String OWNER_ID = "owner_id";

    public static final String APP_ID = "app_id";

    public static final String OWNER_NAME = "owner_name";

    public static final String OWNER_MOBILE = "owner_mobile";

    public static final String IS_DELETED = "is_deleted";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

}
