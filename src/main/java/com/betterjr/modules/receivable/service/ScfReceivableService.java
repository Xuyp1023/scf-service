package com.betterjr.modules.receivable.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.entity.ScfOrderRelation;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;
import com.betterjr.modules.order.service.ScfOrderRelationService;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.receivable.dao.ScfReceivableMapper;
import com.betterjr.modules.receivable.entity.ScfReceivable;

@Service
public class ScfReceivableService extends BaseService<ScfReceivableMapper, ScfReceivable> implements IScfOrderInfoCheckService  {
   
    @Autowired
    private ScfOrderRelationService orderRelationService;
    @Autowired
    private ScfOrderService orderService;
    
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
     * anIsOnlyNormal 是否过滤，仅查询正常未融资数据 1：未融资 0：查询所有
     */
    public Page<ScfReceivable> queryReceivable(Map<String, Object> anMap, String anIsOnlyNormal,  String anFlag, int anPageNum, int anPageSize) {
        //操作员只能查询本机构数据
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        if(BetterStringUtils.equals(anIsOnlyNormal, "1")) {
            anMap.put("businStatus", "0");
        }
        Page<ScfReceivable> anReceivableList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
        
        //补全关联信息
        for(ScfReceivable anReceivable : anReceivableList) {
            Map<String, Object> anReceivableIdMap = new HashMap<String, Object>();
            anReceivableIdMap.put("infoId", anReceivable.getId());
            List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(anReceivableIdMap);
            fillReceivableInfo(anReceivable, orderRelationList);
        }
        
        return anReceivableList;
    }
    
    /**
     * 根据订单关联关系补全汇票信息
     */
    public void fillReceivableInfo(ScfReceivable anReceivable, List<ScfOrderRelation> anOrderRelationList) {
        for(ScfOrderRelation anOrderRelation : anOrderRelationList) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("id", anOrderRelation.getOrderId());
            List<ScfOrder> orderList = orderService.findOrder(queryMap);
            for(ScfOrder anOrder : orderList) {
                anReceivable.setInvoiceList(anOrder.getInvoiceList());
                anReceivable.setTransportList(anOrder.getTransportList());
                anReceivable.setAcceptBillList(anOrder.getAcceptBillList());
                anOrder.chearRelationInfo();
            }
            anReceivable.setOrderList(orderList);
        }
    }
    
    /**
     * 查询应收账款
     */
    public List<ScfReceivable> findReceivable(Map<String, Object> anMap) {
    	return this.selectByClassProperty(ScfReceivable.class, anMap);
    }
    
    /**
     * 检查是否存在相应id、操作机构、业务状态的应收账款
     * @param anId  应收账款id
     * @param anOperOrg 操作机构
     */
    public void checkInfoExist(Long anId, String anOperOrg) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        List<ScfReceivable> receivableList = new LinkedList<ScfReceivable>();
        //可编辑的业务状态
        String[] anBusinStatusList = {"0"};
        anMap.put("id", anId);
        anMap.put("operOrg", anOperOrg);
        //查询每个状态数据
        for(int i = 0; i < anBusinStatusList.length; i++) {
            anMap.put("businStatus", anBusinStatusList[i]);
            List<ScfReceivable> tempReceivableList = this.selectByClassProperty(ScfReceivable.class, anMap);
            receivableList.addAll(tempReceivableList);
        }
        if (Collections3.isEmpty(receivableList)) {
            logger.warn("不存在相对应id,操作机构,业务状态的应收账款");
            throw new BytterTradeException(40001, "不存在相对应id,操作机构,业务状态的应收账款");
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
     * 变更应收账款状态   0:可用 1:过期 2:冻结
     * @param anId 应收账款流水号
     * @param anStatus 状态
     * @param anCheckOperOrg 是否检查操作机构权限
     */
    private ScfReceivable saveReceivableStatus(Long anId, String anStatus, boolean anCheckOperOrg) {
        ScfReceivable anReceivable = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anReceivable, "无法获取应收账款信息");
        //检查用户权限
        if (anCheckOperOrg) {
            checkOperator(anReceivable.getOperOrg(), "当前操作员无法变更应收账款信息");
        }
        //变更状态
        anReceivable.setBusinStatus(anStatus);
        anReceivable.setModiOperId(UserUtils.getOperatorInfo().getId());
        anReceivable.setModiOperName(UserUtils.getOperatorInfo().getName());
        anReceivable.setModiDate(BetterDateUtils.getNumDate());
        anReceivable.setModiTime(BetterDateUtils.getNumTime());
        //数据存盘
        this.updateByPrimaryKeySelective(anReceivable);
        return anReceivable;
    }

    /**
     * 变更应收账款信息--可用
     * 0:可用 1:过期 2:冻结
     * @param anId 应收账款流水号
     * @param anCheckOperOrg 是否检查操作机构权限
     */
    public ScfReceivable saveNormalReceivable(Long anId, boolean anCheckOperOrg) {
        return this.saveReceivableStatus(anId, "0", anCheckOperOrg);
    }
    
    /**
     * 变更应收账款信息--过期
     * 0:可用 1:过期 2:冻结
     * @param anId 应收账款流水号
     * @param anCheckOperOrg 是否检查操作机构权限
     */
    public ScfReceivable saveExpireReceivable(Long anId, boolean anCheckOperOrg) {
        return this.saveReceivableStatus(anId, "1", anCheckOperOrg);
    }
    
    /**
     * 变更应收账款信息--冻结
     * 0:可用 1:过期 2:冻结
     * @param anId 应收账款流水号
     * @param anCheckOperOrg 是否检查操作机构权限
     */
    public ScfReceivable saveForzenReceivable(Long anId, boolean anCheckOperOrg) {
        return this.saveReceivableStatus(anId, "2", anCheckOperOrg);
    }
}
