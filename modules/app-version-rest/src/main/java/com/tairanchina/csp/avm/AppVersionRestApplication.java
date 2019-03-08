package com.tairanchina.csp.avm;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.tairanchina.csp")
@Configuration
@MapperScan("com.tairanchina.csp.avm.mapper")
public class AppVersionRestApplication {

    private static final Logger logger = LoggerFactory.getLogger(AppVersionRestApplication.class);

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @PostConstruct
    void started() {
        //设置时区为UTC格式
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(AppVersionRestApplication.class).run(args);
    }
}
