package com.betterjr.modules.receivable.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.receivable.dao.ScfReceivableMapper;
import com.betterjr.modules.receivable.entity.ScfReceivable;

@Service
public class ScfReceivableService extends BaseService<ScfReceivableMapper, ScfReceivable>{
   
    
    /**
     * 应收账款编辑
     */
    public ScfReceivable saveModifyReceivable(ScfReceivable anMoidReceivable, Long anId, String anFileList) {
        logger.info("Begin to modify receivable");
        
        ScfReceivable anReceivable = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anReceivable, "无法获取原应收账款信息");
        //检查用户是否有权限编辑
        checkOperator(anReceivable.getOperOrg(), "当前操作员不能修改该应收账款");
        //检查应收账款状态 0:可用 1:过期 2:冻结
        checkStatus(anReceivable.getBusinStatus(), "1", true, "当前应收账款已过期,不允许被编辑");
        checkStatus(anReceivable.getBusinStatus(), "2", true, "当前应收账款已冻结,不允许被编辑");
        //应收账款信息变更迁移初始化
        anReceivable.initModifyValue(anMoidReceivable);
        //数据存盘
        this.updateByPrimaryKey(anReceivable);
        return anReceivable;
    }
    

    /**
     * 应收账款分页查询
     */
    public Page<ScfReceivable> queryReceivable(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        //操作员只能查询本机构数据
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        
        Page<ScfReceivable> anReceivableList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
        
        return anReceivableList;
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
