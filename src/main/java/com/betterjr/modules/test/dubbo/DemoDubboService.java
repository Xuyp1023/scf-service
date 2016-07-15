package com.betterjr.modules.test.dubbo;

import java.util.Map;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.test.IDemoService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;
import com.betterjr.modules.sys.security.ShiroUser;
import com.betterjr.modules.test.entity.TestEntityObject;
import com.betterjr.modules.test.service.DemoService;
import com.jarvis.lib.util.BeanUtil;

@Service(interfaceClass=IDemoService.class)
public class DemoDubboService implements IDemoService{
	
	Logger logger=LoggerFactory.getLogger(DemoDubboService.class);

	@Autowired
	private DemoService demo;
	@Override
	public String webQueryEntity(Map<String, String> map, String name) {
		// TODO Auto-generated method stub
		logger.debug("test dubbo case,input="+name+","+map);
		
		Session session=UserUtils.getSession();
		if(session!=null){
			for(Object obj:session.getAttributeKeys()){
				logger.debug("test dubbo case,session attrs:"+session.getAttribute(obj)+",KEY="+obj);
			}
		}
		
		ShiroUser user=UserUtils.getPrincipal();
		if(user!=null)
			logger.debug("test dubbo case,ShiroUser="+BeanUtil.toString(user));
		
		TestEntityObject obj=(TestEntityObject)RuleServiceDubboFilterInvoker.getInputObj();
		if(obj!=null)
			logger.debug("test dubbo case,entity obj="+BeanUtil.toString(obj));
		//调用本地service
		
		String result= demo.queryTestEntity(obj);
		return new AjaxObject(result).toJson();
	}
	
	public void webAddEntity(Map<String, String> map, String name){
		TestEntityObject obj=(TestEntityObject)RuleServiceDubboFilterInvoker.getInputObj();
		if(obj!=null){
			logger.debug("test dubbo case,entity obj="+BeanUtil.toString(obj));
			demo.addTestEntity(obj);
		}else{
			logger.error("test dubbo case,entity obj=null");
		}
	}

}
