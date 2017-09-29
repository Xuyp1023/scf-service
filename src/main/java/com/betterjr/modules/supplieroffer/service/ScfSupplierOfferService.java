package com.betterjr.modules.supplieroffer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.supplieroffer.dao.ScfSupplierOfferMapper;
import com.betterjr.modules.supplieroffer.data.OfferConstantCollentions;
import com.betterjr.modules.supplieroffer.entity.ScfSupplierOffer;

@Service
public class ScfSupplierOfferService extends BaseService<ScfSupplierOfferMapper, ScfSupplierOffer> {

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;

    @Autowired
    private CustAccountService custAccountService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;

    /**
     * 新增供应商利率
     * 
     * @param anOffer
     * @return
     */
    public ScfSupplierOffer saveAddSupplierOffer(ScfSupplierOffer anOffer) {

        BTAssert.notNull(anOffer, "插入供应商报价为空,操作失败");
        BTAssert.notNull(anOffer.getCustNo(), "请选择供应商,操作失败");
        BTAssert.notNull(anOffer.getCoreCustNo(), "请选择核心企业,操作失败");
        BTAssert.notNull(anOffer.getCoreCustRate(), "请填写利率,操作失败");
        BTAssert.notNull(UserUtils.getOperatorInfo(), "请先登录,操作失败");
        logger.info("Begin to add saveAddSupplierOffer" + UserUtils.getOperatorInfo().getName() + " anOffer=" + anOffer);
        ScfSupplierOffer supplierOffer = checkIsOrNotExit(anOffer.getCustNo(), anOffer.getCoreCustNo(),
                OfferConstantCollentions.OFFER_BUSIN_STATUS_EFFECTIVE);
        BTAssert.isNull(supplierOffer, "当前企业已经设置了利率，请修改!");
        anOffer.saveAddValue(UserUtils.getOperatorInfo());
        anOffer.setCustName(custAccountService.queryCustName(anOffer.getCustNo()));
        anOffer.setCoreCustName(custAccountService.queryCustName(anOffer.getCoreCustNo()));
        anOffer.setOperOrg(baseService.findBaseInfo(anOffer.getCoreCustNo()).getOperOrg());
        this.insert(anOffer);
        logger.info("end to add saveAddSupplierOffer" + UserUtils.getOperatorInfo().getName());
        return anOffer;

    }

    /**
     * 修改供应商利率信息
     * 
     * @param anCustNo
     * @param anCoreCustNo
     * @param anCoreCustRate
     *            利率
     * @return
     */
    public ScfSupplierOffer saveUpdateSupplierOffer(Long anCustNo, Long anCoreCustNo, double anCoreCustRate) {

        BTAssert.notNull(anCustNo, "请选择供应商,操作失败");
        BTAssert.notNull(anCoreCustNo, "请选择核心企业,操作失败");
        BTAssert.notNull(anCoreCustRate, "请填写利率,操作失败");
        BTAssert.notNull(UserUtils.getOperatorInfo(), "请先登录,操作失败");
        logger.info("Begin to  saveUpdateSupplierOffer" + UserUtils.getOperatorInfo().getName());
        ScfSupplierOffer supplierOffer = checkIsOrNotExit(anCustNo, anCoreCustNo, OfferConstantCollentions.OFFER_BUSIN_STATUS_EFFECTIVE);
        BTAssert.notNull(supplierOffer, "还没有新增当前供应商的利率,操作失败");
        checkStatus(supplierOffer.getOperOrg(), UserUtils.getOperatorInfo().getOperOrg(), false, "你没有当前企业的操作权限！操作失败");
        supplierOffer.setBusinStatus(OfferConstantCollentions.OFFER_BUSIN_STATUS_NOEFFECTIVE);
        this.updateByPrimaryKey(supplierOffer);
        supplierOffer.saveAddValue(UserUtils.getOperatorInfo());
        supplierOffer.setCoreCustRate(new BigDecimal(anCoreCustRate));
        this.insert(supplierOffer);
        logger.info("end to  saveUpdateSupplierOffer" + UserUtils.getOperatorInfo().getName());
        return supplierOffer;
    }

