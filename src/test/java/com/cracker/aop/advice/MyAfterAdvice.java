package com.cracker.aop.advice;

import java.lang.reflect.Method;

public class MyAfterAdvice implements com.cracker.aop.advice.AfterAdvice {
    @Override
    public void after(Method method, Object[] args, Object target, Object returnVal) {
        System.out.println("after...");
    }
}
