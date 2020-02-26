package com.sys.monitor.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author willis
 * @chapter 字段别名
 * @section
 * @since 2018年09月09日 00:14
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface FieldQualifier {
    String value() default "";
    /** 别名*/
    String alias() default "";
    int weight() default 0;
    /** 日期格式，只对日期类型字段有效*/
    String dateFmt() default "yyyy-MM-dd HH:mm:ss";
    /** 不包含某个字段*/
    boolean exclude() default false;
    int sequence() default 0;

    /**
     * 是否自适应大小
     * @return
     */
    boolean autoSize() default false;

    /**
     * 列宽，会影响autoSize属性
     * @return
     */
    int width() default 0;
}
