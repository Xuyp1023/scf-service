package com.betterjr.modules.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.order.dao.ScfOrderRelationMapper;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.entity.ScfOrderRelation;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;
import com.betterjr.modules.order.helper.ScfOrderInfoCheckFactory;
import com.betterjr.modules.order.helper.ScfOrderRelationType;
import com.betterjr.modules.receivable.service.ScfReceivableService;

@Service
public class ScfOrderRelationService extends BaseService<ScfOrderRelationMapper, ScfOrderRelation> {

    
    @Autowired
    private ScfAcceptBillService acceptBillService;
    @Autowired
    private ScfOrderService orderService;
    @Autowired
    private ScfReceivableService receivbaleService;
    
    /**
     * 订单关联关系保存
     */
    public List<ScfOrderRelation> addOrderRelation(String anEnterType, Long anEnterId, String anInfoType, String anInfoIdList) {
        logger.info("Begin to add OrderRelation");
        List<ScfOrderRelation> resultList = new ArrayList<ScfOrderRelation>();
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
                //作为返回值添加
                resultList.add(anOrderRelation);
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
                resultList.add(anOrderRelation);
            }
        }
        //当两个id都不为订单时
        else{
            //从enterId 来上溯到orderId
            Map<String, Object> anMap = new HashMap<String, Object>();
            anMap.put("infoId", anEnterId);
            anMap.put("infoType", anEnterType);
            List<ScfOrderRelation> orderRelationList = this.selectByProperty(anMap);
            // 若未找到相应信息生成默认数据
            if (Collections3.isEmpty(orderRelationList)) {
                // 通过票据生成订单，并于此票据建立
                if(BetterStringUtils.equals(anEnterType, ScfOrderRelationType.ACCEPTBILL.getCode())) {
                    ScfOrder order = new ScfOrder(acceptBillService.selectByPrimaryKey(anEnterId));
                    orderService.insert(order);
                    ScfOrderRelation relation = new ScfOrderRelation();
                    relation.initAddValue();
                    relation.setOrderId(order.getId());
                    relation.setInfoType(ScfOrderRelationType.ACCEPTBILL.getCode());
                    relation.setInfoId(anEnterId);
                    this.insert(relation);
                    orderRelationList.add(relation);
                    resultList.add(relation);
                }
                //通过应收账款生成
                if(BetterStringUtils.equals(anEnterType, ScfOrderRelationType.RECEIVABLE.getCode())) {
                    ScfOrder order = new ScfOrder(receivbaleService.selectByPrimaryKey(anEnterId));
                    orderService.insert(order);
                    ScfOrderRelation relation = new ScfOrderRelation();
                    relation.initAddValue();
                    relation.setOrderId(order.getId());
                    relation.setInfoType(ScfOrderRelationType.RECEIVABLE.getCode());
                    relation.setInfoId(anEnterId);
                    this.insert(relation);
                    orderRelationList.add(relation);
                    resultList.add(relation);
                }
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
                resultList.add(anOrderRelation);
            }
        }
        return resultList;
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
