package com.cracker.aop.pointcut.impl;

import com.cracker.aop.pointcut.RegexExpreseionPointCut;
import com.cracker.utils.RegexMatchUtils;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class RegexExpressionPointCutResolver implements RegexExpreseionPointCut {

    @Override
    public boolean matchClass(Class<?> targetClass, String expression) throws Exception {
        //表达式中的类名
        String className = RegexMatchUtils.matchClassName(expression);

        //将.转换为普通的字符
        expression.replace(".", "\\.");
        expression.replace("*", ".*");
        //全路径
        String name = targetClass.getName();
        boolean matches = Pattern.matches(className, name);
        return matches;
    }

    @Override
    public boolean matchMethod(Class<?> targetClass, Method method, String expresseion) throws Exception {
        boolean isMatch = matchClass(targetClass, expresseion);
        if(!isMatch){
            return false;
        }
//        String param = RegexMatchUtils.matchMethodParam(expresseion);
        String matchName = RegexMatchUtils.matchMethodName(expresseion);
        //仅匹配方法名
        String methodName = method.getName();
        //暂时只匹配方法名
        //参数的匹配比较麻烦 这里未实现
        if("*".equals(matchName)){
            return true;
        }
        return Pattern.matches(matchName, methodName);
    }

}
