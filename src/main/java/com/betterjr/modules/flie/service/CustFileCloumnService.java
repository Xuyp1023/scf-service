package com.betterjr.modules.flie.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.modules.flie.dao.CustFileCloumnMapper;
import com.betterjr.modules.flie.data.FileResolveConstants;
import com.betterjr.modules.flie.entity.CustFileCloumn;

@Service
public class CustFileCloumnService extends BaseService<CustFileCloumnMapper, CustFileCloumn> {

    
    
    public List<CustFileCloumn> queryFileCloumnByInfoType(String anInfoType, String anUpFlag) {
        
        
        Map<String,Object> paramMap= QueryTermBuilder.newInstance()
                .put("infoType", anInfoType)
                .put("upFlag", anUpFlag)
                .put("businStatus", "0")
                .build();
        
        return this.selectByClassProperty(CustFileCloumn.class, paramMap, "cloumnOrder asc");
    }

    public List<CustFileCloumn> queryFileCloumnByInfoTypeIsMust(String anInfoType, String anUpFlag) {
        
        Map<String,Object> paramMap= QueryTermBuilder.newInstance()
                .put("infoType", anInfoType)
                .put("upFlag", anUpFlag)
                .put("businStatus", "0")
                .put("isMust", FileResolveConstants.RESOLVE_FILE_IS_MUST)
                .build();
        
        return this.selectByClassProperty(CustFileCloumn.class, paramMap, "cloumnOrder asc");
 
    }

}
