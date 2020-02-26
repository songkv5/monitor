/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.sys.monitor.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * MyBatis扫描接口，使用的tk.mybatis.spring.mapper.MapperScannerConfigurer，如果你不使用通用Mapper
 * 注意，由于MapperScannerConfigurer执行的比较早，所以必须有下面的注解
 *
 * @author rocyuan
 */
@Configuration
@MapperScan(value = "com.sys.monitor.mapper", sqlSessionFactoryRef = "mybatisSqlSessionFactoryBean")
@Slf4j
@EnableTransactionManagement(order = 1)
public class MyBatisConfig {

    @Autowired
    @Qualifier("ds")
    private DataSource dataSource;

    private static final String PRIMARY_KEY = "master";
    private static final String SLAVE_PREFIX = "slave";
    // mp的动态数据源默认使用下划线分组
    private static final String UNDERLINE = "_";

    /**
     * 添加分页插件
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }

    @Bean
    @Qualifier("txManager")
    public DataSourceTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "agreementTransactionTemplate")
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(txManager());
    }



    @Bean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(@Value("${mybatis.sql.print:false}") boolean printSql) {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
//        mybatisSqlSessionFactoryBean.setPlugins(new MybatisSqlExecutePlugin(printSql), paginationInterceptor());
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            mybatisSqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
        } catch (IOException e) {
            log.warn("mapper.xml配置出错");
        }
        return mybatisSqlSessionFactoryBean;
    }
}
