package com.betterjr.modules.acceptbill.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.betterjr.common.data.SimpleDataEntity;
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
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.entity.ScfOrderRelation;
import com.betterjr.modules.order.entity.ScfTransport;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;
import com.betterjr.modules.order.helper.ScfOrderRelationType;
import com.betterjr.modules.order.service.ScfOrderRelationService;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.receivable.entity.ScfReceivable;

@Service
public class ScfAcceptBillService extends BaseService<ScfAcceptBillMapper, ScfAcceptBill> implements IScfOrderInfoCheckService {

    private static final Logger logger = LoggerFactory.getLogger(ScfAcceptBillService.class);

    @Autowired
    private ScfOrderRelationService orderRelationService;
    @Autowired
    private ScfOrderService orderService;
    @Autowired
    private ScfRequestService requestService;
    @Autowired
    private CustAccountService custAccountService;

    /**
     * 汇票信息分页查询
     * 
     * @param anIsOnlyNormal
     *            是否过滤，仅查询正常未融资数据 1：未融资 0：查询所有
     */
    public Page<ScfAcceptBill> queryAcceptBill(Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {

        // 操作员只能查询本机构数据
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        // 构造custNo查询条件
        // 当用户为供应商
        if (UserUtils.supplierUser()) {
            anMap.put("supplierNo", anMap.get("custNo"));
            // 当用户为经销商
        }
        else if (UserUtils.sellerUser()) {
            anMap.put("buyerNo", anMap.get("custNo"));
            // 当用户为核心企业
        }
        else if (UserUtils.coreUser()) {
            anMap.put("coreCustNo", anMap.get("custNo"));
        }
        anMap.remove("custNo");
        // 仅查询正常未融资数据
        if (BetterStringUtils.equals(anIsOnlyNormal, "1")) {
            anMap.put("businStatus", new String[] { "0", "1" });
            // 已审核
            anMap.put("aduit", "1");
        }
        Page<ScfAcceptBill> anAcceptBillList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
        // 补全关联信息
        for (ScfAcceptBill anAcceptBill : anAcceptBillList) {
            //核心企业名称
            anAcceptBill.setCoreCustName(custAccountService.queryCustName(anAcceptBill.getCoreCustNo()));
            Map<String, Object> acceptBillIdMap = new HashMap<String, Object>();
            acceptBillIdMap.put("infoId", anAcceptBill.getId());
            acceptBillIdMap.put("infoType", ScfOrderRelationType.ACCEPTBILL.getCode());
            List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(acceptBillIdMap);
            fillAcceptBillInfo(anAcceptBill, orderRelationList);
        }

        return anAcceptBillList;
    }

    /**
     * 新增汇票信息
     */
    public ScfAcceptBill addAcceptBill(ScfAcceptBill anAcceptBill) {
        anAcceptBill.initAddValue(anAcceptBill);
        // 补充供应商客户号和持票人信息
        this.insert(anAcceptBill);
        return anAcceptBill;
    }

    /**
     * 汇票信息无分页查询
     * 
     * @param anIsOnlyNormal
     *            是否过滤，仅查询正常未融资数据 1：未融资 0：查询所有
     */
    public List<ScfAcceptBill> findAcceptBillList(String anCustNo, String anIsOnlyNormal) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        // 构造custNo查询条件
        // 当用户为供应商
        if (UserUtils.supplierUser()) {
            anMap.put("supplierNo", anCustNo);
            // 当用户为经销商
        }
        else if (UserUtils.sellerUser()) {
            anMap.put("buyerNo", anCustNo);
        }
        // 仅查询正常未融资数据
        if (BetterStringUtils.equals(anIsOnlyNormal, "1")) {
            anMap.put("businStatus", new String[] { "0", "1" });
        }
        return this.findAcceptBill(anMap);
    }

