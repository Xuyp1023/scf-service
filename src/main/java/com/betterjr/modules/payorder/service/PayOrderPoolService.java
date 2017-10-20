package com.betterjr.modules.payorder.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.payorder.dao.PayOrderPoolMapper;
import com.betterjr.modules.payorder.data.PayOrderPoolConstantCollentions;
import com.betterjr.modules.payorder.data.PayOrderPoolRecordConstantCollentions;
import com.betterjr.modules.payorder.entity.PayOrderPool;
import com.betterjr.modules.payorder.entity.PayOrderPoolRecord;
import com.betterjr.modules.supplieroffer.data.ReceivableRequestConstantCollentions;
import com.betterjr.modules.supplieroffer.entity.ScfReceivableRequest;
import com.betterjr.modules.supplieroffer.service.ScfReceivableRequestService;

/**
 * 
 * @ClassName: PayOrderPoolService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xuyp
 * @date 2017年10月20日 下午3:54:11 
 *
 */
@Service
public class PayOrderPoolService extends BaseService<PayOrderPoolMapper, PayOrderPool> {

    @Autowired
    private ScfReceivableRequestService requestService;

    @Autowired
    private PayOrderPoolRecordService recordService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;

    /**
     * 融资申请完成时新增申请记录信息
     * @param anRequestNo
     * @return
     */
    public PayOrderPool saveAddPayRecord(String anRequestNo) {

        logger.info("begin to add saveAddPayRecord " + UserUtils.getOperatorInfo().getName() + " anRequestNo="
                + anRequestNo);
        BTAssert.hasLength(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = findFinishRequestByPrimaryKey(anRequestNo);

        PayOrderPool pool = saveAddOrFindPoolByRequest(request);

        PayOrderPoolRecord record = recordService.saveAddPayRecordByRequest(request, pool.getId());

        logger.info("end to add saveAddPayRecord " + UserUtils.getOperatorInfo().getName() + "  返回值: pool=" + pool);
        return pool;

    }

    /**
     * 企业查询付款池列表
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     * GTErequestPayDate
     * LTErequestPayDate
     * factoryNo
     */
    public Page<PayOrderPool> queryPayPoolList(Map<String, Object> anMap, String anFlag, int anPageNum,
            int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");

        anMap = Collections3.filterMap(anMap, new String[] { "GTErequestPayDate", "LTErequestPayDate", "factoryNo" });
        anMap = Collections3.filterMapEmptyObject(anMap);
        if (!anMap.containsKey(PayOrderPoolRecordConstantCollentions.MAP_CONTAIN_PROPERTIES_FACTORY_NO)) {
            anMap.put("factoryNo", queryCurrentUserCustNos());
        }
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());

        Page<PayOrderPool> poolList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag),
                "id Desc");

        return poolList;

    }

    /**
     * 更新付款数量的变化
     * @Title: saveUpdateAmount 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anId
     * @param @param anAmountLength
     * @param @param anType 
     *                      1: 从未付款到付款中
     *                      2： 付款中到复核中
     *                      3： 复核中失败
     *                      4：复核中成功
     *                      5: 从复核中到付款中
     * @param @return 参数说明 
     * @return PayOrderPool 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月17日 下午1:51:44
     */
    public PayOrderPool saveUpdateAmount(Long anId, Long anAmountLength, String anType) {

        BTAssert.notNull(anId, "付款池为空,操作失败");
        PayOrderPool pool = this.selectByPrimaryKey(anId);
        BTAssert.notNull(pool, "付款池为空,操作失败");

        if (PayOrderPoolConstantCollentions.PAYPOOL_SAVEUPDATEAMOUNT_TYPE_NOPAYTOPAYING.equals(anType)) {
            Long noPayAmount = pool.getNoPayAmount();
            Long payingAmount = pool.getPayingAmount();
            if (noPayAmount.longValue() < anAmountLength.longValue()) {
                BTAssert.notNull(null, "付款池未付款条数不足" + anAmountLength + ",操作失败");
            }
            pool.setNoPayAmount(noPayAmount - anAmountLength);
            pool.setPayingAmount(payingAmount + anAmountLength);
        }

        if (PayOrderPoolConstantCollentions.PAYPOOL_SAVEUPDATEAMOUNT_TYPE_PAYINGTOAUDIT.equals(anType)) {

            if (pool.getPayingAmount().longValue() < anAmountLength.longValue()) {
                BTAssert.notNull(null, "付款池付款中条数不足" + anAmountLength + ",操作失败");
            }
            pool.setPayingAmount(pool.getPayingAmount() - anAmountLength);
            pool.setAuditAmount(pool.getAuditAmount() + anAmountLength);
        }

        if (PayOrderPoolConstantCollentions.PAYPOOL_SAVEUPDATEAMOUNT_TYPE_AUDITTOPAYFUILURE.equals(anType)) {

            if (pool.getAuditAmount().longValue() < anAmountLength.longValue()) {
                BTAssert.notNull(null, "付款池复核中条数不足" + anAmountLength + ",操作失败");
            }
            pool.setAuditAmount(pool.getAuditAmount() - anAmountLength);
            pool.setPayFailureAmount(pool.getPayFailureAmount() + anAmountLength);
        }

        if (PayOrderPoolConstantCollentions.PAYPOOL_SAVEUPDATEAMOUNT_TYPE_AUDITTOPAYSUCCESS.equals(anType)) {

            if (pool.getAuditAmount().longValue() < anAmountLength.longValue()) {
                BTAssert.notNull(null, "付款池复核中条数不足" + anAmountLength + ",操作失败");
            }
            pool.setAuditAmount(pool.getAuditAmount() - anAmountLength);
            pool.setPaySuccessAmount(pool.getPaySuccessAmount() + anAmountLength);
        }

        if (PayOrderPoolConstantCollentions.PAYPOOL_SAVEUPDATEAMOUNT_TYPE_AUDITTOPAYING.equals(anType)) {

            if (pool.getAuditAmount().longValue() < anAmountLength.longValue()) {
                BTAssert.notNull(null, "付款池复核中条数不足" + anAmountLength + ",操作失败");
            }
            pool.setAuditAmount(pool.getAuditAmount() - anAmountLength);
            pool.setPayingAmount(pool.getPayingAmount() + anAmountLength);
        }

        this.updateByPrimaryKeySelective(pool);

        return pool;

    }

    /**
     * 获取当前登录用户所在的所有公司id集合
     * 
     * @return
     */
    private List<Long> queryCurrentUserCustNos() {

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
     * 新增申请信息
     * @param anRequest
     * @return
     */
    private PayOrderPool saveAddOrFindPoolByRequest(ScfReceivableRequest anRequest) {

        BTAssert.notNull(anRequest, "融资信息为空,操作失败");

        Map map = QueryTermBuilder.newInstance().put("requestPayDate", anRequest.getRequestPayDate())
                .put("factoryNo", anRequest.getFactoryNo()).build();

        List<PayOrderPool> poolList = this.selectByProperty(map, "id Desc");

        if (Collections3.isEmpty(poolList)) {

            return saveAddPoolByRequest(anRequest);

        } else {

            PayOrderPool pool = Collections3.getFirst(poolList);
            pool.setPayAmount(incrWithLength(pool.getPayAmount(), 1));
            pool.setBalance(MathExtend.add(pool.getBalance(), anRequest.getRequestPayBalance()));
            pool.setNoPayAmount(incrWithLength(pool.getNoPayAmount(), 1));
            this.updateByPrimaryKeySelective(pool);
            return pool;
        }

    }

    /**
     * 通过融资申请新增付款指令池记录信息
     * @param anRequest
     * @return
     */
    private PayOrderPool saveAddPoolByRequest(ScfReceivableRequest anRequest) {

        BTAssert.notNull(anRequest, "融资信息为空,操作失败");

        PayOrderPool pool = new PayOrderPool().saveAddInitValue();
        pool.setBalance(anRequest.getRequestPayBalance());
        pool.setFactoryName(anRequest.getFactoryName());
        pool.setFactoryNo(anRequest.getFactoryNo());
        pool.setRequestPayDate(anRequest.getRequestPayDate());
        pool.setOperOrg(baseService.findBaseInfo(pool.getFactoryNo()).getOperOrg());

        this.insertSelective(pool);

        return pool;
    }

    /**
     * 自增指定步长
     * @param anPayAmount
     * @param anI
     * @return
     */
    private Long incrWithLength(Long anPayAmount, int anLength) {

        if (anPayAmount == null) {
            anPayAmount = 0L;
        }

        return anPayAmount + anLength;
    }

    /**
     * 查询已经完结且未付款的申请单据的申请信息
     * @param anRequestNo
     * @return
     */
    private ScfReceivableRequest findFinishRequestByPrimaryKey(String anRequestNo) {

        BTAssert.hasLength(anRequestNo, "融资信息为空,操作失败");
        ScfReceivableRequest request = requestService.selectByPrimaryKey(anRequestNo);
        BTAssert.notNull(request, "融资信息为空,操作失败");
        BTAssert.isTrue(ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_BUSIN_STATUS_FINISHED
                .equals(request.getBusinStatus()), "融资信息不符合条件,操作失败");
        BTAssert.isTrue(
                ReceivableRequestConstantCollentions.RECEIVABLE_REQUEST_PAY_STATUS_INIT.equals(request.getPayStatus()),
                "当前申请已经进行付款操作");

        return request;
    }

}
