package com.betterjr.modules.loan.dubbo;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.loan.IScfDeliveryNoticeService;
import com.betterjr.modules.loan.entity.ScfDeliveryNotice;
import com.betterjr.modules.loan.service.ScfDeliveryNoticeService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IScfDeliveryNoticeService.class)
public class DeliveryNoticeDubboService implements IScfDeliveryNoticeService {
    protected final Logger logger = LoggerFactory.getLogger(DeliveryNoticeDubboService.class);

    @Autowired
    private ScfDeliveryNoticeService deliveryNoticeService;

    @Override
    public String webQueryDeliveryNoticeList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        logger.debug("分页查询发货通知列表，入参：" + anMap);
        anMap = (Map) RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("发货通知单查询成功",
                deliveryNoticeService.queryDeliveryNoticeList(anMap, anFlag, anPageNum, anPageSize)).toJson();
    }

    @Override
    public String webFindDeliveryNoticeList(Map<String, Object> anMap, String factorNo) {
        logger.debug("查询发货通知列表，入参：" + anMap);
        return AjaxObject.newOk("发货通知单查询成功", deliveryNoticeService.getNoticeSelectList(factorNo)).toJson();
    }

    @Override
    public String webAddDeliveryNotice(Map<String, Object> anMap) {
        logger.debug("新增发货通知，入参：" + anMap);
        String fillList = anMap.get("fileList") == null ? "" : anMap.get("fileList").toString();
        return AjaxObject
                .newOk(deliveryNoticeService
                        .addDeliveryNotice((ScfDeliveryNotice) RuleServiceDubboFilterInvoker.getInputObj(), fillList))
                .toJson();
    }

    @Override
    public String webSaveModifyDeliveryNotice(Map<String, Object> anMap, Long anId) {
        logger.debug("修改发货通知，入参：" + anMap);
        logger.debug("新增发货通知，入参：" + anMap);
        String fillList = anMap.get("fileList") == null ? "" : anMap.get("fileList").toString();
        return AjaxObject.newOk(deliveryNoticeService
                .saveModifyEnquiry((ScfDeliveryNotice) RuleServiceDubboFilterInvoker.getInputObj(), anId, fillList))
                .toJson();
    }

}
