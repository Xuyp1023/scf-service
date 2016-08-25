package com.betterjr.modules.loan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.loan.dao.ScfDeliveryNoticeMapper;
import com.betterjr.modules.loan.entity.ScfDeliveryNotice;

@Service
public class ScfDeliveryNoticeService extends BaseService<ScfDeliveryNoticeMapper, ScfDeliveryNotice> {

    /**
     * 保存修改改
     * @param anNotice
     * @return
     */
    public ScfDeliveryNotice saveModifyEnquiry(ScfDeliveryNotice anNotice, Long anId) {
        BTAssert.notNull(anNotice, "修改通知单失败-anNotice不能为空");
        
        //检查该数据据是否合法
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("custNo", anNotice.getCustNo());
        map.put("id", anId);
        if(Collections3.isEmpty(selectByClassProperty(ScfDeliveryNotice.class, map))){
            throw new IllegalArgumentException("修改通知单失败，找不到源数据id:"+anId);
        }
        
        anNotice.initModify();
        anNotice.setId(anId);
        this.updateByPrimaryKeySelective(anNotice);
        return anNotice;
    }
    
    /**
     * 查询通知单列表
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfDeliveryNotice> queryDeliveryNoticeList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        if(BetterStringUtils.isEmpty(anMap.get("businStatus").toString())){
            anMap.put("businStatus", new String[]{"1","0"});
        }
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1==anFlag);
     }

    /**
     * 新增通知单
     * @param anNotice
     * @return
     */
     public ScfDeliveryNotice addDeliveryNotice(ScfDeliveryNotice anNotice) {
         BTAssert.notNull(anNotice, "新增通知单失败anNotice不能为空");
         anNotice.init();
         this.insert(anNotice);
         return anNotice;
     }
     
     public void saveRelationRequest(String anRequestNo, String anIds){
         anIds.split(",");
         List<String> list = BetterStringUtils.splitTrim(anIds);
         for (String id : list) {
             ScfDeliveryNotice delivery = selectByPrimaryKey(id);
             if(BetterStringUtils.equals("1", delivery.getBusinStatus())){
                 throw new BytterException("通知单已使用NoticeNo:-"+delivery.getNoticeNo());
             }
             
             delivery.setRequestNo(anRequestNo);
             delivery.setBusinStatus("1");
             saveModifyEnquiry(delivery, Long.parseLong(id));
        }
     }

}