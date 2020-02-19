package com.sys.monitor.controller;

import com.sys.monitor.entity.resp.HttpResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author willis
 * @desc
 * @since 2020年02月18日 17:48
 */
@RestController
@RequestMapping("monitor")
public class MonitorController extends AbstractController {
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
}
