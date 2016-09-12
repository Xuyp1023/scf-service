package com.betterjr.modules.loan.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.enquiry.service.ScfOfferService;
import com.betterjr.modules.loan.IScfBillRequestService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.service.ScfBillRequestService;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfBillRequestService.class)
public class RequestBillDubboService implements IScfBillRequestService {
    protected final Logger logger = LoggerFactory.getLogger(IScfBillRequestService.class);
    
    @Autowired
    private ScfOrderService orderService;
    @Autowired
    private ScfBillRequestService billRequestService;
    @Autowired
    private ScfOfferService offerService;
    @Autowired
    private ScfRequestService requestService;

    @Override
    public String webAddBillRequest(Map<String, Object> anMap) {
        logger.debug("新增票据融资申请，入参：" + anMap);
        ScfRequest request = (ScfRequest) RuleServiceDubboFilterInvoker.getInputObj();
        request.setTradeStatus("100");
        request = billRequestService.addBillRequest((ScfRequest) RuleServiceDubboFilterInvoker.getInputObj());
        // 关联订单
        orderService.saveInfoRequestNo(request.getRequestType(), request.getRequestNo(), request.getOrders());
        // 冻结订单
        orderService.forzenInfos(request.getRequestNo(), null);
        return AjaxObject.newOk(request).toJson();
    }

    @Override
    public String webQueryBillRequestList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询票据融资申请，入参：" + anMap);
        return AjaxObject.newOkWithPage("分页查询融资申请成功",
                billRequestService.queryRequestList((Map) RuleServiceDubboFilterInvoker.getInputObj(), anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webToRequest(String anId) {
        logger.debug("获取票据申请数据，入参：" + anId);
        return AjaxObject.newOk(offerService.selectByPrimaryKey(Long.parseLong(anId))).toJson();
    }
    
    @Override
    public String updateRequestTradeStauts(String anRequestNo, String anTradeStatus){
        logger.debug("修改票据申请的融资状态，入参：anTradeStatus=" + anTradeStatus);
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        request.setTradeStatus(anTradeStatus);
        return AjaxObject.newOk(requestService.saveModifyRequest(request, anRequestNo)).toJson();
    }

}
