package com.betterjr.modules.commission.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.commission.dao.CommissionFileDownMapper;
import com.betterjr.modules.commission.entity.CommissionFile;
import com.betterjr.modules.commission.entity.CommissionFileDown;

@Service
public class CommissionFileDownService extends BaseService<CommissionFileDownMapper,CommissionFileDown>{

    
public Page queryFileDownList(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        
        BTAssert.notNull(anMap,"查询佣金文件条件为空");
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        //查询当前公司的佣金文件
        //anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        
        Page<CommissionFileDown> fileList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
       
        return fileList;
    }
}
