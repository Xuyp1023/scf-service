package com.betterjr.modules.order.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.order.dao.ScfOrderMapper;
import com.betterjr.modules.order.entity.ScfOrder;

@Service
public class ScfOrderService extends BaseService<ScfOrderMapper, ScfOrder>{
    
    /**
     * 订单信息分页查询
     */
    public Page<ScfOrder> queryOrder(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
      //操作员只能查询本机构数据
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        
        Page<ScfOrder> anOrderList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));

        return anOrderList;
    }

    /**
     * 订单信息编辑
     */
    public ScfOrder saveModifyOrder(ScfOrder anModiOrder, Long anId, String anFileList) {
        logger.info("Begin to modify order");
        
        ScfOrder anOrder = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anOrder, "无法获取订单信息");
        //检查用户是否有权限编辑
        checkOperator(anOrder.getOperOrg(), "当前操作员不能修改该订单");
        //检查应收账款状态 0:可用 1:过期 2:冻结
        checkStatus(anOrder.getBusinStatus(), "1", true, "当前订单已过期,不允许被编辑");
        checkStatus(anOrder.getBusinStatus(), "2", true, "当前订单已冻结,不允许被编辑");
        //应收账款信息变更迁移初始化
        anOrder.initModifyValue(anModiOrder);
        //数据存盘
        this.updateByPrimaryKey(anOrder);
        return anOrder;
        
    }
    
    /**
     * 检查用户是否有权限操作数据
     */
    private void checkOperator(String anOperOrg, String anMessage) {
        if (BetterStringUtils.equals(UserUtils.getOperatorInfo().getOperOrg(), anOperOrg) == false) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }
    
    /**
     * 检查状态信息
     */
    private void checkStatus(String anBusinStatus, String anTargetStatus, boolean anFlag, String anMessage) {
        if (BetterStringUtils.equals(anBusinStatus, anTargetStatus) == anFlag) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }
}
