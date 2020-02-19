package com.sys.monitor.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author willis
 * @desc
 * @since 2020年02月18日 17:11
 */
@Slf4j
public class HttpUtil {
    public static OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder.readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        return client;
    }
    public static final MediaType JSONType = MediaType.parse("application/json; charset=utf-8");

    public static String sendGetRequest(String url, Map<String, String> header, Map<String, String> params) {
        String retStr = null;
        Request.Builder builder = new Request.Builder();
        if (header != null && header.size() > 0) {
            Set<Map.Entry<String, String>> entries = header.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
            while(iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        url = buildUrl(url, params);
        Request result = builder.url(url).build();
        try {
            log.info("正在执行http get 请求， url={}", url);
            Response response = getClient().newCall(result).execute();
            ResponseBody body = response.body();
            int code = response.code();
            retStr = body.string();

        } catch (Exception e) {
            log.error("http invoke error,url={}", url, e);
        }
        log.error("http get 请求处理结束,url={};result={}", url, retStr);
        return retStr;
    }

    public static byte[] sendPostJsonRequest(String url, Map<String, String> header, Map<String, String> params, Map postEntity) {
        log.info("正在调用远程post请求，url={}, header={}; param={}；requestBody={}" , url, JSON.toJSONString(header), JSON.toJSONString(params), JSON.toJSONString(postEntity));
        Request.Builder builder = new Request.Builder();
        builder.header("Content-type", "application/json");
        if (header != null && header.size() > 0) {
            Set<Map.Entry<String, String>> entries = header.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
            while(iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        url = buildUrl(url, params);
        if (postEntity != null) {
            RequestBody requestBody = RequestBody.create(JSONType, JSON.toJSONString(postEntity));
            builder.post(requestBody);
        }
        Request request = builder.url(url).build();
        OkHttpClient client = getClient();
        try {
            Response response = client.newCall(request).execute();
            byte[] bytes = response.body().bytes();
            return bytes;
        } catch (Exception e) {
            log.error("http invoke error, url={}", url, e);
        }
        return null;
    }

    private static String buildUrl(final String url,final Map<String, String> params) {
        String result = url;
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
            StringBuffer querySb = new StringBuffer("?");
            while(iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                querySb.append(entry.getKey()).append("=").append(entry.getValue());
                if (iterator.hasNext()) {
                    querySb.append("&");
                }
            }
            result = url.concat(querySb.toString());
        }
        return result;
    }
}
