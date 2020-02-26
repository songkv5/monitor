package com.sys.monitor.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author willis
 * @chapter
 * @section
 * @since 2018年10月21日 22:24
 */
public class ClassUtil {
    public static <T> List<Field> getAllField(final Class<T> clazz) {
        if (clazz == null) {
            return Collections.emptyList();
        }
        List<Field> fields = new ArrayList<>();
        Class<?> crtClazz = clazz;
        while(crtClazz != null) {
            Field[] fieldsTmp = crtClazz.getDeclaredFields();
            if (fieldsTmp != null && fieldsTmp.length > 0) {
                for (Field field : fieldsTmp) {
                    fields.add(field);
                }
            }
            crtClazz = crtClazz.getSuperclass();
        }
        return fields;
    }
}
