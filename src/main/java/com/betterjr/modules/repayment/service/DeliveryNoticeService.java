package com.betterjr.modules.repayment.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.repayment.dao.ScfDeliveryNoticeMapper;
import com.betterjr.modules.repayment.entity.ScfDeliveryNotice;

@Service
public class DeliveryNoticeService extends BaseService<ScfDeliveryNoticeMapper, ScfDeliveryNotice> {

    /**
     * 保存修改改
     * @param notice
     * @return
     */
    public int saveModifyEnquiry(ScfDeliveryNotice notice) {
        notice.setUpdateBaseInfo();
        return this.updateByPrimaryKeySelective(notice);
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
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1==anFlag);
     }

    /**
     * 新增通知单
     * @param notice
     * @return
     */
     public int addDeliveryNotice(ScfDeliveryNotice notice) {
         notice.init();
         return this.insert(notice);

     }

}
