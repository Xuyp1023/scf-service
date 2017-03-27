package com.betterjr.modules.supplychain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.KeyAndValueObject;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.supplychain.dao.ScfSupplierBankMapper;
import com.betterjr.modules.supplychain.entity.ScfSupplierBank;

/**
 * 供应商银行账户管理
 *
 * @author zhoucy
 *
 */
@Service
public class ScfSupplierBankService extends BaseService<ScfSupplierBankMapper, ScfSupplierBank> {

    private static final Logger logger = LoggerFactory.getLogger(ScfSupplierBankService.class);

    public List<ScfSupplierBank> findScfSupplierBankList() {
        logger.info("Begin to find supplier bank information list.");
        final String operOrg = UserUtils.findOperOrg();
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("operOrg", operOrg);
        map.put("accountStatus", "1");
        final List<ScfSupplierBank> bankList = this.selectByProperty(map);
        return bankList;
    }

    /**
     * 根据客户号查询客户银行账户信息
     *
     * @param anCustNo
     * @return
     */
    public List<ScfSupplierBank> findSupplierBankByCustNo(final Long anCustNo) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("custNo", anCustNo);
        map.put("accountStatus", "1");
        final List<ScfSupplierBank> bankList = this.selectByProperty(map);
        return bankList;
    }

    /**
     * 根据核心企业编号和资金管理系统中的客户号，获得客户在系统中的编号
     *
     * @param anCoreCustNo
     * @param anBtCustNo
     * @return
     */
    public ScfSupplierBank findCustNoByBtCustNo(final String anCoreOperOrg, final String anBtCustNo) {
        final Map<String, Object> termMap = new HashMap();
        termMap.put("coreOperOrg", anCoreOperOrg);
        termMap.put("btNo", anBtCustNo);
        final ScfSupplierBank supplier = Collections3.getFirst(this.selectByProperty(termMap));
        logger.info("findCustNoByBtCustNo anCoreOperOrg=" + anCoreOperOrg + ", anBtCustNo = " + anBtCustNo + ", " + supplier);
        if (supplier == null) {

            // 表示不存在关联关系
            return null;
        }
        else {

            return supplier;
        }
    }

    /**
     * 根据机构和银行账户获得供应商的银行账户信息
     *
     * @param anBankAccount
     * @return
     */
    public List<ScfSupplierBank> findCustByBankAccount(final String anOrg, final String anBankAccount) {
        if (BetterStringUtils.isBlank(anOrg) || BetterStringUtils.isBlank(anBankAccount)) {

            throw new BytterValidException("findCustNoByBankAccount the openorg or bankAccount is null");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("operOrg", anOrg);
        map.put("bankAccount", anBankAccount);
        final List<ScfSupplierBank> bankList = this.selectByProperty(map);
        if (Collections3.isEmpty(bankList)) {

            return new ArrayList(0);
        }
        else {

            return bankList;
        }
    }

    /**
     * 保存供应商银行账户的关系信息；该方法用于产生数字证书信息时使用<br>
     * 首先找到银行账户对应的核心企业的资金管理系统中的btNo，然后根据btNo 和核心企业编号来更新企业的组织机构代码
     *
     * @param anBankAccount
     *            客户银行账户
     * @param anOperOrg
     *            操作员所在机构
     * @return 成功更新，则返回更新的银行账户信息 ;
     */
    public Set<ScfSupplierBank> saveCustOperOrg(final Long anCoreCustNo, final String anBankAccount, final String anOperOrg) {
        final Map<String, Object> termMap = new HashMap();
        termMap.put("coreCustNo", anCoreCustNo);
        termMap.put("bankAccount", anBankAccount);
        final List<ScfSupplierBank> workData = this.selectByProperty(termMap);
        if (Collections3.isEmpty(workData)) {

            throw new BytterTradeException("使用  核心企业客户号 " + anCoreCustNo +", 以及供应商的银行账户 " + anBankAccount +", 不能获得供应商信息");
        }

        return saveCustOperOrgAndCustNo(workData, anOperOrg, false);
    }

    private Set<ScfSupplierBank> saveCustOperOrgAndCustNo(final List<ScfSupplierBank> anTermList, final Object anValue, final boolean anCustNo) {
        if (Collections3.isEmpty(anTermList)) {

            return new HashSet(0);
        }
        final Set<KeyAndValueObject> workKeySet = new HashSet();
        for (final ScfSupplierBank spBank : anTermList) {
            workKeySet.add(new KeyAndValueObject(spBank.getBtNo(), spBank.getCoreCustNo()));
        }
        final Map<String, Object> termMap = new HashMap();
        final Set<ScfSupplierBank> result = new HashSet();
        for (final KeyAndValueObject keyAndValue : workKeySet) {
            termMap.clear();
            termMap.put("btNo", keyAndValue.getKey());
            termMap.put("coreCustNo", keyAndValue.getValue());
            for (final ScfSupplierBank spBank : this.selectByProperty(termMap)) {
                if (anCustNo) {
                    spBank.setCustNo((Long) anValue);
                }
                else {
                    spBank.setOperOrg((String) anValue);
                }
                this.updateByPrimaryKey(spBank);
                result.add(spBank);
            }
        }

        return result;
    }

    /**
     * 用于增加客户信息中使用。处理逻辑同saveCustOperOrg
     *
     * @param anBankAccount
     * @param anOperOrg
     * @param anCustNo
     * @return
     */
    public Set<ScfSupplierBank> saveCustNoByBankAccount(final String anBankAccount, final String anOperOrg, final Long anCustNo) {

        return saveCustOperOrgAndCustNo(findCustByBankAccount(anBankAccount, anOperOrg), anCustNo, true);
    }

    /**
     * 根据核心企业客户号和供应商银行账户信息，获得银行账户信息
     * @param anCoreCustNo 核心企业客户号
     * @param anBankAccount 供应商银行账户
     * @return
     */
    public ScfSupplierBank findScfBankByBankAccount(final String anCoreOperOrg, final String anBankAccount) {
        if (anCoreOperOrg != null && BetterStringUtils.isNotBlank(anBankAccount)) {
            final Map<String, Object> termMap = new HashMap();
            termMap.put("bankAccount", anBankAccount);
            termMap.put("coreOperOrg", anCoreOperOrg);

            return Collections3.getFirst(this.selectByProperty(termMap));
        }

        return null;
    }

    public List<ScfSupplierBank> findScfBankByCoreOperOrg(final Long anCoreOperOrg){

        return this.selectByProperty("coreOperOrg", anCoreOperOrg);
    }
}
