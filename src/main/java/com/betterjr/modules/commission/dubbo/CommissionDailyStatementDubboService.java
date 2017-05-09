package com.betterjr.modules.commission.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.commission.ICommissionDailyStatementService;
import com.betterjr.modules.commission.service.CommissionDailyStatementService;

@Service(interfaceClass=ICommissionDailyStatementService.class)
public class CommissionDailyStatementDubboService implements ICommissionDailyStatementService{

    @Autowired
    private CommissionDailyStatementService dailyStatementService;

    @Override
    public String webQueryDailyStatement(Map<String, Object> anParam, int anPageNum, int anPageSize) {
        return AjaxObject.newOk("分页查询日报表", dailyStatementService.queryDailyStatement(anParam, anPageNum, anPageSize)).toJson();
    }
    
    @Override
    public String webFindDailyStatementCount(String anMonth, Long anCustNo) {
        try {
            return AjaxObject.newOk("查询月账单统计数据",dailyStatementService.findDailyStatementCount(anMonth, anCustNo)).toJson();   
        }
        catch (Exception e) {
            BTAssert.notNull(null, "查询月账单统计数据异常："+e);
        }
        return null;
    }

    @Override
    public String webFindDailyStatementBasicsInfo(Map<String, Object> anParam) {
        try {
            return AjaxObject.newOk("查询当月日账单基础信息",dailyStatementService.findDailyStatementBasicsInfo(anParam)).toJson();
        }
        catch (Exception e) {
            BTAssert.notNull(null, "查询当月日账单基础信息异常："+e);
        }
        return null;
    }

    @Override
    public String webUpdateDailyStatement(Long anDailyStatementId, String anBusinStatus) {
        if(dailyStatementService.saveDailyStatementById(anDailyStatementId, anBusinStatus)){
            return AjaxObject.newOk("更新成功").toJson();
        }else{
            return AjaxObject.newError("更新失败").toJson();
        }
    }

    @Override
    public String webDelDailyStatement(Long anDailyStatementId) {
        if(dailyStatementService.delDailyStatement(anDailyStatementId)){
            return AjaxObject.newOk("删除成功").toJson();
        }else{
            return AjaxObject.newError("删除失败").toJson();
        }
    }

    @Override
    public String webFindPayResultCount(String anPayDate, Long anOwnCustNo) {
        return AjaxObject.newOk("支付记录总数",dailyStatementService.findPayResultCount(anPayDate, anOwnCustNo)).toJson();
    }

    @Override
    public String webQueryPayResultRecord(Long anOwnCustNo, String anPayDate, int anFlag, int anPageNum, int anPageSize) {
        return AjaxObject.newOkWithPage("分页查询支付记录", dailyStatementService.queryPayResultRecord(anOwnCustNo, anPayDate, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    @Override
    public String webFindPayResultInfo(String anPayDate,Long anOwnCustNo){
        return AjaxObject.newOk("查询支付信息",dailyStatementService.findPayResultInfo(anPayDate, anOwnCustNo)).toJson();
    }
    
    @Override
    public String webSaveDailyStatement(String anDailyRefNo,String anPayDate,Long anOwnCustNo){
        return AjaxObject.newOk("添加记录",dailyStatementService.saveDailyStatement(anDailyRefNo, anPayDate, anOwnCustNo)).toJson();
    }
    
    @Override
    public String webFindDailyStatementById(Long anDailyStatementId){
        return AjaxObject.newOk("查询日账单详情", dailyStatementService.findDailyStatementById(anDailyStatementId)).toJson();
    }
    
    @Override
    public String webQueryDailyStatementRecordById(Long anDailyStatementId,int anPageNum,int anPageSize,String anFlag){
        return AjaxObject.newOk("查询日账单详情", dailyStatementService.queryDailyStatementRecordByDailyId(anDailyStatementId, anPageNum, anPageSize, anFlag)).toJson();
    }
}
