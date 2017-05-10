package com.betterjr.modules.delivery.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.commission.service.CommissionMonthlyStatementService;
import com.betterjr.modules.delivery.dao.DeliveryRecordStatementMapper;
import com.betterjr.modules.delivery.data.DeliveryConstantCollentions;
import com.betterjr.modules.delivery.entity.DeliveryRecordStatement;

@Service
public class DeliveryRecordStatementService extends BaseService<DeliveryRecordStatementMapper, DeliveryRecordStatement> {

    @Autowired
    private CommissionMonthlyStatementService montylyService;
    /**
     * 根据快递账单的凭证编号查询当前下面的月账单
     * @param anDeliverRefNo
     * @return
     */
    public List<DeliveryRecordStatement> queryDeliveryStatementList(String anDeliverRefNo){
        
        BTAssert.notNull(anDeliverRefNo,"查询佣金文件条件为空");
        //查询当前公司的佣金文件
        Map<String,Object> queryMap = QueryTermBuilder.newInstance().put("operOrg", UserUtils.getOperatorInfo().getOperOrg())
        .put("deliverRefNo", anDeliverRefNo)
        .put("businStatus", DeliveryConstantCollentions.DELIVERY_STATEMENT_BUSIN_STATUS_CANUSERD)
        .build();
        
        return this.selectByProperty(queryMap);
        
    }

    /**
     * 插入投递明细帐信息
     * @param anRecordStatementList
     * @return
     */
    public List<DeliveryRecordStatement> saveAddStatementByList(List<DeliveryRecordStatement> anRecordStatementList) {
        
        for (DeliveryRecordStatement deliveryRecordStatement : anRecordStatementList) {
            
            deliveryRecordStatement.saveAddInit();
            this.insert(deliveryRecordStatement);
        }
        
        return anRecordStatementList;
    }
    
    /**
     * 更新对账单明细状态
     * @param anId
     * @param anExpressStatus
     * @return
     */
    public DeliveryRecordStatement saveUpdateStatementExpressStatus(Long anId,String anExpressStatus){
        
        DeliveryRecordStatement statement = this.selectByPrimaryKey(anId);
        statement.setExpressStatus(anExpressStatus);
        this.updateByPrimaryKeySelective(statement);
        return statement;
    }

    /**
     * 青海移动查询投递账单明细情况
     * @param anQueryMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page queryDeliveryStatementList(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        
      //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
      //是核心企业查询
        if(!(anMap.containsKey("expressStatus") && anMap.get("expressStatus") !=null) ){
            List<String> businStatusList=new ArrayList<>();
            businStatusList.add(DeliveryConstantCollentions.DELIVERY_STATEMENT_EXPRESS_STATUS_EXPRESS);
            businStatusList.add(DeliveryConstantCollentions.DELIVERY_STATEMENT_EXPRESS_STATUS_CONFIREM);
            anMap.put("expressStatus", businStatusList);
        }
        anMap.put("ownOperOrg", UserUtils.getOperatorInfo().getOperOrg());
        anMap.put("businStatus", DeliveryConstantCollentions.DELIVERY_STATEMENT_BUSIN_STATUS_CANUSERD);
        
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
    }

  //删除对账单明细和重置月账单为审核状态
    public void saveDeleteRecordStatementByRecordId(Long anRecordId) {
        
        ////0 可用   1 不可用，已删除状态
        Map<String,Object> queryMap = QueryTermBuilder.newInstance()
                .put("deliverId", anRecordId)
                .put("businStatus", DeliveryConstantCollentions.DELIVERY_STATEMENT_BUSIN_STATUS_CANUSERD)
                .build();
        
        List<DeliveryRecordStatement> recordStatementList = this.selectByProperty(queryMap);
        for (DeliveryRecordStatement statement : recordStatementList) {
            Long monthlyId=statement.getMonthlyStatementId();
            montylyService.saveMonthlyStatement(monthlyId, "2");
            this.delete(statement);
        }
        
    }
    
}
