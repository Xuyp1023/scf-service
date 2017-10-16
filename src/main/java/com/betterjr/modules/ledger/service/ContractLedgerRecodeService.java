package com.betterjr.modules.ledger.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.ledger.dao.ContractLedgerRecodeMapper;
import com.betterjr.modules.ledger.entity.ContractLedgerRecode;

/***
 * 合同台账记录服务类
 * @author hubl
 *
 */
@Service
public class ContractLedgerRecodeService extends BaseService<ContractLedgerRecodeMapper, ContractLedgerRecode> {

    private static final Logger logger = LoggerFactory.getLogger(ContractLedgerRecodeService.class);

    /***
     * 添加合同台账记录
     * @param anContractId
     */
    public void addContractLedgerRecode(Long anContractId, String anBusinStatus) {
        logger.info("添加合同台账记录参数：" + anContractId);
        ContractLedgerRecode contractLedgerRecode = new ContractLedgerRecode();
        contractLedgerRecode.initValue(anContractId, anBusinStatus);
        this.insert(contractLedgerRecode);
    }

    /***
     * 查询记录
     * @param anContractId
     * @return
     */
    public Map<String, Object> findContractLedgerRecode(Long anContractId) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("contractId", anContractId);
        anMap.put("businStatus", "0");
        List<ContractLedgerRecode> addList = this.selectByProperty(anMap, " D_OPER_DATE"); // 查询登记记录
        ContractLedgerRecode addRecode = Collections3.getFirst(addList);
        List<ContractLedgerRecode> aduitList = this.selectByProperty(anMap, " D_OPER_DATE"); // 查询审核记录
        ContractLedgerRecode aduitRecode = Collections3.getFirst(aduitList);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (addRecode != null) {
            resultMap.put("addOperName", addRecode.getOperName());
            resultMap.put("addOperDate", BetterDateUtils.formatDispDate(addRecode.getOperDate()));
        }
        if (aduitRecode != null) {
            resultMap.put("aduitOperName", aduitRecode.getOperName());
            resultMap.put("aduitOperDate", BetterDateUtils.formatDispDate(aduitRecode.getOperDate()));
        }
        return resultMap;
    }
}
