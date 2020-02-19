package com.sys.monitor.job;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author willis
 * @desc
 * @since 2020年02月18日 15:30
 */
@EnableScheduling
public class ApiMonitorTask {

    /**
     * 每周一发送慢接口通知
     */
    @Scheduled(cron = "${cron.alarm.week:0 30 8 * * MON}")
    public void weekAlarm() {

    }
}
