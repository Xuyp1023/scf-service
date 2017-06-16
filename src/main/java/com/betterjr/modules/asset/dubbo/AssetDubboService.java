package com.betterjr.modules.asset.dubbo;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.asset.IScfAssetService;
import com.betterjr.modules.asset.service.ScfAssetService;

@Service(interfaceClass =IScfAssetService.class)
public class AssetDubboService implements IScfAssetService {

    @Autowired
    private ScfAssetService assetService;
    @Override
    public String webFindAsset(Map<String, Object> anMap) {
        
        Long assetId=Long.parseLong(anMap.get("assetId").toString());
        return AjaxObject.newOk("查询成功", assetService.findAssetByid(assetId)).toJson();
        
    }
    @Override
    public String webQueryCanUseBaseData(Long anCustNo, String anDataType, int anPageNum, int anPageSize, String anFlag) {
        
        return AjaxObject
                .newOkWithPage("资产基础信息查询成功", assetService.queryFinanceBaseDataList(anCustNo, anDataType, anFlag, anPageNum, anPageSize))
                .toJson();
    }
    @Override
    public String webFindAssetById(Long anAssetId) {
        
        return AjaxObject.newOk("查询成功", assetService.findAssetByid(anAssetId)).toJson();
    }

}
