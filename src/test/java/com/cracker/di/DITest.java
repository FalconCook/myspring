package com.cracker.di;

import com.cracker.core.BeanReference;
import com.cracker.core.beandefinition.DefaultBeanDefinition;
import com.cracker.core.factory.DefaultBeanFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DITest {
    static DefaultBeanFactory bf = new DefaultBeanFactory();

    @Test
    public void testConstructorDI() throws Exception {

        DefaultBeanDefinition bd = new DefaultBeanDefinition();
        bd.setBeanClass(ABean.class);
        List<Object> args = new ArrayList<>();
        args.add("abean");
        args.add(new BeanReference("cbean"));
        bd.setConstructorArgument(args);
        bf.registerBeanDefinition("abean", bd);

        bd = new DefaultBeanDefinition();
        bd.setBeanClass(CBean.class);
        args = new ArrayList<>();
        args.add("cbean");
        bd.setConstructorArgument(args);
        bf.registerBeanDefinition("cbean", bd);

        ABean abean = (ABean) bf.getBean("abean");

        abean.doSomthing();
    }

    @Test
    public void testStaticFactoryMethodDI() throws Exception {

        DefaultBeanDefinition bd = new DefaultBeanDefinition();
        bd.setBeanClass(ABeanFactory.class);
        bd.setFactoryMethodName("getABean");
        List<Object> args = new ArrayList<>();
        args.add("abean02");
        args.add(new BeanReference("cbean02"));
        bd.setConstructorArgument(args);
        bf.registerBeanDefinition("abean02", bd);

        bd = new DefaultBeanDefinition();
        bd.setBeanClass(CBean.class);
        args = new ArrayList<>();
        args.add("cbean02");
        bd.setConstructorArgument(args);
        bf.registerBeanDefinition("cbean02", bd);

        ABean abean = (ABean) bf.getBean("abean02");

        abean.doSomthing();
    }

    @Test
    public void testFactoryMethodDI() throws Exception {

        DefaultBeanDefinition bd = new DefaultBeanDefinition();
        bd.setFactoryBeanName("abeanFactory");
        bd.setFactoryMethodName("getABean2");
        List<Object> args = new ArrayList<>();
        args.add("abean03");
        args.add(new BeanReference("cbean02"));
        bd.setConstructorArgument(args);
        bf.registerBeanDefinition("abean03", bd);

        bd = new DefaultBeanDefinition();
        bd.setBeanClass(ABeanFactory.class);
        bf.registerBeanDefinition("abeanFactory", bd);

        bd = new DefaultBeanDefinition();
        bd.setBeanClass(CBean.class);
        args = new ArrayList<>();
        args.add("cbean02");
        bd.setConstructorArgument(args);
        bf.registerBeanDefinition("cbean02", bd);

        ABean abean = (ABean) bf.getBean("abean03");

        abean.doSomthing();
    }

    @Test
    public void testChildTypeDI() throws Exception {

        DefaultBeanDefinition bd = new DefaultBeanDefinition();
        bd.setBeanClass(ABean.class);
        List<Object> args = new ArrayList<>();
        args.add("abean04");
        args.add(new BeanReference("ccbean01"));
        bd.setConstructorArgument(args);
        bf.registerBeanDefinition("abean04", bd);

        bd = new DefaultBeanDefinition();
        bd.setBeanClass(CCBean.class);
        args = new ArrayList<>();
        args.add("Ccbean01");
        bd.setConstructorArgument(args);
        bf.registerBeanDefinition("ccbean01", bd);

        ABean abean = (ABean) bf.getBean("abean04");

        abean.doSomthing();
    }
}
