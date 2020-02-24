package com.sys.monitor.job;

import com.sys.monitor.adapter.CatAdapter;
import com.sys.monitor.adapter.CatMonitorQueryRequest;
import com.sys.monitor.entity.MonitorItem;
import com.sys.monitor.entity.cat.ApiMonitorProperty;
import com.sys.monitor.entity.ding.RobotMsg;
import com.sys.monitor.enums.ApiType;
import com.sys.monitor.enums.DingRobotMsgTypeEnum;
import com.sys.monitor.service.DingService;
import com.sys.monitor.service.MonitorReadService;
import com.sys.monitor.service.param.RobotMsgReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author willis
 * @desc
 * @since 2020年02月18日 15:30
 */
@EnableScheduling
@Component
public class ApiCatMonitorTask {
    @Autowired
    private MonitorReadService monitorReadService;
    @Autowired
    private CatAdapter catAdapter;
    @Value("${monitor.norm.rest.line95:600}")
    private long restLine95;
    @Value("${monitor.norm.dubbo.line95:150}")
    private long dubboLine95;
    @Autowired
    private DingService dingService;

    /**
     * 每周一发送慢接口通知
     */
    @Scheduled(cron = "${cron.alarm.week:0 30 8 * * MON}")
    public void weekAlarm() {
        List<MonitorItem> monitorItems = monitorReadService.listMonitors();
        /**
         * key = robotToken
         * value = robotSecret
         */
        Map<String, String> robotMap = monitorItems.stream().collect(Collectors.toMap(arg -> arg.getDingRobotToken(), arg -> arg.getDingRobotSecret(), (v1, v2) -> v1));

        /**
         * key == appId
         */
        Map<String, MonitorItem> monitorMap = monitorItems.stream().collect(Collectors.toMap(arg -> arg.getAppId(), arg -> arg, (v1, v2) -> v1));
        /**
         * key = robotId
         * value = robot下的appId列表
         */
//        Map<String, List<MonitorItem>> robotApps = monitorItems.stream().collect(Collectors.groupingBy(arg -> arg.getDingRobotToken()));

        /**
         * key = robotId
         * value = 接口
         */
        Map<String, List<ApiMonitorProperty>> map = new HashMap<>();

        if (monitorItems != null && monitorItems.size() > 0) {
            monitorItems.forEach(item -> {
                List<ApiMonitorProperty> list = getOptimizingApis(item);
                if (list != null || list.size() > 0) {
                    list.forEach(arg -> {
                        MonitorItem monitorItem = monitorMap.get(arg.getAppId());
                        if (monitorItem == null) {
                            return;
                        }
                        String dingRobotToken = monitorItem.getDingRobotToken();
                        map.computeIfAbsent(dingRobotToken, key -> {
                            return new ArrayList<>();
                        }).add(arg);
                    });
                }
            });
        }
        // 发送
        sendAlert(map, robotMap, monitorMap);
    }

    /**
     * @param map key = robotId，value=接口列表
     * @param robotMap
     * @param monitorMap
     */
    private void sendAlert(Map<String, List<ApiMonitorProperty>> map, Map<String, String> robotMap, Map<String, MonitorItem> monitorMap) {
        long crtTs = System.currentTimeMillis();
        if (map != null && map.size() > 0) {
            map.forEach((k, v) -> {
                String secret = robotMap.get(k);
                String sign = dingService.sign4Robot(secret, crtTs);
                RobotMsgReq msgReq = RobotMsgReq.newBuilder()
                        .robotToken(k)
                        .timestamp(crtTs)
                        .sign(sign)
                        .msgType(DingRobotMsgTypeEnum.MARKDOWN)
                        .msg(buildMarkdownCode(v, monitorMap))
                        .build();
                // 发送消息
                dingService.sendRobotMsg(msgReq);
            });
        }
    }

