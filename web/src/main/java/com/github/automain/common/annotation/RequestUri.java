package com.github.automain.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestUri {
    String value();// 请求地址
    String label() default "";// 请求需要的权限标识，为空时不限制权限
    String slave() default "";// 本次请求查询的从库名称，为空时查询默认主库
}
