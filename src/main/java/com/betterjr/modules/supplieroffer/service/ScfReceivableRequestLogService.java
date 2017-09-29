package com.betterjr.modules.supplieroffer.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.supplieroffer.dao.ScfReceivableRequestLogMapper;
import com.betterjr.modules.supplieroffer.entity.ScfReceivableRequest;
import com.betterjr.modules.supplieroffer.entity.ScfReceivableRequestLog;

@Service
public class ScfReceivableRequestLogService extends BaseService<ScfReceivableRequestLogMapper, ScfReceivableRequestLog> {

    
    /**
     * 新增融资日志
     * @param anRequest
     * @return
     */
    public ScfReceivableRequestLog saveAddRequestLog(ScfReceivableRequest anRequest) {

        ScfReceivableRequestLog log = new ScfReceivableRequestLog();
        if (anRequest != null && StringUtils.isNoneBlank(anRequest.getRequestNo())) {

            log.initAddValue(UserUtils.getOperatorInfo());

            fillDataByReceivableRequest(anRequest, log);

            this.insertSelective(log);
        }

        return log;

    }

    private void fillDataByReceivableRequest(ScfReceivableRequest anRequest, ScfReceivableRequestLog log) {
        
        if (UserUtils.supplierUser() || UserUtils.sellerUser()) {
            log.setCustName(anRequest.getCustName());
            log.setCustNo(anRequest.getCustNo());
        }else if (UserUtils.coreUser()) {
            log.setCustName(anRequest.getCoreCustName());
            log.setCustNo(anRequest.getCoreCustNo());
        }else if (UserUtils.factorUser()) {
            log.setCustName(anRequest.getFactoryName());
            log.setCustNo(anRequest.getFactoryNo());
        }else {
            CustInfo custInfo = UserUtils.getDefCustInfo();
            log.setCustName(custInfo.getCustName());
            log.setCustNo(custInfo.getCustNo());
        }

        log.setEquityNo(anRequest.getRequestNo());

        log.setModiMessage(anRequest.getBusinStatus());
    }
}
