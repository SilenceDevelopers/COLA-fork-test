package com.alibaba.demo.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisruptorHandler {

    int order() default 0;  //数值越小，优先级越高
    boolean parallel() default false;   //同order内是否并行
}
