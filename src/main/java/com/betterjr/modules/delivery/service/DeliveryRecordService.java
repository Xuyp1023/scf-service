package com.betterjr.modules.delivery.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.commission.service.CommissionMonthlyStatementService;
import com.betterjr.modules.delivery.dao.DeliveryRecordMapper;
import com.betterjr.modules.delivery.entity.DeliveryRecord;
import com.betterjr.modules.delivery.entity.DeliveryRecordStatement;

@Service
public class DeliveryRecordService extends BaseService<DeliveryRecordMapper, DeliveryRecord> {
    
    @Autowired
    private DeliveryRecordStatementService recordStatementService;
    
    @Autowired
    private CommissionMonthlyStatementService montylyService;
    
    /**
     * 查询快递状态详情
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public List<DeliveryRecord> queryDeliveryRecordList(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize){
        
        BTAssert.notNull(anMap,"查询快递账单条件为空");
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        //查询当前公司的佣金文件
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        
        Page<DeliveryRecord> recordList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
        for (DeliveryRecord deliveryRecord : recordList) {
            List<DeliveryRecordStatement> list = recordStatementService.queryDeliveryStatementList(deliveryRecord.getRefNo());
            deliveryRecord.setRecordStatementList(list);
        }
        return recordList;
        
    }
    
    public DeliveryRecord findDeliveryRecord(String anRefNo){
        
        BTAssert.notNull(anRefNo,"查询快递账单条件为空");
        DeliveryRecord record = this.selectOne(new DeliveryRecord(anRefNo));
        BTAssert.notNull(record,"未查询到快递账单信息");
        List<DeliveryRecordStatement> list = recordStatementService.queryDeliveryStatementList(record.getRefNo());
        record.setRecordStatementList(list);
        return  record;
    }
    
    public DeliveryRecord saveAddDeliveryRecord(Map<String,Object> anMap){
        
        BTAssert.notNull(anMap,"请选择月账单!");
        BTAssert.notNull(anMap.get("monthList"),"请选择月账单!");
        List<Long> monthlyStatementIdList = getMonthlyStatementRefNoList(anMap.get("monthList").toString());
        
    }
    
    public List<Long> getMonthlyStatementRefNoList(String anValue){
        
        List<Long> refNoList=new ArrayList<>();
        if(BetterStringUtils.isNotBlank(anValue)){
            
            if(anValue.contains(",")){
                String[] split = anValue.split(",");
                for (String string : split) {
                    refNoList.add(Long.parseLong(string.trim())); 
                }
            }else{
                refNoList.add(Long.parseLong(anValue.trim())); 
            }
            
        }
        return refNoList;
    }

}
