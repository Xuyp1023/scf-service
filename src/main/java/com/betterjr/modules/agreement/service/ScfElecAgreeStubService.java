package com.betterjr.modules.agreement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustLoginRecord;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.account.service.CustAndOperatorRelaService;
import com.betterjr.modules.account.service.CustLoginService;
import com.betterjr.modules.agreement.dao.ScfElecAgreeStubMapper;
import com.betterjr.modules.agreement.data.ScfElecAgreeStubInfo;
import com.betterjr.modules.agreement.entity.ScfElecAgreeStub;

/**
 * 
 * @author zhoucy 电子合同签约方信息管理
 */
@Service
public class ScfElecAgreeStubService extends BaseService<ScfElecAgreeStubMapper, ScfElecAgreeStub> {
    private static final Logger logger = LoggerFactory.getLogger(ScfElecAgreeStubService.class);

    /***
     * 保存电子合同签约方的信息
     * 
     * @param anAppNo
     *            电子合同编号
     * @param anCustNoList
     *            签约方客户号
     */
    @Autowired
    private CustLoginService loginService;

    @Autowired
    private CustAndOperatorRelaService custAndOperRelaService;

    private String findCustRemoteAddr(Long anCustNo) {
        String tmpOperOrg = custAndOperRelaService.findOperOrgByCustNo(anCustNo);
        if (BetterStringUtils.isNotBlank(tmpOperOrg)) {
            CustLoginRecord loginRecord = this.loginService.findLastLoginRecord(tmpOperOrg);
            if (loginRecord != null) {
                return loginRecord.getIpaddr();
            }
        }
        logger.warn("findCustRemoteAddr not Find CustLoginRecord anCustNo is " + anCustNo);
        return "127.0.0.1";
    }

    /**
     * 创建电子合同的签约方信息
     * 
     * @param anAppNo
     *            电子合同编号
     * @param anCustNoList
     *            签约方客户号列表
     */
    public void saveScfElecAgreeStub(String anAppNo, List<Long> anCustNoList) {
        Map termMap = new HashMap();
        if (BetterStringUtils.isBlank(anAppNo) || Collections3.isEmpty(anCustNoList)) {

            throw new BytterValidException("save ScfElecAgreeStub appNo or CustNo is null");
        }

        // 删除已经存在的合同签约方，增加新加入的签约方
        termMap.put("appNo", anAppNo);
        this.deleteByExample(termMap);
        ScfElecAgreeStub tmpElecAgreeStub = null;
        for (Long custNo : anCustNoList) {
            tmpElecAgreeStub = new ScfElecAgreeStub(anAppNo, custNo);
            tmpElecAgreeStub.setIpaddr(findCustRemoteAddr(custNo));
            this.insert(tmpElecAgreeStub);
        }
    }

    /**
     * 根据电子合同订单号，查找签署方信息
     * 
     * @param anAppNo
     *            电子合同订单号
     * @return
     */
    public List<ScfElecAgreeStubInfo> findSignerList(String anAppNo, CustAccountService custAccountService) {
        List<ScfElecAgreeStubInfo> result = this.selectByClassProperty(ScfElecAgreeStubInfo.class, "appNo", anAppNo);
        if (custAccountService != null) {
            for (ScfElecAgreeStubInfo stubInfo : result) {
                stubInfo.setCustName(custAccountService.queryCustName(stubInfo.getCustNo()));
            }
        }
        
        return result;
    }

    /**
     * 根据订单号，获得签约方的客户号
     * 
     * @param anAppNo
     * @return
     */
    public Long findContractCustNo(String anAppNo) {
        Set<Long> custNoSet = new HashSet(UserUtils.findCustNoList());
        for (ScfElecAgreeStub agreeStub : this.selectByProperty("appNo", anAppNo)) {
            if (custNoSet.contains(agreeStub.getCustNo())) {
                return agreeStub.getCustNo();
            }
        }
        logger.warn("findContractCustNo not Find match CustNo， the appNo is " + anAppNo);

        return -1L;
    }

    /**
     * 查找沃通需要的电子合同签署机构的信息
     * 
     * @param anAppNo
     *            电子合同订单号
     * @return
     */
    public List<Map> findSignerForWosign(String anAppNo) {
        List result = new ArrayList();
        Map<String, Object> dataMap;
        for (ScfElecAgreeStub agreeStub : findSignerList(anAppNo, null)) {
            dataMap = new HashMap();
            dataMap.put("userID", agreeStub.getCustNo());
            dataMap.put("ip", agreeStub.getIpaddr());
            result.add(dataMap);
        }

        return result;
    }

    /**
     * 保存签约方操作状态
     * 
     * @param anCustNo
     *            签约方
     * @param anAppNo
     *            电子合同订单号
     * @param anStatus
     *            操作状态
     */
    public void saveElecAgreeStubStatus(Long anCustNo, String anAppNo, String anStatus) {
        logger.info("Update agree sign information with custNo:" + anCustNo + " appNo:" + anAppNo + " status:" + anStatus);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("custNo", anCustNo);
        map.put("appNo", anAppNo);

        List<ScfElecAgreeStub> elecStubList = this.selectByProperty(map);
        if (Collections3.isEmpty(elecStubList)) {
            logger.error("Can't find agree sign information with custNo:" + anCustNo + ", appNo:" + anAppNo);
            throw new BytterTradeException(40001, "无法获取电子合同签署记录！");
        }

        ScfElecAgreeStub curStub = Collections3.getFirst(elecStubList);
        if ("1".equals(curStub.getOperStatus())) {
            logger.error("Can't modify agree sign information for status not 0.");
            throw new BytterTradeException(40001, "当前电子合同签署已经处理！");
        }
        ScfElecAgreeStub.updateSignInfo(curStub, anStatus);
        this.updateByPrimaryKeySelective(curStub);
    }
    
    public ScfElecAgreeStub saveAddInitValueStub(String anAppNo,Long anCustNo,String anBusinStatus){
        ScfElecAgreeStub stu=new ScfElecAgreeStub(anAppNo, anCustNo);
        stu.setOperStatus(anBusinStatus);
        stu.setOperCode(UserUtils.getOperatorInfo().getId()+"");
        stu.setOperName(UserUtils.getOperatorInfo().getName());
        stu.setOperTime(BetterDateUtils.getNumDateTime());
        this.insertSelective(stu);
        return stu;
    }
}