package com.betterjr.modules.busintype.Service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.busintype.dao.ScfBusinTypeMapper;
import com.betterjr.modules.busintype.entity.ScfBusinType;

@Service
public class ScfBusinTypeService extends BaseService<ScfBusinTypeMapper, ScfBusinType>{

    
    public List<ScfBusinType> queryEffectiveBusinType(Map<String,Object> anParamMap){
        
        anParamMap=Collections3.filterMap(anParamMap, new String[]{"businStatus","id","businTypeName"});
        
        if(anParamMap !=null && !anParamMap.containsKey("businStatus")){
            
            anParamMap.put("businStatus", "1");
        }
        List<ScfBusinType> list = this.selectByProperty(anParamMap);
        return list;
        
    }
}
