package com.cracker.di;

public class FBean {

    private String name;

    private int age;

    private ABean aBean;

    public FBean() {
    }

    public FBean(String name, int age, ABean aBean) {
        this.name = name;
        this.age = age;
        this.aBean = aBean;
    }

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

    public ABean getaBean() {
        return aBean;
    }

    public void setaBean(ABean aBean) {
        this.aBean = aBean;
    }
}