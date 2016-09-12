package com.betterjr.modules.loan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfPressMoneyMapper;
import com.betterjr.modules.loan.entity.ScfPressMoney;

@Service
public class ScfPressMoneyService extends BaseService<ScfPressMoneyMapper, ScfPressMoney> {

    @Autowired
    private CustAccountService custAccountService;
    /**
     * 新增催收记录
     * @param anPress
     * @return
     */
    public ScfPressMoney addPressMoney(ScfPressMoney anPress) {
        BTAssert.notNull(anPress, "新增催收记录失败-anPressMoney不能为空");
        anPress.init();
        this.insert(anPress);
        return findPressMoneyDetail(anPress.getId());
    }
    
    /**
     * 修改催收记录
     * 
     * @param anPress
     * @return
     */
    public ScfPressMoney saveModifyPressMoney(ScfPressMoney anPress, Long anId) {
        BTAssert.notNull(anPress, "修改催收记录失败-anPressMoney不能为空");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("factorNo", anPress.getFactorNo());
        map.put("id", anId);
        if(Collections3.isEmpty(selectByClassProperty(ScfPressMoney.class, map))){
            throw new IllegalArgumentException("修改催收记录失败-找不到原数据");
        }

        anPress.initModify();
        anPress.setId(anId);
        this.updateByPrimaryKeySelective(anPress);
        return findPressMoneyDetail(anId);
    }

    /**
     * 分页查询催收记录列表
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfPressMoney> queryPressMoneyList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
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
    public List<ScfPressMoney> findPresMoneyist(Map<String, Object> anMap) {
        List<ScfPressMoney> list = this.selectByClassProperty(ScfPressMoney.class, anMap);
        for (ScfPressMoney press : list) {
            press.setCustName(custAccountService.queryCustName(press.getCustNo()));
            press.setFactorName(custAccountService.queryCustName(press.getFactorNo()));
        }
        return list;
    }
    
    /**
     * 查询催收记录详情
     * 
     * @param anId
     * @return
     */
    public ScfPressMoney findPressMoneyDetail(Long anId) {
        BTAssert.notNull(anId, "查询催收记录详情失败-anId不能为空");
        ScfPressMoney press =  this.selectByPrimaryKey(anId);
        if(null == press){
            return new ScfPressMoney();
        }
        return press;
    }
    

}
