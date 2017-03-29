package com.betterjr.modules.supplychain.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.client.supplychian.CustCoreCorp;
import com.betterjr.modules.client.supplychian.ScfCapitalFlowInfo;
import com.betterjr.modules.client.supplychian.SupplyAcceptBill;
import com.betterjr.modules.client.supplychian.SupplyBankInfo;
import com.betterjr.modules.client.supplychian.SupplyInfo;
import com.betterjr.modules.client.supplychian.SupplyPaymentInfo;
import com.betterjr.modules.supplychain.data.CoreCustInfo;
import com.betterjr.modules.supplychain.data.ScfClientDataDetail;
import com.betterjr.modules.supplychain.data.ScfClientDataProcess;
import com.betterjr.modules.supplychain.entity.CoreDataProcessDetail;
import com.betterjr.modules.supplychain.entity.CoreDataProcessInfo;
import com.betterjr.modules.supplychain.entity.CoreSupplierInfo;
import com.betterjr.modules.supplychain.entity.CustCoreCorpInfo;
import com.betterjr.modules.supplychain.entity.CustTempEnrollInfo;
import com.betterjr.modules.supplychain.entity.ScfBankPaymentFlow;
import com.betterjr.modules.supplychain.entity.ScfCapitalFlow;
import com.betterjr.modules.supplychain.entity.ScfSupplierBank;
import com.betterjr.modules.sys.entity.DictItemInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Multimap;

/**
 * 提供供应链金融客户数据调用服务，实现和客户端的通信
 *
 * @author zhoucy
 *
 */
@Service("scfClientService")
public class ScfClientService {
    private static Logger logger = LoggerFactory.getLogger(ScfClientService.class);
    protected static final String ATTACH_DATA = "attachData";
    private static final String FACTOR_CORE_CUSTINFO = "FactorCoreCustInfo";

    private static Boolean worked = Boolean.FALSE;
    private static Map<String, ClientDataConverter> coverterMap = initConverter();
    @Autowired
    private SupplyAccoRequestService supplyAccoService;

    @Autowired
    private ScfSupplierBankService supplierBankService;

    @Autowired
    private CoreDataProcessService dataProcessLogService;

    @Autowired
    private CoreDataDetailProcessService dataDetailProcessService;

    @Autowired
    private ScfFactorClientHelperService clientHelpService;

    /**
     * 查询未注册的账户信息
     *
     * @return
     */
    public List<ScfClientDataDetail> findUnCheckAccount(final Map anMap) {
        final List<ScfClientDataDetail> result = new ArrayList();
        final String operOrg = (String)anMap.get(ATTACH_DATA);
        final CoreCustInfo  coreCustInfo = findCoreCustInfoByOperOrg(operOrg);

        if (worked == Boolean.FALSE) {
            for (final CustTempEnrollInfo tmpEnroll : this.supplyAccoService.findUncheckedAccount(coreCustInfo.getCustNo())) {

                result.add(createClientDetail(tmpEnroll));
            }
            // worked = Boolean.TRUE;
        }

        return result;
    }

    public static CoreCustInfo findCoreCustInfoByOperOrg(final String anOperOrg) {
        final DictItemInfo itemInfo = DictUtils.getDictItem(FACTOR_CORE_CUSTINFO, anOperOrg);
        logger.info("findCoreCustInfoByOperOrg-itemInfo:"+itemInfo);
        final CoreCustInfo custInfo = new CoreCustInfo();
        if (itemInfo != null) {
            custInfo.setOperOrg(anOperOrg);
            custInfo.setCustName(itemInfo.getItemName());
            custInfo.setCustNo(Long.parseLong(itemInfo.getItemCode()));
            custInfo.setPartnerCode(itemInfo.getSubject());
        }
        logger.info("findCoreCustInfoByOperOrg-custInfo:"+custInfo);
        return custInfo;
    }

    public static Map initConverter() {
        final Map<String, ClientDataConverter> ccMap = new LinkedHashMap();
        ccMap.put("account", new ClientDataConverter<CoreSupplierInfo>(new TypeReference<List<SupplyInfo>>() {
        }, CoreSupplierInfo.class));

        ccMap.put("accountBank", new ClientDataConverter<ScfSupplierBank>(new TypeReference<List<SupplyBankInfo>>() {
        }, ScfSupplierBank.class));

        ccMap.put("acceptBill", new ClientDataConverter<ScfAcceptBill>(new TypeReference<List<SupplyAcceptBill>>() {
        }, ScfAcceptBill.class));

        ccMap.put("accountBankFlow", new ClientDataConverter<ScfBankPaymentFlow>(new TypeReference<List<SupplyPaymentInfo>>() {
        }, ScfBankPaymentFlow.class));

        ccMap.put("processCoreCorp", new ClientDataConverter<CustCoreCorpInfo>(new TypeReference<List<CustCoreCorp>>() {
        }, CustCoreCorpInfo.class));

        ccMap.put("accountPayStream", new ClientDataConverter<ScfCapitalFlow>(new TypeReference<List<ScfCapitalFlowInfo>>() {
        }, ScfCapitalFlow.class));

        return ccMap;
    }

