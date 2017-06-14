package com.betterjr.modules.asset.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.asset.IScfAssetService;
import com.betterjr.modules.asset.service.ScfAssetService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass =IScfAssetService.class)
public class AssetDubboService implements IScfAssetService {

    @Autowired
    private ScfAssetService assetService;
    @Override
    public String webFindAsset(Map<String, Object> anMap) {
        
        //Map map = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        Long assetId=Long.parseLong(anMap.get("assetId").toString());
        Long custNo=Long.parseLong(anMap.get("custNo").toString());
        boolean onOff=Boolean.parseBoolean(anMap.get("onOff").toString());
        return AjaxObject.newOk("查询成功", assetService.findAssetByid(assetId, custNo, onOff)).toJson();
        
    }
    @Override
    public String webQueryCanUseBaseData(Long anCustNo, String anDataType, int anPageNum, int anPageSize, String anFlag) {
        
        return AjaxObject
                .newOkWithPage("资产基础信息查询成功", assetService.queryFinanceBaseDataList(anCustNo, anDataType, anFlag, anPageNum, anPageSize))
                .toJson();
    }

}
