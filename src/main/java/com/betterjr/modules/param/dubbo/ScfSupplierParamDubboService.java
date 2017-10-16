package com.betterjr.modules.param.dubbo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.param.IScfSupplierParamService;
import com.betterjr.modules.param.entity.SupplierParam;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfSupplierParamService.class)
public class ScfSupplierParamDubboService implements IScfSupplierParamService {

    @Autowired
    private CustAccountService custAccountService;

    @Override
    public String webSaveSupplierParam(Map<String, Object> anMap) {
        SupplierParam param = (SupplierParam) RuleServiceDubboFilterInvoker.getInputObj();
        DictUtils.saveObject("SupplierParam", param.getCustNo() + "", "供应商基础设置", param);
        return AjaxObject.newOk("供应商基础设置成功", param).toJson();
    }

    /***
     * 根据供应商所选机构客户号查询供应商参数
     * @param custNo
     * @return
     */
    @Override
    public String webQuerySupplierParam(String anCustNo) {
        SupplierParam param = DictUtils.loadObject("SupplierParam", anCustNo, SupplierParam.class);
        return AjaxObject.newOk("供应商基础设置查询成功", param).toJson();
    }

    /**
     * 核心企业基础参数设置
     * @param anMap
     * @return
     */
    @Override
    public String webSaveCoreParam(Map<String, Object> anMap) {
        DictUtils.saveObject("CoreParam", anMap.get("custNo") + "", "核心企业基础设置", anMap);
        anMap.put("agencyName", getCustName(anMap.get("agencyNo").toString()));
        return AjaxObject.newOk("核心企业基础设置成功", anMap).toJson();
    }

    /***
     * 根据核心企业客户号查询基础设置信息
     * @param custNo
     * @return
     */
    @Override
    public String webQueryCoreParam(String anCustNo) {
        Map<String, Object> param = DictUtils.loadObject("CoreParam", anCustNo, HashMap.class);
        String agencyNo = (String) param.get("agencyNo");
        param.put("agencyName", getCustName(agencyNo));
        return AjaxObject.newOk("核心企业基础设置查询成功", param).toJson();
    }

    public String getCustName(String anAgencyNo) {
        String agencyName = "";
        if (StringUtils.isNotBlank(anAgencyNo)) {
            String[] agencyNoArr = anAgencyNo.split(",");
            for (int i = 0; i < agencyNoArr.length; i++) {
                if (i == 0) {
                    agencyName += custAccountService.queryCustName(Long.parseLong(agencyNoArr[i]));
                } else {
                    agencyName += "," + custAccountService.queryCustName(Long.parseLong(agencyNoArr[i]));
                }
            }
        }
        return agencyName;
    }
}
