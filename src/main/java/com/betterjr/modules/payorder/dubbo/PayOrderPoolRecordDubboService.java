package com.betterjr.modules.payorder.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.payorder.IPayOrderPoolRecordService;
import com.betterjr.modules.payorder.service.PayOrderPoolRecordService;

/**
 * 
 * @ClassName: PayOrderPoolRecordDubboService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xuyp
 * @date 2017年10月20日 下午2:42:21 
 *
 */
@Service(interfaceClass = IPayOrderPoolRecordService.class)
public class PayOrderPoolRecordDubboService implements IPayOrderPoolRecordService {

    @Autowired
    private PayOrderPoolRecordService recordService;

    @Override
    public String webQueryRecordPage(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        return AjaxObject.newOkWithPage("付款指令查询成功", recordService.queryRecordPage(anMap, anFlag, anPageNum, anPageSize))
                .toJson();
    }

    @Override
    public String webQueryRecordList(Map<String, Object> anMap) {

        return AjaxObject.newOk("付款指令查询成功", recordService.queryRecordList(anMap)).toJson();
    }

    @Override
    public String webQueryRecordListByFileId(Long anSourceFileId, String anPayRecordBusinStatus) {

        return AjaxObject
                .newOk("付款指令查询成功", recordService.queryRecordListByFileId(anSourceFileId, anPayRecordBusinStatus))
                .toJson();
    }

    @Override
    public String webQueryRecordListByFileId(Long anId) {

        return AjaxObject.newOk("付款指令查询成功", recordService.queryRecordListByFileId(anId)).toJson();
    }

}
