package com.betterjr.modules.order.service;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
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
}
