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
 * 接口统计备份
 * </p>
 *
 * @author willis
 * @since 2020-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RobotStaticBackup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 备份ID
     */
    @TableId(value = "backup_id", type = IdType.AUTO)
    private Long backupId;

    /**
     * 钉钉机器人token
     */
    private String dingRobotToken;

    /**
     * 附件链接
     */
    private String attachUrl;

    /**
     * 开始时间
     */
    private Date dateBegin;

    /**
     * 开始时间
     */
    private Date dateEnd;

    /**
     * 是否是默认：0=否；1=是.最近一周的接口数据为默认
     */
    private Integer isDefault;

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


    public static final String BACKUP_ID = "backup_id";

    public static final String DING_ROBOT_TOKEN = "ding_robot_token";

    public static final String ATTACH_URL = "attach_url";

    public static final String DATE_BEGIN = "date_begin";

    public static final String DATE_END = "date_end";

    public static final String IS_DEFAULT = "is_default";

    public static final String IS_DELETED = "is_deleted";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

}
