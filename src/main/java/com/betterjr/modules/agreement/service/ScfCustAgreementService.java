package com.betterjr.modules.agreement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.dao.CustAgreementMapper;
import com.betterjr.modules.agreement.data.ScfSupplierAgreement;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.agreement.utils.SupplyChainUtil;
import com.betterjr.modules.customer.ICustMechBankAccountService;
import com.betterjr.modules.customer.entity.CustMechBankAccount;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;

/***
 * 
 * @author hubl
 *
 */
@Service
public class ScfCustAgreementService extends BaseService<CustAgreementMapper, CustAgreement>
        implements IScfOrderInfoCheckService {

    @Autowired
    private CustAccountService custAccoService;
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileService;
    @Reference(interfaceClass = ICustMechBankAccountService.class)
    private ICustMechBankAccountService custMechBankAccountService;

    /**
     * 查询用户合同分页信息
     * 
     * @param anParam
     * @return
     */
    public Page<CustAgreement> queryCustAgreementsByPage(Map<String, Object> anParam, int anPageNum, int anPageSize) {
        logger.info("Begin get customer agreement list." + anParam);
        Map<String, Object> map = new HashMap();
        SupplyChainUtil.addCondition(anParam, map);
        if (UserUtils.factorUser()) { // 如果当前登录是保理公司，则需要增加保理客户号的查询条件
            map.put("factorNo", UserUtils.getDefCustInfo().getCustNo());
        } else {
            // 获取当前登录操作员信息
            map.put("operOrg", UserUtils.findOperOrg());
        }
        Page<CustAgreement> agreements = this.selectPropertyByPage(map, anPageNum, anPageSize,
                "1".equals(anParam.get("flag")), "id desc");

        return agreements;
    }

    /**
     * 添加供应商合同
     * 
     * @param anMap
     * @param fileList
     * @return
     */
    public CustAgreement addCustAgreement(CustAgreement anCustAgreement, String anFileList) {
        // 初始化合同默认值
        anCustAgreement.initDefValue(UserUtils.getOperatorInfo(), anCustAgreement.getBuyerNo(),
                custAccoService.queryCustName(anCustAgreement.getBuyerNo()),
                custAccoService.queryCustName(anCustAgreement.getSupplierNo()));
        if (StringUtils.isNotBlank(anCustAgreement.getIsDeleted())) {
            anCustAgreement.setDefaultFlag(anCustAgreement.getIsDeleted());
            anCustAgreement.setStatus("1");// 当从初录添加的贸易合同，默认为已启用状态
        }
        // 保存合同附件信息
        anCustAgreement.setBatchNo(custFileService.updateCustFileItemInfo(anFileList, anCustAgreement.getBatchNo()));

        // 保存客户合同信息
        this.insert(anCustAgreement);
        return anCustAgreement;
    }

    /*****
     * 添加系统默认的贸易合同信息
     * @param anCoreCustNo 核心企业客户号
     * @param anSupplierNo 供应商客户号
     * @param anFactorNo   保理公司客户号
     * @param anFileList   附件列表(多附件以,号分隔)
     * @return 贸易合同信息
     */
    public CustAgreement addSysCustAgreement(Long anCoreCustNo, Long anSupplierNo, Long anFactorNo, String anFileList) {
        Map<String, Object> anMap = new HashMap();
        anMap.put("buyerNo", anCoreCustNo);
        anMap.put("supplierNo", anSupplierNo);
        anMap.put("factorNo", anFactorNo);
        anMap.put("status", "0");
        CustAgreement custAgreement = findCustAgreement(anMap);
        if (custAgreement == null) {
            custAgreement = new CustAgreement();
            // 获取客户银行账号信息
            CustMechBankAccount bankAccount = custMechBankAccountService.findDefaultBankAccount(anSupplierNo);
            custAgreement.initSysValue(UserUtils.getOperatorInfo(), anCoreCustNo,
                    custAccoService.queryCustName(anCoreCustNo), anSupplierNo,
                    custAccoService.queryCustName(anSupplierNo), anFactorNo, bankAccount);// 保存合同附件信息
            if (StringUtils.isNotBlank(anFileList)) {
                custAgreement
                        .setBatchNo(custFileService.updateCustFileItemInfo(anFileList, custAgreement.getBatchNo()));
            }
            // 保存客户合同信息
            this.insert(custAgreement);
        }
        return custAgreement;
    }

    public CustAgreement findCustAgreement(Map<String, Object> anMap) {
        List<CustAgreement> custAgreementList = this.selectByProperty(anMap);
        return Collections3.getFirst(custAgreementList);
    }

    /**
     * 根据合同ID获取合同明细
     * 
     * @param anParam
     * @return
     */
    public CustAgreement findCustAgreementDetail(Long anAgreeId) {
        logger.info("Begin to get customer agreement detail, agree id:" + anAgreeId);
        CustAgreement anAgreement = this.selectByPrimaryKey(anAgreeId);

        return anAgreement;
    }

    /**
     * 删除关联的合同附件信息
     * @param anId
     * @param anBillId
     */
    public void deleteFileItem(Long anId, Long anAgreeId) {
        CustAgreement agree = this.selectByPrimaryKey(anAgreeId);
        if (agree == null) {

            return;
        }

        this.custFileService.deleteFileItem(anId, agree.getBatchNo());
    }

    public boolean deleteContractAgree(Long anAgreeId) {
        return this.deleteByPrimaryKey(anAgreeId) > 0;
    }

    /**
     * 设置指定客户合同状态
     * 
     * @param anAgreeId
     */
    public void saveCustAgreementStatus(Long anAgreeId, int anType) {
        logger.info("Begin to saveCustAgreementStatus customer agreement, agree id:" + anAgreeId);
        CustAgreement tmpAgreement = this.selectByPrimaryKey(anAgreeId);
        String reqStatus = String.valueOf(anType);
        if (null == tmpAgreement) {

            return;
        }

        if (tmpAgreement.getStatus().compareTo(reqStatus) > 0) {

            throw new BytterTradeException(40001, "合同状态变化错误！");
        } else if (tmpAgreement.getStatus().compareTo(reqStatus) < 0) {
            if ("1".equals(reqStatus) && (tmpAgreement.getBatchNo() == null)) {

                throw new BytterTradeException(40001, "合同没有相关附件，不能启用！");
            }
        } else {
            return;
        }

        tmpAgreement.setStatus(reqStatus);
        this.updateByPrimaryKey(tmpAgreement);

        logger.debug(
                "saveCustAgreementStatus customer agreement to status " + anType + "， successful with id " + anAgreeId);
    }

    /**
     * 更新客户合同信息
     * 
     * @param anParam
     * @param fileList
     * @return 更新后的客户合同信息
     */
    public CustAgreement modifyCustAgreement(CustAgreement anCustAgreement, Long anId, String anFileList) {
        logger.info("Begin to update customer agreement.");
        logger.info("update param:" + anCustAgreement);
        CustAgreement tmpAgreement = this.selectByPrimaryKey(anId);

        // 只更新数据，不能更新状态，而且只有未启用的合同才能更新
        if ("0".equals(tmpAgreement.getStatus()) == false) {

            throw new BytterTradeException(40001, "已启用或废止的合同不能修改！");
        }
        CustAgreement reqAgreement = anCustAgreement;
        reqAgreement.modifyAgreement(tmpAgreement);

        reqAgreement.setBatchNo(custFileService.updateCustFileItemInfo(anFileList, reqAgreement.getBatchNo()));
        // 保存附件信息

        this.updateByPrimaryKey(reqAgreement);

        return reqAgreement;
    }

    /**
     * 根据批次号获取合同附件列表
     * 
     * @param anParam
     * @return
     */
    public List<CustFileItem> findCustFileItems(Long id) {
        logger.info("Begin get customer agreement accessory list, id:" + id);
        CustAgreement anAgree = this.selectByPrimaryKey(id);
        if (null == anAgree) {

            throw new BytterTradeException(40001, "无法获取合同信息！");
        }

        return custFileService.findCustFiles(anAgree.getBatchNo());
    }

    /**
     * 查询融资申请的合同信息
     * 
     * @param anBillNoMap
     *            ,键是合同号，值是票据信息
     * @return
     */
    public List<ScfSupplierAgreement> findScfRequestAgree(Map<Long, String> anBillNoMap) {
        List list = new ArrayList();
        for (Map.Entry<Long, String> ent : anBillNoMap.entrySet()) {
            CustAgreement supplierAgree = this.selectByPrimaryKey(ent.getKey());
            if (supplierAgree != null) {
                list.add(ScfSupplierAgreement.createInstance(supplierAgree, ent.getValue()));
            }
        }

        return list;
    }

    /**
     * 获取当前票据对应的已启用合同列表 <br>
     * 
     * @return
     */
    public List<SimpleDataEntity> findRelativeAgreeList() {
        logger.info("Begin to get relative agreement list.");
        List<SimpleDataEntity> tmpList = new ArrayList<SimpleDataEntity>();
        String tmpOperOrg = UserUtils.findOperOrg();
        if (StringUtils.isBlank(tmpOperOrg)) {

            return tmpList;
        }

        Map<String, Object> map = new HashMap();
        logger.debug("operator org:" + tmpOperOrg);
        map.put("operOrg", tmpOperOrg);
        map.put("status", "1");
        List<CustAgreement> agreeList = this.selectByProperty(map);
        logger.debug("agree list size:" + agreeList.size());
        SimpleDataEntity tmpData;
        for (CustAgreement agree : agreeList) {
            logger.debug("agree no:" + agree.getAgreeNo() + " agree name:" + agree.getAgreeName());
            tmpData = new SimpleDataEntity(Long.toString(agree.getId()), agree.getAgreeName());
            tmpList.add(tmpData);
        }

        return tmpList;
    }

    /**
     * 根据合同ID查找合同号信息
     * 
     * @param anAgreeId
     * @return
     */
    public String findAgreeNoById(String anAgreeId) {

        return findAgreeNoById(anAgreeId, false);
    }

    /**
     * 根据合同号，获得合同对应的文件批次号
     * @param anAgreeId 合同号
     * @return
     */
    public Long findBatchNoById(Long anAgreeId) {
        try {
            if (anAgreeId == null) {
                return Long.MIN_VALUE;
            } else {
                return Long.parseLong(findAgreeNoById(Long.toString(anAgreeId), true));
            }
        }
        catch (Exception ex) {

            return new Long(Integer.MIN_VALUE);
        }
    }

    private String findAgreeNoById(String anAgreeId, boolean anBatchNo) {
        try {
            Long agrId = Long.parseLong(anAgreeId);
            if (agrId > 0) {
                CustAgreement agree = this.selectByPrimaryKey(agrId);
                if (anBatchNo) {
                    if (agree.getBatchNo() != null && agree.getBatchNo() > 0) {

                        return Long.toString(agree.getBatchNo());
                    }
                } else {
                    return agree.getAgreeNo();
                }
            }
        }
        catch (Exception ex) {
            logger.error("findAgreeNoById has error", ex);
        }

        return "";
    }

    @Override
    public void checkInfoExist(Long anId, String anOperOrg) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("id", anId);
        List<CustAgreement> custAgreementList = this.selectByProperty(anMap);
        if (Collections3.isEmpty(custAgreementList)) {
            logger.error("不存在相对应合同编号,操作机构的合同");
            throw new BytterTradeException(40001, "不存在相对应合同编号,操作机构的合同");
        }
    }

}
