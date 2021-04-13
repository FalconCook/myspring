package com.cracker.core.factory;


import com.cracker.core.BeanReference;
import com.cracker.core.PropertyValue;
import com.cracker.core.aware.BeanFactoryAware;
import com.cracker.core.beandefinition.BeanDefinition;
import com.cracker.core.beandefinition.BeanDefinitionRegistry;
import com.cracker.core.postprocessor.AopPostProcessor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Cracker
 */
public class DefaultBeanFactory implements BeanFactory, BeanDefinitionRegistry, Closeable {

    private final Log log = LogFactory.getLog(this.getClass());

    private Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    private Map<String,Object> beanMap = new ConcurrentHashMap<>(256);

    //记录正在创建的bean
    private ThreadLocal<Set<String>> buildingBeans = new ThreadLocal<>();

    //private List<BeanPostProcessor> beanPostProcessors = Collections.synchronizedList(new ArrayList<>());

    //记录观察者
    private List<AopPostProcessor> aopPostProcessors = new ArrayList<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception{
        //参数检查
        Objects.requireNonNull(beanName,"注册bean需要输入beanName");
        Objects.requireNonNull(beanDefinition,"注册bean需要输入beanDefinition");

        //检验给入的bean是否合法
        if (!beanDefinition.validate()){
            throw new Exception("名字为["+beanName+"]的bean定义不合法,"+beanDefinition);
        }

        if (this.containsBeanDefinition(beanName)){
            throw new Exception("名字为["+beanName+"]的bean定义已经存在,"+this.getBeanDefinition(beanName));
        }

        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitionMap.get(beanName);
    }

