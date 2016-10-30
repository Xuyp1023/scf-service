package com.betterjr.modules.receivable.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.agreement.entity.CustAgreement;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.loan.helper.RequestType;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.entity.ScfOrderRelation;
import com.betterjr.modules.order.entity.ScfTransport;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;
import com.betterjr.modules.order.helper.ScfOrderRelationType;
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
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;
    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;
    
    /**
     * 应收账款编辑
     */
    public ScfReceivable saveModifyReceivable(ScfReceivable anMoidReceivable, Long anId, String anFileList, String anOtherFileList) {
        logger.info("Begin to modify receivable");
        
        ScfReceivable anReceivable = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anReceivable, "无法获取原应收账款信息");
        //检查用户是否有权限编辑
//        checkOperator(anReceivable.getOperOrg(), "当前操作员不能修改该应收账款");
        //检查应收账款状态 0:可用 1:过期 2:冻结
        if(!UserUtils.factorUser()) {
            checkStatus(anReceivable.getBusinStatus(), "1", true, "当前应收账款已过期,不允许被编辑");
            checkStatus(anReceivable.getBusinStatus(), "2", true, "当前应收账款已冻结,不允许被编辑");
        }
        //应收账款信息变更迁移初始化
        anMoidReceivable.setId(anId);
        anMoidReceivable.initModifyValue(UserUtils.getOperatorInfo());
        //保存附件信息
        anMoidReceivable.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anFileList, anReceivable.getBatchNo()));
        anMoidReceivable.setOtherBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anOtherFileList, anReceivable.getOtherBatchNo()));
        //数据存盘
        this.updateByPrimaryKeySelective(anMoidReceivable);
        return anMoidReceivable;
    }
    

    /**
     * 应收账款分页查询
     * anIsOnlyNormal 是否过滤，仅查询正常未融资数据 1：未融资 0：查询所有
     */
    public Page<ScfReceivable> queryReceivable(Map<String, Object> anMap, String anIsOnlyNormal,  String anFlag, int anPageNum, int anPageSize) {
        //操作员只能查询本机构数据
//        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        if(!UserUtils.coreUser()) {
            // 已审核
               anMap.put("aduit", "1");
           }
        if(BetterStringUtils.equals(anIsOnlyNormal, "1")) {
            anMap.put("businStatus", "0");
        }
        //应收账款模糊查询
        anMap = Collections3.fuzzyMap(anMap, new String[]{"receivableNo"}); 
        Page<ScfReceivable> anReceivableList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "aduit,businStatus, receivableNo");
        
        //补全关联信息
        for(ScfReceivable anReceivable : anReceivableList) {
            Map<String, Object> anReceivableIdMap = QueryTermBuilder.newInstance().put("infoId", anReceivable.getId()).put("infoType", ScfOrderRelationType.RECEIVABLE.getCode()).build();
            List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(anReceivableIdMap);
            fillReceivableInfo(anReceivable, orderRelationList);
        }
        
        return anReceivableList;
    }
    
    /**
     * 应收账款无分页查询
     * anIsOnlyNormal 是否过滤，仅查询正常未融资数据 1：未融资 0：查询所有
     */
    public List<ScfReceivable> findReceivableList(String anCustNo, String anIsOnlyNormal) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("custNo", anCustNo);
        if(BetterStringUtils.equals(anIsOnlyNormal, "1")) {
            anMap.put("businStatus", "0");
        }
        
        //查询当前用户未融资的应收账
        List<ScfReceivable> receivaList = this.selectByProperty(anMap);
        List<ScfReceivable> retList = new ArrayList<>();
        
        //过滤掉 不能融资的应收账（发票或者合同不全）
        for (ScfReceivable receivable : receivaList) {
            if(false == orderService.checkInfoCompleted(receivable.getId().toString(), RequestType.RECEIVABLE.getCode())){
                continue;
            }
            retList.add(receivable);
        }
        return retList;
    }
    
    public ScfReceivable findReceivableDetailsById(Long anId) {
        Map<String, Object> queryMap = QueryTermBuilder.newInstance().put("id", anId).build();
        List<ScfReceivable> receivableList = this.findReceivable(queryMap);
        return Collections3.getFirst(receivableList);
    }
    
    /**
     * 查询应收账款信息,包含所有下属信息
     */
    public List<ScfReceivable> findReceivable(Map<String, Object> anMap) {
        List<ScfReceivable> anReceivbaleList = this.selectByClassProperty(ScfReceivable.class, anMap);
        //下属信息
        for (ScfReceivable anReceivbale : anReceivbaleList) {
            Map<String, Object> receivbaleIdMap = QueryTermBuilder.newInstance().put("infoId", anReceivbale.getId())
                    .put("infoType", ScfOrderRelationType.RECEIVABLE.getCode()).build();
            List<ScfOrderRelation> orderRelationList = orderRelationService.findOrderRelation(receivbaleIdMap);
            fillReceivableInfo(anReceivbale, orderRelationList);
        }
        return anReceivbaleList;
    }
    
    /**
     * 根据订单关联关系补全汇票信息
     */
    public void fillReceivableInfo(ScfReceivable anReceivable, List<ScfOrderRelation> anOrderRelationList) {
        anReceivable.setAgreementList(new ArrayList<CustAgreement>());
        anReceivable.setOrderList(new ArrayList<ScfOrder>());
        anReceivable.setTransportList(new ArrayList<ScfTransport>());
        anReceivable.setInvoiceList(new ArrayList<ScfInvoice>());
        anReceivable.setAcceptBillList(new ArrayList<ScfAcceptBill>());
        for(ScfOrderRelation anOrderRelation : anOrderRelationList) {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("id", anOrderRelation.getOrderId());
            //由于是id查出，取出数据
            ScfOrder anOrder = Collections3.getFirst(orderService.findOrder(queryMap));
            anReceivable.getInvoiceList().addAll(anOrder.getInvoiceList());
            anReceivable.getTransportList().addAll(anOrder.getTransportList());
            anReceivable.getAcceptBillList().addAll(anOrder.getAcceptBillList());
            anReceivable.getAgreementList().addAll(anOrder.getAgreementList());
            //清除order下面的信息
            anOrder.clearRelationInfo();
            //若数据来源不为自动生成，则加入
            if(!"0".equals(anOrder.getDataSource())){
                anReceivable.getOrderList().add(anOrder);
            }
        }
    }
    
    
    /**
     * 检查是否存在相应id、操作机构、业务状态的应收账款
     * @param anId  应收账款id
     * @param anOperOrg 操作机构
     */
    public void checkInfoExist(Long anId, String anOperOrg) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        //可编辑的业务状态
        String[] anBusinStatusList = { "0" };
        anMap.put("id", anId);
        //anMap.put("operOrg", anOperOrg);
        anMap.put("businStatus", anBusinStatusList);
        List<ScfReceivable> receivableList = this.selectByClassProperty(ScfReceivable.class, anMap);
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
    
    /**
     * 应收账款新增
     */
    public ScfReceivable addReceivable(ScfReceivable anReceivable, String anFileList, String anOtherFileList) {
        anReceivable.initAddValue(UserUtils.getOperatorInfo());
        //操作机构设置为供应商
        anReceivable.setOperOrg(baseService.findBaseInfo(anReceivable.getCustNo()).getOperOrg());
        //保存附件信息
        anReceivable.setBatchNo(custFileDubboService.updateCustFileItemInfo(anOtherFileList, anReceivable.getBatchNo()));
        //保存其他文件信息
        anReceivable.setOtherBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anReceivable.getOtherBatchNo()));
        this.insert(anReceivable);
        return anReceivable;
    }
    
    /**
     * 审核应收信息
     */
    public ScfReceivable saveAduitReceivable(Long anId) {
        ScfReceivable anReceivable = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anReceivable, "无法获得汇票信息");
        BTAssert.isTrue(anReceivable.getAduit().equals("0"), "所选汇票已审核");
        anReceivable.setAduit("1");
        this.updateByPrimaryKey(anReceivable);
        return anReceivable;
    }
}
