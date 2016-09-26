package com.betterjr.modules.loan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfExemptMapper;
import com.betterjr.modules.loan.entity.ScfExempt;
import com.betterjr.modules.loan.entity.ScfPayPlan;
import com.betterjr.modules.loan.entity.ScfPressMoney;
import com.betterjr.modules.loan.entity.ScfRequest;

@Service
public class ScfExemptService extends BaseService<ScfExemptMapper, ScfExempt> {

    @Autowired
    private ScfRequestService requestService;
    @Autowired
    private ScfPayPlanService payPlanService;
    @Autowired
    private CustAccountService custAccountService;

    /**
     * 新增豁免记录
     * @param anExempt
     * @return
     */
    public ScfExempt addExempt(ScfExempt anExempt) {
        BTAssert.notNull(anExempt, "新增豁免记录失败-anExempt不能为空");
        
        //获取申请单信息
        ScfRequest request = requestService.selectByPrimaryKey(anExempt.getRequestNo());
        anExempt.setCustNo(request.getCustNo());
        
        //获取还款计划信息
        ScfPayPlan plan = payPlanService.findPayPlanByRequest(anExempt.getRequestNo());
        anExempt.setPayPlanId(plan.getId());
        
        //保存豁免
        anExempt.init();
        this.insert(anExempt);
        
        setCustName(anExempt);
        return anExempt;
    }
    
    /**
     * 修改豁免记录
     * 
     * @param anExempt
     * @return
     */
    public ScfExempt saveModifyExempt(ScfExempt anExempt, Long anId) {
        BTAssert.notNull(anExempt, "修改豁免记录失败-anExempt不能为空");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("factorNo", anExempt.getFactorNo());
        map.put("id", anId);
        if(Collections3.isEmpty(selectByClassProperty(ScfExempt.class, map))){
            throw new BytterTradeException(40001, "修改豁免记录失败-找不到原数据");
        }

        anExempt.initModify();
        anExempt.setId(anId);
        this.updateByPrimaryKeySelective(anExempt);
        return findExemptDetail(anId);
    }

    /**
     * 查询豁免记录列表
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfExempt> queryExemptList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
    }

    /**
     * 查询催收列表
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public List<ScfExempt> findExemptList(Map<String, Object> anMap) {
        List<ScfExempt> list = this.selectByClassProperty(ScfExempt.class, anMap);
        for (ScfExempt exempt : list) {
            exempt.setCustName(custAccountService.queryCustName(exempt.getCustNo()));
            exempt.setFactorName(custAccountService.queryCustName(exempt.getFactorNo()));
        }
        return list;
    }
    
    /**
     * 查询豁免记录详情
     * 
     * @param anId
     * @return
     */
    public ScfExempt findExemptDetail(Long anId) {
        BTAssert.notNull(anId, "查询豁免记录详情失败-anId不能为空");
        ScfExempt exempt =  this.selectByPrimaryKey(anId);
        if(null == exempt){
            return new ScfExempt();
        }
        
        setCustName(exempt);
        return exempt;
    }
    
    private void setCustName(ScfExempt anExempt){
        anExempt.setCustName(custAccountService.queryCustName(anExempt.getCustNo()));
        anExempt.setFactorName(custAccountService.queryCustName(anExempt.getFactorNo()));
    }
    
}
