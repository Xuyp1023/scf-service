package com.betterjr.modules;

import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public abstract class BasicServiceTest<T> {
	
	private ClassPathXmlApplicationContext ctx;

	@Before
	public void startSpring() {
		String[] configFiles = new String[] { "spring-context-scf-service.xml" };
		ctx = new ClassPathXmlApplicationContext(configFiles);
		ctx.start();
	}

	@After
	public void closeSpring() {
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
