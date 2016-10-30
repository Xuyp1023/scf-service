package com.betterjr.modules.push.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.betterjr.common.service.BaseService;
import com.betterjr.modules.push.dao.ScfSupplierPushDetailMapper;
import com.betterjr.modules.push.entity.ScfSupplierPushDetail;

@Service
public class ScfSupplierPushDetailService extends BaseService<ScfSupplierPushDetailMapper, ScfSupplierPushDetail> {

    private static final Logger logger = LoggerFactory.getLogger(ScfSupplierPushDetailService.class);
    
    public boolean addPushDetail(ScfSupplierPushDetail scfPushDetail){
        return true;
//      return this.insert(scfPushDetail)>0;  
    }
    
}
