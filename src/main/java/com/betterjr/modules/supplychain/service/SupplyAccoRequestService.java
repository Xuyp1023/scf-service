package com.betterjr.modules.supplychain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.config.ParamNames;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.modules.account.dubbo.interfaces.ICustInfoService;
import com.betterjr.modules.customer.ICustRelationService;
import com.betterjr.modules.rule.service.BusinRuleService;
import com.betterjr.modules.supplychain.dao.CustTempEnrollInfoMapper;
import com.betterjr.modules.supplychain.entity.CoreSupplierInfo;
import com.betterjr.modules.supplychain.entity.CustTempEnrollInfo;
import com.betterjr.modules.supplychain.entity.ScfSupplierBank;

/**
 * 客户注册管理
 * 
 * @author zhoucy
 *
 */
@Service
public class SupplyAccoRequestService extends BaseService<CustTempEnrollInfoMapper, CustTempEnrollInfo> {
    private static final Logger logger = LoggerFactory.getLogger(SupplyAccoRequestService.class);

    @Autowired
    private BusinRuleService businRuleService;

    @Autowired
    @Reference(interfaceClass = ICustInfoService.class)
    private ICustInfoService accountService;

    /**
     * 添加供应商客户账户
     * 
     * 
     * @param anMap
     * @return 返回注册成功后的供应商用户信息
     */
    public CustTempEnrollInfo addSupplyAccount(Map<String, String> anMap) {
        logger.info("Begin add supply account.");

        CustTempEnrollInfo request = businRuleService.buildRequest(new CustTempEnrollInfo(), anMap, "addSupplyAccount");
        request.initdefValue();
      /*  boolean accountExists = this.accountService.checkAccountExists(request.getCustType(), request.getIdentType(), request.getIdentNo());
        if (accountExists) {
            throw new BytterTradeException(40001, "抱歉，该证件号码已开户");
        }*/
        boolean tempAccountExists = checkTempAccountExists(request.getCustType(), request.getIdentType(), request.getIdentNo());
        if (tempAccountExists) {
            throw new BytterTradeException(40001, "抱歉，该证件号码已注册");
        }
        // 0101为默认的银行网点编码
/*        boolean bankExists = this.accountService.checkAcooBankExists(ParamNames.DEFAULT_BANK_NET_NO, request.getBankAccount());
        if (bankExists) {
            throw new BytterTradeException(40001, "抱歉，该银行账户已存在");
        }
*/        boolean tempBankExists = checkTempAcooBankExists(request.getBankAccount());
        if (tempBankExists) {
            throw new BytterTradeException(40001, "抱歉，该银行账户已注册");
        }

        // 保存临时客户信息
        this.insert(request);

        return request;
    }

    /**
     * 校验临时用户注册表中用户证件号码是否重复 <br>
     * 
     * @param anCustType
     * @param anIdentType
     * @param anIdentNo
     * @return
     */
    private boolean checkTempAccountExists(String anCustType, String anIdentType, String anIdentNo) {
        if (BetterStringUtils.isNotBlank(anIdentType) && BetterStringUtils.isNotBlank(anIdentNo)) {
            Map<String, Object> map = new HashMap();
            map.put("custType", anCustType);
            map.put("identType", anIdentType);
            map.put("identNo", anIdentNo);
            return Collections3.isEmpty(this.selectByProperty(map)) == false;
        }
        return false;
    }

    /**
     * 校验临时用户注册表中银行账户是否重复 <br>
     * 
     * @param anNetNo
     * @param anBankAccount
     * @return
     */
    private boolean checkTempAcooBankExists(String anBankAccount) {
        if (BetterStringUtils.isNotBlank(anBankAccount)) { 
            return Collections3.isEmpty(this.selectByProperty("bankAccount", anBankAccount)) == false;
        }
        return false;
    }

    /**
     * 根据操作员机构获取临时用户信息
     * 
     * @param operOrg
     * @return
     */
    public CustTempEnrollInfo getCustEnrollByOperOrg(String operOrg) {
        logger.info("begin get customer enroll by operator orgnize " + operOrg);
        Map<String, Object> termMap = new HashMap();
        termMap.put("operOrg", operOrg);
        termMap.put("custType", "0");
        List<CustTempEnrollInfo> anCustEnrollList = this.selectByProperty(termMap);
        if (Collections3.isEmpty(anCustEnrollList)) {
            throw new BytterTradeException(40001, "无法获取临时用户信息！");
        }
        CustTempEnrollInfo anCustEnroll = anCustEnrollList.get(0);

        return anCustEnroll;
    }

