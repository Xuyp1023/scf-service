package com.betterjr.modules.acceptbill.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.PlatformBaseRuleType;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.dao.ScfAcceptBillMapper;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.receivable.entity.ScfReceivable;

@Service
public class ScfAcceptBillService extends BaseService<ScfAcceptBillMapper, ScfAcceptBill> {

    private static final Logger logger = LoggerFactory.getLogger(ScfAcceptBillService.class);

    /**
     * 汇票信息分页查询
     */
    public Page<ScfAcceptBill> queryAcceptBill(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
       
        //操作员只能查询本机构数据
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        //构造custNo查询条件
        //当用户为供应商
        if(UserUtils.findInnerRules().contains( PlatformBaseRuleType.SUPPLIER_USER)) {
            anMap.put("supplierNo", anMap.get("custNo"));
            //当用户为经销商
        }else if(UserUtils.findInnerRules().contains( PlatformBaseRuleType.SELLER_USER)) {
            anMap.put("supplierNo", anMap.get("custNo"));
        }
        anMap.remove("custNo");
        
        Page<ScfAcceptBill> anAcceptBillList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));

        return anAcceptBillList;
    }

    /**
     * 汇票信息编辑修改
     */
    public ScfAcceptBill saveModifyAcceptBill(ScfAcceptBill anModiAcceptBill, Long anId, String anFileList) {
        logger.info("Begin to modify AcceptBill");

        ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAcceptBill, "无法获得汇票信息");

        // 检查当前操作员是否可以修改该票据信息
        checkOperator(anAcceptBill.getOperOrg(), "当前操作员不能修改该票据");

        // 检查状态,仅未处理和未融资状态允许修改汇票信息
        checkStatus(anAcceptBill.getBusinStatus(), "2", true, "当前票据已融资,不允许修改");
        checkStatus(anAcceptBill.getBusinStatus(), "3", true, "当前票据已过期,不允许修改");
        checkStatus(anAcceptBill.getFinanceFlag(), "0", false, "当前票据已融资,不允许修改");
        // 数据编辑初始化
        anAcceptBill.initModifyValue(anModiAcceptBill);
        // 设置汇票状态(businStatus:1-完善资料)
        anAcceptBill.setBusinStatus("1");
        // 设置附件批次号
        // anAcceptBill.setBatchNo(10);
        // 数据存盘
        this.updateByPrimaryKeySelective(anAcceptBill);
        return anAcceptBill;
    }
    
    /**
     * 检查是否存在相应id、操作机构、业务状态的汇票信息
     * @param anId  汇票信息id
     * @param anBusinStatuses 汇票信息状态,当多个状态时以逗号分隔
     * @param anOperOrg 操作机构
     */
    public void checkInvoiceExist(Long anId, String anBusinStatuses, String anOperOrg) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        List<ScfAcceptBill> acceptBillList = new LinkedList<ScfAcceptBill>();
        String[] anBusinStatusList = anBusinStatuses.split(",");
        anMap.put("id", anId);
        anMap.put("operOrg", anOperOrg);
        //查询每个状态数据
        for(int i = 0; i < anBusinStatusList.length; i++) {
            anMap.put("businStatus", anBusinStatusList[i]);
            List<ScfAcceptBill> tempAcceptBillList = this.selectByClassProperty(ScfAcceptBill.class, anMap);
            acceptBillList.addAll(tempAcceptBillList);
        }
        if (Collections3.isEmpty(acceptBillList)) {
            logger.warn("不存在相对应id,操作机构,业务状态的汇票信息");
            throw new BytterTradeException(40001, "不存在相对应id,操作机构,业务状态的汇票信息");
        }
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
