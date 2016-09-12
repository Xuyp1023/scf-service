package com.betterjr.modules.loan.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.loan.dao.ScfRequestMapper;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.TransRequest;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.order.helper.ScfOrderRelationType;

@Service
public class ScfBillRequestService extends BaseService<ScfRequestMapper, ScfRequest> {
    
    private ScfRequestService requestService;
    
    /**
     * 新增融资申请
     * 
     * @param anRequest
     * @return
     */
    public ScfRequest addBillRequest(ScfRequest anRequest) {
        BTAssert.notNull(anRequest, "新增融资申请失败-anRequest不能为空");
        anRequest.setRequestType(ScfOrderRelationType.ORDER.getCode());
        anRequest.setPeriodUnit(1);
        anRequest.setRequestDate(BetterDateUtils.getNumDate());
        anRequest.setTradeStatus(RequestTradeStatus.REQUEST.getCode());
        return requestService.addRequest(anRequest);
    }
    
    /**
     * 查询融资申请列表
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<TransRequest> queryRequestList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        Page<ScfRequest> page = requestService.queryRequestList(anMap, anFlag, anPageNum, anPageSize);
        Page<TransRequest> billPage = new Page<>(anPageNum, anPageSize, anFlag == 1);
        for (ScfRequest scfRequest : page) {
            TransRequest transRequest = new TransRequest();
            transRequest.setRequest(scfRequest);
            transRequest.setOrder(requestService.fillOrderInfo(scfRequest));
            billPage.add(transRequest);
        }
        return billPage;
    }
}
