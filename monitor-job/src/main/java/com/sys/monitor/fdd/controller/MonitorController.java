package com.sys.monitor.fdd.controller;

import com.sys.monitor.entity.RobotStaticBackup;
import com.sys.monitor.entity.resp.HttpResponse;
import com.sys.monitor.fdd.invoker.OceanstackInvoker;
import com.sys.monitor.fdd.job.ApiCatMonitorTask;
import com.sys.monitor.service.RobotStaticBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author willis
 * @desc
 * @since 2020年02月18日 17:48
 */
@RestController
@RequestMapping("/monitor")
public class MonitorController extends AbstractController{
    @Autowired
    private RobotStaticBackupService robotStaticBackupService;

    @Autowired
    private OceanstackInvoker oceanstackInvoker;

    @Autowired
    private ApiCatMonitorTask apiCatMonitorTask;


    @RequestMapping("/{appId}")
    public HttpResponse<Object> getMonitor(@PathVariable(value = "appId") String appId) {
        try {
            throw new NullPointerException();
        } catch (Exception e) {
            getLogger().error("msg={},", "123", e);
        }
        getLogger().info("appId={}", appId);
        return null;
    }

    @GetMapping("/backup/{backupId}")
    public void getBackup(@PathVariable("backupId") Long backupId, HttpServletResponse response) {
        RobotStaticBackup backup = robotStaticBackupService.getBackup(backupId);
        if (backup == null) {
            return;
        }
        String attachUrl = backup.getAttachUrl();
        String url = oceanstackInvoker.getDownloadToken(attachUrl, 24L * 3600L);
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            try {
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                response.setHeader("Content-Type", "html");
                writer.write("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body><h2>".concat("有点问题").concat("</h2></body></html>"));
                writer.close();
            } catch (IOException e1) {
            }
        }
    }

    @RequestMapping("/test")
    public void test() {
        apiCatMonitorTask.weekAlarm();
    }


}
