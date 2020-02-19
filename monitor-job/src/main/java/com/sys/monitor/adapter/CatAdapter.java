package com.sys.monitor.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sys.monitor.entity.cat.ApiMonitorProperty;
import com.sys.monitor.enums.ApiType;
import com.sys.monitor.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author willis
 * @desc
 * @since 2020年02月18日 15:32
 */
@Component
@Slf4j
public class CatAdapter {
    @Value("${monitor.url}")
    private String catUrl;
    private SimpleDateFormat SHORT_DATE = new SimpleDateFormat("yyyyMMdd");

    public List<ApiMonitorProperty> listMonitorInfo(CatMonitorQueryRequest restReq) {
        Map<String, String> param = new HashMap<>();
        param.put("ip", "All");
        param.put("forceDownload", "json");

        if (restReq != null) {
            ApiType apiType = restReq.getApiType();
            String appId = restReq.getAppId();
            Date startDate = restReq.getStartDate();
            Date endDate = restReq.getEndDate();
            if (apiType != null) {
                param.putIfAbsent("type", apiType.getCode());
            }
            if (appId != null) {
                param.putIfAbsent("domain", appId);
            }
            if (startDate != null) {
                param.putIfAbsent("startDate", SHORT_DATE.format(startDate));
            }
            if (endDate != null) {
                param.putIfAbsent("endDate", SHORT_DATE.format(endDate));
            }
        }
        String jsonStr = HttpUtil.sendGetRequest(catUrl, null, param);
        JSONObject json = JSONObject.parseObject(jsonStr);
        JSONArray array = json.getJSONObject("displayNameReport").getJSONArray("results");
        return array.stream().map(arg -> {
            JSONObject object = (JSONObject) arg;
            String detail = object.getString("detail");
            ApiMonitorProperty result = JSON.parseObject(detail, ApiMonitorProperty.class);
            result.setAppId(restReq.getAppId());
            return result;
        }).collect(Collectors.toList());
    }
}
