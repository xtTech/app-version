package com.tairanchina.csp.avm.annotation;

import com.tairanchina.csp.avm.entity.OperationRecordLog;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface OperationRecord {

    OperationRecordLog.OperationType type();

    OperationRecordLog.OperationResource resource();

    OperationRecordLog.OperationDescription description();

}