    private ScfClientDataDetail createClientDetail(final CustTempEnrollInfo anTmpEnroll) {
        final ScfClientDataDetail clientDataDetail = new ScfClientDataDetail();
        clientDataDetail.setBankAcc(anTmpEnroll.getBankAccount());
        clientDataDetail.setBankAccName(anTmpEnroll.getBankAcountName());
        clientDataDetail.setWorkFlag("1");

        return clientDataDetail;
    }

    /**
     * 通过调度设置状态为未处理，避免数据抢夺的问题。
     */
    public void putUnCheckStatus() {

        worked = Boolean.FALSE;
    }

    /**
     * 查询处理的账户信息，如果在当天已经处理过，则不再处理
     *
     * @param anCoreCustNo
     * @return
     */
    public List<ScfClientDataDetail> findWorkAccount(final Map anMap) {
        final List<ScfClientDataDetail> result = new ArrayList();
        final ScfClientDataProcess clientData = BeanMapper.map(anMap, ScfClientDataProcess.class);
        final String operOrg = (String)anMap.get(ATTACH_DATA);
        final CoreCustInfo  coreCustInfo = findCoreCustInfoByOperOrg(operOrg);
        final List<CoreDataProcessDetail> tmpDetailList = dataDetailProcessService.findProcessDetailByProc(operOrg, clientData);
        final Multimap<String, Collection> mutMap = ReflectionUtils.listConvertToMuiltMap(tmpDetailList, "btNo");
        Collection cc;
        for (final ScfSupplierBank bank : this.supplierBankService.findScfBankByCoreOperOrg(coreCustInfo.getCustNo())) {
            cc = mutMap.get(bank.getBtNo());
            if (checkProcessed(cc, bank.getBankAccount(), clientData.getWorkType())) {

                result.add(createClientDetailByBank(bank));
            }
        }

        return result;
    }

    private boolean checkProcessed(final Collection<CoreDataProcessDetail> anCc, final String anBankAccount, final String anWorkType) {
        if (Collections3.isEmpty(anCc) || (" 1".indexOf(anWorkType) > 0)) {

            return true;
        }

        for (final CoreDataProcessDetail processDetail : anCc) {
            if (BetterStringUtils.isNotBlank(processDetail.getBankAccount())) {

                return processDetail.getBankAccount().equalsIgnoreCase(anBankAccount) == false;
            }
        }
        return true;
    }

    private ScfClientDataDetail createClientDetailByBank(final ScfSupplierBank anTmpEnroll) {
        final ScfClientDataDetail clientDataDetail = new ScfClientDataDetail();
        clientDataDetail.setBankAcc(anTmpEnroll.getBankAccount());
        clientDataDetail.setBankAccName(anTmpEnroll.getBankAccountName());
        clientDataDetail.setBtCustId(anTmpEnroll.getBtNo());
        clientDataDetail.setWorkFlag("1");

        return clientDataDetail;
    }

    /**
     * 处理从资金管理系统推送过来的数据
     *
     * @param anMap
     * @return
     */
    public boolean saveProcessPushData(final Map anMap) {
        logger.info("saveProcessPushData data is :"+anMap);

        return processData(anMap, false);
    }

    /**
     * 处理从资金管理系统推送过来的数据
     *
     * @param anMap
     * @return
     */
    public boolean saveProcessCoreCorp(final Map anMap) {
        logger.info("saveProcessPushData data is :"+anMap);

        processData(anMap, false);
        final String operOrg = (String)anMap.get(ATTACH_DATA);
        final ScfClientDataProcess dataProcess = BeanMapper.map(anMap, ScfClientDataProcess.class);
        this.dataProcessLogService.processWorkStatus(operOrg, dataProcess.getWorkType(), dataProcess.getWorkStatus());
        return true;
    }

    /**
     * 处理业务数据，处理单个账户，用于注册信息的处理
     *
     * @param anMap
     * @return
     */
    public boolean saveProcessData(final Map anMap) {

        return processData(anMap, false);
    }

