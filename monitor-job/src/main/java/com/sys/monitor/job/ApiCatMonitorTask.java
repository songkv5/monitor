package com.sys.monitor.job;

import com.sys.monitor.adapter.CatAdapter;
import com.sys.monitor.adapter.CatMonitorQueryRequest;
import com.sys.monitor.component.ExcelBuildAdapter;
import com.sys.monitor.entity.AppOwner;
import com.sys.monitor.entity.AppWhiteList;
import com.sys.monitor.entity.MonitorConfigDetail;
import com.sys.monitor.entity.cat.ApiMonitorProperty;
import com.sys.monitor.entity.cat.CatApiProperty4Excel;
import com.sys.monitor.entity.ding.RobotMsg;
import com.sys.monitor.enums.ApiType;
import com.sys.monitor.enums.DingRobotMsgTypeEnum;
import com.sys.monitor.service.AppOwnerService;
import com.sys.monitor.service.DingService;
import com.sys.monitor.service.MonitorReadService;
import com.sys.monitor.service.param.RobotMsgReq;
import com.sys.monitor.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
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
    @Autowired
    private AppOwnerService appOwnerService;

    /**
     * 每周一发送慢接口通知
     */
    @Scheduled(cron = "${cron.alarm.week:0 30 8 * * MON}")
    public void weekAlarm() {
        List<MonitorConfigDetail> monitorItems = monitorReadService.listFromDb();
        /**
         * key = robotToken
         * value = robotSecret
         */
        Map<String, String> robotMap = monitorItems.stream().collect(Collectors.toMap(arg -> arg.getDingRobotToken(), arg -> arg.getDingRobotSecret(), (v1, v2) -> v1));

        /**
         * key == appId,
         * value = 一般列表只有一个条目
         */
        Map<String, List<MonitorConfigDetail>> appMonitorsMap = monitorItems.stream().collect(Collectors.groupingBy(arg -> arg.getAppId()));

        Map<String, List<AppOwner>> ownerMap = appOwnerService.mapOwners(monitorItems.stream().map(arg -> arg.getAppId()).distinct().collect(Collectors.toList()));
        /**
         * key = robotToken
         * value = 接口
         */
        Map<String, List<ApiMonitorProperty>> map = new HashMap<>();

        if (monitorItems != null && monitorItems.size() > 0) {
            monitorItems.forEach(item -> {
                List<ApiMonitorProperty> list = getOptimizingApis(item);
                if (list != null || list.size() > 0) {
                    list.forEach(arg -> {
                        String dingRobotToken = item.getDingRobotToken();
                        map.computeIfAbsent(dingRobotToken, key -> {
                            return new ArrayList<>();
                        }).add(arg);
                    });
                }
            });
        }
        // 详细接口
        prepareFileDump(map, ownerMap);
        // 发送
        sendAlert(map, robotMap, ownerMap);
    }

    private void prepareFileDump(Map<String, List<ApiMonitorProperty>> map, Map<String, List<AppOwner>> ownerMap) {
        map.forEach((k, v) -> {
            String robotToken = k;
            List<ApiMonitorProperty> apis = v;
            List<CatApiProperty4Excel> list = apis.stream().map(this::convertApiProperty).collect(Collectors.toList());
            Map<String, List<CatApiProperty4Excel>> appApisMap = list.stream().collect(Collectors.groupingBy(arg -> arg.getAppId()));
            buildFile(appApisMap, robotToken, ownerMap);
        });
    }


    /**
     * key = appId
     * value = 接口列表
     * @param appApisMap
     */
    private void buildFile(Map<String, List<CatApiProperty4Excel>> appApisMap, String robot, Map<String, List<AppOwner>> ownerMap) {
        ExcelBuildAdapter.ExcelBuilder<CatApiProperty4Excel> builder = ExcelBuildAdapter.<CatApiProperty4Excel>newBuilder("E:\\" + robot + "")
                .maxRowPerSheet(1000)
                .startColumnNum(1);
        Set<Map.Entry<String, List<CatApiProperty4Excel>>> entries = appApisMap.entrySet();
        Iterator<Map.Entry<String, List<CatApiProperty4Excel>>> iterator = entries.iterator();
        // 合并单元格的起始行
        int startRowNum = 0;
        while(iterator.hasNext()) {
            Map.Entry<String, List<CatApiProperty4Excel>> next = iterator.next();
            List<CatApiProperty4Excel> list = next.getValue();
            String appId = next.getKey();
            builder.append(list, true);
            // 合并的单元格显示的内容
            StringBuffer text = new StringBuffer();
            text.append(appId);
            List<AppOwner> appOwners = ownerMap.get(appId);
            if (appOwners != null & appOwners.size() > 0) {
                text.append("\r\n")
                        .append("负责人: ");
                for (int i = 0; i < appOwners.size(); i ++) {
                    AppOwner o = appOwners.get(i);
                    text.append(o.getOwnerName());
                    if (i < appOwners.size() -1) {
                        text.append(";");
                    }
                }
            }
            // TODO 加上时间范围
            builder.mergeCells(startRowNum, startRowNum + list.size(), 0, 0, text.toString());
            startRowNum += (1 + list.size());
        }
        ExcelBuildAdapter adapter = builder.get();
        // excel文件
        File excelFile = adapter.getFile();
    }

    private CatApiProperty4Excel convertApiProperty(ApiMonitorProperty source) {
        if (source == null) {
            return null;
        }
        CatApiProperty4Excel result = new CatApiProperty4Excel();
        BeanUtils.copyProperties(source, result);
        result.setTps(CommonUtil.numberStrFmt(source.getTps(), 2));
        result.setTotalCount(CommonUtil.numberStrFmt(source.getTotalCount(), 0));
        result.setSum2(CommonUtil.numberStrFmt(source.getSum2(), 2));
        result.setStd(CommonUtil.numberStrFmt(source.getStd(), 2));
        result.setMin(CommonUtil.numberStrFmt(source.getMin(), 2).concat("ms"));
        result.setMax(CommonUtil.numberStrFmt(source.getMax(), 2).concat("ms"));
        result.setLine95Value(CommonUtil.numberStrFmt(source.getLine95Value(), 2).concat("ms"));
        result.setLine90Value(CommonUtil.numberStrFmt(source.getLine90Value(), 2).concat("ms"));
        result.setLine50Value(CommonUtil.numberStrFmt(source.getLine50Value(), 2).concat("ms"));
        result.setFailPercent(source.getFailPercent().concat("%"));
        result.setFailCount(CommonUtil.numberStrFmt(source.getFailCount(), 0));
        result.setAvg(CommonUtil.numberStrFmt(source.getAvg(), 2).concat("ms"));
        return result;
    }
    /**
     * @param map key = robotId，value=接口列表
     * @param robotMap
     * @param ownerMap
     */
    private void sendAlert(Map<String, List<ApiMonitorProperty>> map, Map<String, String> robotMap, Map<String, List<AppOwner>> ownerMap) {
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
                        .msg(buildMarkdownCode(v, ownerMap))// 某个robot要发送的慢接口
                        .build();
                // 发送消息
                dingService.sendRobotMsg(msgReq);
            });
        }
    }

    private RobotMsg buildMarkdownCode(List<ApiMonitorProperty> list, Map<String, List<AppOwner>> ownerMap) {
        StringBuffer markdownBuffer = new StringBuffer();
        markdownBuffer.append("## [惊愕]接口爬不动啦，快过来拉一把[天使]\n\n");
        int apiCount = 10;
        List<String> atUsers = new ArrayList<>();
        if (list != null && list.size() > apiCount) {
            List<ApiMonitorProperty> showList = list.subList(0, apiCount);
            Map<String, List<ApiMonitorProperty>> map = showList.stream().collect(Collectors.groupingBy(arg -> arg.getAppId()));

            // 按应用分组
            /**
             * k = appId
             * v = appId对应的慢接口配置
             */
            map.forEach((k, v) -> {
                markdownBuffer.append("### ").append("[向右]").append(k).append("[向左]").append("\n\n");
                List<AppOwner> owners = ownerMap.get(k);
                // @到人
                if (owners != null && owners.size() > 0) {
                    markdownBuffer.append("**负责人：** ");
                    owners.forEach(receiver -> {
                        markdownBuffer.append("@").append(receiver.getOwnerMobile()).append(" ");
                    });
                    markdownBuffer.append("\n\n");
                    atUsers.addAll(owners.stream().map(arg -> arg.getOwnerMobile()).collect(Collectors.toList()));
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
     * @param monitor
     * @return
     */
    private List<ApiMonitorProperty> getOptimizingApis(MonitorConfigDetail monitor) {
        List<ApiMonitorProperty> result = new ArrayList<>();
        String appId = monitor.getAppId();
        String dingRobotToken = monitor.getDingRobotToken();
        String apiType = monitor.getApiType();
        ApiType type = ApiType.getByCode(apiType);
        if (type == ApiType.UNKNOWN) {
            return (List<ApiMonitorProperty>) Collections.EMPTY_LIST;
        }

        if (appId == null || dingRobotToken == null) {
            return (List<ApiMonitorProperty>) Collections.EMPTY_LIST;
        }
        long crtTs = System.currentTimeMillis();
        Long line95 = null;
        if (type == ApiType.DUBBO) {
            line95 = monitor.getLine95() == null ? dubboLine95 : monitor.getLine95().longValue();
        } else if (type == ApiType.REST) {
            line95 = monitor.getLine95() == null ? restLine95 : monitor.getLine95().longValue();
        }
        List<AppWhiteList> whiteList = monitor.getWhiteList();

        CatMonitorQueryRequest request = CatMonitorQueryRequest.newBuilder()
                .startDate(new Date(crtTs - 1000L * 7L * 24L * 3600L))
                .endDate(new Date(crtTs))
                .appId(appId)
                .apiType(type)
                .build();
        // 接口信息
        List<ApiMonitorProperty> list = catAdapter.listMonitorInfo(request);
        List<String> whiteApis = Optional.ofNullable(whiteList).orElse((List<AppWhiteList>) Collections.EMPTY_LIST)
                .stream().map(arg -> arg.getApi()).collect(Collectors.toList());
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
        return result;
    }
}
