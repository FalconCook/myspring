package com.cracker.aop;


public class TestFactory {
    public Object createMethod(){
        return new User();
    }

    public static Object staticCreateMethod(){
        return new User();
    }
}
