package com.tairanchina.csp.avm.annotation;


import com.tairanchina.csp.avm.entity.AccessLog;

import java.lang.annotation.*;


/**
 * 访问记录
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AccessRecord {

    String api();

    AccessLog.ApiDescription apiDescription();

}
