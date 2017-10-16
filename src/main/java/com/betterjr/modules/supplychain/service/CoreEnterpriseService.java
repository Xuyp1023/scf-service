package com.betterjr.modules.supplychain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.KeyAndValueObject;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.betterjr.modules.supplychain.dao.CoreSupplierInfoMapper;
import com.betterjr.modules.supplychain.entity.CoreSupplierInfo;
import com.betterjr.modules.sys.entity.DictItemInfo;

/**
 * 核心企业供应商信息管理
 * @author zhoucy
 *
 */
@Service
public class CoreEnterpriseService extends BaseService<CoreSupplierInfoMapper, CoreSupplierInfo> {

    private static final Logger logger = LoggerFactory.getLogger(CoreEnterpriseService.class);

    /**
     * 查询核心企业的客户号和名字，主要的处理规则是根据数据字典中定义的企业组织代码OperOrg，<br>
     * 查找字典中信息，获得企业的客户号和名字
     * 
     * @return
     */
    private KeyAndValueObject findCoreCustNo() {

        DictItemInfo dictItem = DictUtils.getDictItem("FactorCoreCustInfo", UserUtils.findOperOrg());
        if (dictItem == null) {
            logger.warn("you not Core Customer, can't use the function!");
            return null;
        }
        try {
            logger.info(dictItem.toString());
            return new KeyAndValueObject(Long.parseLong(dictItem.getItemCode()), dictItem.getItemName());
        }
        catch (Exception ex) {
            logger.error("covert String to Long has error " + dictItem.getItemCode());
            return null;
        }
    }

    /**
     * 保存核心企业上传的供应商信息
     * 
     * @param anList
     * @param anOperOrg
     * @return
     */
    public boolean saveUploadSupplierData(List<CoreSupplierInfo> anList, Long anCoreCustNo) {
        if (Collections3.isEmpty(anList)) {

            return true;
        }
        Set<String> distinctBtNo = ReflectionUtils.listToKeySet(anList, "btNo");
        Map<String, Object> termMap = new HashMap<String, Object>();
        termMap.put("coreCustNo", anCoreCustNo);
        termMap.put("btNo", distinctBtNo);
        Map<String, CoreSupplierInfo> mapCoreSupplier = ReflectionUtils.listConvertToMap(this.selectByProperty(termMap),
                "btNo");
        CoreSupplierInfo tmpSupplier;
        for (CoreSupplierInfo scfBank : anList) {
            tmpSupplier = mapCoreSupplier.get(scfBank.getBtNo());
            if (tmpSupplier == null) {
                scfBank.fillDefaultValue();
                this.insert(scfBank);
            } else {
                scfBank.modifyDefaultValue(tmpSupplier);
                this.updateByPrimaryKey(scfBank);
            }
        }
        return true;
    }

    /**
     * 根据资金管理系统中的客户号查找供应商资料。
     * 
     * @param anBtCustNo
     * @return
     */
    public CoreSupplierInfo findByBtCustNo(Long anCoreCustNo, String anBtCustNo) {
        Map<String, Object> termMap = new HashMap<String, Object>();
        termMap.put("coreCustNo", anCoreCustNo);
        termMap.put("btNo", anBtCustNo);
        List<CoreSupplierInfo> coreSupplierlist = this.selectByProperty(termMap);

        return Collections3.getFirst(coreSupplierlist);
    }

    /**
     * 根据核心企业编号和资金管理系统中的客户号，获得客户在系统中的编号
     * 
     * @param anCoreCustNo
     * @param anBtCustNo
     * @return
     */
    public Long findCustNoByBtCustNo(Long anCoreCustNo, String anBtCustNo) {
        CoreSupplierInfo supplier = findByBtCustNo(anCoreCustNo, anBtCustNo);
        if (supplier == null) {

            return 0L;
        } else {

            return supplier.getCustNo();
        }
    }
}