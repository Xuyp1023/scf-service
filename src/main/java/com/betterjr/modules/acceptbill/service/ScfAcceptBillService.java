package com.betterjr.modules.acceptbill.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.PlatformBaseRuleType;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.dao.ScfAcceptBillMapper;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.entity.ScfOrderRelation;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;
import com.betterjr.modules.order.service.ScfInvoiceService;
import com.betterjr.modules.order.service.ScfOrderRelationService;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.order.service.ScfTransportService;
import com.betterjr.modules.receivable.service.ScfReceivableService;

@Service
public class ScfAcceptBillService extends BaseService<ScfAcceptBillMapper, ScfAcceptBill> implements IScfOrderInfoCheckService {

    private static final Logger logger = LoggerFactory.getLogger(ScfAcceptBillService.class);

    @Autowired
    private ScfOrderRelationService orderRelationService;
    @Autowired
    private ScfOrderService orderService;
    
    /**
     * 汇票信息分页查询
     * @param anIsOnlyNormal 是否过滤，仅查询正常未融资数据 1：未融资 0：查询所有
     */
    public Page<ScfAcceptBill> queryAcceptBill(Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {
       
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
        Page<ScfAcceptBill> anAcceptBillList = new Page<ScfAcceptBill>();
        //仅查询正常未融资数据
        if(BetterStringUtils.equals(anIsOnlyNormal, "1")) {
            anMap.put("businStatus", "0");
            anAcceptBillList.addAll(this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag)));
            anMap.put("businStatus", "1");
            anAcceptBillList.addAll(this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag)));
        }
        else {
            anAcceptBillList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
        }
        //补全关联信息
        for(ScfAcceptBill anAcceptBill : anAcceptBillList) {
            Map<String, Object> acceptBillIdMap = new HashMap<String, Object>();
            acceptBillIdMap.put("infoId", anAcceptBill.getId());
            List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(acceptBillIdMap);
            fillAcceptBillInfo(anAcceptBill, orderRelationList);
        }

        return anAcceptBillList;
    }
    
    /**
     * 根据订单关联关系补全汇票信息
     */
    public void fillAcceptBillInfo(ScfAcceptBill anAcceptBill, List<ScfOrderRelation> anOrderRelationList) {
        for(ScfOrderRelation anOrderRelation : anOrderRelationList) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("id", anOrderRelation.getOrderId());
            List<ScfOrder> orderList = orderService.findOrder(queryMap);
            for(ScfOrder anOrder : orderList) {
                anAcceptBill.setInvoiceList(anOrder.getInvoiceList());
                anAcceptBill.setTransportList(anOrder.getTransportList());
                anAcceptBill.setReceivableList(anOrder.getReceivableList());
                anOrder.chearRelationInfo();
            }
            anAcceptBill.setOrderList(orderList);
        }
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
     * 查询汇票信息
     */
    public List<ScfAcceptBill> findAcceptBill(Map<String, Object> anMap) {
    	return this.selectByClassProperty(ScfAcceptBill.class, anMap);
    }
    
    /**
     * 检查是否存在相应id、操作机构、业务状态的汇票信息
     * @param anId  汇票信息id
     * @param anOperOrg 操作机构
     */
    public void checkInfoExist(Long anId, String anOperOrg) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        List<ScfAcceptBill> acceptBillList = new LinkedList<ScfAcceptBill>();
        String[] anBusinStatusList = {"0","1"};
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
    /**
     * 变更汇票信息状态 0未处理，1完善资料，2已融资，3已过期
     * 融资标志，0未融资，1已融资，2收款，3已还款
     * @param anId 汇票流水号
     * @param anStatus 状态
     * @param anCheckOperOrg 是否检查操作机构权限
     * @return
     */
    private ScfAcceptBill saveAcceptBillStatus(Long anId, String anStatus, String anFinanceFlag, boolean anCheckOperOrg) {
        ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAcceptBill, "无法获取汇票信息");
        //检查用户权限
        if (anCheckOperOrg) {
            checkOperator(anAcceptBill.getOperOrg(), "当前操作员无法变更汇票信息");
        }
        //变更状态
        anAcceptBill.setBusinStatus(anStatus);
        anAcceptBill.setFinanceFlag(anFinanceFlag);
        anAcceptBill.setModiOperId(UserUtils.getOperatorInfo().getId());
        anAcceptBill.setModiOperName(UserUtils.getOperatorInfo().getName());
        anAcceptBill.setModiDate(BetterDateUtils.getNumDate());
        anAcceptBill.setModiTime(BetterDateUtils.getNumTime());
        //数据存盘
        this.updateByPrimaryKeySelective(anAcceptBill);
        return anAcceptBill;
    }
    
    /**
     * 变更汇票信息 -- 完善资料
     * 0未处理，1完善资料，2已融资，3已过期
     * 融资标志，0未融资，1已融资，2收款，3已还款
     * @param anId
     * @param anCheckOperOrg
     * @return
     */
    public ScfAcceptBill saveNormalAcceptBill(Long anId, boolean anCheckOperOrg) {
        return this.saveAcceptBillStatus(anId, "1", "0", anCheckOperOrg);
    }
    
    /**
     * 变更汇票信息 -- 已融资
     * 0未处理，1完善资料，2已融资，3已过期
     * 融资标志，0未融资，1已融资，2收款，3已还款
     * @param anId
     * @param anCheckOperOrg
     * @return
     */
    public ScfAcceptBill saveFinancedAcceptBill(Long anId, boolean anCheckOperOrg) {
        return this.saveAcceptBillStatus(anId, "2","1", anCheckOperOrg);
    }
    
    /**
     * 变更汇票信息 -- 已过期
     * 0未处理，1完善资料，2已融资，3已过期
     * 融资标志，0未融资，1已融资，2收款，3已还款
     * @param anId
     * @param anCheckOperOrg
     * @return
     */
    public ScfAcceptBill saveExpireAcceptBill(Long anId, boolean anCheckOperOrg) {
        return this.saveAcceptBillStatus(anId, "3","0", anCheckOperOrg);
    }

}
