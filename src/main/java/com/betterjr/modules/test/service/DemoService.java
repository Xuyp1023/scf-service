package com.betterjr.modules.test.service;

import java.util.List;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.StaticThreadLocal;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.sys.security.ShiroUser;
import com.betterjr.modules.test.dao.TestEntityObjectMapper;
import com.betterjr.modules.test.entity.TestEntityObject;
import com.jarvis.lib.util.BeanUtil;


@Service
public class DemoService extends BaseService<TestEntityObjectMapper, TestEntityObject>{
	Logger logger=LoggerFactory.getLogger(DemoService.class);
	
	public String queryTestEntity(TestEntityObject obj) {
		// TODO Auto-generated method stub
		Map map=StaticThreadLocal.getDubboMethodParaMap();
		
		for(Object key:map.keySet()){
			logger.debug("test dubbo case:local service,input="+key+":"+map.get(key));
		}
		
		Session session=UserUtils.getSession();
		ShiroUser user=UserUtils.getPrincipal();
		
		if("admin123456".equalsIgnoreCase(obj.getName())){
			throw new BytterTradeException("业务异常");
		}
		
		List<TestEntityObject> re=this.select(obj);
		logger.debug("test dubbo case:local service,selected result:"+re);
		
		return "test dubbo case,input="+map+",session="+BeanUtil.toString(session)+",entity="+BeanUtil.toString(re);
	}
	
	public void addTestEntity(TestEntityObject anRecord){
		logger.debug("test dubbo case:local service,input:"+BeanUtil.toString(anRecord));
		this.insert(anRecord);
//		logger.debug("test dubbo case:local service,input2:"+BeanUtil.toString(anRecord));
//		this.insert(anRecord);
	}



}