    @Override
    public Boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        return this.doGetBean(beanName);
    }

    @Override
    public void registerBeanPostProcessor(AopPostProcessor processor) {
        aopPostProcessors.add(processor);
    }

    //不需要判断scope,因为只有单例bean才需要放入map中
    //使用protected保证只有DefaultBeanFactory的子类可以调用该方法
    public Object doGetBean(String beanName) throws Exception{
        Objects.requireNonNull(beanName,"beanName不能为空");

        // 记录正在创建的Bean
        Set<String> ingBeans = this.buildingBeans.get();

        if (ingBeans == null) {
            ingBeans = new HashSet<>();
            this.buildingBeans.set(ingBeans);
        }

        // 检测循环依赖
        if (ingBeans.contains(beanName)) {
            throw new Exception("检测到" + beanName + "存在循环依赖：" + ingBeans);
        }

        // 记录正在创建的Bean
        ingBeans.add(beanName);

        Object instance = beanMap.get(beanName);

        if (instance != null){
            return instance;
        }



        BeanDefinition beanDefinition = this.getBeanDefinition(beanName);
        Objects.requireNonNull(beanDefinition,"beanDefinition不能为空");

        Class<?> type = beanDefinition.getBeanClass();
        //因为总共就只有3种方式,也不需要扩充或者是修改代码了,所以就不需要考虑使用策略模式了
        if (type != null){
            if (StringUtils.isBlank(beanDefinition.getFactoryMethodName())){
                instance = this.createInstanceByConstructor(beanDefinition);
            } else {
                instance = this.createInstanceByStaticFactoryMethod(beanDefinition);
            }
        }else {
            instance = this.createInstanceByFactoryBean(beanDefinition);
        }

        this.doInit(beanDefinition,instance);

        // 创建好实例后，移除创建中记录
        ingBeans.remove(beanName);

        // 给入属性依赖
        this.setPropertyDIValues(beanDefinition, instance);

        //添加aop处理
        instance = this.applyAopBeanPostProcessor(instance, beanName);

        if (beanDefinition.isSingleton()){
            beanMap.put(beanName,instance);
        }

        return instance;
    }




    //修改后的构造方法
    private Object createInstanceByConstructor(BeanDefinition beanDefinition) throws Exception {
        try {
            //获取真正的参数值
            Object[] args = this.getConstructorArgument(beanDefinition);
            if (args == null) {
                return beanDefinition.getBeanClass().newInstance();
            } else {
                // 决定构造方法
                return this.determineConstructor(beanDefinition, args).newInstance(args);
            }
        } catch (SecurityException e1) {
            log.error("创建bean的实例异常,beanDefinition：" + beanDefinition, e1);
            throw e1;
        }
    }

    //修改后的静态工厂方法
    private Object createInstanceByStaticFactoryMethod(BeanDefinition beanDefinition) throws Exception{
        Class<?> type = beanDefinition.getBeanClass();
        Object[] realArgs = this.getRealValues(beanDefinition.getConstructorArgument());
        Method m = this.determineFactoryMethod(beanDefinition, realArgs, null);
        return m.invoke(type, realArgs);
    }

    //修改后的工厂bean方法
    private Object createInstanceByFactoryBean(BeanDefinition beanDefinition) throws Exception{
        Object factoryBean = this.doGetBean(beanDefinition.getFactoryBeanName());
        Object[] realArgs = this.getRealValues(beanDefinition.getConstructorArgument());
        Method m = this.determineFactoryMethod(beanDefinition, realArgs, factoryBean.getClass());
        return m.invoke(factoryBean, realArgs);
    }

    //初始化方法
    private void doInit(BeanDefinition beanDefinition, Object instance) throws Exception{
        if (StringUtils.isNotBlank(beanDefinition.getBeanInitMethodName())){
            Method method = instance.getClass().getMethod(beanDefinition.getBeanInitMethodName(),new Class<?>[]{});
            method.invoke(instance,new Object[]{});
        }
    }

    private Object applyAopBeanPostProcessor(Object instance, String beanName) throws Exception {
        for(AopPostProcessor postProcessor: aopPostProcessors){
            instance = postProcessor.postProcessWeaving(instance, beanName);
        }
        return instance;
    }

    private Object[] getConstructorArgument(BeanDefinition beanDefinition) throws Exception{
        return this.getRealValues(beanDefinition.getConstructorArgument());
    }

    private Object[] getRealValues(List<?> defs) throws Exception{
        if (CollectionUtils.isEmpty(defs)){return null;}
        Object[] values = new Object[defs.size()];
        int i = 0;
        //values数组的元素
        Object value = null;
        for (Object realValue : defs){
            if (realValue == null){
                value = null;
            }else if (realValue instanceof BeanReference){
                value = this.doGetBean(((BeanReference) realValue).getBeanName());
            }else {value = realValue;}
            values[i++] = value;
        }
        return values;
    }

    //查找构造方法的方法
    private Constructor determineConstructor(BeanDefinition beanDefinition, Object[] args) throws Exception{
        Constructor constructor = null;

        //当没有任何一个参数时直接获取无参构造方法
        if (args == null){
            return beanDefinition.getBeanClass().getConstructor(new Class<?>[]{});
        }

        //对于原型bean,第二次开始获取Bean实例时,可直接获取第一次缓存的构造方法
        constructor = beanDefinition.getConstructor();
        if (constructor != null){
            return constructor;
        }

        //根据参数类型获取精确匹配的构造方法
        Class<?>[] paramTypes = new Class[args.length];
        int j = 0;
        for (Object paramType : args){
            paramTypes[j++] = paramType.getClass();
        }
        try {
            constructor = beanDefinition.getConstructor();
        }catch (Exception e){
            //此异常不需要进行处理
        }

        if (constructor == null){
            //把所有的构造器全部遍历出来一一比对
            Outer: for (Constructor<?> allConstructor : beanDefinition.getBeanClass().getConstructors()){
                Class<?>[] pTypes = allConstructor.getParameterTypes();
                //此构造方法的参数长度等于提供参数长度
                if (pTypes.length == args.length){
                    for (int i = 0;i<pTypes.length;i++){

                        //如果第一个参数的类型就已经不匹配了,就直接不再继续比对了,直接跳转到外循环
                        if (!pTypes[i].isAssignableFrom(args[i].getClass())){
                            continue Outer;
                        }
                    }

                    //如果以上皆匹配的话,就直接获取到这个构造器,然后直接让循环终止
                    constructor = allConstructor;
                    break Outer;
                }
            }
        }

        if (constructor != null){
            if (beanDefinition.isPrototype()){
                //对原型bean构造器进行缓存方便下次查找
                beanDefinition.setConstructor(constructor);
            }
            return constructor;
        }else {
            throw new Exception("不存在对应的构造方法!"+beanDefinition);
        }
    }

    //工厂查找方法
    private Method determineFactoryMethod(BeanDefinition bd, Object[] args, Class<?> type) throws Exception {
        if (type == null) {
            type = bd.getBeanClass();
        }
        String methodName = bd.getFactoryMethodName();
        if (args == null) {
            return type.getMethod(methodName,new Class<?>[]{});
        }
        Method m = null;
        // 对于原型bean,从第二次开始获取bean实例时，可直接获得第一次缓存的构造方法。
        m = bd.getFactoryMethod();
        if (m != null) {
            return m;
        }
        // 根据参数类型获取精确匹配的方法
        Class[] paramTypes = new Class[args.length];
        int j = 0;
        for (Object p : args) {
            paramTypes[j++] = p.getClass();
        }
        try {
            m = type.getMethod(methodName, paramTypes);
        } catch (Exception e) {
            // 这个异常不需要处理
        }
        if (m == null) {
            // 没有精确参数类型匹配的，则遍历匹配所有的方法
            // 判断逻辑：先判断参数数量，再依次比对形参类型与实参类型
            outer: for (Method m0 : type.getMethods()) {
                if (!m0.getName().equals(methodName)) {
                    continue;
                }
                Class<?>[] paramterTypes = m.getParameterTypes();
                if (paramterTypes.length == args.length) {
                    for (int i = 0; i < paramterTypes.length; i++) {
                        if (!paramterTypes[i].isAssignableFrom(args[i].getClass())) {
                            continue outer;
                        }
                    }
                    m = m0;
                    break outer;
                }
            }
        }
        if (m != null) {
            // 对于原型bean,可以缓存找到的方法，方便下次构造实例对象。在BeanDefinfition中获取设置所用方法的方法。
            if (bd.isPrototype()) {
                bd.setFactoryMethod(m);
            }
            return m;
        } else {
            throw new Exception("不存在对应的构造方法！" + bd);
        }
    }

    private void setPropertyDIValues(BeanDefinition bd, Object instance) throws Exception {
        if (CollectionUtils.isEmpty(bd.getPropertyValues())) {
            return;
        }
        for (PropertyValue pv : bd.getPropertyValues()) {
            if (StringUtils.isBlank(pv.getName())) {
                continue;
            }
            Class<?> clazz = instance.getClass();
            Field p = clazz.getDeclaredField(pv.getName());

            p.setAccessible(true);

            Object rv = pv.getValue();
            Object v = null;
            if (rv == null) {
                v = null;
            } else if (rv instanceof BeanReference) {
                v = this.doGetBean(((BeanReference) rv).getBeanName());
            } else if (rv instanceof Object[]) {
                // TODO 处理集合中的bean引用
            } else if (rv instanceof Collection) {
                // TODO 处理集合中的bean引用
            } else if (rv instanceof Properties) {
                // TODO 处理properties中的bean引用
            } else if (rv instanceof Map) {
                // TODO 处理Map中的bean引用
            } else {
                v = rv;
            }

            p.set(instance, v);

        }
    }

    @Override
    public void close() throws IOException {

    }
}
