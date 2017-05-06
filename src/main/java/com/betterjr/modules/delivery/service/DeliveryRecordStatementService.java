package com.betterjr.modules.delivery.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.delivery.dao.DeliveryRecordStatementMapper;
import com.betterjr.modules.delivery.data.DeliveryConstantCollentions;
import com.betterjr.modules.delivery.entity.DeliveryRecordStatement;

@Service
public class DeliveryRecordStatementService extends BaseService<DeliveryRecordStatementMapper, DeliveryRecordStatement> {

    
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
    
}
