package com.cracker.aop.advice;

import java.lang.reflect.Method;

public class MyBeforeAdvice implements com.cracker.aop.advice.BeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println("before...");
    }
}