    /**
     * 验证成功后，保存关联的资料信息
     * 
     * @param anDataList
     *            处理的数据集
     * @param anCoreCustNo
     *            核心企业代码
     * @return
     */
    public boolean saveUnRegisterAccount(List anDataList, Long anCoreCustNo) {
        List<CoreSupplierInfo> suppList = anDataList;
        CustTempEnrollInfo scfRel;
        for (CoreSupplierInfo coreSupp : suppList) {
            scfRel = findUnRegisterAccount(coreSupp.getBankAccountName(), coreSupp.getBankAccount(), true);
            if (scfRel == null) {

                continue;
            }
            scfRel.modifyStatus(coreSupp.getOperOrg(), coreSupp.getCoreCustNo(), coreSupp.getBtNo(), coreSupp.getCustNo());
            this.updateByPrimaryKey(scfRel);
        }

        return true;
    }

    /**
     * 根据银行账户信息获得注册的客户资料
     * 
     * @param anAccountName
     * @param anBankAccount
     * @return
     */
    public CustTempEnrollInfo findByBankAccount(ScfSupplierBank anSupplierBank) {
        if (anSupplierBank != null) {
            return findUnRegisterAccount(anSupplierBank.getBankAccountName(), anSupplierBank.getBankAccount(), false);
        }
        return null;
    }

    /**
     * 根据注册的银行账户和银行户名查找注册未完成的信息
     * 
     * @param anAccountName
     *            银行户名
     * @param anBankAccount
     *            银行账户
     * @return
     */
    protected CustTempEnrollInfo findUnRegisterAccount(String anAccountName, String anBankAccount, boolean anCheck) {
        if (BetterStringUtils.isBlank(anBankAccount) || BetterStringUtils.isBlank(anAccountName)) {

            return null;
        }
        Map<String, Object> termMap = new HashMap();
        termMap.put("bankAcountName", anAccountName);
        termMap.put("bankAccount", anBankAccount);
        for (CustTempEnrollInfo custTemp : this.selectByProperty(termMap)) {
            if (anCheck) {
                if (BetterStringUtils.defShortString(custTemp.getBtNo()) && MathExtend.smallValue(custTemp.getCoreCustNo())) {

                    return custTemp;
                }
            }
            else {
                return custTemp;
            }
        }

        return null;
    }

    // 查找未处理的注册信息
    public List<CustTempEnrollInfo> findUncheckedAccount(Long anCoreCustNo) {

        return this.selectPropertyByPage("status", new String[] { "0", "9" }, 1, 5, false);
    }

    /**
     * 保存账户检查的结果数据
     * 
     * @param anAccountName
     *            银行账户名称
     * @param anBankAccount
     *            银行账户
     * @param anStatus
     *            状态，true表示成功，false表示失败
     */
    public void saveCheckedStatus(String anAccountName, String anBankAccount, Long anCoreCustNo, String anBtCustId, boolean anStatus) {
        Map<String, Object> termMap = new HashMap();
        termMap.put("bankAcountName", anAccountName);
        termMap.put("bankAccount", anBankAccount);
        String workStatus = anStatus ? "1" : "2";
        for (CustTempEnrollInfo custTemp : this.selectByProperty(termMap)) {
            if (custTemp.unChecked()) {
                custTemp.setStatus(workStatus);
                custTemp.setBtNo(anBtCustId);
                custTemp.setCoreCustNo(anCoreCustNo);
                this.updateByPrimaryKey(custTemp);
            }
        }
    }

    public void addSupplyAccountFromOpenAcco(Map<String, Object> anMap) {
        CustTempEnrollInfo tmpEnroll = BeanMapper.map(anMap, CustTempEnrollInfo.class);
        if (this.checkTempAcooBankExists(tmpEnroll.getBankAccount()) == false) {
            tmpEnroll.initDefOpenAccoValue();
            this.insert(tmpEnroll);
        }
    }
}
