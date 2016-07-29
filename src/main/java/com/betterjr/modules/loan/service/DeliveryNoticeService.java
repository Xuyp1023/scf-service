package com.betterjr.modules.loan.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.loan.dao.ScfDeliveryNoticeMapper;
import com.betterjr.modules.loan.entity.ScfDeliveryNotice;

@Service
public class DeliveryNoticeService extends BaseService<ScfDeliveryNoticeMapper, ScfDeliveryNotice> {

    /**
     * 保存修改改
     * @param anNotice
     * @return
     */
    public ScfDeliveryNotice saveModifyEnquiry(ScfDeliveryNotice anNotice, Long anId) {
        BTAssert.notNull(anNotice, "anNotice不能为空");
        
        //检查该数据据是否合法
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("custNo", anNotice.getCustNo());
        map.put("id", anId);
        BTAssert.notNull(selectByClassProperty(ScfDeliveryNotice.class, map), "找不到源数据");
        
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
         BTAssert.notNull(anNotice, "anNotice不能为空");
         anNotice.init();
         this.insert(anNotice);
         return anNotice;
     }

}
