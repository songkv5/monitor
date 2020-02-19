package com.sys.monitor.adapter;

import com.sys.monitor.enums.ApiType;

import java.util.Date;

/**
 * @Author willis
 * @desc cat监控请求参数
 * @since 2020年02月18日 16:19
 */
public class CatMonitorQueryRequest {
    /**
     * appId
     */
    private String appId;
    /**
     * 开始日期
     */
    private Date startDate;
    /**
     * 结束日期
     */
    private Date endDate;
    /**
     * 接口类型
     */
    private ApiType apiType;

    public String getAppId() {
        return appId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public ApiType getApiType() {
        return apiType;
    }

    private CatMonitorQueryRequest() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    public static class Builder{
        private CatMonitorQueryRequest request;
        private Builder() {
            this.request = new CatMonitorQueryRequest();
        }

        public Builder appId(String appId) {
            this.request.appId = appId;
            return this;
        }
        public Builder startDate(Date startDate) {
            this.request.startDate = startDate;
            return this;
        }
        public Builder endDate(Date endDate) {
            this.request.endDate = endDate;
            return this;
        }

        public Builder apiType(ApiType apiType) {
            this.request.apiType = apiType;
            return this;
        }
        public CatMonitorQueryRequest build() {
            return this.request;
        }
    }
}
