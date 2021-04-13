package com.cracker.reflect;

import java.lang.reflect.Constructor;

/*
 如何通过Class对象获取构造方法。
 */
public class Demo2 {
	
	public static void main(String[] args) throws Exception {
		//获取到对应的Class对象
		Class clazz = Class.forName("cn.itcast.Person");
		
		//通过Class对象获取对应的构造方法
		/*Constructor[] constructors = clazz.getConstructors();  // getConstructors()获取一个类的所有公共的构造方法
		for(Constructor constructor : constructors){
			System.out.println(constructor);
		}
		
		Constructor[] constructors =  clazz.getDeclaredConstructors(); //获取到一个类的所有构造方法，包括私有的在内 。
		for(Constructor constructor : constructors){
			System.out.println(constructor);
		}
		*/
		
		/*Constructor constructor = clazz.getConstructor(int.class,String.class);  // getConstructor 获取单个指定的构造方法。
		Person p  = (Person) constructor.newInstance(999,"小城"); // newInstance()创建一个对象
		System.out.println(p);*/
		
		
		
		//获取私有的构造函数
		Constructor constructor =  clazz.getDeclaredConstructor(null);
		//暴力反射
		constructor.setAccessible(true);
		Person p  =(Person) constructor.newInstance(null);
		System.out.println(p);
		
		
	}

}
