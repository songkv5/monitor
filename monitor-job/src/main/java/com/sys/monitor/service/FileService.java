package com.sys.monitor.service;

import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.sys.monitor.entity.SevenOxUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @Author willis
 * @desc
 * @since 2020年02月27日 14:33
 */
@Service
@Slf4j
public class FileService {
    /**
     * 上传文件到七牛
     * @param sourceFile
     * @return
     */
    public String upload2SevenOx(File sourceFile, String upToken) {
        Configuration cfg = new Configuration(Region.region0());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Response response = uploadManager.put(sourceFile, null, upToken);
            byte[] body = response.body();
            String result = new String(body, "utf-8");
            SevenOxUploadResponse res = JSON.parseObject(result, SevenOxUploadResponse.class);
            if (res != null) {
                return res.getUrl();
            }
        } catch (Exception e) {
            log.error("文件上传失败,", e);
        }
        return null;
    }
}
