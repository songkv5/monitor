package com.sys.monitor.test.invoker;

import com.sys.monitor.fdd.invoker.OceanstackInvoker;
import com.sys.monitor.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author willis
 * @desc
 * @since 2020年02月27日 18:01
 */
public class OceanstackInvokerTest extends BaseTest {
    @Autowired
    private OceanstackInvoker oceanstackInvoker;

    @Test
    public void getUploadTokenTest() {
        String token = oceanstackInvoker.getUploadToken();
        System.out.println("token = " + token);
    }
}
