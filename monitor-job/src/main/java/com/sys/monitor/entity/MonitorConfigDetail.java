package com.sys.monitor.entity;

import java.util.List;

/**
 * @Author willis
 * @desc
 * @since 2020年02月26日 16:04
 */
public class MonitorConfigDetail extends MonitorConfig {
    /**
     * 负责人
     */
    private List<AppOwner> owners;
    /**
     * 接口白名单
     */
    private List<AppWhiteList> whiteList;

    public List<AppOwner> getOwners() {
        return owners;
    }

    public void setOwners(List<AppOwner> owners) {
        this.owners = owners;
    }

    public List<AppWhiteList> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<AppWhiteList> whiteList) {
        this.whiteList = whiteList;
    }
}
