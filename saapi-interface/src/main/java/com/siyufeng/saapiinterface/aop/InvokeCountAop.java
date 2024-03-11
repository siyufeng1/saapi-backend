package com.siyufeng.saapiinterface.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author 司雨枫
 * 调用次数切面
 */
@Aspect
@Component
public class InvokeCountAop {
    //切点
    @Pointcut("execution(* com.siyufeng.saapiinterface.controller.*.*(..))")
    public void pointcut() {
    }

    @AfterReturning("pointcut()")
    public void afterReturning(JoinPoint joinPoint) {
        String name = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("调用次数" + "+1");
    }
}
