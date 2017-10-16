package com.betterjr.modules.order.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.order.dao.ScfInvoiceMapper;
import com.betterjr.modules.order.data.ScfInvoiceAndAccess;
import com.betterjr.modules.order.entity.ScfInvoice;
import com.betterjr.modules.order.entity.ScfInvoiceItem;
import com.betterjr.modules.order.entity.ScfOrder;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;

@Service
public class ScfInvoiceService extends BaseService<ScfInvoiceMapper, ScfInvoice> implements IScfOrderInfoCheckService {

    @Autowired
    private ScfInvoiceItemService invoiceItemService;
    @Autowired
    private ScfOrderService orderService;
    @Autowired
    private CustAccountService custAccountService;

    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;

    /**
     * 订单发票信息录入
     */
    public ScfInvoice addInvoice(ScfInvoice anInvoice, String anInvoiceIds, String anFileList) {
        logger.info("Begin to add Invoice");
        if (StringUtils.isBlank(anFileList)) {
            logger.error("发票附件信息不能为空");
            throw new BytterTradeException(40001, "发票附件信息不能为空");
        }
        anInvoice.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anInvoice.getBatchNo()));
        anInvoice.initAddValue();
        // 发票初始状态为正常
        anInvoice.setBusinStatus("1");
        if (!StringUtils.isBlank(anInvoiceIds)) {
            invoiceItemService.saveInvoiceItemRelation(anInvoice.getId(), anInvoiceIds.split(","));
        }
        this.insert(anInvoice);
        return anInvoice;
    }

    /**
     * 订单发票信息查询
     */
    public Page<ScfInvoice> queryInvoice(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        // 限定操作员查询本机构数据
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        if (!(UserUtils.factorUser() || UserUtils.platformUser())) {
            // 如果不为平台或者保理公司仅显示可用发票
            anMap.put("businStatus", "1");
        }
        Page<ScfInvoice> anInvoiceList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
        for (ScfInvoice anInvoice : anInvoiceList) {
            anInvoice.setCustName(custAccountService.queryCustName(anInvoice.getCustNo()));
        }
        fillInvoiceItem(anInvoiceList);

        return anInvoiceList;
    }

    /**
     * 发票信息编辑修改
     */
    public ScfInvoice saveModifyInvoice(ScfInvoice anModiInvoice, String anFileList, String anInvoiceItemIds) {
        // 保存发票信息
        ScfInvoice anInvoice = this.selectByPrimaryKey(anModiInvoice.getId());
        BTAssert.notNull(anInvoice, "无法获取发票信息");
        if (StringUtils.isBlank(anFileList)) {
            logger.error("发票附件信息不能为空");
            throw new BytterTradeException(40001, "发票附件信息不能为空");
        }
        anModiInvoice.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anFileList, anInvoice.getBatchNo()));
        anModiInvoice.setId(anInvoice.getId());
        anModiInvoice.initModifyValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(anModiInvoice);

        // 先查询出原有的发票详情
        List<ScfInvoiceItem> oldInvoiceItemList = invoiceItemService.findItemsByInvoiceId(anModiInvoice.getId());
        // 保存现有的发票详情
        if (!StringUtils.isBlank(anInvoiceItemIds)) {
            String[] invoiceIds = anInvoiceItemIds.split(",");
            invoiceItemService.saveInvoiceItemRelation(anModiInvoice.getId(), invoiceIds);
            // 删除取消的发票详情
            List<String> newInvoiceItemIdList = Arrays.asList(invoiceIds);
            for (ScfInvoiceItem anInvoiceItem : oldInvoiceItemList) {
                if (!newInvoiceItemIdList.contains(anInvoiceItem.getId().toString())) {
                    invoiceItemService.saveDeleteInvoiceItem(anInvoiceItem.getId());
                }
            }
        }
        return anInvoice;
    }

    /**
     * 变更发票状态
     * 发票状态，0失效，1正常，2过期，3冻结
     */
    private ScfInvoice saveInvoiceStatus(Long anId, String anStatus) {
        ScfInvoice anInvoice = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anInvoice, "无法获取发票信息");
        // 检查用户权限
        // checkOperator(anInvoice.getOperOrg(), "当前操作员无权限变更发票状态");
        // 变更相关状态
        anInvoice.setBusinStatus(anStatus);
        anInvoice.setModiOperId(UserUtils.getOperatorInfo().getId());
        anInvoice.setModiOperName(UserUtils.getOperatorInfo().getName());
        anInvoice.setModiDate(BetterDateUtils.getNumDate());
        anInvoice.setModiTime(BetterDateUtils.getNumTime());
        // 数据存盘
        this.updateByPrimaryKeySelective(anInvoice);
        return anInvoice;
    }

    /**
     * 检查是否存在相应id、操作机构、业务状态的订单发票
     * @param anId  发票id
     * @param anOperOrg 操作机构
     */
    @Override
    public void checkInfoExist(Long anId, String anOperOrg) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        String[] anBusinStatusList = { "1" };
        anMap.put("id", anId);
        if (!(UserUtils.factorUser() || UserUtils.platformUser())) {
            anMap.put("businStatus", anBusinStatusList);
        }
        List<ScfInvoice> invoiceList = this.selectByClassProperty(ScfInvoice.class, anMap);
        if (Collections3.isEmpty(invoiceList)) {
            logger.warn("不存在相对应id,操作机构,业务状态的发票");
            throw new BytterTradeException(40001, "不存在相对应id,操作机构,业务状态的发票");
        }
    }

    /**
     * 查询发票信息
     */
    public List<ScfInvoice> findInvoice(Map<String, Object> anMap) {

        List<ScfInvoice> invoiceList = this.selectByClassProperty(ScfInvoice.class, anMap);

        fillInvoiceItem(invoiceList);
        return invoiceList;
    }

    /**
     * 查询发票下的项目详情并填充
     */
    public void fillInvoiceItem(List<ScfInvoice> anInvoiceList) {
        for (ScfInvoice anInvoice : anInvoiceList) {
            List<ScfInvoiceItem> invoiceItemList = invoiceItemService.findItemsByInvoiceId(anInvoice.getId());
            anInvoice.setInvoiceItemList(invoiceItemList);
        }
    }

    /**
     * 变更发票状态-失效
     * 发票状态，0失效，1正常，2过期，3冻结
     */
    public ScfInvoice saveInvalidInvoice(Long anId) {
        return this.saveInvoiceStatus(anId, "0");
    }

    /**
     * 变更发票状态-正常
     * 发票状态，0失效，1正常，2过期，3冻结
     */
    public ScfInvoice saveNormalInvoice(Long anId) {
        return this.saveInvoiceStatus(anId, "1");
    }

    /**
     * 变更发票状态-过期
     * 发票状态，0失效，1正常，2过期，3冻结
     */
    public ScfInvoice saveExpireInvoice(Long anId) {
        return this.saveInvoiceStatus(anId, "2");
    }

    /**
     * 变更发票状态-冻结
     * 发票状态，0失效，1正常，2过期，3冻结
     */
    public ScfInvoice saveForzenInvoice(Long anId) {
        return this.saveInvoiceStatus(anId, "3");
    }

    /*
     * 检查用户是否有权限操作数据
     */
    private void checkOperator(String anOperOrg, String anMessage) {
        if (StringUtils.equals(UserUtils.getOperatorInfo().getOperOrg(), anOperOrg) == false) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

    /**
     * 根据票据ID查找票据信息
     * 
     * @param anBillNo
     * @return
     */
    public List<ScfInvoiceAndAccess> queryScfInvoiceByBillNo(Long anBillId, String anAgreeNo) {
        List<ScfInvoiceAndAccess> invoiceList = this.selectByClassProperty(ScfInvoiceAndAccess.class,
                makeCondition(anBillId));
        for (ScfInvoiceAndAccess invoice : invoiceList) {

            invoice.setAgreeNo(anAgreeNo);
        }

        return invoiceList;
    }

    private Map<String, Object> makeCondition(Long anBillId) {
        Map<String, Object> termMap = new HashMap();
        termMap.put("billId", anBillId);
        termMap.put("status", "1");
        return termMap;
    }

    /**
     * 删除发票 包括发票详情
     */
    public ScfInvoice saveDeleteInvoice(Long anId) {
        ScfInvoice anInvoice = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anInvoice, "无法获取发票信息");
        this.deleteByPrimaryKey(anId);
        invoiceItemService.deleteByProperty("invoiceId", anId);
        return anInvoice;
    }

    /**
     * 系统在融资申请默认生成发票,可关联关系后冻结，不然在检查是否存在时会报错
     */
    public ScfInvoice addSysInvoice(Long anCustNo, String anFileList) {
        ScfInvoice anInvoice = new ScfInvoice();
        anInvoice.initAddValue();
        anInvoice.setBusinStatus("1");
        anInvoice.setCustNo(anCustNo);
        anInvoice.setOperOrg(baseService.findBaseInfo(anCustNo).getOperOrg());
        anInvoice.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anInvoice.getBatchNo()));
        this.insert(anInvoice);
        return anInvoice;
    }

    /**
     * 查询出具保理方案下的发票,用于完善资料
     * 出具保理方案 - 110
     * 融资方确认方案 - 120
     * 发起融资背景确认 - 130
     * 核心企业确认背景 - 140
     * 放款确认 - 150
     * 完成融资 - 160
     * 放款完成 - 170
     */
    public Page<ScfInvoice> queryIncompletedInvoice(Map<String, Object> anMap, String anFlag, int anPageNum,
            int anPageSize) {
        List<ScfInvoice> invoiceList = new ArrayList<ScfInvoice>();
        // 增加融资状态条件
        anMap.put("GTEtradeStatus", "110");
        anMap.put("LTEtradeStatus", "110");
        List<ScfOrder> orderList = orderService.findOrderListByRequest(anMap);
        for (ScfOrder anOrder : orderList) {
            invoiceList.addAll(anOrder.getInvoiceList());
        }
        for (ScfInvoice anInvoice : invoiceList) {
            anInvoice.setCustName(custAccountService.queryCustName(anInvoice.getCustNo()));
        }
        return Page.listToPage(invoiceList);
    }
}
