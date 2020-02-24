package com.sys.monitor.test.job;

import com.sys.monitor.job.ApiCatMonitorTask;
import com.sys.monitor.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author willis
 * @desc
 * @since 2020年02月24日 11:36
 */
public class ApiMonitorTaskTest extends BaseTest {
    @Autowired
    private ApiCatMonitorTask task;

    @Test
    public void weekAlarmTest() {
        task.weekAlarm();
    }
}
