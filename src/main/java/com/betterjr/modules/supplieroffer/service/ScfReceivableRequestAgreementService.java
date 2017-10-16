package com.betterjr.modules.supplieroffer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.supplieroffer.dao.ScfReceivableRequestAgreementMapper;
import com.betterjr.modules.supplieroffer.data.AgreementConstantCollentions;
import com.betterjr.modules.supplieroffer.entity.ScfReceivableRequest;
import com.betterjr.modules.supplieroffer.entity.ScfReceivableRequestAgreement;

@Service
public class ScfReceivableRequestAgreementService
        extends BaseService<ScfReceivableRequestAgreementMapper, ScfReceivableRequestAgreement> {

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;

    @Autowired
    private CustAccountService custAccountService;

    // 查询
    /**
     * 核心企业查询合同信息
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfReceivableRequestAgreement> queryAgreementWithCore(Map<String, Object> anMap, String anFlag,
            int anPageNum, int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        if (!anMap.containsKey("coreCustNo")) {
            anMap.put("coreCustNo", getCurrentUserCustNos());
        }
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap, new String[] { "custNo", "coreCustNo", "agreementType" });
        /* anMap.put("NEbusinStatus", "3"); */
        List<String> list = new ArrayList<>();
        list.add("0");
        list.add("3");
        anMap.put("NEbusinStatus", list);
        Page<ScfReceivableRequestAgreement> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize,
                "1".equals(anFlag), "id desc");
        return page;
    }

    /**
     * 查询供应商查询合同模版信息
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfReceivableRequestAgreement> queryAgreementWithSupplier(Map<String, Object> anMap, String anFlag,
            int anPageNum, int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        if (!anMap.containsKey("custNo")) {
            anMap.put("custNo", getCurrentUserCustNos());
        }
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap, new String[] { "custNo", "coreCustNo", "agreementType" });
        List<String> list = new ArrayList<>();
        list.add("0");
        list.add("3");
        anMap.put("NEbusinStatus", list);
        Page<ScfReceivableRequestAgreement> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize,
                "1".equals(anFlag), "id desc");
        return page;
    }

    /**
     * 获取当前登录用户所在的所有公司id集合
     * 
     * @return
     */
    private List<Long> getCurrentUserCustNos() {

        CustOperatorInfo operInfo = UserUtils.getOperatorInfo();
        BTAssert.notNull(operInfo, "查询可用资产失败!请先登录");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        BTAssert.notNull(custInfos, "查询可用资产失败!获取当前企业失败");
        List<Long> custNos = new ArrayList<>();
        for (CustInfo custInfo : custInfos) {
            custNos.add(custInfo.getId());
        }
        return custNos;
    }

    /**
     * 通过融资请求新增电子合同信息
     * 
     * @param anRequest
     * @return 1: 模式1 2 表示模式2签署的合同
     */
    public ScfReceivableRequestAgreement saveAddCoreAgreementByRequest(ScfReceivableRequest anRequest,
            String agreementType) {

        ScfReceivableRequestAgreement agreement = new ScfReceivableRequestAgreement();
        agreement.saveAddValue(UserUtils.getOperatorInfo());
        // 需要查询当前合同模版的文件id
        agreement.setAgreementTemplateId(findAgreementTemplate(true));
        agreement.setBalance(anRequest.getRequestPayBalance());
        agreement.setCoreCustName(anRequest.getCoreCustName());
        agreement.setCoreCustNo(anRequest.getCoreCustNo());
        agreement.setCustNo(anRequest.getCustNo());
        agreement.setCustName(anRequest.getCustName());
        agreement.setReceivableRequestNo(anRequest.getRequestNo());
        if (anRequest.getFactoryNo() != null) {
            agreement.setFactoryNo(anRequest.getFactoryNo());
        }
        if (StringUtils.isNoneBlank(anRequest.getFactoryName())) {
            agreement.setFactoryName(anRequest.getFactoryName());
        }

        agreement.setAgreementType(agreementType);
        agreement.setAgreementName("应收账款提前付款电子合同");
        if ("2".equals(agreementType)) {
            agreement.setAgreementName("应收账款转让合同");
            agreement.setCoreCustNo(anRequest.getFactoryNo());
            agreement.setCoreCustName(anRequest.getFactoryName());
        }
        this.insertSelective(agreement);
        anRequest.setCoreAgreement(agreement);
        anRequest.setCoreAgreementId(agreement.getId());
        return agreement;
    }

    public ScfReceivableRequestAgreement saveAddPlatAgreementByRequest(ScfReceivableRequest anRequest) {

        ScfReceivableRequestAgreement agreement = new ScfReceivableRequestAgreement();
        agreement.saveAddValue(UserUtils.getOperatorInfo());
        // 需要查询当前合同模版的文件id
        agreement.setAgreementTemplateId(findAgreementTemplate(false));
        agreement.setBalance(anRequest.getRequestPayPlatBalance());
        agreement.setCoreCustNo(findPlatCustInfo());
        agreement.setCoreCustName(custAccountService.queryCustName(agreement.getCoreCustNo()));
        agreement.setCustNo(anRequest.getCustNo());
        agreement.setCustName(anRequest.getCustName());
        agreement.setAgreementName("企e金服服务合同");
        agreement.setReceivableRequestNo(anRequest.getRequestNo());
        agreement.setAgreementType("0");
        this.insertSelective(agreement);
        anRequest.setPlatAgreement(agreement);
        anRequest.setPlatAgreementId(agreement.getId());
        return agreement;
    }

    /**
     * 供应商签署合同
     * 
     * @param anRequest
     */
    public void saveSupplierSignAgreement(ScfReceivableRequest anRequest) {

        ScfReceivableRequestAgreement coreAgreement = this.selectByPrimaryKey(anRequest.getCoreAgreementId());
        coreAgreement.saveSignValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(coreAgreement);
        anRequest.setCoreAgreement(coreAgreement);

        ScfReceivableRequestAgreement platAgreement = this.selectByPrimaryKey(anRequest.getPlatAgreementId());
        platAgreement.saveSignValue(UserUtils.getOperatorInfo());
        platAgreement.setCoreDate(BetterDateUtils.getNumDate());
        platAgreement.setCoreTime(BetterDateUtils.getNumTime());
        platAgreement.setBusinStatus(AgreementConstantCollentions.AGREMENT_BUSIN_STATUS_CORE_SIGNED);
        this.updateByPrimaryKeySelective(platAgreement);
        anRequest.setPlatAgreement(platAgreement);
    }

    /**
     * 核心企业签署电子合同
     * 
     * @param anRequest
     */
    public void saveCoreSignAgreement(ScfReceivableRequest anRequest) {

        ScfReceivableRequestAgreement coreAgreement = this.selectByPrimaryKey(anRequest.getCoreAgreementId());
        coreAgreement.saveCoreSignValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(coreAgreement);
        anRequest.setCoreAgreement(coreAgreement);
    }

    /**
     * 废止合同状态
     * 
     * @param anRequest
     */
    public void saveAnnulAgreement(ScfReceivableRequest anRequest) {

        ScfReceivableRequestAgreement coreAgreement = this.selectByPrimaryKey(anRequest.getCoreAgreementId());
        coreAgreement.setBusinStatus(AgreementConstantCollentions.AGREMENT_BUSIN_STATUS_ANNUL);
        this.updateByPrimaryKeySelective(coreAgreement);

        ScfReceivableRequestAgreement platAgreement = this.selectByPrimaryKey(anRequest.getCoreAgreementId());
        platAgreement.setBusinStatus(AgreementConstantCollentions.AGREMENT_BUSIN_STATUS_ANNUL);
        this.updateByPrimaryKeySelective(platAgreement);
    }

    /**
     * 查询平台的企业信息
     * 
     * @return
     */
    public Long findPlatCustInfo() {

        return custMechBaseService.findPlatCustNo();

    }

    /**
     * 查找企业的合同模版
     * 
     * @return
     */
    private Long findAgreementTemplate(boolean isCore) {

        return 0l;

    }

    public void saveFactorySignAgreement(ScfReceivableRequest anRequest) {

        ScfReceivableRequestAgreement coreAgreement = this.selectByPrimaryKey(anRequest.getCoreAgreementId());
        coreAgreement.saveFactorySignValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(coreAgreement);
        anRequest.setCoreAgreement(coreAgreement);

    }

    public List<SimpleDataEntity> queryFactory() {
        final List<SimpleDataEntity> result = new ArrayList<SimpleDataEntity>();

        for (ScfReceivableRequestAgreement agreement : this.mapper.queryDictFactory()) {
            if (agreement != null && agreement.getFactoryNo() != null
                    && StringUtils.isNoneBlank(agreement.getFactoryName())) {
                result.add(new SimpleDataEntity(agreement.getFactoryName(), String.valueOf(agreement.getFactoryNo())));
            }
        }
        return result;
    }

}
