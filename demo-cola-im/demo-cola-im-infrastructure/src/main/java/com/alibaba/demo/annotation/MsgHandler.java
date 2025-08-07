package com.alibaba.demo.annotation;

import com.alibaba.demo.enums.CmdEnum;
import com.google.protobuf.MessageLite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MsgHandler {

    CmdEnum cmd();

    Class<? extends MessageLite> message();
}
