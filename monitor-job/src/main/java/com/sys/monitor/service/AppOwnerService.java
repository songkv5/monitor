package com.sys.monitor.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sys.monitor.entity.AppOwner;
import com.sys.monitor.mapper.AppOwnerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @Author willis
 * @desc
 * @since 2020年02月26日 16:57
 */
@Service
public class AppOwnerService {
    @Autowired
    private AppOwnerMapper appOwnerMapper;
    public Map<String, List<AppOwner>> mapOwners(List<String> appIds) {
        if (appIds == null || appIds.size() == 0) {
            return (Map<String, List<AppOwner>>) Collections.EMPTY_MAP;
        }
        QueryWrapper<AppOwner> wrapper = Wrappers.<AppOwner>query()
                .in(AppOwner.APP_ID, appIds)
                .eq(AppOwner.IS_DELETED, 0);

        List<AppOwner> appOwners = appOwnerMapper.selectList(wrapper);
        if (appOwners == null || appOwners.size() == 0) {
            return (Map<String, List<AppOwner>>) Collections.EMPTY_MAP;
        }
        return appOwners.stream().collect(Collectors.groupingBy(arg -> arg.getAppId()));
    }
}
