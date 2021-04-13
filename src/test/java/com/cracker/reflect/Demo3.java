package com.cracker.reflect;

import java.lang.reflect.Method;

/*
 通过Class对象获取到对应的方法。
 
 在反射技术中使用了Method类描述了方法的。
 
 */
public class Demo3 {
	
	public static void main(String[] args) throws Exception {
		//获取到对应的Class对象
		Class clazz = Class.forName("cn.itcast.Person");
		//获取到所有公共的方法
		/*Method[] methods = clazz.getMethods(); // getMethods() 获取所有 的公共方法而已。
		Method[] methods = clazz.getDeclaredMethods(); //获取到所有的方法，但是不包含父类的方法。
		for(Method method  : methods){
			System.out.println(method);
		}*/
		
		Person p = new Person(110,"狗娃");
		/*	
		Method m = clazz.getMethod("eat", int.class);
		m.invoke(p, 3); //invoke 执行一个方法。 第一个参数：方法的调用对象。 第二参数： 方法所需要的参数。
		
		
		//执行私有的方法
		Method m =clazz.getDeclaredMethod("sleep",int.class);
		//设置访问权限允许访问
		m.setAccessible(true);
		m.invoke(null, 6);*/
		
		Method m = clazz.getMethod("sum", int[].class);
		m.invoke(p,new int[]{12,5,9});
		
	}
	

}
