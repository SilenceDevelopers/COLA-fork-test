package com.alibaba.demo.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisruptorHandler {

    int order() default 0;
    boolean parallel() default false;
}
