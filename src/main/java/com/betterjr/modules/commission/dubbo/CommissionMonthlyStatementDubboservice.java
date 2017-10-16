package com.betterjr.modules.commission.dubbo;

import java.text.ParseException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.commission.ICommissionMonthlyStatementService;
import com.betterjr.modules.commission.service.CommissionMonthlyStatementService;

@Service(interfaceClass = ICommissionMonthlyStatementService.class)
public class CommissionMonthlyStatementDubboservice implements ICommissionMonthlyStatementService {

    @Autowired
    private CommissionMonthlyStatementService monthlyStatementService;

    @Override
    public String webSaveComissionMonthlyStatement(Map<String, Object> anParam) {
        try {
            return AjaxObject.newOk("月报表添加成功", monthlyStatementService.saveComissionMonthlyStatement(anParam)).toJson();
        }
        catch (Exception e) {
            BTAssert.notNull(null, e.getMessage());
        }
        return null;
    }

    @Override
    public String webQueryMonthlyStatement(Map<String, Object> anParam, int anPageNum, int anPageSize) {
        try {
            return AjaxObject.newOkWithPage("查询月账单列表",
                    monthlyStatementService.queryMonthlyStatement(anParam, anPageNum, anPageSize)).toJson();
        }
        catch (ParseException e) {
            BTAssert.notNull(null, e.getMessage());
        }
        return null;
    }

    @Override
    public String webFindMonthlyStatementById(Long anMonthlyId) {
        return AjaxObject.newOk("查找详情", monthlyStatementService.findMonthlyStatementById(anMonthlyId)).toJson();
    }

    @Override
    public String webSaveMonthlyStatement(Long anMonthlyId, String anBusinStatus) {
        if (monthlyStatementService.saveMonthlyStatement(anMonthlyId, anBusinStatus)) {
            return AjaxObject.newOk("更新成功").toJson();
        } else {
            return AjaxObject.newError("更新失败").toJson();
        }
    }

    @Override
    public String webDelMonthlyStatement(Long anMonthlyId) {
        if (monthlyStatementService.delMonthlyStatement(anMonthlyId)) {
            return AjaxObject.newOk("删除成功").toJson();
        } else {
            return AjaxObject.newError("删除失败").toJson();
        }
    }

}
