package com.sys.monitor.test.adapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sys.monitor.enums.ApiType;
import com.sys.monitor.adapter.CatAdapter;
import com.sys.monitor.adapter.CatMonitorQueryRequest;
import com.sys.monitor.entity.cat.ApiMonitorProperty;
import com.sys.monitor.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @Author willis
 * @desc
 * @since 2020年02月18日 16:44
 */
public class CatAdapterTest extends BaseTest {
    @Autowired
    private CatAdapter catAdapter;

    @Test
    public void listMonitorInfo() {
        CatMonitorQueryRequest.Builder builder = CatMonitorQueryRequest.newBuilder();
        builder.apiType(ApiType.REST)
                .appId("ddd")
                .endDate(new Date())
                .startDate(new Date(System.currentTimeMillis() - 7L * 24L * 3600L * 1000L));

        List<ApiMonitorProperty> list = catAdapter.listMonitorInfo(builder.build());
        System.out.println("list=" + JSON.toJSONString(list, SerializerFeature.PrettyFormat));
    }
}
