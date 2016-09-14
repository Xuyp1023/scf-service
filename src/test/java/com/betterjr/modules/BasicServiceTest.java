package com.betterjr.modules;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public abstract class BasicServiceTest<T> {
	
	private static ClassPathXmlApplicationContext ctx;

	@BeforeClass
	public static void startSpring() {
		String[] configFiles = new String[] { "spring-context-scf-dubbo-provider.xml" };
		ctx = new ClassPathXmlApplicationContext(configFiles);
		ctx.start();
	}

	@AfterClass
	public static void closeSpring() {
		if (ctx != null) {
			ctx.close();
		}
	}
	
	public T getServiceObject(){
		T service = (T) this.getCtx().getBean(this.getTargetServiceClass());
		return service;
	}
	
	public abstract Class<T> getTargetServiceClass();

	public ClassPathXmlApplicationContext getCtx() {
		return ctx;
	}
	
	

}
