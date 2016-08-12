package com.betterjr.modules.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.order.dao.ScfOrderRelationMapper;
import com.betterjr.modules.order.entity.ScfOrderRelation;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;
import com.betterjr.modules.order.helper.ScfOrderInfoCheckFactory;
import com.betterjr.modules.order.helper.ScfOrderRelationType;

@Service
public class ScfOrderRelationService extends BaseService<ScfOrderRelationMapper, ScfOrderRelation> {

    @Autowired
    private ScfOrderService scfOrderCheckService;

    /**
     * 订单关联关系保存
     */
    public List<ScfOrderRelation> addOrderRelation(String anEnterType, Long anEnterId, String anInfoType, String anInfoIdList) {
        logger.info("Begin to add OrderRelation");
        List<ScfOrderRelation> anOrderRelationList = new ArrayList<ScfOrderRelation>();
        String[] anInfoIds = anInfoIdList.split(",");
        //检查相应单据是否存在
        IScfOrderInfoCheckService enterCheck = ScfOrderInfoCheckFactory.create(anEnterType);
        enterCheck.checkInfoExist(anEnterId, UserUtils.getOperatorInfo().getOperOrg());
        IScfOrderInfoCheckService infoCheck = ScfOrderInfoCheckFactory.create(anInfoType);
        for(String anInfoId : anInfoIds) {
            infoCheck.checkInfoExist(Long.valueOf(anInfoId), UserUtils.getOperatorInfo().getOperOrg());
        }
        //订单管理关系保存
        if(BetterStringUtils.equals(ScfOrderRelationType.ORDER.getCode(), anEnterType)) {
            for(String anInfoId : anInfoIds) {
                ScfOrderRelation anOrderRelation = new ScfOrderRelation();
                anOrderRelation.initAddValue();
                anOrderRelation.setOrderId(anEnterId);
                anOrderRelation.setInfoId(Long.valueOf(anInfoId));
                anOrderRelation.setInfoType(anInfoType);
                this.insert(anOrderRelation);
                anOrderRelationList.add(anOrderRelation);
            }
        }
        //选择订单关联
        else if (BetterStringUtils.equals(ScfOrderRelationType.ORDER.getCode(), anInfoType)){
            for(String anInfoId : anInfoIds) {
                ScfOrderRelation anOrderRelation = new ScfOrderRelation();
                anOrderRelation.initAddValue();
                anOrderRelation.setOrderId(Long.valueOf(anInfoId));
                anOrderRelation.setInfoId(anEnterId);
                anOrderRelation.setInfoType(anEnterType);
                this.insert(anOrderRelation);
                anOrderRelationList.add(anOrderRelation);
            }
        }
        //当两个id都不为订单时
        else{
            //根据enterId查询订单Id
            Map<String, Object> anMap = new HashMap<String, Object>();
            anMap.put("infoId", anEnterId);
            anMap.put("infoType", anEnterType);
            List<ScfOrderRelation> orderRelationList = this.selectByProperty(anMap);
            if(Collections3.isEmpty(orderRelationList)) {
                throw new BytterTradeException(40001, "请先选择订单进行关联");
            }
            //从enterId 来上溯到orderId
            Long anOrderId = Collections3.getFirst(orderRelationList).getId();
            for(String anInfoId : anInfoIds) {
                ScfOrderRelation anOrderRelation = new ScfOrderRelation();
                anOrderRelation.initAddValue();
                anOrderRelation.setOrderId(anOrderId);
                anOrderRelation.setInfoId(Long.valueOf(anInfoId));
                anOrderRelation.setInfoType(anInfoType);
                this.insert(anOrderRelation);
                anOrderRelationList.add(anOrderRelation);
            }
        }
        return anOrderRelationList;
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
