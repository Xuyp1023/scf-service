package com.betterjr.modules.loan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterException;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.loan.dao.ScfDeliveryNoticeMapper;
import com.betterjr.modules.loan.entity.ScfDeliveryNotice;

@Service
public class ScfDeliveryNoticeService extends BaseService<ScfDeliveryNoticeMapper, ScfDeliveryNotice> {

    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    /**
     * 保存修改改
     * @param anNotice
     * @return
     */
    public ScfDeliveryNotice saveModifyEnquiry(ScfDeliveryNotice anNotice, Long anId, String anFileList) {
        BTAssert.notNull(anNotice, "修改通知单失败-anNotice不能为空");

        if (false == StringUtils.isBlank(anFileList)) {
            anNotice.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, null).toString());
        }

        // 检查该数据据是否合法
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("custNo", anNotice.getCustNo());
        map.put("id", anId);
        if (Collections3.isEmpty(selectByClassProperty(ScfDeliveryNotice.class, map))) {
            throw new BytterTradeException(40001, "修改通知单失败，找不到源数据id:" + anId);
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
    public Page<ScfDeliveryNotice> queryDeliveryNoticeList(Map<String, Object> anMap, int anFlag, int anPageNum,
            int anPageSize) {
        anMap = Collections3.filterMap(anMap,
                new String[] { "businStatus", "custNo", "noticeNo", "GTEregDate", "LTEregDate" });
        if (StringUtils.isEmpty(anMap.get("businStatus").toString())) {
            anMap.put("businStatus", new String[] { "1", "0" });
        }
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, 1 == anFlag);
    }

    /**
     * 查询通知单列表
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public List<Map<String, Object>> getNoticeSelectList(String anFactorNo) {

        Map<String, Object> qyMap = QueryTermBuilder.newInstance().put("businStatus", "0").put("custNo", anFactorNo)
                .build();
        List<ScfDeliveryNotice> notiecList = this.selectByClassProperty(ScfDeliveryNotice.class, qyMap);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ScfDeliveryNotice notice : notiecList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", notice.getNoticeNo());
            map.put("value", notice.getId());
            list.add(map);
        }
        return list;
    }

    /**
     * 新增通知单
     * @param anNotice
     * @return
     */
    public ScfDeliveryNotice addDeliveryNotice(ScfDeliveryNotice anNotice, String anFileList) {
        BTAssert.notNull(anNotice, "新增通知单失败anNotice不能为空");
        anNotice.init();
        if (false == StringUtils.isBlank(anFileList)) {
            anNotice.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, null).toString());
        }
        this.insert(anNotice);
        return anNotice;
    }

    public void saveRelationRequest(String anRequestNo, String anIds) {
        List<String> list = BetterStringUtils.splitTrim(anIds);
        for (String id : list) {
            ScfDeliveryNotice delivery = selectByPrimaryKey(id);
            if (StringUtils.equals("1", delivery.getBusinStatus())) {
                throw new BytterException("通知单已使用NoticeNo:-" + delivery.getNoticeNo());
            }

            delivery.setRequestNo(anRequestNo);
            delivery.setBusinStatus("1");
            saveModifyEnquiry(delivery, Long.parseLong(id), null);
        }
    }

}