    /**
     * 根据核心企业和供应商查询利率
     * 
     * @param anCustNo
     * @param anCoreCustNo
     * @return
     */
    public ScfSupplierOffer findOffer(Long anCustNo, Long anCoreCustNo) {

        BTAssert.notNull(anCustNo, "请选择供应商,查询失败");
        BTAssert.notNull(anCoreCustNo, "请选择核心企业,查询失败");
        BTAssert.notNull(UserUtils.getOperatorInfo(), "请先登录,查询失败");
        logger.info("Begin to  findOffer" + UserUtils.getOperatorInfo().getName());
        ScfSupplierOffer supplierOffer = checkIsOrNotExit(anCustNo, anCoreCustNo, OfferConstantCollentions.OFFER_BUSIN_STATUS_EFFECTIVE);
        BTAssert.notNull(supplierOffer, "还没有新增当前供应商的利率,查询失败");
        /* checkStatus(supplierOffer.getOperOrg(), UserUtils.getOperatorInfo().getOperOrg(), false, "你没有当前企业的操作权限！操作失败"); */
        logger.info("end to  findOffer" + UserUtils.getOperatorInfo().getName());
        return supplierOffer;
    }

    /**
     * 查询当前企业的所有已经设置了的供应商列表
     * 
     * @param anCoreCustNo
     * @return
     */
    public List<Long> queryAllCust(Long anCoreCustNo) {

        BTAssert.notNull(anCoreCustNo, "请选择核心企业,查询失败");
        BTAssert.notNull(UserUtils.getOperatorInfo(), "请先登录,查询失败");

        return this.mapper.countAllPayResultRecord(anCoreCustNo);
    }

    /**
     * 获取当前登录用户所在的所有公司id集合
     * 
     * @return
     */
    private List<Long> getCurrentUserCustNos() {

        CustOperatorInfo operInfo = UserUtils.getOperatorInfo();
        BTAssert.notNull(operInfo, "查询可用资产失败!请先登录");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        BTAssert.notNull(custInfos, "查询可用资产失败!获取当前企业失败");
        List<Long> custNos = new ArrayList<>();
        for (CustInfo custInfo : custInfos) {
            custNos.add(custInfo.getId());
        }
        return custNos;
    }

    /**
     * 查询供应商利率
     * 
     * @param anMap
     * @param anFlag
     *            是否查询总条数
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfSupplierOffer> queryScfSupplierOfferPage(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");
        BTAssert.notNull(anMap.get("coreCustNo"), "查询条件为空,操作失败");
        anMap = Collections3.filterMapEmptyObject(anMap);
        logger.info("Begin to  queryScfSupplierOfferPage" + UserUtils.getOperatorInfo().getName());
        anMap = Collections3.filterMap(anMap, new String[] { "custNo", "coreCustNo" });
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        anMap.put("businStatus", OfferConstantCollentions.OFFER_BUSIN_STATUS_EFFECTIVE);
        Page<ScfSupplierOffer> offerList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
        logger.info("end to  queryScfSupplierOfferPage" + UserUtils.getOperatorInfo().getName());
        return offerList;

    }

    /**
     * 检查状态信息
     */
    public void checkStatus(String anBusinStatus, String anTargetStatus, boolean anFlag, String anMessage) {
        if (BetterStringUtils.equals(anBusinStatus, anTargetStatus) == anFlag) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

    /**
     * 判断是否已经存在当前企业的利率关系如果存在返回对象， 如果不存在 返回null
     * 
     * @param anCustNo
     * @param anCoreCustNo
     * @param anStatus
     * @return
     */
    public ScfSupplierOffer checkIsOrNotExit(Long anCustNo, Long anCoreCustNo, String anStatus) {

        Map<String, Object> paramMap = QueryTermBuilder.newInstance().put("custNo", anCustNo).put("coreCustNo", anCoreCustNo)
                .put("businStatus", anStatus).put("operOrg", baseService.findBaseInfo(anCoreCustNo).getOperOrg()).build();

        List<ScfSupplierOffer> selectByProperty = this.selectByProperty(paramMap);
        if (!Collections3.isEmpty(selectByProperty)) {

            return Collections3.getFirst(selectByProperty);
        }
        else {

            return null;
        }

    }

    /**
     * 
     * @param anCustNo
     * @return
     */
    public List<ScfSupplierOffer> queryAllFactoryByCustNo(Long anCustNo) {

        List<ScfSupplierOffer> list = new ArrayList<>();
        list = this.mapper.queryAllFactoryByCustNo(anCustNo);
        return list;

    }

}
