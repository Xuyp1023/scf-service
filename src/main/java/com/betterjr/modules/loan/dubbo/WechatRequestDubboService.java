package com.betterjr.modules.loan.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.enquiry.service.ScfOfferService;
import com.betterjr.modules.loan.IScfWechatRequestService;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.service.WechatRequestService;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfWechatRequestService.class)
public class WechatRequestDubboService implements IScfWechatRequestService {
    protected final Logger logger = LoggerFactory.getLogger(IScfWechatRequestService.class);
    
    @Autowired
    private WechatRequestService wechatRequestService;
    @Autowired
    private ScfOfferService offerService;
    @Autowired
    private ScfRequestService requestService;

    @Override
    public String webAddRequest(Map<String, Object> anMap) {
        logger.debug("微信版-新增票据融资申请，入参：" + anMap);
        ScfRequest request = (ScfRequest) RuleServiceDubboFilterInvoker.getInputObj();
        request = wechatRequestService.saveRequest(request);
        return AjaxObject.newOk(this.webFindRequestByNo(request.getRequestNo())).toJson();
    }

    @Override
    public String webQueryRequestList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询票据融资申请，入参：" + anMap);
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        
        if(UserUtils.supplierUser() || UserUtils.sellerUser()){
            anMap.put("custNo", UserUtils.getDefCustInfo().getCustNo());
        }
        else if(UserUtils.factorUser()){
            anMap.put("factorNo", UserUtils.getDefCustInfo().getCustNo());
        }
        
        return AjaxObject.newOkWithPage("分页查询融资申请成功",
                wechatRequestService.queryRequestList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webRequestByOffer(String anOfferId) {
        logger.debug("获取票据申请数据，入参：" + anOfferId);
        return AjaxObject.newOk(offerService.selectByPrimaryKey(Long.parseLong(anOfferId))).toJson();
    }
    
    @Override
    public String updateRequestTradeStauts(String anRequestNo, String anTradeStatus){
        logger.debug("修改票据申请的融资状态，入参：anTradeStatus=" + anTradeStatus);
        ScfRequest request = requestService.findRequestDetail(anRequestNo);
        request.setTradeStatus(anTradeStatus);
        return AjaxObject.newOk(requestService.saveModifyRequest(request, anRequestNo)).toJson();
    }
    
    @Override
    public String webFindRequestByNo(String anRequestNo){
        logger.debug("根据申请编号查询融资申请，入参：anRequestNo=" + anRequestNo);
        return AjaxObject.newOk(wechatRequestService.findRequestByNo(anRequestNo)).toJson();
    }
    
    @Override
    public String webFindRequestByBill(String anBillId){
        logger.debug("根据票据id查询融资申请，入参：anBillId=" + anBillId);
        return AjaxObject.newOk(wechatRequestService.findRequestByBill(anBillId)).toJson();
    }


    
}