    private RobotMsg buildMarkdownCode(List<ApiMonitorProperty> list, Map<String, MonitorItem> monitorMap) {
        StringBuffer markdownBuffer = new StringBuffer();
        markdownBuffer.append("## [惊愕]接口爬不动啦，快过来拉一把[天使]\n\n");
        List<String> atUsers = new ArrayList<>();
        if (list != null && list.size() > 0) {
            Map<String, List<ApiMonitorProperty>> map = list.stream().collect(Collectors.groupingBy(arg -> arg.getAppId()));
            map.forEach((k, v) -> {
                // 按应用分组
                /**
                 * k = appId
                 * v = appId对应的慢接口
                 */
                MonitorItem monitorItem = monitorMap.get(k);
                if (monitorItem == null) {
                    return;
                }
                markdownBuffer.append("### ").append("[向右]").append(k).append("[向左]").append("\n\n");
                List<String> receivers = monitorItem.getReceivers();
                // @到人
                if (receivers != null && receivers.size() > 0) {
                    markdownBuffer.append("**负责人：** ");
                    receivers.forEach(receiver -> {
                        markdownBuffer.append("@").append(receiver).append(" ");
                    });
                    markdownBuffer.append("\n\n");
                }
                markdownBuffer.append("\n\n");
                v.forEach(arg -> {
                    markdownBuffer
                            .append("**接口：** ").append(arg.getId()).append("\n\n")
                            .append("**95Line：** ").append(arg.getLine95Value()).append("ms").append("\n\n")
                            .append("**总调用次数：** ").append(arg.getTotalCount()).append("\n\n")
                            .append("**失败次数：** ").append(arg.getFailCount()).append("\n\n")
                            .append("**时间范围：** ").append(arg.getTimeRange()).append("\n\n")
                            .append("---")
                            .append("\n\n");
                });
                atUsers.addAll(receivers);
            });

        }
        return RobotMsg.Builder.builder()
                .title("[惊愕]接口爬不动啦，快过来拉一把[天使]")
                .content(markdownBuffer.toString())
                .atUserMobiles(atUsers.stream().distinct().collect(Collectors.toList()))
                .build();
    }

    /**
     * 后去需要优化的接口
     * @param monitorItem
     * @return
     */
    private List<ApiMonitorProperty> getOptimizingApis(MonitorItem monitorItem) {
        List<ApiMonitorProperty> result = new ArrayList<>();
        String appId = monitorItem.getAppId();
        String dingRobotToken = monitorItem.getDingRobotToken();
        List<ApiType> types = monitorItem.getTypes();

        if (appId == null || dingRobotToken == null) {
            return (List<ApiMonitorProperty>) Collections.EMPTY_LIST;
        }
        if (types == null || types.size() == 0) {
            types = Arrays.asList(ApiType.DUBBO, ApiType.REST);
        }
        long crtTs = System.currentTimeMillis();
        Long dubboLine95 = monitorItem.getDubboLine95();
        Long urlLine95 = monitorItem.getUrlLine95();
        List<String> whiteApis = monitorItem.getWhiteApis();

        for (ApiType type : types) {
            long line95 = restLine95;
            switch (type) {
                case DUBBO:{
                    line95 = dubboLine95 == null ? this.dubboLine95 : dubboLine95;
                    break;
                }
                case REST:{
                    line95 = urlLine95 == null ? restLine95 : urlLine95;
                    break;
                }
                default:{
                    break;
                }
            }
            CatMonitorQueryRequest request = CatMonitorQueryRequest.newBuilder()
                    .startDate(new Date(crtTs - 1000L * 7L * 24L * 3600L))
                    .endDate(new Date(crtTs))
                    .appId(appId)
                    .apiType(type)
                    .build();
            // 接口信息
            List<ApiMonitorProperty> list = catAdapter.listMonitorInfo(request);
            for (ApiMonitorProperty item : list) {
                Double line95Value = item.getLine95Value();
                // 忽略白名单
                if (whiteApis != null && whiteApis.contains(item.getId())) {
                    continue;
                }
                if (line95Value > line95) {
                    result.add(item);
                }
            }
        }
        return result;
    }
}
