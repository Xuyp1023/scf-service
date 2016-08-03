package com.betterjr.modules.loan.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfRequestApprovedMapper;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestApproved;

@Service
public class ScfRequestApprovedService extends BaseService<ScfRequestApprovedMapper, ScfRequestApproved> {

    @Autowired
    private ScfRequestService requestService;
    
    @Autowired
    private CustAccountService custAccountService;
    
    /**
     * 保存融资审批相关信息
     * 
     * @param anApproved
     * @return
     */
    public ScfRequestApproved addApproved(ScfRequestApproved anApproved) {
        BTAssert.notNull(anApproved, "approved不能为空");
        ScfRequest request =  requestService.findRequestDetail(anApproved.getRequestNo());
        anApproved.setFactorNo(request.getFactorNo());
        anApproved.setCustNo(request.getCoreCustNo());
        anApproved.init();
        this.insert(anApproved);
        return anApproved;
    }

    /**
     * 获取融资审批详情
     * 
     * @param anRequestNo
     * @param anFactorNo
     * @return
     */
    public ScfRequestApproved findApprovedDetail(String anRequestNo) {
        List<ScfRequestApproved> approvedList = this.selectByProperty("requestNo", anRequestNo);
        if (Collections3.isEmpty(approvedList) || approvedList.size() == 0) {
            logger.debug("无数据！");
            return new ScfRequestApproved();
        }
        
        //设置相关企业名称
        ScfRequestApproved approved = Collections3.getFirst(approvedList);
        approved.setCustName(custAccountService.queryCustName(approved.getCustNo()));
        approved.setCoreCustName(custAccountService.queryCustName(approved.getCoreCustNo()));
        approved.setFactorName(custAccountService.queryCustName(approved.getFactorNo()));
        return approved;
    }
    
    /**
     * 获取融资审批详情(无数据返回null)
     * 
     * @param anRequestNo
     * @param anFactorNo
     * @return
     */
    public ScfRequestApproved findApprovedDetail2(String anRequestNo) {
        List<ScfRequestApproved> approvedList = this.selectByProperty("requestNo", anRequestNo);
        if (Collections3.isEmpty(approvedList) || approvedList.size() == 0) {
            logger.debug("无数据！");
            return null;
        }
        
        //设置相关企业名称
        ScfRequestApproved approved = Collections3.getFirst(approvedList);
        approved.setCustName(custAccountService.queryCustName(approved.getCustNo()));
        approved.setCoreCustName(custAccountService.queryCustName(approved.getCoreCustNo()));
        approved.setFactorName(custAccountService.queryCustName(approved.getFactorNo()));
        return approved;
    }

    /**
     * 修改融资审批
     * 
     * @param rquestNo
     * @param factorNo
     * @return
     */
    public ScfRequestApproved saveModifyApproved(ScfRequestApproved anApproved) {
        BTAssert.notNull(anApproved, "approved不能为空");

        List<ScfRequestApproved> list = this.selectByProperty("requestNo", anApproved.getRequestNo());
        if(Collections3.isEmpty(list) || list.size() == 0){
            throw new IllegalArgumentException("修改失败，找不到源数据-requestNo:"+ anApproved.getRequestNo());
        }

        ScfRequestApproved approved = Collections3.getFirst(list);
        anApproved.setId(approved.getId());
        this.updateByPrimaryKeySelective(anApproved); 
        return anApproved;
    }
    
    /**
     * (申请企业/核心企业)查询融资审批
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfRequestApproved> queryApprovedList(Map<String, Object>  anMap, int anFlag, int anPageNum, int anPageSize) {
        if(null != anMap.get("coreCustNo") && BetterStringUtils.isNotEmpty( anMap.get("coreCustNo").toString()) &&
           null == anMap.get("coreCustAduit") && BetterStringUtils.isEmpty( anMap.get("coreCustAduit").toString())){
            anMap.put("coreCustAduit", new String[]{"0","1","2"});
        }
        
        Page<ScfRequestApproved> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1==anFlag);
        for (ScfRequestApproved approved : page) {
            approved.setCustName(custAccountService.queryCustName(approved.getCustNo()));
            approved.setCoreCustName(custAccountService.queryCustName(approved.getCoreCustNo()));
            approved.setFactorName(custAccountService.queryCustName(approved.getFactorNo()));
        }
        
        return page;
    }

}
