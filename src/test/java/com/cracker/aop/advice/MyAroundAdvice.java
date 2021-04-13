package com.cracker.aop.advice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyAroundAdvice implements com.cracker.aop.advice.AroundAdvice {

    @Override
    public Object around(Method method, Object[] args, Object target) throws InvocationTargetException, IllegalAccessException {
        System.out.println("before...");
        Object invoke = method.invoke(target, args);
        System.out.println("after...");
        return invoke;
    }
}
