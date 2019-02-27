package com.tairanchina.csp.avm.config;

import com.tairanchina.csp.avm.interceptor.AccessControlAllowInterception;
import com.tairanchina.csp.avm.interceptor.AdminInterceptor;
import com.tairanchina.csp.avm.interceptor.AppInterceptor;
import com.tairanchina.csp.avm.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by hzlizx on 2018/5/10 0010
 */
@Configuration
public class InterceptionConfig extends WebMvcConfigurerAdapter{

    @Autowired
    private AccessControlAllowInterception accessControlAllowInterception;

    @Autowired
    private UserInterceptor userInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Autowired
    private AppInterceptor appInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessControlAllowInterception)
                .addPathPatterns("/*")
                .addPathPatterns("/*/**");
        registry.addInterceptor(userInterceptor).addPathPatterns().excludePathPatterns(
                "/swagger-ui.html",
                "/swagger-resources",
                "/v2/api-docs",
                "/swagger-resources/configuration/ui",
                "/user/register",
                "/user/login"
        );
        registry.addInterceptor(adminInterceptor).addPathPatterns(
                "/admin/*",
                "/admin/*/**",
                "/log/*",
                "/log/*/**",
                "/chatbot/*",
                "/chatbot/*/**"
        ).excludePathPatterns(
                "/admin/isAdmin"
        );
        registry.addInterceptor(appInterceptor).addPathPatterns(
                "/ios",
                "/ios/*",
                "/ios/*/**",
                "/android",
                "/android/*",
                "/android/*/**",
                "/channel",
                "/channel/*",
                "/channel/*/**",
                "/route",
                "/route/*",
                "/route/*/**",
                "/package",
                "/package/*",
                "/package/*/**",
                "/capi",
                "/capi/*",
                "/capi/*/**",
                "/white",
                "/white/*",
                "/white/*/**"
        );
        super.addInterceptors(registry);
    }
}
