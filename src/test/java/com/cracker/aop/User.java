package com.cracker.aop;

public class User {

    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void init(){
        System.out.println("init...");
    }

    public void destory(){
        System.out.println("destory...");
    }

    @Override
    public String toString() {
        return name + ":" + age;
    }

    public void sayHello(){
        System.out.println("hello...");
    }
}
