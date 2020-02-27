package com.sys.monitor.test.service;

import com.sys.monitor.entity.RobotStaticBackup;
import com.sys.monitor.service.RobotStaticBackupService;
import com.sys.monitor.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Author willis
 * @desc
 * @since 2020年02月27日 18:28
 */

public class RobotStaticBackupServiceTest extends BaseTest {
    @Autowired
    private RobotStaticBackupService service;

    @Test
    public void addTest() {
        RobotStaticBackup entity = new RobotStaticBackup();
        entity.setAttachUrl("www");
        entity.setDateBegin(new Date());
        entity.setDateEnd(new Date());
        entity.setDingRobotToken("123123123");
        entity.setCreateTime(new Date());
        Long id = service.addBackup(entity);
        System.out.println("id = " + id);
    }
}
