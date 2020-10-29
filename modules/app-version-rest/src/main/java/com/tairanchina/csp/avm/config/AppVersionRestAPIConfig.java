package com.tairanchina.csp.avm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
public class AppVersionRestAPIConfig {


    @Bean
    public Docket appVersionApiDoc() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("APP版本控制相关接口")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true).pathMapping("/").select()
                .paths(regex("/app/api/.*"))// 过滤的接口
                .build().apiInfo(apiInfo());
    }


    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("APP版本控制").description("服务端接口").version("1.0.0").termsOfServiceUrl("/doc.html").contact(new Contact("刘春","北京英孚泰克信息技术股份有限公司","itcchina.com")).license("北京英孚泰克信息技术股份有限公司").licenseUrl("http://www.itcchina.com/").build();
        return apiInfo;
    }
}
