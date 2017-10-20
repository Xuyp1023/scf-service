package com.betterjr.modules.payorder.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.payorder.IPayOrderPoolService;
import com.betterjr.modules.payorder.service.PayOrderPoolService;

/**
 * 
 * @ClassName: PayOrderPoolDubboService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xuyp
 * @date 2017年10月20日 下午2:42:10 
 *
 */
@Service(interfaceClass = IPayOrderPoolService.class)
public class PayOrderPoolDubboService implements IPayOrderPoolService {

    @Autowired
    private PayOrderPoolService poolService;

    @Override
    public String webSaveAddPayRecord(String anRequestNo) {

        return AjaxObject.newOk("付款指令新增成功", poolService.saveAddPayRecord(anRequestNo)).toJson();
    }

    @Override
    public String webQueryPayPoolList(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        return AjaxObject.newOkWithPage("付款指令查询成功", poolService.queryPayPoolList(anMap, anFlag, anPageNum, anPageSize))
                .toJson();
    }

}
