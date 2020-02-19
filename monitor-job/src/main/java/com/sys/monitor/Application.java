package com.sys.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author willis
 * @desc
 * @since 2020年02月18日 14:28
 */
@SpringBootApplication(scanBasePackages = "com.sys.monitor")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
