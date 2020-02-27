package com.sys.monitor.mapper;

import com.sys.monitor.entity.RobotStaticBackup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 接口统计备份 Mapper 接口
 * </p>
 *
 * @author willis
 * @since 2020-02-27
 */
public interface RobotStaticBackupMapper extends BaseMapper<RobotStaticBackup> {

    @Update("update robot_static_backup set is_default=0 where ding_robot_token = #{robotToken} and backup_id!= ${backupId} and is_default=1")
    Integer closeOtherDefault(@Param("robotToken") String robotToken, @Param("backupId") Long backupId);
}
