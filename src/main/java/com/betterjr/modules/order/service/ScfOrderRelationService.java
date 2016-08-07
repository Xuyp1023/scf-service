package com.betterjr.modules.order.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.order.dao.ScfOrderRelationMapper;
import com.betterjr.modules.order.entity.ScfOrderRelation;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;
import com.betterjr.modules.order.helper.ScfOrderInfoCheckFactory;

@Service
public class ScfOrderRelationService extends BaseService<ScfOrderRelationMapper, ScfOrderRelation> {

    @Autowired
    private ScfOrderService scfOrderCheckService;

    /**
     * 订单关联关系保存
     */
    public ScfOrderRelation addOrderRelation(ScfOrderRelation anOrderRelation) {
        logger.info("Begin to add OrderRelation");
        // 检查相应id是否存在
        scfOrderCheckService.checkInfoExist(anOrderRelation.getOrderId(), UserUtils.getOperatorInfo().getOperOrg());
        IScfOrderInfoCheckService orderInfoCheckService = ScfOrderInfoCheckFactory.create(anOrderRelation.getInfoType());
        orderInfoCheckService.checkInfoExist(anOrderRelation.getInfoId(), UserUtils.getOperatorInfo().getOperOrg());
        anOrderRelation.initAddValue();
        this.insert(anOrderRelation);
        return anOrderRelation;
    }
    
    /**
     * 订单关系删除
     */
    public int saveDeleteOrderRelation(Long anId) {
        logger.info("Begin to Delete OrderRelation");
        
        ScfOrderRelation anOrderRelation = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anOrderRelation, "无法获取订单关联信息");
        //检查当前操作员是否有权限删除
        scfOrderCheckService.checkInfoExist(anOrderRelation.getOrderId(), UserUtils.getOperatorInfo().getOperOrg());
        //数据存盘
        return this.deleteByPrimaryKey(anId);
    }
    
    /**
     * 查询订单下面的关联关系列表
     */
    public List<ScfOrderRelation> findOrderRelation(Map<String, Object> anMap) {
        return this.selectByClassProperty(ScfOrderRelation.class, anMap);
    }
}
