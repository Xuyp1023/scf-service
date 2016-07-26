package com.betterjr.modules.order.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.order.dao.ScfTransportMapper;
import com.betterjr.modules.order.entity.ScfTransport;

@Service
public class ScfTransportService extends BaseService<ScfTransportMapper, ScfTransport> {
    
    /**
     * 订单运输单据录入
     */
    public ScfTransport addTransport(ScfTransport anTransport) {
        logger.info("Begin to add Product");
        //初始化系统信息
        anTransport.initAddValue();
        this.insert(anTransport);
        return anTransport;
    }

    /**
     * 订单运输单据分页查询
     */
    public Page queryTransport(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        //操作员只能查询本机构数据
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        
        Page<ScfTransport> anTransportList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
        
        return anTransportList;
    }
}
