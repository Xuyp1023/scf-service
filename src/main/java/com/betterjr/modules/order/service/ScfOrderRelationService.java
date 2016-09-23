package com.betterjr.modules.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.order.dao.ScfOrderRelationMapper;
import com.betterjr.modules.order.entity.ScfOrderRelation;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;
import com.betterjr.modules.order.helper.ScfOrderInfoCheckFactory;
import com.betterjr.modules.order.helper.ScfOrderRelationType;

@Service
public class ScfOrderRelationService extends BaseService<ScfOrderRelationMapper, ScfOrderRelation> {

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
                anOrderRelation.setOrderId(anEnterId);
                anOrderRelation.setInfoId(Long.valueOf(anInfoId));
                anOrderRelation.setInfoType(anInfoType);
                //检查关系是否存在，若存在则不再添加
                if(checkRelationExists(anOrderRelation.getOrderId(), anOrderRelation.getInfoId(), anOrderRelation.getInfoType())) {
                    continue;
                }
                anOrderRelation.initAddValue();
                this.insert(anOrderRelation);
                anOrderRelationList.add(anOrderRelation);
            }
        }
        //选择订单关联
        else if (BetterStringUtils.equals(ScfOrderRelationType.ORDER.getCode(), anInfoType)){
            for(String anInfoId : anInfoIds) {
                ScfOrderRelation anOrderRelation = new ScfOrderRelation();
                anOrderRelation.setOrderId(Long.valueOf(anInfoId));
                anOrderRelation.setInfoId(anEnterId);
                anOrderRelation.setInfoType(anEnterType);
                //检查关系是否存在，若存在则不再添加
                if(checkRelationExists(anOrderRelation.getOrderId(), anOrderRelation.getInfoId(), anOrderRelation.getInfoType())) {
                    continue;
                }
                anOrderRelation.initAddValue();
                this.insert(anOrderRelation);
                anOrderRelationList.add(anOrderRelation);
            }
        }
        //当两个id都不为订单时
        else{
            //从enterId 来上溯到orderId
            Map<String, Object> anMap = new HashMap<String, Object>();
            anMap.put("infoId", anEnterId);
            anMap.put("infoType", anEnterType);
            List<ScfOrderRelation> orderRelationList = this.selectByProperty(anMap);
            if(Collections3.isEmpty(orderRelationList)) {
                throw new BytterTradeException(40001, "请先选择订单进行关联");
            }
            //将上溯出来的订单与所选信息关联
            Long anOrderId = Collections3.getFirst(orderRelationList).getOrderId();
            for(String anInfoId : anInfoIds) {
                ScfOrderRelation anOrderRelation = new ScfOrderRelation();
                anOrderRelation.setOrderId(anOrderId);
                anOrderRelation.setInfoId(Long.valueOf(anInfoId));
                anOrderRelation.setInfoType(anInfoType);
                //检查关系是否存在，若存在则不再添加
                if(checkRelationExists(anOrderRelation.getOrderId(), anOrderRelation.getInfoId(), anOrderRelation.getInfoType())) {
                    continue;
                }
                anOrderRelation.initAddValue();
                this.insert(anOrderRelation);
                anOrderRelationList.add(anOrderRelation);
            }
        }
        return anOrderRelationList;
    }
    
    /**
     * 订单关系删除
     */
    public int saveDeleteOrderRelation(String anEnterType, Long anEnterId, String anInfoType, Long anInfoId) {
        logger.info("Begin to Delete OrderRelation");
        
        //检查相应id内容是否存在
        IScfOrderInfoCheckService enterCheck = ScfOrderInfoCheckFactory.create(anEnterType);
        enterCheck.checkInfoExist(anEnterId, UserUtils.getOperatorInfo().getOperOrg());
        IScfOrderInfoCheckService infoCheck = ScfOrderInfoCheckFactory.create(anInfoType);
        infoCheck.checkInfoExist(Long.valueOf(anInfoId), UserUtils.getOperatorInfo().getOperOrg());
        Map<String, Object> anMap = new HashMap<String, Object>();
        int count = 0;
        //订单管理进入
        if(ScfOrderRelationType.ORDER.getCode().equals(anEnterType)) {
            anMap.put("orderId", anEnterId);
            anMap.put("infoType", anInfoType);
            anMap.put("infoId", anInfoId);
            return this.deleteByExample(anMap);
        }
        //非订单管理，保存订单的关联关系
        else if(ScfOrderRelationType.ORDER.getCode().equals(anInfoType)) {
            anMap.put("orderId", anInfoId);
            anMap.put("infoType", anEnterType);
            anMap.put("infoId", anEnterId);
            return this.deleteByExample(anMap);
        }
        //非订单管理，保存非订单关联关系
        else {
            Map<String, Object> anEnterMap = new HashMap<String, Object>();
            Map<String, Object> anInfoMap = new HashMap<String, Object>();
            anEnterMap.put("infoId", anEnterId);
            anEnterMap.put("infoType", anEnterType);
            anInfoMap.put("infoId", anInfoId);
            anInfoMap.put("infoType", anInfoType);
            //删除记录
            List<ScfOrderRelation> anEnterRelationList = this.selectByProperty(anEnterMap); 
            List<ScfOrderRelation> anInfoRelationList = this.selectByProperty(anInfoMap); 
            for(ScfOrderRelation anEnterRelation : anEnterRelationList) {
                for(ScfOrderRelation anInfoRelation : anInfoRelationList) {
                    if(anEnterRelation.getOrderId() == anInfoRelation.getOrderId()) {
                        this.delete(anInfoRelation);
                        count ++;
                    }
                }
            }
            return count;
        }
    }
    
    /**
     * 查询订单下面的关联关系列表
     */
    public List<ScfOrderRelation> findOrderRelation(Map<String, Object> anMap) {
        return this.selectByClassProperty(ScfOrderRelation.class, anMap);
    }
    
    /**
     * 检查关系是否已存在
     */
    private boolean checkRelationExists(Long anOrderId, Long anInfoId, String anInfoType) {
        Map<String, Object> queryMap = QueryTermBuilder.newInstance().put("orderId", anOrderId).put("infoId", anInfoId).put("infoType", anInfoType)
                .build();
        List<ScfOrderRelation> orderRelationList = this.selectByProperty(queryMap);
        return !Collections3.isEmpty(orderRelationList);
    }
}