    private boolean processData(final Map anMap, final boolean anBatch) {
        ClientDataConverter dataCover;
        ScfClientDataDetail detail;
        int workCount = 0;
        final Map<String, Object> obj = (Map) anMap.get("data");
        logger.info("processData-obj:"+obj);
        String processType;
        final String operOrg = (String)anMap.get(ATTACH_DATA);
        logger.info("processData-operOrg:"+operOrg);
        for (final Map.Entry ent : obj.entrySet()) {
            final Collection tmpList = (Collection) ent.getValue();
            logger.info("processData-tmpList:"+tmpList);
            dataCover = coverterMap.get(ent.getKey());
            processType = (String) ent.getKey();
            for (final Object objx : tmpList) {
                if (objx instanceof Map) {
                    detail = BeanMapper.map(objx, ScfClientDataDetail.class);
                    if ("1".equals(detail.getWorkStatus())) {
                        try{
                            final String tmpData = (String) ((Map) objx).get("data");
                            final List tmpValues = dataCover.convert(tmpData);
                            if ("processCoreCorp".equalsIgnoreCase(processType)){
                                clientHelpService.saveCoreCorpData(processType, detail, tmpValues, operOrg);
                            }
                            else{
                                clientHelpService.saveCustomerData(processType, detail, tmpValues, operOrg);
                            }
                            workCount = tmpValues.size();
                        }catch(final Exception ex){
                            logger.error("dataCover :" + ent.getKey(),ex);
                        }
                    }
                    else if ("2".equals(detail.getWorkStatus())) {
                        this.dataDetailProcessService.saveProcessDetail(operOrg, processType, detail);
                        continue;
                    }
                    else {
                        workCount = 0;
                    }
                    if (anBatch) {
                        detail.setCount(workCount);
                        // detail.setWorkStatus("1");
                        this.dataDetailProcessService.saveProcessDetail(operOrg, processType, detail);
                    }
                    else if ("account".equals(ent.getKey())) {
                        supplyAccoService.saveCheckedStatus(detail.getBankAccName(), detail.getBankAcc(), operOrg, detail.getBtCustId(),
                                "1".equals(detail.getWorkStatus()));
                    }
                }
            }
        }

        return true;
    }

    /**
     * 处理业务数据，用于调度的批量信息的处理
     *
     * @param anMap
     * @return
     */
    public boolean saveBatchProcessData(final Map anMap) {
        processData(anMap, true);
        final String operOrg = (String)anMap.get(ATTACH_DATA);
        final ScfClientDataProcess dataProcess = BeanMapper.map(anMap, ScfClientDataProcess.class);
        this.dataProcessLogService.processWorkStatus(operOrg, dataProcess.getWorkType(), dataProcess.getWorkStatus());

        return true;
    }

    /**
     * 处理业务数据，用于调度的批量信息的处理
     *
     * @param anMap
     * @return
     */
    public boolean noticeProcess(final Map anMap) {
        logger.info("requestData :" + anMap);

        return true;
    }

    private ScfClientDataProcess createDataProcess(final CoreDataProcessInfo anData) {
        final ScfClientDataProcess result = new ScfClientDataProcess();
        result.setWorkType(anData.getProcessType());
        result.setStartDate(anData.getStartDate());
        result.setEndDate(anData.getEndDate());
        result.setWorkStatus(anData.getWorkStatus());
        result.setWorkDate(anData.getWorkDate());

        return result;
    }

    public List<ScfClientDataProcess> findDataProcess(final Map anMap) {
        final List<ScfClientDataProcess> result = new ArrayList();
        final String operOrg = (String)anMap.get(ATTACH_DATA);
        for (final CoreDataProcessInfo data : this.dataProcessLogService.findCoreDataProcess(operOrg)) {

            result.add(createDataProcess(data));
        }

        return result;
    }

    public static class ClientDataConverter<T> {
        private final TypeReference typeR;
        private final Class<T> workClass;

        public ClientDataConverter(final TypeReference anTypeR, final Class anClass) {
            typeR = anTypeR;
            workClass = anClass;
        }

        public List<T> convert(final String anData) {
            Object listData;
            final List<T> result = new ArrayList();
            try {
                listData = JsonMapper.buildNormalMapper().readValue(anData, typeR);
                for (final Object obj : (List) listData) {
                    result.add(BeanMapper.map(obj, workClass));
                }
            }
            catch (final IOException e) {

                e.printStackTrace();
            }

            return result;
        }
    }

}