package com.betterjr.modules.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillService;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.agreement.service.ScfCustAgreementService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestType;
import com.betterjr.modules.loan.service.ScfRequestService;
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
    
    @Autowired
    private ScfRequestService requestService;

    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;

    /**
     * 订单信息分页查询
     * 
     * @param anIsOnlyNormal
     *            是否过滤，仅查询正常未融资数据 1：未融资 0：查询所有
     */
    public Page<ScfOrder> queryOrder(Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {
        // 操作员只能查询本机构数据
        // anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        // 只查询数据非自动生成的数据来源
        anMap.put("dataSource", "1");
        if (BetterStringUtils.equals(anIsOnlyNormal, "1")) {
            anMap.put("businStatus", "0");
        }
        // 模糊查询
        anMap = Collections3.fuzzyMap(anMap, new String[] { "orderNo", "settlement" });

        Page<ScfOrder> anOrderList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "businStatus,orderNo");

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
                anOrder.getAcceptBillList().addAll(acceptBillService.selectByProperty(queryMap));
            }
            else if (BetterStringUtils.equals(ScfOrderRelationType.INVOICE.getCode(), anOrderRealtion.getInfoType())) {
                anOrder.getInvoiceList().addAll((invoiceService.findInvoice(queryMap)));
            }
            else if (BetterStringUtils.equals(ScfOrderRelationType.RECEIVABLE.getCode(), anOrderRealtion.getInfoType())) {
                anOrder.getReceivableList().addAll(receivableService.selectByProperty(queryMap));
            }
            else if (BetterStringUtils.equals(ScfOrderRelationType.TRANSPORT.getCode(), anOrderRealtion.getInfoType())) {
                anOrder.getTransportList().addAll(transportService.findTransport(queryMap));
            }
        }
    }

    /**
     * 查询订单信息，无分页，前台调用
     * 
     * @param anIsOnlyNormal
     *            是否过滤，仅查询正常未融资数据 1：未融资 0：查询所有
     */
    public List<ScfOrder> findOrderList(Map<String, Object> anMap, String anIsOnlyNormal) {
        // 只查询数据非自动生成的数据来源
        anMap.put("dataSource", "1");
        if (BetterStringUtils.equals(anIsOnlyNormal, "1")) {
            anMap.put("businStatus", "0");
        }

        // 查询当前用户未融资的订单
        List<ScfOrder> orderList = this.selectByProperty(anMap);
        List<ScfOrder> retList = new ArrayList<>();

        // 过滤掉 不能融资的订单信（发票或者合同不全）
        for (ScfOrder anOrder : orderList) {
            if (false == checkInfoCompleted(anOrder.getId().toString(), RequestType.ORDER.getCode())) {
                continue;
            }
            // 核心企业名称
            anOrder.setCustName(custAccountService.queryCustName(anOrder.getCustNo()));
            anOrder.setCoreCustName(custAccountService.queryCustName(anOrder.getCoreCustNo()));
            retList.add(anOrder);
        }
        return retList;
    }

    /**
     * 根据Id查看订单详情，前台调用
     */
    public ScfOrder findOrderDetailsById(Long anId) {
        Map<String, Object> queryMap = QueryTermBuilder.newInstance().put("id", anId).build();
        List<ScfOrder> orderList = this.findOrder(queryMap);
        return Collections3.getFirst(orderList);
    }

    /**
     * 通过融资申请信息,查询融资基本信息，无分页。 包含所有下属信息
     * 
     * @param anRequestNo
     *            融资申请编号 1：订单，2:票据;3:应收款;4:经销商
     * @param anRequestType
     */
    public List<Object> findInfoListByRequest(String anRequestNo, String anRequestType) {
        ScfOrderRelationType orderRealtionType = null;
        List<Object> result = new ArrayList<Object>();
        // 订单融资
        if ("1".equals(anRequestType) || "4".equals(anRequestType)) {
            orderRealtionType = ScfOrderRelationType.ORDER;
            // 订单已经包含下属信息，直接返回
            return this.findRelationInfo(anRequestNo, orderRealtionType);
        }
        else if ("2".equals(anRequestType)) {
            orderRealtionType = ScfOrderRelationType.ACCEPTBILL;
            // 查询汇票基本信息
            List<ScfAcceptBill> acceptBillList = this.findRelationInfo(anRequestNo, orderRealtionType);
            // 通过汇票基本信息查询汇票所有信息
            List<Long> anIdList = new ArrayList<Long>();
            for (ScfAcceptBill acceptBill : acceptBillList) {
                anIdList.add(acceptBill.getId());
            }
            Map<String, Object> queryAcceptBillMap = QueryTermBuilder.newInstance().put("id", anIdList.toArray()).build();
            result.addAll(acceptBillService.findAcceptBill(queryAcceptBillMap));
            return result;
        }
        else if ("3".equals(anRequestType)) {
            orderRealtionType = ScfOrderRelationType.RECEIVABLE;
            // 查询应收账款基本信息
            List<ScfReceivable> receivableList = this.findRelationInfo(anRequestNo, orderRealtionType);
            // 通过应收账款基本信息查询应收账款所有信息
            List<Long> anIdList = new ArrayList<Long>();
            for (ScfReceivable receiable : receivableList) {
                anIdList.add(receiable.getId());
            }
            Map<String, Object> queryReceivableMap = QueryTermBuilder.newInstance().put("id", anIdList.toArray()).build();
            result.addAll(receivableService.findReceivable(queryReceivableMap));
            return result;
        }
        return result;
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
    public ScfOrder saveModifyOrder(ScfOrder anModiOrder, Long anId, String anFileList, String anOtherFileList) {
        logger.info("Begin to modify order");

        ScfOrder anOrder = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anOrder, "无法获取订单信息");
        // 检查用户是否有权限编辑
        // checkOperator(anOrder.getOperOrg(), "当前操作员不能修改该订单");
        // 检查应收账款状态 0:可用 1:过期 2:冻结
        if (!UserUtils.factorUser()) {
            checkStatus(anOrder.getBusinStatus(), "1", true, "当前订单已过期,不允许被编辑");
            checkStatus(anOrder.getBusinStatus(), "2", true, "当前订单已冻结,不允许被编辑");
        }
        // 应收账款信息变更迁移初始化
        anModiOrder.setId(anOrder.getId());
        anModiOrder.initModifyValue(UserUtils.getOperatorInfo());
        // 保存附件信息
        anModiOrder.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anOtherFileList, anOrder.getBatchNo()));
        // 保存其他文件信息
        anModiOrder.setOtherBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anFileList, anOrder.getOtherBatchNo()));
        // 数据存盘
        this.updateByPrimaryKeySelective(anModiOrder);
        return anModiOrder;
    }

    /**
     * 订单信息新增
     */
    public ScfOrder addOrder(ScfOrder anOrder, String anFileList, String anOtherFileList) {
        logger.info("Begin to add order");
        anOrder.initAddValue(UserUtils.getOperatorInfo());
        // 操作机构设置为供应商
        anOrder.setOperOrg(baseService.findBaseInfo(anOrder.getCustNo()).getOperOrg());
        // 保存附件信息
        anOrder.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anOrder.getBatchNo()));
        // 保存其他文件信息
        anOrder.setOtherBatchNo(custFileDubboService.updateCustFileItemInfo(anOtherFileList, anOrder.getOtherBatchNo()));
        this.insert(anOrder);
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
        if(!(UserUtils.factorUser()||UserUtils.platformUser())) {
            anMap.put("businStatus", anBusinStatusList);
        }
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
     * 变更订单状态 0:可用 1:过期 2:冻结
     * 
     * @param anId
     *            订单流水号
     * @param anStatus
     *            状态
     * @param anCheckOperOrg
     *            是否检查操作机构权限
     * @return
     */
    private ScfOrder saveOrderStatus(Long anId, String anStatus, boolean anCheckOperOrg) {
        ScfOrder anOrder = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anOrder, "无法获取订单信息");
        // 是否检查操作机构
        if (anCheckOperOrg) {
            checkOperator(anOrder.getOperOrg(), "当前操作员无法变更订单系信息");
        }
        // 变更状态
        anOrder.setBusinStatus(anStatus);
        anOrder.initModifyValue(UserUtils.getOperatorInfo());
        // 数据存盘
        this.updateByPrimaryKeySelective(anOrder);
        return anOrder;
    }

    /**
     * 变更订单信息 -- 可用 0:可用 1:过期 2:冻结
     * 
     * @param anId
     *            订单流水号
     * @param anCheckOperOrg
     *            是否检查操作机构权限
     * @return
     */
    public ScfOrder saveNormalOrder(Long anId, boolean anCheckOperOrg) {
        return this.saveOrderStatus(anId, "0", anCheckOperOrg);
    }

    /**
     * 变更订单信息 -- 过期 0:可用 1:过期 2:冻结
     * 
     * @param anId
     *            订单流水号
     * @param anCheckOperOrg
     *            是否检查操作机构权限
     * @return
     */
    public ScfOrder saveExpireOrder(Long anId, boolean anCheckOperOrg) {
        return this.saveOrderStatus(anId, "1", anCheckOperOrg);
    }

    /**
     * 变更订单信息 -- 冻结 0:可用 1:过期 2:冻结
     * 
     * @param anId
     *            订单流水号
     * @param anCheckOperOrg
     *            是否检查操作机构权限
     * @return
     */
    public ScfOrder saveForzenOrder(Long anId, boolean anCheckOperOrg) {
        return this.saveOrderStatus(anId, "2", anCheckOperOrg);
    }

    /**
     * 由requestNo融资申请编号查询相关联信息,不包含所关联的信息 anInfoType ScfOrderRelationType资料类型
     */
    public List findRelationInfo(String anRequestNo, ScfOrderRelationType anInfoType) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("requestNo", anRequestNo);
        List<ScfOrder> orderList = this.findOrder(anMap);
        // 查询订单信息时 直接返回订单
        if (BetterStringUtils.equals(ScfOrderRelationType.ORDER.getCode(), anInfoType.getCode())) {
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
        for (ScfOrder anOrder : anOrderList) {
            this.saveForzenOrder(anOrder.getId(), false);
        }
        List<ScfInvoice> anInvoiceList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.INVOICE);
        for (ScfInvoice anInvoice : anInvoiceList) {
            invoiceService.saveForzenInvoice(anInvoice.getId());
        }
        List<ScfReceivable> anReceivableList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.RECEIVABLE);
        for (ScfReceivable anScfReceivable : anReceivableList) {
            receivableService.saveForzenReceivable(anScfReceivable.getId(), false);
        }
        List<ScfAcceptBill> anAcceptBillList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.ACCEPTBILL);
        for (ScfAcceptBill anAcceptBill : anAcceptBillList) {
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
     * 检查订单下发票所关联订单是否勾选完成
     */
    public List<ScfOrder> checkCompleteInvoice(String anRequestType, String anIdList) {
        String[] anChosedIds = anIdList.split(",");
        Map<String, Object> anQueryInvoice = new HashMap<String, Object>();
        anQueryInvoice.put("orderId", anChosedIds);
        // 查询发票关系
        anQueryInvoice.put("infoType", "1");
        List<ScfOrderRelation> invoiceRelationList = orderRelationService.findOrderRelation(anQueryInvoice);
        List<Long> invoiceIdList = new ArrayList<Long>();
        for (ScfOrderRelation orderRelation : invoiceRelationList) {
            invoiceIdList.add(orderRelation.getInfoId());
        }
        // 通过发票id上溯订单信息
        Map<String, Object> anQueryOrderRelation = new HashMap<String, Object>();
        anQueryOrderRelation.put("infoId", invoiceIdList.toArray());
        anQueryOrderRelation.put("infoType", "1");
        List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(anQueryOrderRelation);
        // 过滤掉已经勾选的订单
        List<String> orderIdList = new ArrayList<String>();
        for (ScfOrderRelation orderRelation : orderRelationList) {
            for (String choseId : anChosedIds) {
                if (!BetterStringUtils.equals(orderRelation.getOrderId().toString(), choseId)) {
                    orderIdList.add(orderRelation.getOrderId().toString());
                }
            }
        }
        Map<String, Object> anQueryOrder = new HashMap<String, Object>();
        anQueryOrder.put("id", orderIdList.toArray());
        return this.selectByClassProperty(ScfOrder.class, anQueryOrder);
    }

    /**
     * requestType 1:订单，2:票据;3:应收款;
     */
    public void saveInfoRequestNo(String anRequestType, String anRequestNo, String anIdList) {
        String[] anIds = anIdList.split(",");
        // 订单
        if (BetterStringUtils.equals(anRequestType, "1") || BetterStringUtils.equals(anRequestType, "4")) {
            for (String anOrderId : anIds) {
                this.saveOrderRequestNo(Long.valueOf(anOrderId), anRequestNo);
            }
        }
        // 非订单
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

                // 若未找到相应信息生成默认数据
                if (Collections3.isEmpty(anOrderRelationList)) {
                    // 通过票据生成
                    if (BetterStringUtils.equals(anRequestType, "2")) {
                        ScfOrder order = new ScfOrder(acceptBillService.selectByPrimaryKey(Long.parseLong(anInfoId)));
                        order.setRequestNo(anRequestNo);
                        this.insert(order);
                        // 保存关联关系
                        ScfOrderRelation relation = new ScfOrderRelation();
                        relation.initAddValue();
                        relation.setOrderId(order.getId());
                        relation.setInfoType(ScfOrderRelationType.ACCEPTBILL.getCode());
                        relation.setInfoId(Long.valueOf(anInfoId));
                        orderRelationService.insert(relation);
                    }
                    // 应收账款生成
                    else if (BetterStringUtils.equals(anRequestType, "3")) {
                        ScfOrder order = new ScfOrder(receivableService.selectByPrimaryKey(Long.parseLong(anInfoId)));
                        order.setRequestNo(anRequestNo);
                        this.insert(order);
                        // 保存关联关系
                        ScfOrderRelation relation = new ScfOrderRelation();
                        relation.initAddValue();
                        relation.setOrderId(order.getId());
                        relation.setInfoType(ScfOrderRelationType.RECEIVABLE.getCode());
                        relation.setInfoId(Long.valueOf(anInfoId));
                        orderRelationService.insert(relation);
                    }
                }
                else {
                    for (ScfOrderRelation anOrderRelation : anOrderRelationList) {
                        this.saveOrderRequestNo(anOrderRelation.getOrderId(), anRequestNo);
                    }
                }
            }
        }
    }

    /**
     * 根据requestNo解冻订单及相关信息状态
     */
    public void unForzenInfoes(String anRequestNo, String anStatus) {
        List<ScfOrder> anOrderList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.ORDER);
        for (ScfOrder anOrder : anOrderList) {
            this.saveNormalOrder(anOrder.getId(), false);
        }
        List<ScfInvoice> anInvoiceList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.INVOICE);
        for (ScfInvoice anInvoice : anInvoiceList) {
            invoiceService.saveNormalInvoice(anInvoice.getId());
        }
        List<ScfReceivable> anReceivableList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.RECEIVABLE);
        for (ScfReceivable anScfReceivable : anReceivableList) {
            receivableService.saveNormalReceivable(anScfReceivable.getId(), false);
        }
        List<ScfAcceptBill> anAcceptBillList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.ACCEPTBILL);
        for (ScfAcceptBill anScfReceivable : anAcceptBillList) {
            acceptBillService.saveNormalAcceptBill(anScfReceivable.getId(), false);
        }
    }

    /**
     * 根据requestNo查询所有附件信息
     */
    public List<CustFileItem> findRequestBaseInfoFileList(String anRequestNo) {
        List<CustFileItem> result = new ArrayList<CustFileItem>();
        // 获取相应信息
        List<ScfOrder> orderList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.ORDER);
        List<CustAgreement> custAgreementList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.AGGREMENT);
        List<ScfTransport> transportList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.TRANSPORT);
        List<ScfInvoice> invoiceList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.INVOICE);
        List<ScfAcceptBill> acceptBillList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.ACCEPTBILL);
        List<ScfReceivable> receivableList = this.findRelationInfo(anRequestNo, ScfOrderRelationType.RECEIVABLE);
        // 订单、其他资料附件
        for (ScfOrder order : orderList) {
            result.addAll(custFileDubboService.findCustFiles(order.getBatchNo()));
            result.addAll(custFileDubboService.findCustFiles(order.getOtherBatchNo()));
        }
        // 贸易合同附件
        for (CustAgreement custAgreement : custAgreementList) {
            result.addAll(custFileDubboService.findCustFiles(custAgreement.getBatchNo()));
        }
        // 运输单据附件
        for (ScfTransport transport : transportList) {
            result.addAll(custFileDubboService.findCustFiles(transport.getBatchNo()));
        }
        // 发票附件
        for (ScfInvoice invoice : invoiceList) {
            result.addAll(custFileDubboService.findCustFiles(invoice.getBatchNo()));
        }
        // 汇票附件
        for (ScfAcceptBill acceptBill : acceptBillList) {
            result.addAll(custFileDubboService.findCustFiles(acceptBill.getBatchNo()));
            result.addAll(custFileDubboService.findCustFiles(acceptBill.getOtherBatchNo()));
        }
        // 应收账款附件
        for (ScfReceivable receivable : receivableList) {
            result.addAll(custFileDubboService.findCustFiles(receivable.getBatchNo()));
            result.addAll(custFileDubboService.findCustFiles(receivable.getOtherBatchNo()));
        }
        // 对文件信息去重
        HashSet<CustFileItem> tempSet = new HashSet<CustFileItem>(result);
        result.clear();
        result.addAll(tempSet);
        return result;
    }

    /**
     * 检查业务所需信息是否完成--贸易合同、发票 1:订单，2:票据;3:应收款;
     */
    public boolean checkInfoCompleted(String anIdList, String anRequestType) {
        String[] idList = anIdList.split(",");
        List<CustAgreement> agreementList = new ArrayList<CustAgreement>();
        List<ScfInvoice> invoiceList = new ArrayList<ScfInvoice>();
        for (String anId : idList) {
            List<ScfOrder> orderList = this.findOrder(QueryTermBuilder.newInstance().put("id", Long.valueOf(anId)).build());
            if ("1".equals(anRequestType)) {
                for (ScfOrder anOrder : orderList) {
                    agreementList.addAll(anOrder.getAgreementList());
                    invoiceList.addAll(anOrder.getInvoiceList());
                }
            }
            else if ("2".equals(anRequestType)) {
                List<ScfAcceptBill> acceptBillList = acceptBillService
                        .findAcceptBill(QueryTermBuilder.newInstance().put("id", Long.valueOf(anId)).build());
                for (ScfAcceptBill anAcceptBill : acceptBillList) {
                    agreementList.addAll(anAcceptBill.getAgreementList());
                    invoiceList.addAll(anAcceptBill.getInvoiceList());
                }
            }
            else if ("3".equals(anRequestType)) {
                List<ScfReceivable> receivableList = receivableService
                        .findReceivable(QueryTermBuilder.newInstance().put("id", Long.valueOf(anId)).build());
                for (ScfReceivable anReceivbale : receivableList) {
                    agreementList.addAll(anReceivbale.getAgreementList());
                    invoiceList.addAll(anReceivbale.getInvoiceList());
                }
            }
        }
        if (Collections3.isEmpty(agreementList)) {
            logger.warn("所选资料不存在贸易合同");
            return false;
            // throw new BytterTradeException(40001, "所选资料不存在贸易合同");
        }
        if (Collections3.isEmpty(invoiceList)) {
            logger.warn("所选资料不存在发票信息");
            // throw new BytterTradeException(40001, "所选资料不存在发票信息");
            return false;
        }

        return true;
    }

    /**
     * 根据requestNo检查是否关联贸易合同和发票，若未关联，生成默认贸易合同和发票
     */
    public void checkAndGenerateTradeAgreement(Long anAcceptBillId, Long anFactorNo) {
        ScfAcceptBill anAcceptBill = acceptBillService.findAcceptBillDetailsById(anAcceptBillId);
        
        List<CustFileItem> fileList = custFileDubboService.findCustFiles(anAcceptBill.getBatchNo());
        // 查询汇票附件中的合同附件和发票附件
        List<Long> agreeFileIdList = new ArrayList<Long>();
        List<Long> invoiceFileIdList = new ArrayList<Long>();
        for (CustFileItem anFile : fileList) {
            if (BetterStringUtils.equals("agreeAccessory", anFile.getFileInfoType())) {
                agreeFileIdList.add(anFile.getId());
            }
            if (BetterStringUtils.equals("invoiceFile", anFile.getFileInfoType())) {
                invoiceFileIdList.add(anFile.getId());
            }
        }
        
        //生成相关数据
        if (Collections3.isEmpty(anAcceptBill.getAgreementList())) {
            CustAgreement anAgree = custAgreementService.addSysCustAgreement(anAcceptBill.getCoreCustNo(), anAcceptBill.getCustNo(), anFactorNo,
                    StringUtils.collectionToDelimitedString(agreeFileIdList, ","));
            orderRelationService.addOrderRelation(ScfOrderRelationType.ACCEPTBILL.getCode(), anAcceptBillId, ScfOrderRelationType.AGGREMENT.getCode(),
                    anAgree.getId().toString());
        }
        if (Collections3.isEmpty(anAcceptBill.getInvoiceList())) {
            ScfInvoice anInvoice = invoiceService.addSysInvoice(anAcceptBill.getCustNo(), StringUtils.collectionToDelimitedString(invoiceFileIdList, ","));
            orderRelationService.addOrderRelation(ScfOrderRelationType.ACCEPTBILL.getCode(), anAcceptBillId, ScfOrderRelationType.INVOICE.getCode(),
                    anInvoice.getId().toString());
            //冻结发票
            invoiceService.saveForzenInvoice(anInvoice.getId());
        }
    }

    /**
     * 查出贸易合同不为1,未启用的贸易合同名称
     */
    public List<String> checkAgreementStatus(Long anAcceptBillId) {
        // 未启用的合同编号
        List<String> agreeNameList = new ArrayList<String>();
        ScfAcceptBill anAcceptBill = acceptBillService.findAcceptBillDetailsById(anAcceptBillId);
        BTAssert.notNull(anAcceptBill, "无法获取票据信息");
        for (CustAgreement anAgree : anAcceptBill.getAgreementList()) {
            if (!BetterStringUtils.equals("1", anAgree.getStatus())) {
                agreeNameList.add(anAgree.getAgreeName());
            }
        }
        return agreeNameList;
    }
    
    /**
     * 查询相应融资条件下的资料
     * 出具保理方案 - 110
     * 融资方确认方案 - 120
     * 发起融资背景确认 - 130
     * 核心企业确认背景 - 140
     * 放款确认 - 150
     * 完成融资 - 160
     * 放款完成 - 170
     */
    public List<ScfOrder> findOrderListByRequest(Map<String, Object> anMap) {
        List<ScfRequest> requestList = requestService.selectByProperty(anMap);
        List<String> requestNoList = Collections3.extractToList(requestList, "requestNo");
        Map<String, Object> queryMap = QueryTermBuilder.newInstance().put("requestNo", requestNoList.toArray()).build();
        return this.findOrder(queryMap);
    }
    
    /**
     * 根据融资类型和资料id串判断资料中的合同是否有未启用的
     * 1:订单，2:票据;3:应收款
     */
    public void checkRequestCustAgreementEnable(String anInfoIdList, String anRequestType) {
        List<CustAgreement> agreeList = new ArrayList<>();
        switch (anRequestType) {
        case "1":
            List<ScfOrder> orderList = this.findOrder(QueryTermBuilder.newInstance().put("id", anInfoIdList.split(",")).build());
            for(ScfOrder anOrder : orderList) {
                agreeList.addAll(anOrder.getAgreementList());
            }
            break;
        case "2":
            List<ScfAcceptBill> acceptBillList = acceptBillService.findAcceptBill(QueryTermBuilder.newInstance().put("id", anInfoIdList.split(",")).build());
            for(ScfAcceptBill anBill : acceptBillList) {
                agreeList.addAll(anBill.getAgreementList());
            }
        case "3":
            List<ScfReceivable> receivableList = receivableService.findReceivable(QueryTermBuilder.newInstance().put("id", anInfoIdList.split(",")).build());
            for(ScfReceivable anReceivable : receivableList) {
                agreeList.addAll(anReceivable.getAgreementList());
            }
        default:
            break;
        }
        for (CustAgreement anAgree : agreeList) {
            BTAssert.isTrue(BetterStringUtils.equals("1", anAgree.getStatus()), "资料中存在未启用合同，请先启用合同！");
        }
    }
}
