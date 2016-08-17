package com.betterjr.modules.order.service;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.agreement.service.ScfCustAgreementService;
import com.betterjr.modules.order.dao.ScfOrderMapper;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.entity.ScfOrderRelation;
import com.betterjr.modules.order.entity.ScfTransport;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;
import com.betterjr.modules.order.helper.ScfOrderRelationType;
import com.betterjr.modules.receivable.entity.ScfReceivable;
import com.betterjr.modules.receivable.service.ScfReceivableService;

@Service
public class ScfOrderService extends BaseService<ScfOrderMapper, ScfOrder> implements IScfOrderInfoCheckService {

    @Autowired
    private ScfOrderRelationService orderRelationService;
    @Autowired
    private ScfCustAgreementService custAgreementService;
    @Autowired
    private ScfTransportService transportService;
    @Autowired
    private ScfInvoiceService invoiceService;
    @Autowired
    private ScfAcceptBillService acceptBillService;
    @Autowired
    private ScfReceivableService receivableService;
    
    @Autowired
    private CustAccountService custAccountService;

    /**
     * 订单信息分页查询
     * @param anIsOnlyNormal 是否过滤，仅查询正常未融资数据 1：未融资 0：查询所有
     */
    public Page<ScfOrder> queryOrder(Map<String, Object> anMap,String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {
        // 操作员只能查询本机构数据
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        if(BetterStringUtils.equals(anIsOnlyNormal, "1")) {
            anMap.put("businStatus", "0");
        }

        Page<ScfOrder> anOrderList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));

        for (ScfOrder anOrder : anOrderList) {
            anOrder.setCustName(custAccountService.queryCustName(anOrder.getCustNo()));
            anOrder.setCoreCustName(custAccountService.queryCustName(anOrder.getCoreCustNo()));
            Map<String, Object> orderIdMap = new HashMap<String, Object>();
            orderIdMap.put("orderId", anOrder.getId());
            List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(orderIdMap);
            fillOrderInfo(anOrder, orderRelationList);
        }

