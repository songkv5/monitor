package com.sys.monitor.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sys.monitor.entity.*;
import com.sys.monitor.enums.ApiType;
import com.sys.monitor.fdd.job.ApiCatMonitorTask;
import com.sys.monitor.mapper.AppOwnerMapper;
import com.sys.monitor.mapper.AppWhiteListMapper;
import com.sys.monitor.mapper.MonitorConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author willis
 * @desc 读取监控配置
 * @since 2020年02月20日 15:05
 */
@Service
@Slf4j
public class MonitorReadService {
    @Autowired
    private MonitorConfigMapper monitorConfigMapper;
    @Autowired
    private AppOwnerMapper appOwnerMapper;
    @Autowired
    private AppWhiteListMapper whiteListMapper;

    public List<MonitorItem> listMonitors() {
        List<MonitorItem> result = new ArrayList<>();
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(ApiCatMonitorTask.class.getClassLoader().getResourceAsStream("monitor.xml"));
            Element rootElement = doc.getRootElement();
            Element monitors = rootElement.element("monitors");
            List<Element> elements = monitors.elements();

            return elements.stream().map(this::convertMonitorItem).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("查询监控配置异常，", e);
        }
        return result;
    }

    private MonitorItem convertMonitorItem(Element element) {
        if (element == null) {
            return null;
        }
        MonitorItem result = new MonitorItem();
        String appId = element.elementText("appId");
        String dingSignSecret = element.elementText("dingSignSecret");
        String dingRobotToken = element.elementText("dingRobotToken");
        Element standard = element.element("standard");
        if (standard != null) {
            Element dubboEle = standard.element("dubbo");
            if (dubboEle != null) {
                Element line95 = dubboEle.element("line95");
                if (line95 != null) {
                    try {
                        result.setDubboLine95(Long.parseLong(line95.getText()));
                    } catch (Exception e) {
                        log.error("数字转换错误,", e);
                    }
                }
            }
            Element urlEle = standard.element("url");
            if (urlEle != null) {
                Element line95 = urlEle.element("line95");
                if (line95 != null) {
                    try {
                        result.setUrlLine95(Long.parseLong(line95.getText()));
                    } catch (Exception e) {
                        log.error("数字转换错误,", e);
                    }
                }
            }
        }


        Element whiteApis = element.element("whiteApis");
        if (whiteApis != null) {
            List<Element> elements = whiteApis.elements();
            if (elements != null && elements.size() > 0) {
                // 白名单
                List<String> apis = elements.stream().map(arg -> arg.getText()).collect(Collectors.toList());
                result.setWhiteApis(apis);
            }
        }
        result.setAppId(appId);
        result.setDingRobotSecret(dingSignSecret);
        result.setDingRobotToken(dingRobotToken);

        Element receivers = element.element("receivers");
        if (receivers != null) {
            List<Element> list = receivers.elements();
            List<String> mobiles = list.stream().map(arg -> arg.getText()).collect(Collectors.toList());
            result.setReceivers(mobiles);
        }

        Element type = element.element("types");
        if (type != null) {
            List<Element> types = type.elements();
            if (types != null && types.size() > 0) {
                List<ApiType> ats = types.stream().map(arg -> {
                    if (arg.getText().equals("url")) {
                        return ApiType.REST;
                    } else if (arg.getText().equals("dubbo")) {
                        return ApiType.DUBBO;
                    } else {
                        return null;
                    }
                }).filter(arg -> arg != null).collect(Collectors.toList());
                result.setTypes(ats);
            }
        }
        return result;
    }

    public List<MonitorConfigDetail> listFromDb() {
        QueryWrapper<MonitorConfig> wrapper = Wrappers.<MonitorConfig>query()
                .eq(MonitorConfig.IS_DELETED, 0);

        List<MonitorConfig> list = monitorConfigMapper.selectList(wrapper);
        if (list == null || list.size() ==0) {
            return (List<MonitorConfigDetail>) Collections.EMPTY_LIST;
        }

        List<String> appIds = list.stream().map(arg -> arg.getAppId()).distinct().collect(Collectors.toList());
        QueryWrapper<AppOwner> wrapperOwner = Wrappers.<AppOwner>query()
                .in(AppOwner.APP_ID, appIds)
                .eq(AppOwner.IS_DELETED, 0);
        // 负责人
        List<AppOwner> owners = appOwnerMapper.selectList(wrapperOwner);
        Map<String, List<AppOwner>> ownerMap = Optional.ofNullable(owners).orElse((List<AppOwner>) Collections.EMPTY_LIST)
                .stream().collect(Collectors.groupingBy(arg -> arg.getAppId()));

        // 白名单
        QueryWrapper<AppWhiteList> wrapperWl = Wrappers.<AppWhiteList>query()
                .in(AppWhiteList.APP_ID, appIds)
                .eq(AppWhiteList.IS_DELETED, 0);
        List<AppWhiteList> whileLists = whiteListMapper.selectList(wrapperWl);
        Map<String, List<AppWhiteList>> whiteMap = Optional.ofNullable(whileLists).orElse((List<AppWhiteList>) Collections.EMPTY_LIST)
                .stream().collect(Collectors.groupingBy(arg -> arg.getAppId()));


        return list.stream().map(arg -> {
            MonitorConfigDetail result = new MonitorConfigDetail();
            BeanUtils.copyProperties(arg, result);
            List<AppOwner> appOwners = ownerMap.get(arg.getAppId());
            result.setOwners(appOwners);

            List<AppWhiteList> appWhiteLists = whiteMap.get(arg.getAppId());
            result.setWhiteList(appWhiteLists);
            return result;
        }).collect(Collectors.toList());

    }
}
