package com.sys.monitor.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sys.monitor.entity.RobotStaticBackup;
import com.sys.monitor.mapper.RobotStaticBackupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author willis
 * @desc
 * @since 2020年02月27日 18:25
 */
@Service
public class RobotStaticBackupService {
    @Autowired
    private RobotStaticBackupMapper mapper;

    public Long addBackup(RobotStaticBackup entity) {
        if (entity == null) {
            return 0L;
        }
        mapper.insert(entity);
        return entity.getBackupId();
    }

    public Integer closeOtherDefault(Long id, String robotToken) {
        return mapper.closeOtherDefault(robotToken, id);
    }

    /**
     * 查询默认备份
     * @param robots
     * @return
     */
    public List<RobotStaticBackup> listDefaultBackups(List<String> robots) {
        if (robots == null || robots.size() == 0) {
            return (List<RobotStaticBackup>) Collections.EMPTY_LIST;
        }
        QueryWrapper<RobotStaticBackup> queryWrapper = Wrappers.<RobotStaticBackup>query();
        queryWrapper.eq(RobotStaticBackup.IS_DEFAULT, 1)
                .eq(RobotStaticBackup.IS_DELETED, 0)
                .in(RobotStaticBackup.DING_ROBOT_TOKEN, robots);
        List<RobotStaticBackup> list = mapper.selectList(queryWrapper);
        return list;
    }

    public RobotStaticBackup getBackup(Long backupId) {
        RobotStaticBackup result = mapper.selectById(backupId);
        return result;
    }
}