        return anOrderList;
    }

    /**
     * 根据订单关联表补全
     */
    public void fillOrderInfo(ScfOrder anOrder, List<ScfOrderRelation> anOrderRelationList) {
        anOrder.setAgreementList(new ArrayList<CustAgreement>());
        anOrder.setAcceptBillList(new ArrayList<ScfAcceptBill>());
        anOrder.setTransportList(new ArrayList<ScfTransport>());
        anOrder.setInvoiceList(new ArrayList<ScfInvoice>());
        anOrder.setReceivableList(new ArrayList<ScfReceivable>());
        for (ScfOrderRelation anOrderRealtion : anOrderRelationList) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("id", anOrderRealtion.getInfoId());
            if (BetterStringUtils.equals(ScfOrderRelationType.AGGREMENT.getCode(), anOrderRealtion.getInfoType())) {
                anOrder.getAgreementList().add(custAgreementService.findCustAgreementDetail(anOrderRealtion.getInfoId()));
            }
            else if (BetterStringUtils.equals(ScfOrderRelationType.ACCEPTBILL.getCode(), anOrderRealtion.getInfoType())) {
                anOrder.getAcceptBillList().addAll(acceptBillService.findAcceptBill(queryMap));
            }
            else if (BetterStringUtils.equals(ScfOrderRelationType.INVOICE.getCode(), anOrderRealtion.getInfoType())) {
                anOrder.getInvoiceList().addAll((invoiceService.findInvoice(queryMap)));
            }
            else if (BetterStringUtils.equals(ScfOrderRelationType.RECEIVABLE.getCode(), anOrderRealtion.getInfoType())) {
                anOrder.getReceivableList().addAll(receivableService.findReceivable(queryMap));
            }
            else if (BetterStringUtils.equals(ScfOrderRelationType.TRANSPORT.getCode(), anOrderRealtion.getInfoType())) {
                anOrder.getTransportList().addAll(transportService.findTransport(queryMap));
            }
        }
    }
    
    /**
     * 查询订单信息，无分页，前台调用
     * @param anIsOnlyNormal 是否过滤，仅查询正常未融资数据 1：未融资 0：查询所有
     */
    public List<ScfOrder> findOrderList(String anCustNo, String anIsOnlyNormal) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("custNo", anCustNo);
        if(BetterStringUtils.equals(anIsOnlyNormal, "1")) {
            anMap.put("businStatus", "0");
        }
        List<ScfOrder> orderList = this.selectByProperty(anMap);
        for(ScfOrder anOrder : orderList) {
            anOrder.setCustName(custAccountService.queryCustName(anOrder.getCustNo()));
            anOrder.setCoreCustName(custAccountService.queryCustName(anOrder.getCoreCustNo()));
        }
        return orderList;
    }
    
    /**
     * 通过融资申请信息,查询融资基本信息，无分页。
     * @param anRequestNo   融资申请编号 1：订单，2:票据;3:应收款;4:经销商
     * @param anRequestType
     */
    public List<Object> findInfoListByRequest(String anRequestNo, String anRequestType) {
        ScfOrderRelationType orderRealtionType = null;
        // 订单融资
        if ("1".equals(anRequestType) || "4".equals(anRequestType)) {
            orderRealtionType = ScfOrderRelationType.ORDER;
        }
        else if ("2".equals(anRequestType)) {
            orderRealtionType = ScfOrderRelationType.ACCEPTBILL;
        }
        else if ("3".equals(anRequestType)) {
            orderRealtionType = ScfOrderRelationType.RECEIVABLE;
        }
        return this.findRelationInfo(anRequestNo, orderRealtionType);
    }
    
    
    /**
     * 查询订单信息，无分页,包含所有关联信息，供后台调用
     */
    public List<ScfOrder> findOrder(Map<String, Object> anMap) {
        List<ScfOrder> orderList = this.selectByClassProperty(ScfOrder.class, anMap);
        for (ScfOrder anOrder : orderList) {
            anOrder.setCustName(custAccountService.queryCustName(anOrder.getCustNo()));
            anOrder.setCoreCustName(custAccountService.queryCustName(anOrder.getCoreCustNo()));
            Map<String, Object> orderIdMap = new HashMap<String, Object>();
            orderIdMap.put("orderId", anOrder.getId());
            List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(orderIdMap);
            fillOrderInfo(anOrder, orderRelationList);
        }
        return orderList;
    }

    /**
     * 订单信息编辑
     */
    public ScfOrder saveModifyOrder(ScfOrder anModiOrder, Long anId, String anFileList) {
        logger.info("Begin to modify order");

        ScfOrder anOrder = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anOrder, "无法获取订单信息");
        // 检查用户是否有权限编辑
        checkOperator(anOrder.getOperOrg(), "当前操作员不能修改该订单");
        // 检查应收账款状态 0:可用 1:过期 2:冻结
        checkStatus(anOrder.getBusinStatus(), "1", true, "当前订单已过期,不允许被编辑");
        checkStatus(anOrder.getBusinStatus(), "2", true, "当前订单已冻结,不允许被编辑");
        // 应收账款信息变更迁移初始化
        anOrder.initModifyValue(anModiOrder);
        // 数据存盘
        this.updateByPrimaryKey(anOrder);
        return anOrder;

    }

    /**
     * 检查是否存在相应id、操作机构、业务状态的订单编号
     * 
     * @param anId
     *            订单id
     * @param anOperOrg
     *            操作机构
     */
    public void checkInfoExist(Long anId, String anOperOrg) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        String[] anBusinStatusList = { "0" };
        anMap.put("id", anId);
        anMap.put("operOrg", anOperOrg);
        anMap.put("businStatus", anBusinStatusList);
        // 查询每个状态数据
        List<ScfOrder> orderList = this.selectByClassProperty(ScfOrder.class, anMap);
        if (Collections3.isEmpty(orderList)) {
            logger.warn("不存在相对应id,操作机构,业务状态的订单");
            throw new BytterTradeException(40001, "不存在相对应id,操作机构,业务状态的订单");
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
     * 变更订单状态  0:可用 1:过期 2:冻结
     * @param anId  订单流水号
     * @param anStatus 状态
     * @param anCheckOperOrg 是否检查操作机构权限
     * @return
     */
    private ScfOrder saveOrderStatus(Long anId, String anStatus, boolean anCheckOperOrg) {
        ScfOrder anOrder = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anOrder, "无法获取订单信息");
        //是否检查操作机构
        if (anCheckOperOrg) {
            checkOperator(anOrder.getOperOrg(), "当前操作员无法变更订单系信息");
        }
        //变更状态
        anOrder.setBusinStatus(anStatus);
        anOrder.setModiOperId(UserUtils.getOperatorInfo().getId());
        anOrder.setModiOperName(UserUtils.getOperatorInfo().getName());
        anOrder.setModiDate(BetterDateUtils.getNumDate());
        anOrder.setModiTime(BetterDateUtils.getNumTime());
        //数据存盘
        this.updateByPrimaryKeySelective(anOrder);
        return anOrder;
    }
    
    /**
     * 变更订单信息 -- 可用
     * 0:可用 1:过期 2:冻结
     * @param anId 订单流水号
     * @param anCheckOperOrg 是否检查操作机构权限
     * @return
     */
    public ScfOrder saveNormalOrder(Long anId, boolean anCheckOperOrg) {
        return this.saveOrderStatus(anId, "0", anCheckOperOrg);
    }
    
    /**
     * 变更订单信息 -- 过期
     * 0:可用 1:过期 2:冻结
     * @param anId 订单流水号
     * @param anCheckOperOrg 是否检查操作机构权限
     * @return
     */
    public ScfOrder saveExpireOrder(Long anId, boolean anCheckOperOrg) {
        return this.saveOrderStatus(anId, "1", anCheckOperOrg);
    }
    
    /**
     * 变更订单信息 -- 冻结
     * 0:可用 1:过期 2:冻结
     * @param anId 订单流水号
     * @param anCheckOperOrg 是否检查操作机构权限
     * @return
     */
    public ScfOrder saveForzenOrder(Long anId, boolean anCheckOperOrg) {
        return this.saveOrderStatus(anId, "2", anCheckOperOrg);
    }
    
    /**
     * 由requestNo融资申请编号查询相关联信息
     * anInfoType ScfOrderRelationType资料类型
     */
    public List findRelationInfo(String anRequestNo, ScfOrderRelationType anInfoType) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("requestNo", anRequestNo);
        List<ScfOrder> orderList = this.findOrder(anMap);
        //查询订单信息时 直接返回订单
        if(BetterStringUtils.equals(ScfOrderRelationType.ORDER.getCode(), anInfoType.getCode())) {
            return orderList;
        }
        List<Object> infoList = new ArrayList<Object>();
        for (ScfOrder anOrder : orderList) {
            if (BetterStringUtils.equals(ScfOrderRelationType.AGGREMENT.getCode(), anInfoType.getCode())) {
                infoList.addAll(anOrder.getAgreementList());
            }
            else if (BetterStringUtils.equals(ScfOrderRelationType.ACCEPTBILL.getCode(), anInfoType.getCode())) {
                infoList.addAll(anOrder.getAcceptBillList());
            }
            else if (BetterStringUtils.equals(ScfOrderRelationType.INVOICE.getCode(), anInfoType.getCode())) {
                infoList.addAll(anOrder.getInvoiceList());
            }
            else if (BetterStringUtils.equals(ScfOrderRelationType.RECEIVABLE.getCode(), anInfoType.getCode())) {
                infoList.addAll(anOrder.getReceivableList());
            }
            else if (BetterStringUtils.equals(ScfOrderRelationType.TRANSPORT.getCode(), anInfoType.getCode())) {
                infoList.addAll(anOrder.getTransportList());
            }
        }
        return infoList;
    }
    
    /**
     * 
     * 根据requestNo冻结订单及相关信息状态
     */
    public void forzenInfos(String anRequestNo, String anStatus) {
        List<ScfOrder> anOrderList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.ORDER);
        for(ScfOrder anOrder : anOrderList) {
            this.saveForzenOrder(anOrder.getId(), false);
        }
        List<ScfInvoice> anInvoiceList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.INVOICE);
        for(ScfInvoice anInvoice : anInvoiceList) {
            invoiceService.saveForzenInvoice(anInvoice.getId());
        }
        List<ScfReceivable> anReceivableList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.RECEIVABLE);
        for(ScfReceivable anScfReceivable : anReceivableList) {
            receivableService.saveForzenReceivable(anScfReceivable.getId(), false);
        }
        List<ScfAcceptBill> anAcceptBillList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.ACCEPTBILL);
        for(ScfAcceptBill anAcceptBill : anAcceptBillList) {
            acceptBillService.saveFinancedAcceptBill(anAcceptBill.getId(), false);
        }
    }
    
    /**
     * 根据订单id,填充rquestNo
     */
    public void saveOrderRequestNo(Long anOrderId, String anRequestNo) {
        ScfOrder anOrder = this.selectByPrimaryKey(anOrderId);
        anOrder.setRequestNo(anRequestNo);
        this.updateByPrimaryKeySelective(anOrder);
    }
    
    /**
     * requestType 1:订单，2:票据;3:应收款;
     */
    public void saveInfoRequestNo(String anRequestType, String anRequestNo, String anIdList){
        String[] anIds = anIdList.split(",");
        // 订单
        if (BetterStringUtils.equals(anRequestType, "1")) {
            for (String anOrderId : anIds) {
                this.saveOrderRequestNo(Long.valueOf(anOrderId), anRequestNo);
            }
        }
        //非订单
        else {
            for (String anInfoId : anIds) {
                Map<String, Object> anMap = new HashMap<String, Object>();
                anMap.put("infoId", anInfoId);
                // 票据
                if (BetterStringUtils.equals(anRequestType, "2")) {
                    anMap.put("infoType", ScfOrderRelationType.ACCEPTBILL.getCode());
                }
                // 应收
                else if (BetterStringUtils.equals(anRequestType, "3")) {
                    anMap.put("infoType", ScfOrderRelationType.RECEIVABLE.getCode());
                }
                List<ScfOrderRelation> anOrderRelationList = orderRelationService.findOrderRelation(anMap);
                for (ScfOrderRelation anOrderRelation : anOrderRelationList) {
                    this.saveOrderRequestNo(anOrderRelation.getOrderId(), anRequestNo);
                }
            }
        }
    }
    
    /**
     * 
     * 根据requestNo解冻订单及相关信息状态
     */
    public void unForzenInfoes(String anRequestNo, String anStatus) {
        List<ScfOrder> anOrderList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.ORDER);
        for(ScfOrder anOrder : anOrderList) {
            this.saveNormalOrder(anOrder.getId(), false);
        }
        List<ScfInvoice> anInvoiceList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.INVOICE);
        for(ScfInvoice anInvoice : anInvoiceList) {
            invoiceService.saveNormalInvoice(anInvoice.getId());
        }
        List<ScfReceivable> anReceivableList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.RECEIVABLE);
        for(ScfReceivable anScfReceivable : anReceivableList) {
            receivableService.saveNormalReceivable(anScfReceivable.getId(), false);
        }
        List<ScfAcceptBill> anAcceptBillList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.ACCEPTBILL);
        for(ScfAcceptBill anScfReceivable : anAcceptBillList) {
            acceptBillService.saveNormalAcceptBill(anScfReceivable.getId(), false);
        }
    }
}
