//package com.alibaba.demo.aspect;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shardingsphere.transaction.api.TransactionType;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//
//@Component
//@Aspect
//// 如果一个方法被多个aop增强, 可以用@Order来控制增强顺序, 值越小优先级越高
//@Order(-1)
//@Slf4j
//public class ShardingTransactionAspect {
//    @Pointcut("@annotation(org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType)")
//    public void pointCut() {
//    }
//
//    @Before("pointCut()")
//    public void doBefore(JoinPoint point) {
//        // 优先获取方法上的注解，如果方法上没有注解，则获取类上的注解
//        MethodSignature signature = (MethodSignature) point.getSignature();
//        Method method = signature.getMethod();
//        TransactionType transactionType = method.getAnnotation(TransactionType.class);
//        if (transactionType == null) {
//            transactionType = point.getTarget().getClass().getAnnotation(TransactionType.class);
//        }
//        if (transactionType == null) {
//            log.warn("无法获取ShardingSphere分布式事务类型");
//            return;
//        }
//        TransactionTypeHolder.set(transactionType.value());
//        log.info("设置ShardingSphere分布式事务类型为：" + transactionType.value().toString());
//    }
//
//    @After("pointCut()")
//    public void doAfter() {
//        TransactionTypeHolder.clear();
//    }
//}
