package com.sys.monitor.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author willis
 * @date 2020-01-02
 */
@Configuration
public class DataSourceConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);
    @Bean
    public ServletRegistrationBean druidServlet() {
        return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    /**
     * 主库
     * @param driver
     * @param url
     * @param username
     * @param password
     * @param publicKey
     * @return
     */
    @Primary
    @Bean(name = "ds")
    public DataSource dataSource(
            @Value("${db.driver-class}") String driver,
            @Value("${db.url}") String url,
            @Value("${db.username}") String username,
            @Value("${db.password}") String password) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        LOGGER.info("durid db password : {}", password);
        druidDataSource.setPassword(password);
        druidDataSource.setConnectionInitSqls(Arrays.asList("set names utf8mb4;"));
        return druidDataSource;
    }

    /**
     * 当加密有public key 时则调用此方法
     *
     * @param driver
     * @param url
     * @param username
     * @param password
     * @param publicKey
     * @return
     */
    private DataSource createDruidDataSource(String driver, String url, String username, String password, String publicKey) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        LOGGER.info("durid db password : {}", password);
        druidDataSource.setPassword(password);
        druidDataSource.setConnectionInitSqls(Arrays.asList("set names utf8mb4;"));
        druidDataSource.setConnectionProperties("config.decrypt=true;config.decrypt.key=" + publicKey);
        try {
            druidDataSource.setFilters("stat, wall,config");
        } catch (SQLException e) {
            LOGGER.error("create druid datasource", e);
        }
        return druidDataSource;
    }

}