    /**
     * 票据转让
     */
    public ScfAcceptBill saveTransferAcceptBill(Long anId, String anHolder, Long anHolderNo) {
        ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAcceptBill, "无法获得汇票信息");
        // 更新之前票据信息
        anAcceptBill.setNextHand(anHolder);
        // 生成新票据
        anAcceptBill.initTransferValue();
        // 变更持票人
        anAcceptBill.setHolder(anHolder);
        anAcceptBill.setHolderNo(anHolderNo);
        this.insert(anAcceptBill);
        return anAcceptBill;
    }

    /**
     * 审核汇票信息
     */
    public ScfAcceptBill saveAduitAcceptBill(Long anId) {
        ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAcceptBill, "无法获得汇票信息");
        BTAssert.isTrue(anAcceptBill.getAduit().equals("1"), "所选汇票已审核");
        anAcceptBill.setAduit("1");
        this.updateByPrimaryKey(anAcceptBill);
        return anAcceptBill;
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
        // 数据存盘
        this.updateByPrimaryKeySelective(anAcceptBill);
        return anAcceptBill;
    }

    /**
     * 查询汇票信息
     */
    public List<ScfAcceptBill> findAcceptBill(Map<String, Object> anMap) {
        List<ScfAcceptBill> anAcceptBillList = this.selectByClassProperty(ScfAcceptBill.class, anMap);
        for(ScfAcceptBill anAcceptBill : anAcceptBillList) {
          //核心企业名称
          anAcceptBill.setCoreCustName(custAccountService.queryCustName(anAcceptBill.getCoreCustNo()));
        }
        return anAcceptBillList;
    }

    /**
     * 查询汇票所有附件
     */
    public List<SimpleDataEntity> findAllFile(Long anId) {
        // 初始化信息
        List<SimpleDataEntity> result = new ArrayList<SimpleDataEntity>();
        List<Long> agreementBathNoList = new ArrayList<Long>();
        List<Long> transportBathNoList = new ArrayList<Long>();
        List<Long> invoiceBathNoList = new ArrayList<Long>();
        List<Long> orderBathNoList = new ArrayList<Long>();
        List<Long> acceptBillBathNoList = new ArrayList<Long>();
        List<Long> receivableBathNoList = new ArrayList<Long>();
        List<Long> otherBathNoList = new ArrayList<Long>();
        // 查询汇票所包含所有信息
        ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAcceptBill, "无法获得汇票信息");
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("infoType", ScfOrderRelationType.ACCEPTBILL.getCode());
        queryMap.put("infoId", anAcceptBill.getId());
        List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(queryMap);
        // 查询相关联信息
        fillAcceptBillInfo(anAcceptBill, orderRelationList);
        // 开始组织batchNo
        // 贸易合同
        for (CustAgreement agreement : anAcceptBill.getAgreementList()) {
            if (null != agreement.getBatchNo()) {
                agreementBathNoList.add(agreement.getBatchNo());
            }
        }
        // 运输单据
        for (ScfTransport transport : anAcceptBill.getTransportList()) {
            if (null != transport.getBatchNo()) {
                transportBathNoList.add(transport.getBatchNo());
            }
        }
        // 发票
        for (ScfInvoice invoice : anAcceptBill.getInvoiceList()) {
            if (null != invoice.getBatchNo()) {
                invoiceBathNoList.add(invoice.getBatchNo());
            }
        }
        // 订单
        for (ScfOrder order : anAcceptBill.getOrderList()) {
            if (null != order.getBatchNo()) {
                orderBathNoList.add(order.getBatchNo());
            }
            if (null != order.getOtherBatchNo()) {
                otherBathNoList.add(order.getOtherBatchNo());
            }
        }
        // 应收账款
        for (ScfReceivable receivable : anAcceptBill.getReceivableList()) {
            if (null != receivable.getBatchNo()) {
                receivableBathNoList.add(receivable.getBatchNo());
            }
        }
        // 汇票信息
        acceptBillBathNoList.add(anAcceptBill.getBatchNo());
        result.add(new SimpleDataEntity("贸易合同附件", StringUtils.collectionToDelimitedString(agreementBathNoList, ",")));
        result.add(new SimpleDataEntity("运输单据附件", StringUtils.collectionToDelimitedString(transportBathNoList, ",")));
        result.add(new SimpleDataEntity("发票附件", StringUtils.collectionToDelimitedString(invoiceBathNoList, ",")));
        result.add(new SimpleDataEntity("订单附件", StringUtils.collectionToDelimitedString(orderBathNoList, ",")));
        result.add(new SimpleDataEntity("应收账款附件", StringUtils.collectionToDelimitedString(receivableBathNoList, ",")));
        result.add(new SimpleDataEntity("汇票信息附件", StringUtils.collectionToDelimitedString(acceptBillBathNoList, ",")));
        result.add(new SimpleDataEntity("其他信息附件", StringUtils.collectionToDelimitedString(otherBathNoList, ",")));
        return result;
    }

    /**
     * 保理公司查询已经融资汇票信息
     */
    public Page<ScfAcceptBill> queryFinancedByFactor(Map<String, Object> anBillConditionMap, Long anFactorNo) {
        List<ScfAcceptBill> acceptBillList = new ArrayList<ScfAcceptBill>();
        //查询相应资金方下的已融资信息
        Map<String, Object> queryRequestMap = new HashMap<String, Object>();
        queryRequestMap.put("factorNo", anFactorNo);
        //已放款融资
        queryRequestMap.put("GTtradeStatus", "150");
        List<ScfRequest> requestList = requestService.findRequestList(queryRequestMap);
        //根据融资申请取出订单信息
        Map<String, Object> queryOrderMap = new HashMap<String, Object>();
        Map<String, Object> queryOrderRelationMap = new HashMap<String, Object>();
        queryOrderRelationMap.put("infoType", "3");//信息类型 0:合同 1:发票 2:运输单据 3:汇票 4:应收账款
        
        for (ScfRequest request : requestList) {
            queryOrderMap.put("requestNo", request.getRequestNo());
            List<ScfOrder> orderList = orderService.findOrder(queryOrderMap);
            // 根据订单查询订单与汇票关系
            for (ScfOrder order : orderList) {
                queryOrderRelationMap.put("orderId", order.getId());
                List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(queryOrderRelationMap);
                //根据关系表查询汇票信息
                for(ScfOrderRelation orderRelation : orderRelationList) {
                    anBillConditionMap.put("id", orderRelation.getInfoId());
                    acceptBillList.addAll(this.findAcceptBill(anBillConditionMap));
                }
            }
        }
        return Page.listToPage(acceptBillList);
    }

    /**
     * 根据订单关联关系补全汇票信息
     */
    public void fillAcceptBillInfo(ScfAcceptBill anAcceptBill, List<ScfOrderRelation> anOrderRelationList) {
        anAcceptBill.setAgreementList(new ArrayList<CustAgreement>());
        anAcceptBill.setOrderList(new ArrayList<ScfOrder>());
        anAcceptBill.setTransportList(new ArrayList<ScfTransport>());
        anAcceptBill.setInvoiceList(new ArrayList<ScfInvoice>());
        anAcceptBill.setReceivableList(new ArrayList<ScfReceivable>());
        for (ScfOrderRelation anOrderRelation : anOrderRelationList) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("id", anOrderRelation.getOrderId());
            List<ScfOrder> orderList = orderService.findOrder(queryMap);
            for (ScfOrder anOrder : orderList) {
                anAcceptBill.getInvoiceList().addAll(anOrder.getInvoiceList());
                anAcceptBill.getTransportList().addAll(anOrder.getTransportList());
                anAcceptBill.getReceivableList().addAll(anOrder.getReceivableList());
                anAcceptBill.getAgreementList().addAll(anOrder.getAgreementList());
                // 清除order下面的信息
                anOrder.clearRelationInfo();
            }
            anAcceptBill.getOrderList().addAll(orderList);
        }
    }

    /**
     * 检查是否存在相应id、操作机构、业务状态的汇票信息
     * 
     * @param anId
     *            汇票信息id
     * @param anOperOrg
     *            操作机构
     */
    public void checkInfoExist(Long anId, String anOperOrg) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        String[] anBusinStatusList = { "0", "1" };
        anMap.put("id", anId);
        anMap.put("operOrg", anOperOrg);
        anMap.put("businStatus", anBusinStatusList);
        // 查询每个状态数据
        List<ScfAcceptBill> acceptBillList = this.selectByClassProperty(ScfAcceptBill.class, anMap);
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
     * 变更融资标志
     */
    public ScfAcceptBill saveFinanceFlag(Long anId, String anFinanceFalg) {
        ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAcceptBill, "无法获取汇票信息");
        anAcceptBill.setFinanceFlag(anFinanceFalg);
        this.updateByPrimaryKeySelective(anAcceptBill);
        return anAcceptBill;
    }

    /**
     * 变更汇票信息状态 0未处理，1完善资料，2已融资，3已过期 
     *        融资标志，0未融资，1已融资，2收款，3已还款，4融资失败             
     * 
     * @param anId
     *            汇票流水号
     * @param anStatus
     *            状态
     * @param anCheckOperOrg
     *            是否检查操作机构权限
     * @return
     */
    private ScfAcceptBill saveAcceptBillStatus(Long anId, String anStatus, String anFinanceFlag, boolean anCheckOperOrg) {
        ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAcceptBill, "无法获取汇票信息");
        // 检查用户权限
        if (anCheckOperOrg) {
            checkOperator(anAcceptBill.getOperOrg(), "当前操作员无法变更汇票信息");
        }
        // 变更状态
        anAcceptBill.setBusinStatus(anStatus);
        anAcceptBill.setFinanceFlag(anFinanceFlag);
        anAcceptBill.setModiOperId(UserUtils.getOperatorInfo().getId());
        anAcceptBill.setModiOperName(UserUtils.getOperatorInfo().getName());
        anAcceptBill.setModiDate(BetterDateUtils.getNumDate());
        anAcceptBill.setModiTime(BetterDateUtils.getNumTime());
        // 数据存盘
        this.updateByPrimaryKeySelective(anAcceptBill);
        return anAcceptBill;
    }

    /**
     * 变更汇票信息 -- 完善资料 0未处理，1完善资料，2已融资，3已过期 
     *        融资标志，0未融资，1已融资，2收款，3已还款，4融资失败
     * 
     * @param anId
     * @param anCheckOperOrg
     * @return
     */
    public ScfAcceptBill saveNormalAcceptBill(Long anId, boolean anCheckOperOrg) {
        return this.saveAcceptBillStatus(anId, "1", "0", anCheckOperOrg);
    }

    /**
     * 变更汇票信息 -- 已融资 0未处理，1完善资料，2已融资，3已过期 
     *        融资标志，0未融资，1已融资，2收款，3已还款，4融资失败
     * 
     * @param anId
     * @param anCheckOperOrg
     * @return
     */
    public ScfAcceptBill saveFinancedAcceptBill(Long anId, boolean anCheckOperOrg) {
        return this.saveAcceptBillStatus(anId, "2", "1", anCheckOperOrg);
    }

    /**
     * 变更汇票信息 -- 已过期 0未处理，1完善资料，2已融资，3已过期 
     *        融资标志，0未融资，1已融资，2收款，3已还款，4融资失败
     * 
     * @param anId
     * @param anCheckOperOrg
     * @return
     */
    public ScfAcceptBill saveExpireAcceptBill(Long anId, boolean anCheckOperOrg) {
        return this.saveAcceptBillStatus(anId, "3", "0", anCheckOperOrg);
    }

}
