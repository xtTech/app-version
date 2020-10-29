package com.tairanchina.csp.avm.config;

import com.tairanchina.csp.avm.interceptor.AccessControlAllowInterception;
import com.tairanchina.csp.avm.interceptor.AdminInterceptor;
import com.tairanchina.csp.avm.interceptor.AppInterceptor;
import com.tairanchina.csp.avm.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by hzlizx on 2018/5/10 0010
 */
@Configuration
public class AppVersionManagerWebConfig extends WebMvcConfigurerAdapter{

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
            "/user/login",
            "/apk/downloadFile/**/**/**.**"
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

    /****
     * Tomcat配置虚拟目录
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/app_version_apk/**").addResourceLocations("file:D:/uploadFile/");
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                //ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
                // ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
                container.addErrorPages( error404Page);
            }
        };
    }
}
