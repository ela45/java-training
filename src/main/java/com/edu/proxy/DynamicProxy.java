package com.edu.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.log4j.Logger;




public class DynamicProxy implements InvocationHandler{
	private final static Logger LOGGER = Logger.getLogger(DynamicProxy.class);

	private Object proxyObject;
	
	private DynamicProxy(Object obj) {
		this.proxyObject=obj;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T newInstanceT(Class<T> clazz) throws InstantiationException, IllegalAccessException {
		Object proxyObject=clazz.newInstance();
		DynamicProxy dynamicProxy=new DynamicProxy(proxyObject);
		
		return (T) Proxy.newProxyInstance(DynamicProxy.class.getClassLoader(), clazz.getInterfaces(), dynamicProxy);
		
	}
	public static Object newInstance(Object obj) {
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
				obj.getClass().getInterfaces(), new DynamicProxy(obj));
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		LOGGER.info("Doing some security things before calling method...."+method.getName());
		
		LOGGER.info("Calling method..."+method.getName());
		
		Object result=method.invoke(this.proxyObject, args);
		
		LOGGER.info("Doing some  things after calling method...."+method.getName());
		
		
		return result;
	}
	
	
}
