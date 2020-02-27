package com.sys.monitor.fdd.controller;

import com.sys.monitor.exception.MonitorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @Author willis
 * @desc
 * @since 2020年02月18日 17:28
 */
public abstract class AbstractController {
    private final static Pattern NUM_PATTERN = Pattern.compile("[\\d]+");
    private final static DateFormat Y_M_D_H_M_S_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static DateFormat Y_M_D_FMT = new SimpleDateFormat("yyyy-MM-dd");
    @InitBinder
    public void initBinder(WebDataBinder binder) {

        //转换日期
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport(){
            //自定义参数格式
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                boolean isNum = NUM_PATTERN.matcher(text).matches();
                //如果传递的是时间戳
                if (isNum) {
                    Date date = new Date(Long.parseLong(text));
                    super.setValue(date);
                    return;
                }
                try {
                    Date date = Y_M_D_H_M_S_FMT.parse(text);
                    super.setValue(date);
                } catch (Exception e) {
                    try {
                        Date date = Y_M_D_FMT.parse(text);
                        super.setValue(date);
                    } catch (Exception ex) {
                        getLogger().error("日期转换错误");
                        throw new MonitorException(0, "可选之间格式：1）时间戳；2）yyyy-MM-dd HH:mm:ss；3）yyyy-MM-dd");
                    }
                }
            }

        });
    }
    protected Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
