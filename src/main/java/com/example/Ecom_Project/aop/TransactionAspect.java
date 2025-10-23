package com.example.Ecom_Project.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionAspect {

    @Before("execution(* com.example.Ecom_Project.service.*.*(..))")
    public void startTransaction() {
        System.out.println("💰 [TRANSACTION] Transaction started");
    }

    @AfterReturning("execution(* com.example.Ecom_Project.service.*.*(..))")
    public void commitTransaction() {
        System.out.println("💰 [TRANSACTION] Transaction committed");
    }

    @AfterThrowing("execution(* com.example.Ecom_Project.service.*.*(..))")
    public void rollbackTransaction() {
        System.out.println("💰 [TRANSACTION] Transaction rolled back due to error");
    }
}