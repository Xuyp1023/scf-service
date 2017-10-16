package com.betterjr.modules.discount.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.discount.IScfDiscountService;
import com.betterjr.modules.discount.entity.ScfDiscount;
import com.betterjr.modules.discount.service.ScfDiscountService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

/***
 * 贴现管理
 * @author hubl
 *
 */
@Service(interfaceClass = IScfDiscountService.class)
public class ScfDiscountDubbo implements IScfDiscountService {

    @Autowired
    private ScfDiscountService discountService;

    @Override
    public String webAddDiscount(Map<String, Object> anMap) {
        ScfDiscount discount = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("贴现添加", discountService.addDiscount(discount)).toJson();
    }

    /***
     * 根据条件分页查询贴现信息
     * @param anParam
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    @Override
    public String webQueryDiscount(Map<String, Object> anParam, int anPageNum, int anPageSize) {
        return AjaxObject.newOkWithPage("贴现分页查询", discountService.queryDiscount(anParam, anPageNum, anPageSize))
                .toJson();
    }

    /***
     * 根据贴现编号查询
     * @param discountId
     * @return
     */
    @Override
    public String webQueryDiscountById(Integer discountId) {
        return AjaxObject.newOk("贴现查询", discountService.queryDiscountById(discountId)).toJson();
    }

}
