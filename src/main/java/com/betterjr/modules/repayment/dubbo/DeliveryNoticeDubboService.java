package com.betterjr.modules.repayment.dubbo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.repayment.IScfDeliveryNoticeService;
import com.betterjr.modules.repayment.entity.ScfDeliveryNotice;
import com.betterjr.modules.repayment.service.DeliveryNoticeService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfDeliveryNoticeService.class)
public class DeliveryNoticeDubboService implements IScfDeliveryNoticeService {
    protected final Logger logger = LoggerFactory.getLogger(DeliveryNoticeDubboService.class);
    
    @Autowired
    private DeliveryNoticeService deliveryNoticeService;
    
    @Override
    public String webQueryDeliveryNoticeList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("查询发货通知列表，入参："+anMap);
        anMap = (Map)RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("发货通知单查询成功", deliveryNoticeService.queryDeliveryNoticeList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }
    
    @Override
    public String webAddDeliveryNotice(Map<String, Object> anMap) {
        logger.debug("新增发货通知，入参："+anMap);
        ScfDeliveryNotice notice = (ScfDeliveryNotice) RuleServiceDubboFilterInvoker.getInputObj();
        if(null == notice){
            return AjaxObject.newError("webAddDeliveryNotice service failed notice =null").toJson();
        }
        return AjaxObject.newOk(deliveryNoticeService.addDeliveryNotice(notice)).toJson();
    }
    
    @Override
    public String webSaveModifyDeliveryNotice(Map<String, Object> anMap, Long anId) {
        logger.debug("修改发货通知，入参："+anMap);
        ScfDeliveryNotice notice = (ScfDeliveryNotice) RuleServiceDubboFilterInvoker.getInputObj();
        if(null == notice){
            return AjaxObject.newError("webSaveModifyDeliveryNotice service failed notice =null").toJson();
        }
        
        //检查该数据据是否合法
        Map<String, Object> anPropValue = new HashMap<String, Object>();
        anPropValue.put("custNo", notice.getCustNo());
        anPropValue.put("id", anId);
        List list = deliveryNoticeService.selectByClassProperty(ScfDeliveryNotice.class, anPropValue);
        if(CollectionUtils.isEmpty(list)){
            notice.setId(anId);
            return AjaxObject.newError("webSaveModifyDeliveryNotice service failed notice error").toJson();
        }
        
        return AjaxObject.newOk(deliveryNoticeService.saveModifyEnquiry(notice)).toJson();
    }
}
