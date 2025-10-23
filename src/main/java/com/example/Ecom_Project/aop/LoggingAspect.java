package com.example.Ecom_Project.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.example.Ecom_Project.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("✅ [LOG] Calling method: " + joinPoint.getSignature());
    }
}