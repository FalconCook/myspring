package com.cracker.aop.pointcut;

import java.lang.reflect.Method;

public interface PointCut {
    //提供两个方法,匹配类和匹配方法
    boolean matchClass(Class<?> targetClass, String expression) throws Exception;
    boolean matchMethod(Class<?> targetClass, Method method, String expression) throws Exception;
}
