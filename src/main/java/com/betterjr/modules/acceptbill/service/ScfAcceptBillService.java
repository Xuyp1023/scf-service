package com.betterjr.modules.acceptbill.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.dao.ScfAcceptBillMapper;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.agreement.service.ScfCustAgreementService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.customer.ICustRelationService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.helper.RequestType;
import com.betterjr.modules.loan.service.ScfRequestService;
import com.betterjr.modules.order.data.ScfInvoiceAndAccess;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.entity.ScfOrderRelation;
import com.betterjr.modules.order.entity.ScfTransport;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;
import com.betterjr.modules.order.helper.ScfOrderRelationType;
import com.betterjr.modules.order.service.ScfInvoiceService;
import com.betterjr.modules.order.service.ScfOrderRelationService;
import com.betterjr.modules.order.service.ScfOrderService;
import com.betterjr.modules.push.service.ScfSupplierPushService;
import com.betterjr.modules.receivable.entity.ScfReceivable;
import com.betterjr.modules.supplychain.entity.CustCoreCorpInfo;
import com.betterjr.modules.supplychain.service.CustCoreCorpService;
import com.betterjr.modules.wechat.ICustWeChatService;

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

    @Autowired
    private CustCoreCorpService coreCorpService;

    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;

    @Autowired
    private ScfCustAgreementService custAgreementSerive;
    @Autowired
    private ScfInvoiceService scfInvoiceService;

    @Reference(interfaceClass = ICustRelationService.class)
    private ICustRelationService relationService;

    @Autowired
    private ScfSupplierPushService supplierPushService;

    @Reference(interfaceClass=ICustWeChatService.class)
    private ICustWeChatService custWeChatService;

    @Reference(interfaceClass=ICustFileService.class)
    private ICustFileService custFileService;

    /**
     * 批量获取票据对应的相关文件信息
     *
     * @param anResult
     * @param anBillId
     * @return
     */
    public List<Long> findBillRelBatchNoWithOutInvoice(final Long anBillId) {

        return findBillRelBatchNo(new ArrayList(), anBillId, false);
    }

    public List<Long> findBillRelBatchNo(final Long anBillId) {

        return findBillRelBatchNo(new ArrayList(), anBillId, true);
    }

    private List<Long> findBillRelBatchNo(final List<Long> anResult, final Long anBillId, final boolean anUseInvoice) {
        final ScfAcceptBill bill = this.selectByPrimaryKey(anBillId);
        if (bill == null) {

            return anResult;
        }
        logger.info("findBillRelBatchNo anBillId =" + anBillId + ", anUseInvoice = " + anUseInvoice);
        anResult.add(bill.getBatchNo());
        anResult.add(this.custAgreementSerive.findBatchNoById(bill.getAgreeId()));
        if (anUseInvoice) {
            for (final ScfInvoiceAndAccess scfInvoice : this.scfInvoiceService.queryScfInvoiceByBillNo(bill.getId(), bill.getAgreeNo())) {
                anResult.add(scfInvoice.getBatchNo());
            }
        }
        logger.info("findBillRelBatchNo result is " + anResult);
        return anResult;
    }



    /**
     * 汇票信息分页查询
     *
     * @param anIsOnlyNormal
     *            是否过滤，仅查询正常未融资数据 1：未融资 0：查询所有
     */
    public Page<ScfAcceptBill> queryAcceptBill(Map<String, Object> anMap, final String anIsOnlyNormal, final String anFlag, final int anPageNum, final int anPageSize) {
        anMap = Collections3.filterMap(anMap, new String[]{"custNo", "coreCustNo", "financeFlag", "businStatus", "GTEinvoiceDate", "LTEinvoiceDate"});
        // 操作员只能查询本机构数据
        //        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        // 构造custNo查询条件
        if(!UserUtils.coreUser()) {
            // 已审核
            anMap.put("aduit", "1");
        }
        anMap.put("holderNo", anMap.get("custNo"));
        anMap.remove("custNo");
        // 仅查询正常未融资数据
        if (BetterStringUtils.equals(anIsOnlyNormal, "1")) {
            anMap.put("businStatus", new String[] { "0", "1" });
        }
        final Page<ScfAcceptBill> anAcceptBillList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "aduit,businStatus,billNo");
        // 补全关联信息
        for (final ScfAcceptBill anAcceptBill : anAcceptBillList) {
            //核心企业名称
            anAcceptBill.setCoreCustName(custAccountService.queryCustName(anAcceptBill.getCoreCustNo()));
            final Map<String, Object> acceptBillIdMap = new HashMap<String, Object>();
            acceptBillIdMap.put("infoId", anAcceptBill.getId());
            acceptBillIdMap.put("infoType", ScfOrderRelationType.ACCEPTBILL.getCode());
            final List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(acceptBillIdMap);
            fillAcceptBillInfo(anAcceptBill, orderRelationList);
        }

        return anAcceptBillList;
    }

    /**
     * 新增汇票信息
     */
    public ScfAcceptBill addAcceptBill(final ScfAcceptBill anAcceptBill, final String anFileList, final String anOtherFileList) {
        anAcceptBill.initAddValue(UserUtils.getOperatorInfo());
        // 补充供应商客户号和持票人信息
        anAcceptBill.setHolder(anAcceptBill.getSupplier());
        anAcceptBill.setHolderNo(anAcceptBill.getSupplierNo());
        //操作机构设置为供应商
        anAcceptBill.setOperOrg(baseService.findBaseInfo(anAcceptBill.getSupplierNo()).getOperOrg());
        //保存附件信息
        anAcceptBill.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anAcceptBill.getBatchNo()));
        anAcceptBill.setOtherBatchNo(custFileDubboService.updateCustFileItemInfo(anOtherFileList, anAcceptBill.getOtherBatchNo()));
        this.insert(anAcceptBill);
        return anAcceptBill;
    }

    /**
     * 汇票信息无分页查询 ,包含所有下属信息
     *
     * @param anIsOnlyNormal
     *            是否过滤，仅查询正常未融资数据 1：未融资 0：查询所有
     */
    public List<ScfAcceptBill> findAcceptBillListByCustNo(final String anCustNo) {
        return this.findAcceptBill(QueryTermBuilder.newInstance().put("holderNo", anCustNo).build());
    }

    /**
     * 汇票信息无分页查询 ,包含所有下属信息
     *
     * @param anIsOnlyNormal
     *            是否过滤，仅查询正常未融资数据 1：未融资 0：查询所有
     */
    public List<ScfAcceptBill> findAcceptBillList(final Map<String, Object> anMap, final String anIsOnlyNormal) {
        // 仅查询正常未融资数据
        if (BetterStringUtils.equals(anIsOnlyNormal, "1")) {
            anMap.put("businStatus", new String[] { "0", "1" });
        }
        //处理查询条件custNo
        anMap.put("holderNo", anMap.get("custNo"));
        anMap.remove("custNo");
        //查询当前用户未融资的汇票
        final List<ScfAcceptBill> retList = new ArrayList<>();
        final List<ScfAcceptBill> billList = this.findAcceptBill(anMap);

        //过滤掉 不能融资的汇票（发票或者合同不全）
        for (final ScfAcceptBill scfAcceptBill : billList) {
            if(false == orderService.checkInfoCompleted(scfAcceptBill.getId().toString(), RequestType.BILL.getCode())){
                continue;
            }
            retList.add(scfAcceptBill);
        }

        return retList;
    }

    /**
     * 根据Id汇票详情查询
     */
    public ScfAcceptBill findAcceptBillDetailsById(final Long anId) {
        final Map<String, Object> queryMap = QueryTermBuilder.newInstance().put("id", anId).build();
        final List<ScfAcceptBill> acceptBillList = this.findAcceptBill(queryMap);
        return Collections3.getFirst(acceptBillList);
    }

    /**
     * 票据转让
     */
    public ScfAcceptBill saveTransferAcceptBill(final Long anId, final String anHolder, final Long anHolderNo) {
        final ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAcceptBill, "无法获得汇票信息");
        // 更新之前票据信息
        anAcceptBill.setNextHand(anHolder);
        anAcceptBill.setNextHandNo(anHolderNo);
        this.updateByPrimaryKeySelective(anAcceptBill);
        // 生成新票据，流水号也是新的
        anAcceptBill.initTransferValue();
        // 变更持票人
        anAcceptBill.setHolder(anHolder);
        anAcceptBill.setHolderNo(anHolderNo);
        this.insert(anAcceptBill);
        saveCopyOrderRelation(anId, anAcceptBill.getId());
        return anAcceptBill;
    }

    /**
     * 复制票据资料的关联关系
     */
    private void saveCopyOrderRelation(final Long anOldId, final Long anAimId) {
        final Map<String, Object> queryMap = QueryTermBuilder.newInstance().put("infoTyoe", ScfOrderRelationType.ACCEPTBILL.getCode())
                .put("infoId", anOldId).build();
        final List<ScfOrderRelation> orderRelationList = orderRelationService.selectByProperty(queryMap);
        for(final ScfOrderRelation orderRelation : orderRelationList) {
            orderRelation.initAddValue();
            orderRelation.setInfoId(anAimId);
            orderRelationService.insert(orderRelation);
        }
    }

    /**
     * 审核汇票信息
     */
    public ScfAcceptBill saveAduitAcceptBill(final Long anId) {
        final ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAcceptBill, "无法获得汇票信息");
        BTAssert.isTrue(anAcceptBill.getAduit().equals("0"), "所选汇票已审核");
        anAcceptBill.setAduit("1");
        this.updateByPrimaryKey(anAcceptBill);
        return anAcceptBill;
    }

    /**
     * 汇票信息编辑修改
     */
    public ScfAcceptBill saveModifyAcceptBill(final ScfAcceptBill anModiAcceptBill, final Long anId, final String anFileList, final String anOtherFileList) {
        logger.info("Begin to modify AcceptBill");

        final ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAcceptBill, "无法获得汇票信息");

        // 检查当前操作员是否可以修改该票据信息
        //        checkOperator(anAcceptBill.getOperOrg(), "当前操作员不能修改该票据");

        // 检查状态,仅未处理和未融资状态允许修改汇票信息
        if(!UserUtils.factorUser()) {
            checkStatus(anAcceptBill.getBusinStatus(), "2", true, "当前票据已融资,不允许修改");
            checkStatus(anAcceptBill.getBusinStatus(), "3", true, "当前票据已过期,不允许修改");
            checkStatus(anAcceptBill.getFinanceFlag(), "0", false, "当前票据已融资,不允许修改");
            anModiAcceptBill.setBusinStatus("1");
        }
        // 数据编辑初始化
        anModiAcceptBill.setId(anId);
        anModiAcceptBill.initModifyValue(UserUtils.getOperatorInfo());
        // 设置汇票状态(businStatus:1-完善资料)
        // 数据存盘
        //保存附件信息
        anModiAcceptBill.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anFileList, anAcceptBill.getBatchNo()));
        anModiAcceptBill.setOtherBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anOtherFileList, anAcceptBill.getOtherBatchNo()));
        this.updateByPrimaryKeySelective(anModiAcceptBill);
        return anModiAcceptBill;
    }

    /**
     * 查询汇票信息,包含所有下属信息
     */
    public List<ScfAcceptBill> findAcceptBill(final Map<String, Object> anMap) {
        final List<ScfAcceptBill> anAcceptBillList = this.selectByClassProperty(ScfAcceptBill.class, anMap);
        //下属信息
        for (final ScfAcceptBill anAcceptBill : anAcceptBillList) {
            //核心企业名称
            anAcceptBill.setCoreCustName(custAccountService.queryCustName(anAcceptBill.getCoreCustNo()));
            final Map<String, Object> acceptBillIdMap = new HashMap<String, Object>();
            acceptBillIdMap.put("infoId", anAcceptBill.getId());
            acceptBillIdMap.put("infoType", ScfOrderRelationType.ACCEPTBILL.getCode());
            final List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(acceptBillIdMap);
            fillAcceptBillInfo(anAcceptBill, orderRelationList);
        }
        return anAcceptBillList;
    }

    /**
     * 查询汇票信息,包含所有下属信息
     */
    public ScfAcceptBill findAcceptBill(final Long anAcceptBillId) {
        final ScfAcceptBill acceptBill = this.selectByPrimaryKey(anAcceptBillId);
        //未查询到数据，返回空对象
        if( null == acceptBill) {
            return new ScfAcceptBill();
        } else {
            //核心企业名称
            acceptBill.setCoreCustName(custAccountService.queryCustName(acceptBill.getCoreCustNo()));
            final Map<String, Object> acceptBillIdMap = new HashMap<String, Object>();
            acceptBillIdMap.put("infoId", acceptBill.getId());
            acceptBillIdMap.put("infoType", ScfOrderRelationType.ACCEPTBILL.getCode());
            final List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(acceptBillIdMap);
            fillAcceptBillInfo(acceptBill, orderRelationList);
            return acceptBill;
        }
    }

    /**
     * 查询汇票所有附件
     */
    public List<CustFileItem> findAllFile(final Long anId) {
        // 初始化信息
        final List<CustFileItem> result = new ArrayList<CustFileItem>();
        final ScfAcceptBill acceptBill = this.findAcceptBill(anId);
        //汇票附件
        result.addAll(custFileDubboService.findCustFiles(acceptBill.getBatchNo()));
        //订单
        for(final ScfOrder order : acceptBill.getOrderList()) {
            result.addAll(custFileDubboService.findCustFiles(order.getBatchNo()));
            result.addAll(custFileDubboService.findCustFiles(order.getOtherBatchNo()));
        }
        //应收账款
        for(final ScfReceivable receivable : acceptBill.getReceivableList()) {
            result.addAll(custFileDubboService.findCustFiles(receivable.getBatchNo()));
        }
        //贸易合同
        for(final CustAgreement agreement : acceptBill.getAgreementList()) {
            result.addAll(custFileDubboService.findCustFiles(agreement.getBatchNo()));
        }
        //运输单据
        for(final ScfTransport transport : acceptBill.getTransportList()) {
            result.addAll(custFileDubboService.findCustFiles(transport.getBatchNo()));
        }
        //发票信息
        for(final ScfInvoice invoice : acceptBill.getInvoiceList()) {
            result.addAll(custFileDubboService.findCustFiles(invoice.getBatchNo()));
        }
        //对文件信息去重
        final HashSet<CustFileItem> tempSet = new HashSet<CustFileItem>(result);
        result.clear();
        result.addAll(tempSet);
        return result;
    }

    /**
     * 保理公司查询已经融资汇票信息
     */
    public Page<ScfAcceptBill> queryFinancedByFactor(final Map<String, Object> anBillConditionMap, final Long anFactorNo) {
        //模糊查询
        Collections3.fuzzyMap(anBillConditionMap, new String[]{"billNo", "invoiceCorp"});
        final List<ScfAcceptBill> acceptBillList = new ArrayList<ScfAcceptBill>();
        //查询相应资金方下的已融资信息
        final Map<String, Object> queryRequestMap = new HashMap<String, Object>();
        queryRequestMap.put("factorNo", anFactorNo);
        //已放款融资
        queryRequestMap.put("GTtradeStatus", "150");
        final List<ScfRequest> requestList = requestService.findRequestList(queryRequestMap);
        //根据融资申请取出订单信息
        final Map<String, Object> queryOrderMap = new HashMap<String, Object>();
        final Map<String, Object> queryOrderRelationMap = new HashMap<String, Object>();
        queryOrderRelationMap.put("infoType", "3");//信息类型 0:合同 1:发票 2:运输单据 3:汇票 4:应收账款

        for (final ScfRequest request : requestList) {
            queryOrderMap.put("requestNo", request.getRequestNo());
            final List<ScfOrder> orderList = orderService.findOrder(queryOrderMap);
            // 根据订单查询订单与汇票关系
            for (final ScfOrder order : orderList) {
                queryOrderRelationMap.put("orderId", order.getId());
                final List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(queryOrderRelationMap);
                //根据关系表查询汇票信息
                for(final ScfOrderRelation orderRelation : orderRelationList) {
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
    public void fillAcceptBillInfo(final ScfAcceptBill anAcceptBill, final List<ScfOrderRelation> anOrderRelationList) {
        anAcceptBill.setAgreementList(new ArrayList<CustAgreement>());
        anAcceptBill.setOrderList(new ArrayList<ScfOrder>());
        anAcceptBill.setTransportList(new ArrayList<ScfTransport>());
        anAcceptBill.setInvoiceList(new ArrayList<ScfInvoice>());
        anAcceptBill.setReceivableList(new ArrayList<ScfReceivable>());
        for (final ScfOrderRelation anOrderRelation : anOrderRelationList) {
            final Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("id", anOrderRelation.getOrderId());
            //由于是id查出，取出数据
            final ScfOrder anOrder = Collections3.getFirst(orderService.findOrder(queryMap));
            anAcceptBill.getInvoiceList().addAll(anOrder.getInvoiceList());
            anAcceptBill.getTransportList().addAll(anOrder.getTransportList());
            anAcceptBill.getReceivableList().addAll(anOrder.getReceivableList());
            anAcceptBill.getAgreementList().addAll(anOrder.getAgreementList());
            // 清除order下面的信息
            anOrder.clearRelationInfo();
            //若数据来源不为自动生成，则加入
            if(!"0".equals(anOrder.getDataSource())){
                anAcceptBill.getOrderList().add(anOrder);
            }
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
    @Override
    public void checkInfoExist(final Long anId, final String anOperOrg) {
        final Map<String, Object> anMap = new HashMap<String, Object>();
        final String[] anBusinStatusList = { "0", "1" };
        anMap.put("id", anId);
        if (!(UserUtils.factorUser()||UserUtils.platformUser())) {
            anMap.put("businStatus", anBusinStatusList);
        }
        // 查询每个状态数据
        final List<ScfAcceptBill> acceptBillList = this.selectByProperty(anMap);
        if (Collections3.isEmpty(acceptBillList)) {
            logger.warn("不存在相对应id,操作机构,业务状态的汇票信息");
            throw new BytterTradeException(40001, "不存在相对应id,操作机构,业务状态的汇票信息");
        }
    }

    /**
     * 检查用户是否有权限操作数据
     */
    private void checkOperator(final String anOperOrg, final String anMessage) {
        if (BetterStringUtils.equals(UserUtils.getOperatorInfo().getOperOrg(), anOperOrg) == false) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

    /**
     * 检查状态信息
     */
    private void checkStatus(final String anBusinStatus, final String anTargetStatus, final boolean anFlag, final String anMessage) {
        if (BetterStringUtils.equals(anBusinStatus, anTargetStatus) == anFlag) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

    /**
     * 变更融资标志
     */
    public ScfAcceptBill saveFinanceFlag(final Long anId, final String anFinanceFalg) {
        final ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anId);
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
    private ScfAcceptBill saveAcceptBillStatus(final Long anId, final String anStatus, final String anFinanceFlag, final boolean anCheckOperOrg) {
        final ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anId);
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
    public ScfAcceptBill saveNormalAcceptBill(final Long anId, final boolean anCheckOperOrg) {
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
    public ScfAcceptBill saveFinancedAcceptBill(final Long anId, final boolean anCheckOperOrg) {
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
    public ScfAcceptBill saveExpireAcceptBill(final Long anId, final boolean anCheckOperOrg) {
        return this.saveAcceptBillStatus(anId, "3", "0", anCheckOperOrg);
    }

    /**
     * 保存上传上来的票据信息数据。处理的逻辑是如果票据信息没有使用就更新，不存在里面增加，<BR>
     * 如果已经使用，则不变。 需要了解票据作废的处理流程。
     *
     * @param anList
     * @param anOperOrg
     * @return
     */
    public boolean saveUploadSupplierData(final List<ScfAcceptBill> anList, final String anCoreOperOrg) {
        // 没数据，直接返回
        if (Collections3.isEmpty(anList)) {
            return true;
        }
        final Set<String> billNoSet = ReflectionUtils.listToKeySet(anList, "btBillNo");
        final Map<String, Object> termMap = new HashMap();
        termMap.put("coreOperOrg", anCoreOperOrg);
        termMap.put("btBillNo", billNoSet);
        final Map<String, ScfAcceptBill> scfAcceptMap = ReflectionUtils.listConvertToMap(this.selectByProperty(termMap), "btBillNo");
        ScfAcceptBill tmpScfBill = null;
        final Set<ScfAcceptBill> tmpBillSet = new HashSet();
        for (final ScfAcceptBill scfAccept : anList) {
            tmpScfBill = scfAcceptMap.get(scfAccept.getBtBillNo());
            if (tmpScfBill == null){
                tmpScfBill = findBillByNo(anCoreOperOrg, scfAccept.getBillNo());
            }

            if (MathExtend.smallValue(scfAccept.getSupplierNo())){
                scfAccept.setSupplierNo(this.relationService.findCustNoByBankInfo(scfAccept.getSupplier(), scfAccept.getSuppBankAccount()));
            }

            if (tmpScfBill == null) {
                // scfAccept.fillDefaultValue();


                final String coreCorpId = scfAccept.getDrawerId();

                if (BetterStringUtils.isBlank(coreCorpId)) {
                    logger.info("资金系统 票据 drawerId 为空:" + scfAccept);
                }

                final CustCoreCorpInfo coreCorpInfo = coreCorpService.findByCorpId(anCoreOperOrg, coreCorpId);
                if (coreCorpInfo == null) {
                    logger.info("资金系统 票据 drawerId 对应核心企业 为空:" + scfAccept);
                } else {
                    scfAccept.setCoreCustNo(coreCorpInfo.getCustNo());
                    scfAccept.setBuyerNo(coreCorpInfo.getCustNo());
                    //scfAccept.setBuyer(coreCorpInfo.getCustName());
                }

                scfAccept.setHolderNo( scfAccept.getSupplierNo());
                scfAccept.setHolder(scfAccept.getHolder());
                scfAccept.setHolderBankAccount(scfAccept.getSuppBankAccount());
                scfAccept.setCoreOperOrg(anCoreOperOrg);
                logger.debug("this is save bill data " + scfAccept);
                this.insert(scfAccept);
                tmpBillSet.add(scfAccept);
            } // 只能更新状态为0未处理的票据信息，状态为“9”表示作废
            else if ("0".equals(tmpScfBill.getBusinStatus()) || "9".equals(scfAccept.getBusinStatus())) {
                scfAccept.modifyDfaultValue(tmpScfBill);
                this.updateByPrimaryKey(scfAccept);

                // 如果出现退票的情况，需要检查是否存在交易，如果存在交易，相关的交易应该也需要撤单。
                if ("9".equals(scfAccept.getBusinStatus())) {
                    //  MQSender.send(MQTopics.ACCESS_BILL, scfAccept);
                }
            }
        }

        for (final ScfAcceptBill sendBill : tmpBillSet) {
            try {
                if (sendBill.sendCheck()) {
                    supplierPushService.pushSupplierInfo(sendBill.getId());
                }
                else {
                    logger.warn("其它原因，不能发送票据信息, getSupplierNo = " + sendBill.getSupplierNo() + ", getBillType =" + sendBill.getBillType());
                }
            }
            catch (final Exception ex) {
                logger.error("发送微信消息出现异常", ex);
            }
        }
        return true;
    }

    /**
     * 检查核心企业中票据的唯一性
     * @param anCoreCustNo
     * @param anBillNo
     * @return
     */
    public ScfAcceptBill findBillByNo(final String anCoreOperOrg, final String anBillNo) {
        final Map<String, Object> termMap = new HashMap();
        termMap.put("coreOperOrg", anCoreOperOrg);
        termMap.put("billNo", anBillNo);

        return Collections3.getFirst(this.selectByProperty(termMap));
    }

    /**
     * 根据汇票id和文件id关联附件信息
     */
    public boolean saveSingleFileLink(final Long anId, final Long anFileId) {
        final ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAcceptBill, "无法获取汇票信息");
        anAcceptBill.setBatchNo(custFileDubboService.updateCustFileItemInfo( anFileId.toString(),anAcceptBill.getBatchNo()));

        return this.updateByPrimaryKey(anAcceptBill) ==1;
    }

    public CustFileItem saveBillFile(final Long anBillId,final String anFileTypeName,final String anFileMediaId){
        final ScfAcceptBill anAcceptBill = this.selectByPrimaryKey(anBillId);
        CustFileItem fileItem = (CustFileItem)custWeChatService.fileUpload(anFileTypeName, anFileMediaId);
        logger.info("custFileItem:"+fileItem);
        if(fileItem!=null){
            fileItem.setBatchNo(custFileService.updateCustFileItemInfo(fileItem.getId().toString(), anAcceptBill.getBatchNo()));
            anAcceptBill.setBatchNo(fileItem.getBatchNo());
            this.updateByPrimaryKey(anAcceptBill);
        }else{
            fileItem=new CustFileItem();
        }
        return fileItem;
    }

}
