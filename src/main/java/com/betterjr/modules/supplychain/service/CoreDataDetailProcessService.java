package com.betterjr.modules.supplychain.service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.supplychain.dao.CoreDataProcessDetailMapper;
import com.betterjr.modules.supplychain.data.ScfClientDataDetail;
import com.betterjr.modules.supplychain.data.ScfClientDataProcess;
import com.betterjr.modules.supplychain.entity.CoreDataProcessDetail;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CoreDataDetailProcessService extends BaseService<CoreDataProcessDetailMapper, CoreDataProcessDetail> {

    /**
     * 根据业务处理，查询已经处理过的业务数据，主要用于二次数据过虑
     * 
     * @param anOpenOrg
     * @param anDataProcess
     * @return
     */
    public List<CoreDataProcessDetail> findProcessDetailByProc(String anOperOrg, ScfClientDataProcess anDataProcess) {
        Map<String, Object> termMap = new HashMap();
        termMap.put("operOrg", anOperOrg);
        termMap.put("processType", anDataProcess.getWorkType());
        termMap.put("workDate", anDataProcess.getWorkDate());
        termMap.put("workStatus", "1");

        return this.selectByProperty(termMap);
    }

    /**
     * 保存处理日志信息，如果不存在就增加存在就修改处理状态
     * 
     * @param anOpenOrg
     *            核心企业信息
     * @param anDataProcess
     *            处理过程
     * @param anDetail
     *            处理明细
     */
    public void saveProcessDetail(String anOpenOrg, String anWorkType, ScfClientDataDetail anDetail) {
        Map<String, Object> termMap = new HashMap();
        termMap.put("operOrg", anOpenOrg);
        termMap.put("processType", anWorkType);
        termMap.put("workDate", anDetail.getWorkDate());
        termMap.put("btNo", anDetail.getBtCustId());
        CoreDataProcessDetail data = null;
        for (CoreDataProcessDetail tmpData : this.selectByProperty(termMap)) {
            if (StringUtils.isNotBlank(anDetail.getBankAcc())) {
                if (anDetail.getBankAcc().equalsIgnoreCase(tmpData.getBankAccount())) {
                    data = tmpData;
                    break;
                }
            } else {
                data = tmpData;
                break;
            }
        }
        if (data == null) {
            data = new CoreDataProcessDetail(anOpenOrg, anWorkType, anDetail);
            this.insert(data);
        } else {
            data.putWorkStatus(anDetail);
            this.updateByPrimaryKey(data);
        }
    }
}
