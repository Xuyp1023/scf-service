package com.betterjr.modules.push.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.push.dao.ScfSupplierPushMapper;
import com.betterjr.modules.push.entity.ScfSupplierPush;
import com.betterjr.modules.push.entity.ScfSupplierPushDetail;

@Service
public class ScfSupplierPushService extends BaseService<ScfSupplierPushMapper, ScfSupplierPush> {

    private static final Logger logger = LoggerFactory.getLogger(ScfSupplierPushService.class);
    
    @Autowired
    private ScfSupplierPushDetailService supplierPushDetailService;
    
    public boolean pushSupplierInfo(Map<String, Object> anMap){
        boolean bool=false;
        try {
            // 先根据需要获取摄推送的企业列表，从刘万林那边接口中获取，也可能是多个机构。这里先暂时写死
            anMap.put("agencyNo", "123456");
            ScfSupplierPushDetail supplierPushDetail=new ScfSupplierPushDetail();
            supplierPushDetail.initDefValue(anMap);
            if(supplierPushDetailService.addPushDetail(supplierPushDetail)){
                ScfSupplierPush supplierPush=new ScfSupplierPush();
                supplierPush.initDefValue(Long.parseLong(anMap.get("coreCustNo").toString()),Long.parseLong(anMap.get("supplierNo").toString()),supplierPushDetail.getId());
                this.insert(supplierPush);
                bool=true;
            }
        }
        catch (Exception e) {
            logger.error("异常："+e.getMessage());
            bool=false;
        }
        return bool;
    }
    
    /***
     * 修改放弃原因
     * @param anPushId 推送信息的编号
     * @param anRemarkId 放弃原因的列表 ID
     * @param anRemark   放弃
     * @return
     */
    public boolean saveSupplierInfo(Long anPushId,String anRemarkId,String anRemark){
        logger.info("入参："+anPushId+"，"+anRemarkId+"，"+anRemark);
        boolean bool=false;
        try {
            ScfSupplierPush supplierPush=this.selectByPrimaryKey(anPushId);
            supplierPush.setRemark(anRemark);
            supplierPush.setRemarkId(anRemarkId);
            return this.updateByPrimaryKey(supplierPush)>0;
        }
        catch (Exception e) {
            logger.error("异常："+e.getMessage());
            bool=false;
        }
        return bool;
    }
    
}
