package com.betterjr.modules.loan.dubbo;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.loan.IscfAssetRegisterService;
import com.betterjr.modules.loan.entity.ScfAssetCheck;
import com.betterjr.modules.loan.entity.ScfAssetRegister;
import com.betterjr.modules.loan.service.ScfAssetCheckService;
import com.betterjr.modules.loan.service.ScfAssetRegisterService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IscfAssetRegisterService.class)
public class AssetRegisterDubboService implements IscfAssetRegisterService {
    protected final Logger logger = LoggerFactory.getLogger(AssetRegisterDubboService.class);

    @Autowired
    private ScfAssetCheckService assetCheckService;

    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    @Autowired
    private ScfAssetRegisterService assetRegisterService;

    @Override
    public String webAddAssetRegister(Map<String, Object> anMap, String anFileList) {

        ScfAssetRegister register = (ScfAssetRegister) RuleServiceDubboFilterInvoker.getInputObj();
        if (false == StringUtils.isBlank(anFileList)) {
            register.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, null));
        }
        return AjaxObject.newOk(assetRegisterService.addRegister(register)).toJson();
    }

    @Override
    public String webFindAssetRegister(String anRequestNo) {
        return AjaxObject.newOk(assetRegisterService.findAssestRegisterByRequestNo(anRequestNo)).toJson();
    }

    @Override
    public String webAddAssetCheck(Map<String, Object> anMap, String anFileList) {
        ScfAssetCheck check = (ScfAssetCheck) RuleServiceDubboFilterInvoker.getInputObj();
        if (false == StringUtils.isBlank(anFileList)) {
            check.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, null));
        }
        return AjaxObject.newOk(assetCheckService.addAssetCheck(check)).toJson();
    }

    @Override
    public String webFindAssetCheck(String anRequestNo) {
        return AjaxObject.newOk(assetCheckService.findAssestCheckByRequestNo(anRequestNo)).toJson();
    }
}
