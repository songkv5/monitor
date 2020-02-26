package com.sys.monitor.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

/**
 * @Author willis
 * @desc
 * @since 2020年02月26日 22:35
 */
public class CommonUtil {
    public static <T extends Number> String numberStrFmt(T amount, int scale, RoundingMode roundingMode) {
        if (amount == null) {
            return "0";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("####");
        if (scale > 0) {
            sb.append(".");
            for (int i = 0; i < scale; i ++) {
                sb.append("#");
            }
        }
        DecimalFormat df = new DecimalFormat(sb.toString());
        if (Objects.nonNull(roundingMode)) {
            df.setRoundingMode(roundingMode);
        } else {
            df.setRoundingMode(RoundingMode.HALF_UP);
        }
        String result = df.format(amount);
        return result;
    }
    public static <T extends Number> String numberStrFmt(T amount, int scale) {
        return numberStrFmt(amount, scale, null);
    }
}
