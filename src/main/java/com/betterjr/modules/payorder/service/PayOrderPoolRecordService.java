package com.betterjr.modules.payorder.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.payorder.dao.PayOrderPoolRecordMapper;
import com.betterjr.modules.payorder.data.PayOrderPoolConstantCollentions;
import com.betterjr.modules.payorder.data.PayOrderPoolRecordConstantCollentions;
import com.betterjr.modules.payorder.entity.PayOrderPoolRecord;
import com.betterjr.modules.supplieroffer.entity.ScfReceivableRequest;

/**
 * 
 * @ClassName: PayOrderPoolRecordService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xuyp
 * @date 2017年10月20日 下午3:41:01 
 *
 */
@Service
public class PayOrderPoolRecordService extends BaseService<PayOrderPoolRecordMapper, PayOrderPoolRecord> {

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;

    /**
     * 根据申请新增单独的每条记录信息
     * @param anRequest
     * @param anPoolId
     * @return
     */
    public PayOrderPoolRecord saveAddPayRecordByRequest(ScfReceivableRequest anRequest, Long anPoolId) {

        BTAssert.notNull(anRequest, "融资信息为空,操作失败");
        PayOrderPoolRecord record = new PayOrderPoolRecord()
                .saveAddInitValue(PayOrderPoolRecordConstantCollentions.PAY_RECORD_INFO_TYPE_REQUEST);

        record.setBalance(anRequest.getRequestPayBalance());
        record.setCustBankAccount(anRequest.getCustBankAccount());
        record.setCustBankAccountName(anRequest.getCustBankAccountName());
        record.setCustBankName(anRequest.getCustBankName());
        record.setDescription("");
        record.setRequestNo(anRequest.getRequestNo());
        record.setPoolId(anPoolId);
        record.setRequestPayDate(anRequest.getRequestPayDate());
        record.setFactoryNo(anRequest.getFactoryNo());
        record.setFactoryName(anRequest.getFactoryName());

        this.insertSelective(record);
        return record;
    }

    /**
     * 查询记录列表
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     * requestPayDate
     * businStatus
     * factoryNo
     */
    public Page<PayOrderPoolRecord> queryRecordPage(Map<String, Object> anMap, String anFlag, int anPageNum,
            int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");

        anMap = Collections3.filterMap(anMap, new String[] { "requestPayDate", "businStatus", "factoryNo" });
        anMap = Collections3.filterMapEmptyObject(anMap);
        if (!anMap.containsKey(PayOrderPoolRecordConstantCollentions.MAP_CONTAIN_PROPERTIES_FACTORY_NO)) {
            anMap.put("factoryNo", queryCurrentUserCustNos());
        }

        if (!anMap.containsKey(PayOrderPoolRecordConstantCollentions.MAP_CONTAIN_PROPERTIES_BUSIN_STATUS)) {
            anMap.put("businStatus",
                    new String[] { PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_NOHANDLE,
                            PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_AUDITING,
                            PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYING,
                            PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYFAILURE,
                            PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYSUCCESS });
        }

        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id Desc");
    }

    /**
     * 根据付款时间，企业，状态查询记录信息
     * @param anMap
     * @return
     */
    public List<PayOrderPoolRecord> queryRecordList(Map<String, Object> anMap) {

        BTAssert.notNull(anMap, "查询条件为空,操作失败");

        anMap = Collections3.filterMap(anMap, new String[] { "requestPayDate", "businStatus", "factoryNo" });
        anMap = Collections3.filterMapEmptyObject(anMap);
        if (!anMap.containsKey(PayOrderPoolRecordConstantCollentions.MAP_CONTAIN_PROPERTIES_FACTORY_NO)) {
            anMap.put("factoryNo", queryCurrentUserCustNos());
        }

        return this.selectByProperty(anMap);
    }

    /**
     * 根据对应的文件id查询所有的付款进入信息
     * @Title: queryRecordListByFileId 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anSourceFileId
     * @param @param anPayRecordBusinStatusPaying
     * @param @return 参数说明 
     * @return List<PayOrderPoolRecord> 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月17日 下午5:08:07
     */
    public List<PayOrderPoolRecord> queryRecordListByFileId(Long anSourceFileId, String anPayRecordBusinStatus) {

        Map map = QueryTermBuilder.newInstance().put("payFileId", anSourceFileId)
                .put("businStatus", anPayRecordBusinStatus).build();
        if (StringUtils.isNoneBlank(anPayRecordBusinStatus)
                && anPayRecordBusinStatus.contains(PayOrderPoolRecordConstantCollentions.MARK_SEPARATOR)) {

            map.put("businStatus", anPayRecordBusinStatus.split(","));

        }

        if (StringUtils.isBlank(anPayRecordBusinStatus)) {

            map.put("businStatus",
                    new String[] { PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_NOHANDLE,
                            PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_AUDITING,
                            PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYING,
                            PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYFAILURE,
                            PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYSUCCESS });

        }

        return this.selectByProperty(map, "id Desc");
    }

    /**
     * 查询所有生效的付款记录信息
     * @Title: queryRecordListByFileId 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anId
     * @param @return 参数说明 
     * @return List<PayOrderPoolRecord> 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月18日 上午10:22:40
     */
    public List<PayOrderPoolRecord> queryRecordListByFileId(Long anId) {

        StringBuilder sb = new StringBuilder()
                .append(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_NOHANDLE).append(",")
                .append(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYING).append(",")
                .append(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_AUDITING).append(",")
                .append(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYFAILURE).append(",")
                .append(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYSUCCESS);

        return queryRecordListByFileId(anId, sb.toString());

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

}
