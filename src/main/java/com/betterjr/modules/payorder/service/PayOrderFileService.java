package com.betterjr.modules.payorder.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.service.DataStoreService;
import com.betterjr.modules.flie.data.ExcelImportUtils;
import com.betterjr.modules.flie.service.FileDownService;
import com.betterjr.modules.order.entity.ScfOrderDO;
import com.betterjr.modules.payorder.dao.PayOrderFileMapper;
import com.betterjr.modules.payorder.data.PayOrderFileConstantCollentions;
import com.betterjr.modules.payorder.data.PayOrderPoolRecordConstantCollentions;
import com.betterjr.modules.payorder.entity.PayOrderFile;
import com.betterjr.modules.payorder.entity.PayOrderPoolRecord;
import com.betterjr.modules.supplieroffer.service.ScfReceivableRequestService;

/**
 * 
 * @ClassName: PayOrderFileService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xuyp
 * @date 2017年10月20日 下午3:12:11 
 *
 */
@Service
public class PayOrderFileService extends BaseService<PayOrderFileMapper, PayOrderFile> {

    /**
     * 符号分隔符
     */
    private static final String MARK_SEPARATOR = ",";
    
    /**
     * map 包含属性 状态
     */
    private static final String MAP_CONTAIN_PROPERTIES_BUSIN_STATUS = "businStatus";

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;

    @Autowired
    private PayOrderPoolRecordService recordService;

    @Reference(interfaceClass = IDomainAttributeService.class)
    private IDomainAttributeService domainAttributeDubboClientService;

    @Autowired
    private FileDownService fileDownService;

    @Autowired
    private PayOrderPoolService poolService;

    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileService;

    @Autowired
    private DataStoreService dataStoreService;

    @Autowired
    private ScfReceivableRequestService requestService;

    /**
     * 通过付款日期生成下载文件
     * @param anRequestPayDate
     * @param anFlag 是否需要插入到数据库 0 不需要插入到数据库  1 需要生成文件并且插入到数据库
     * @return
     */
    public PayOrderFile saveAddFile(String anRequestPayDate, String anFlag) {

        BTAssert.hasLength(anRequestPayDate, "付款日期为空,操作失败");
        BTAssert.hasLength(anFlag, "查询条件为空,操作失败");
        logger.info("begin to saveAddFile " + UserUtils.getOperatorInfo().getName() + " 参数： anRequestPayDate="
                + anRequestPayDate + "  anFlag=" + anFlag);
        List<PayOrderPoolRecord> recordList = queryPayRecordList(anRequestPayDate);

        PayOrderFile file = createPayOrderFileByRecordList(recordList, anFlag,
                PayOrderFileConstantCollentions.PAY_FILE_INFO_TYPE_DOWNREQUESTFILE);
        logger.info("end to saveAddFile  返回值： file=" + file);
        return file;

    }

    /**
     * 查询文件记录
     * @Title: queryFileList 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param anRequestPayDate  付款日期
     * @param anbusinStatus   状态
     * @param anInfoType   付款类型   0生成付款文件 1上传的付款结果文件
     * @return 参数说明 
     * @throws 
     * @author xuyp
     * @date 2017年10月20日 下午3:47:06
     */
    
    public List<PayOrderFile> queryFileList(String anRequestPayDate, String anbusinStatus, String anInfoType) {

        BTAssert.hasLength(anRequestPayDate, "付款日期为空,操作失败");
        BTAssert.hasLength(anInfoType, "查询条件为空,操作失败");
        // BTAssert.hasLength(anbusinStatus, "查询条件为空,操作失败");

        Map queryMap = new HashMap<>(4);
        queryMap.put("requestPayDate", anRequestPayDate);
        queryMap.put("factoryNo", queryCurrentUserCustNos());
        queryMap.put("businStatus", anbusinStatus);
        queryMap.put("infoType", anInfoType);
        if (anbusinStatus.contains(MARK_SEPARATOR)) {
            queryMap.put("businStatus", anbusinStatus.split(MARK_SEPARATOR));
        }

        if (StringUtils.isBlank(anbusinStatus)) {
            queryMap.put("businStatus", new String[] { PayOrderFileConstantCollentions.PAY_FILE_BUSIN_STATUS_NOCONFIRM,
                    PayOrderFileConstantCollentions.PAY_FILE_BUSIN_STATUS_CONFIRM });
        }

        if (anInfoType.contains(MARK_SEPARATOR)) {
            queryMap.put("infoType", anInfoType.split(MARK_SEPARATOR));
        }

        return this.selectByProperty(queryMap);

    }

    /**
     * 查询文件记录
     * @Title: queryFilePage 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anMap
     * @param @param anFlag
     * @param @param anPageNum
     * @param @param anPageSize
     * @param @return 参数说明 
     * @return Page<PayOrderFile> 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月19日 下午1:33:19
     * GTErequestPayDate  LTErequestPayDate   businStatus  infoType
     */
    public Page<PayOrderFile> queryFilePage(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {

        BTAssert.notNull(anMap, "查询条件为空");
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap,
                new String[] { "GTErequestPayDate", "LTErequestPayDate", "businStatus", "infoType" });
        if (!anMap.containsKey(MAP_CONTAIN_PROPERTIES_BUSIN_STATUS)) {
            anMap.put("businStatus", new String[] { PayOrderFileConstantCollentions.PAY_FILE_BUSIN_STATUS_NOCONFIRM,
                    PayOrderFileConstantCollentions.PAY_FILE_BUSIN_STATUS_CONFIRM });
        }

        anMap.put("factoryNo", queryCurrentUserCustNos());
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id Desc");
    }

    /**
     * 查询文件列表
     * @Title: queryFileListByMap 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anMap
     * @param @return 参数说明 
     * @return List<PayOrderFile> 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月19日 下午2:02:14
     */
    public List<PayOrderFile> queryFileListByMap(Map<String, Object> anMap) {

        BTAssert.notNull(anMap, "查询条件为空");
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap = Collections3.filterMap(anMap, new String[] { "GTErequestPayDate", "LTErequestPayDate", "businStatus",
                "infoType", "GTEregDate", "LTEregDate", "regDate", "lockedStatus" });
        if (!anMap.containsKey(MAP_CONTAIN_PROPERTIES_BUSIN_STATUS)) {
            anMap.put("businStatus", new String[] { PayOrderFileConstantCollentions.PAY_FILE_BUSIN_STATUS_NOCONFIRM,
                    PayOrderFileConstantCollentions.PAY_FILE_BUSIN_STATUS_CONFIRM });
        }

        anMap.put("factoryNo", queryCurrentUserCustNos());
        return this.selectByProperty(anMap, "id Desc");
    }

    /**
     * 上传付款结果解析
     * @Title: saveResolveFile 
     * @Description:  
     * @param @param anFileItemId 付款结果文件上传的fileitem id 
     * @param @param anSourceFileId   下载的付款结果文件id
     * @param @return 参数说明 
     * @return PayOrderFile 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月17日 下午3:30:34
     */
    public PayOrderFile saveResolveFile(Long anFileItemId, Long anSourceFileId) {

        BTAssert.notNull(anFileItemId, "文件对象为空,解析失败");
        BTAssert.notNull(anSourceFileId, "文件对象为空,解析失败");
        logger.info("begin to saveResolveFile " + UserUtils.getOperatorInfo().getName() + " 参数： anFileItemId="
                + anFileItemId + "  anSourceFileId=" + anSourceFileId);
        PayOrderFile sourceFile = this.selectByPrimaryKey(anSourceFileId);
        BTAssert.notNull(sourceFile, "未找到原下载文件,解析失败");
        BTAssert.isTrue(
                PayOrderFileConstantCollentions.PAY_FILE_LOCKED_STATUS_CANUPLOAD.equals(sourceFile.getLockedStatus()),
                "当前文件已经解析");

        List<PayOrderPoolRecord> recordList = saveResolveFileToRecordList(anFileItemId);

        BTAssert.isTrue(!Collections3.isEmpty(recordList), "当前文件没有记录");

        List<PayOrderPoolRecord> sourceRecordList = recordService.queryRecordListByFileId(anSourceFileId,
                PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYING);
        // 校验两个文件数据是否相同 并且更新付款状态
        checkFileRecordListEqure(sourceRecordList, recordList);

        // 插入一条新的付款文件记录信息
        saveAddFileBySourceFile(sourceFile.getId(), anFileItemId);
        // 修改源文件状态 LockedStatus
        sourceFile.setLockedStatus(PayOrderFileConstantCollentions.PAY_FILE_LOCKED_STATUS_UPLOADED);
        this.updateByPrimaryKeySelective(sourceFile);

        // 修改数据库池中的数量
        poolService.saveUpdateAmount(sourceFile.getPoolId(), Long.parseLong(sourceRecordList.size() + ""), "2");

        logger.info(
                "end to saveResolveFile " + UserUtils.getOperatorInfo().getName() + " 参数： PayOrderFile=" + sourceFile);
        return sourceFile;
    }

    /**
     * 查询付款详情
     * @Title: findFileDetailByPrimaryKey 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anid
     * @param @return 参数说明 
     * @return PayOrderFile 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月18日 上午10:23:33
     */
    public PayOrderFile findFileDetailByPrimaryKey(Long anid) {

        BTAssert.notNull(anid, "条件为空,操作失败");
        PayOrderFile file = this.selectByPrimaryKey(anid);
        BTAssert.notNull(file, "未查到当前文件信息,操作失败");
        List<PayOrderPoolRecord> recordList = recordService.queryRecordListByFileId(file.getId());
        file.setRecordList(recordList);

        return file;

    }

    /**
     * 审核文件
     * @Title: saveAuditFileByPrimaryKey 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anid
     * @param @return 参数说明 
     * @return PayOrderFile 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月20日 下午3:16:15
     */
    public PayOrderFile saveAuditFileByPrimaryKey(Long anid) {

        logger.info(
                "begin to saveAuditFileByPrimaryKey " + UserUtils.getOperatorInfo().getName() + " 参数： anid=" + anid);
        PayOrderFile file = findFileDetailByPrimaryKey(anid);
        BTAssert.isTrue(PayOrderFileConstantCollentions.PAY_FILE_LOCKED_STATUS_UPLOADED.equals(file.getLockedStatus()),
                "当前文件尚未解析，操作失败");
        BTAssert.isTrue(!Collections3.isEmpty(file.getRecordList()), "当前文件没有付款记录，操作失败");

        if (!queryCurrentUserCustNos().contains(file.getFactoryNo())) {
            BTAssert.notNull(null, "你没有审核权限，操作失败");
        }

        List<PayOrderPoolRecord> paySuccessList = new ArrayList<>(file.getRecordList().size());
        List<PayOrderPoolRecord> payFailureList = new ArrayList<>(file.getRecordList().size());
        // 更新每条记录的状态
        for (PayOrderPoolRecord record : file.getRecordList()) {
            BTAssert.isTrue(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_AUDITING
                    .equals(record.getBusinStatus()), "当前记录不是复核中，操作失败");
            BTAssert.isTrue(StringUtils.isNoneBlank(record.getBusinStatusChinese()), "当前记录没有解析结果,操作失败");

            // 付款结果只能是付款成功和付款失败
            boolean businStatusChineseFlag = PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_CHINESS_PAYSUCCESS
                    .equals(record.getBusinStatusChinese())
                    || PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_CHINESS_PAYFAILURE
                            .equals(record.getBusinStatusChinese());
            BTAssert.isTrue(businStatusChineseFlag, "当前记录付款结果异常" + record.getBusinStatusChinese() + ",操作异常");

            if (PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_CHINESS_PAYSUCCESS
                    .equals(record.getBusinStatusChinese())) {
                record.setBusinStatus(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYSUCCESS);
                paySuccessList.add(record);
            } else if (PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_CHINESS_PAYFAILURE
                    .equals(record.getBusinStatusChinese())) {
                record.setBusinStatus(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYFAILURE);
                payFailureList.add(record);
            } else {
                BTAssert.notNull(null, "当前记录付款结果异常" + record.getBusinStatusChinese() + ",操作异常");
            }

            recordService.updateByPrimaryKeySelective(record);

        }

        // 更新文件状态
        file.saveAuditValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(file);
        // 更新上传文件的状态
        saveUpdateUploadFileStatus(file, PayOrderFileConstantCollentions.PAY_FILE_BUSIN_STATUS_CONFIRM);

        // 更新付款池状态和数量
        // 更新到成功
        poolService.saveUpdateAmount(file.getPoolId(), Long.parseLong(paySuccessList.size() + ""), "4");
        // 更新到失败
        poolService.saveUpdateAmount(file.getPoolId(), Long.parseLong(payFailureList.size() + ""), "3");

        // 更新融资申请状态申请表

        requestService.saveUpdatePayStatusByRecordList(paySuccessList,
                PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYSUCCESS);

        requestService.saveUpdatePayStatusByRecordList(payFailureList,
                PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYFAILURE);

        logger.info("end to saveAuditFileByPrimaryKey " + UserUtils.getOperatorInfo().getName() + " 返回值： file=" + file);
        return file;

    }

    /**
     * 删除文件
     * @Title: saveDeleteFileByPrimaryKey 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anid PayOrderFile主键
     * @param @return 参数说明 
     * @return PayOrderFile 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月18日 下午2:16:19
     */
    public PayOrderFile saveDeleteFileByPrimaryKey(Long anid) {

        logger.info(
                "begin to saveDeleteFileByPrimaryKey " + UserUtils.getOperatorInfo().getName() + " 参数： anid=" + anid);
        PayOrderFile file = findFileDetailByPrimaryKey(anid);
        BTAssert.isTrue(PayOrderFileConstantCollentions.PAY_FILE_LOCKED_STATUS_UPLOADED.equals(file.getLockedStatus()),
                "当前文件尚未解析，操作失败");
        BTAssert.isTrue(PayOrderFileConstantCollentions.PAY_FILE_BUSIN_STATUS_NOCONFIRM.equals(file.getBusinStatus()),
                "已经审核或删除的文件无法删除，操作失败");
        BTAssert.isTrue(!Collections3.isEmpty(file.getRecordList()), "当前文件没有付款记录，操作失败");

        if (!queryCurrentUserCustNos().contains(file.getFactoryNo())) {
            BTAssert.notNull(null, "你没有审核权限，操作失败");
        }

        // 更新文件状态
        file.setLockedStatus(PayOrderFileConstantCollentions.PAY_FILE_LOCKED_STATUS_CANUPLOAD);
        this.updateByPrimaryKeySelective(file);
        saveUpdateUploadFileStatus(file, PayOrderFileConstantCollentions.PAY_FILE_BUSIN_STATUS_DELETEED);

        // 更新付款记录状态
        for (PayOrderPoolRecord record : file.getRecordList()) {
            BTAssert.isTrue(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_AUDITING
                    .equals(record.getBusinStatus()), "付款结果状态不符合,操作失败");
            record.setBusinStatus(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYING);
            recordService.updateByPrimaryKeySelective(record);
        }
        // 更新付款池数量

        poolService.saveUpdateAmount(file.getPoolId(), Long.parseLong(file.getRecordList().size() + ""), "5");

        logger.info(
                "end to saveDeleteFileByPrimaryKey " + UserUtils.getOperatorInfo().getName() + " 返回值： file=" + file);
        return file;

    }

    /**
     * 文件上传时根据原付款文件生成上传文件的数据库副本
     * @Title: saveAddFileBySourceFile 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anId 源数据库文件副本主键
     * @param @param anFileItemId 参数说明  上传文件
     * @return void 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月19日 下午2:49:28
     */
    private PayOrderFile saveAddFileBySourceFile(Long anId, Long anFileItemId) {

        PayOrderFile file = this.selectByPrimaryKey(anId);

        file.saveAddInitValue(PayOrderFileConstantCollentions.PAY_FILE_INFO_TYPE_UPLOADPAYRESULT);
        file.setLockedStatus(PayOrderFileConstantCollentions.PAY_FILE_LOCKED_STATUS_UPLOADED);
        file.setFileItemId(anFileItemId);
        // 文件上次详情
        CustFileItem fileItem = custFileService.findOne(anFileItemId);
        file.setFileName(fileItem.getFileName());
        file.setSourceFileId(anId);

        this.insertSelective(file);
        return file;
    }

    /**
     * 更新付款文件对应的上传解析文件状态
     * @Title: saveUpdateUploadFileStatus 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param file
     * @param @param anBusinStatus 参数说明 
     * @return void 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月18日 下午2:38:34
     */
    private void saveUpdateUploadFileStatus(PayOrderFile file, String anBusinStatus) {
        Map map = QueryTermBuilder.newInstance().put("sourceFileId", file.getId())
                .put("businStatus", PayOrderFileConstantCollentions.PAY_FILE_BUSIN_STATUS_NOCONFIRM).build();
        List<PayOrderFile> list = this.selectByProperty(map);
        for (PayOrderFile payOrderFile : list) {
            if (PayOrderFileConstantCollentions.PAY_FILE_BUSIN_STATUS_CONFIRM.equals(anBusinStatus)) {
                payOrderFile.saveAuditValue(UserUtils.getOperatorInfo());
            }
            payOrderFile.setBusinStatus(anBusinStatus);
            this.updateByPrimaryKeySelective(payOrderFile);
        }
    }

    /**
     * 校验下载的文件和上传解析的文件记录是否一致 并更新原记录的解析信息
     * @Title: checkFileRecordListEqure 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anSourceRecordList  原下载的付款文件记录
     * @param @param anRecordList 参数说明     解析出来记录列表
     * @return void 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月17日 下午5:41:42
     */
    private void checkFileRecordListEqure(List<PayOrderPoolRecord> anSourceRecordList,
            List<PayOrderPoolRecord> anRecordList) {

        for (PayOrderPoolRecord sourceRecord : anSourceRecordList) {

            // 校验当前对象和数据库的记录一致
            checkRecordEqureFromList(sourceRecord, anRecordList);
            // 更新付款状态 和描述信息
            recordService.updateByPrimaryKeySelective(sourceRecord);
        }

    }

    /**
     * 检查当前记录是否在集合中 并给原记录设置 解析结果和状态
     * @Title: checkRecordEqureFromList 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anSourceRecord
     * @param @param anRecordList 参数说明 
     * @return void 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月18日 上午9:16:15
     */
    private void checkRecordEqureFromList(PayOrderPoolRecord anSourceRecord, List<PayOrderPoolRecord> anRecordList) {

        PayOrderPoolRecord record = findRecordFromListByRequestNo(anSourceRecord.getRequestNo(), anRecordList);

        // 校验申请编号 银行信息是否相等
        checkRecordEqure(record, anSourceRecord);
        // 校验金额
        if (!record.getBalance().equals(anSourceRecord.getBalance())) {
            BTAssert.notNull(null, "对比对象付款金额,操作失败");
        }
        // 校验付款状态
        if (StringUtils.isBlank(record.getBusinStatusChinese())) {
            BTAssert.notNull(null, "解析对象的付款结果为空,操作失败");
        }

        anSourceRecord.setBusinStatusChinese(record.getBusinStatusChinese());
        anSourceRecord.setDescription(record.getDescription());
        anSourceRecord.setBusinStatus(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_AUDITING);
    }

    /**
     * 从集合中查找到相同的requestNo的记录，并且封装成一个PayOrderPoolRecord 对象返回
     * @Title: findRecordFromListByRequestNo 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anRecordList
     * @param @return 参数说明 
     * @return PayOrderPoolRecord 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月18日 上午9:20:19
     */
    private PayOrderPoolRecord findRecordFromListByRequestNo(String anRequestNo,
            List<PayOrderPoolRecord> anRecordList) {

        PayOrderPoolRecord record = new PayOrderPoolRecord();
        for (PayOrderPoolRecord payRecord : anRecordList) {

            if (StringUtils.isBlank(payRecord.getRequestNo())) {
                BTAssert.notNull(null, "对比对象申请编号为空,操作失败");
            }

            if (payRecord.getRequestNo().equals(anRequestNo)) {

                // 第一条相同的记录 最开始复制
                if (StringUtils.isBlank(record.getRequestNo())) {
                    try {
                        BeanUtils.copyProperties(record, payRecord);
                    }
                    catch (Exception e) {
                        record.setRequestNo(payRecord.getRequestNo());
                        record.setBalance(payRecord.getBalance());
                        record.setBusinStatusChinese(payRecord.getBusinStatusChinese());
                        record.setCustBankAccount(payRecord.getCustBankAccount());
                        record.setCustBankAccountName(payRecord.getCustBankAccountName());
                        record.setCustBankName(payRecord.getCustBankName());
                        record.setDescription(payRecord.getDescription());
                        e.printStackTrace();
                    }
                } else {

                    // 后面开始比较两个对象是否相同
                    checkRecordEqure(record, payRecord);
                    // 新增值
                    record.setBalance(MathExtend.add(payRecord.getBalance(), record.getBalance()));

                }

            }

        }

        return record;
    }

    /**
     * 校验两个PayOrderPoolRecord对象的申请编号，银行信息是否一致
     * @Title: checkRecordEqure 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anRecord
     * @param @param anPayRecord 参数说明 
     * @return void 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月18日 上午10:05:24
     */
    private void checkRecordEqure(PayOrderPoolRecord anRecord, PayOrderPoolRecord anPayRecord) {

        if (!anRecord.getRequestNo().equals(anPayRecord.getRequestNo())) {
            BTAssert.notNull(null, "对比对象申请编号不同,操作失败");
        }

        if (!anRecord.getCustBankAccount().equals(anPayRecord.getCustBankAccount())) {
            BTAssert.notNull(null, "对比对象银行帐号,操作失败");
        }

        if (!anRecord.getCustBankAccountName().equals(anPayRecord.getCustBankAccountName())) {
            BTAssert.notNull(null, "对比对象银行帐户名,操作失败");
        }

        if (!anRecord.getCustBankName().equals(anPayRecord.getCustBankName())) {
            BTAssert.notNull(null, "对比对象银行名称,操作失败");
        }

    }

    /**
     * 根据文件id 解析出记录列表
     * @Title: saveResolveFileToRecordList 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anFileItemId
     * @param @return 参数说明 
     * @return List<PayOrderPoolRecord> 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月17日 下午4:55:48
     */
    private List<PayOrderPoolRecord> saveResolveFileToRecordList(Long anFileItemId) {

        // 文件上次详情
        CustFileItem fileItem = custFileService.findOne(anFileItemId);
        InputStream is = dataStoreService.loadFromStore(fileItem);
        List<PayOrderPoolRecord> recordList = new ArrayList<>();
        try {
            recordList = ExcelImportUtils.importObj(PayOrderPoolRecord.class, is, fileItem.getFileType());
        }
        catch (Exception e) {
            logger.info(UserUtils.getOperatorInfo().getName() + "文件导入失败! 文件Id为：" + fileItem.getId() + "  产生错误信息："
                    + e.getMessage());
            BTAssert.notNull(null, e.getMessage());
        }
        return recordList;
    }

    /**
     * 新增付款文件  
     * @Title: createPayOrderFileByRecordList 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param anRecordList
     * @param @param anFlag  0 不需要修改数据库    1需要修改数据库
     * @param @param anInfoType   文件作用类型 0生成付款文件 1上传的付款结果文件
     * @param @return 参数说明 
     * @return PayOrderFile 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月18日 上午10:08:04
     */
    private PayOrderFile createPayOrderFileByRecordList(List<PayOrderPoolRecord> anRecordList, String anFlag,
            String anInfoType) {

        BTAssert.notEmpty(anRecordList, "没有相应的申请记录信息");
        PayOrderFile file = new PayOrderFile().saveAddInitValue(anInfoType);
        BigDecimal balance = new BigDecimal(0);
        Long payAmount = 0L;
        for (PayOrderPoolRecord payOrderPoolRecord : anRecordList) {
            balance = MathExtend.add(balance, payOrderPoolRecord.getBalance());
            payAmount = payAmount + 1;
            file.setPoolId(payOrderPoolRecord.getPoolId());
            file.setRequestPayDate(payOrderPoolRecord.getRequestPayDate());
            file.setFactoryNo(payOrderPoolRecord.getFactoryNo());
            file.setFactoryName(payOrderPoolRecord.getFactoryName());
        }
        file.setBalance(balance);
        file.setPayAmount(payAmount);
        file.setRecordList(anRecordList);
        file.setFileName(file.getFactoryName() + "付款指令" + file.getRegDate() + ".xlsx");

        if (PayOrderFileConstantCollentions.PAY_FILE_REPOSITORY_FLAG_OK.equals(anFlag)) {
            saveInsertToBaseAndCreateFile(file);
        }

        return file;
    }

    /**
     * 将封装付款记录明细项的付款文件插入到数据库 并且生成付款文件
     * @Title: saveInsertToBaseAndCreateFile 
     * @Description: 将封装付款记录明细项的付款文件插入到数据库 并且生成付款文件
     * @param @param anFile 参数说明 
     * @return void 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月17日 上午9:50:54
     */
    private PayOrderFile saveInsertToBaseAndCreateFile(PayOrderFile anFile) {

        BTAssert.notNull(anFile, "文件对象为空,生成文件失败");
        BTAssert.notEmpty(anFile.getRecordList(), "没有相应的申请记录信息");

        Long fileId = findTemplateFileId("GLOBAL_PAYORDER_EXPORT_TEMPLATE");
        BTAssert.notNull(fileId, "请先联系平台上传导出模版");

        if (anFile.getRecordList().size() == 1) {
            List<PayOrderPoolRecord> recordList = anFile.getRecordList();
            recordList.add(new PayOrderPoolRecord());
            anFile.setRecordList(recordList);
        }
        String endFlag = UserUtils.getOperatorInfo().getName() + "在" + BetterDateUtils.getDateTime()
                + "生成,(*上传提交付款结果时请删除此行)";
        Map map = QueryTermBuilder.newInstance().put("file", anFile).put("recordList", anFile.getRecordList())
                .put("endFlag", endFlag).build();
        CustFileItem fileItem = fileDownService.uploadCommissionRecordFileis(map, fileId, anFile.getFileName());
        anFile.setFileItemId(fileItem.getId());
        // 插入到数据库
        this.insertSelective(anFile);
        // 更新每条记录的状态
        for (PayOrderPoolRecord record : anFile.getRecordList()) {
            record.setBusinStatus(PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_PAYING);
            record.setPayFileId(anFile.getId());
            recordService.updateByPrimaryKeySelective(record);
        }
        // 更新付款指令池
        poolService.saveUpdateAmount(anFile.getPoolId(), anFile.getPayAmount(), "1");

        return anFile;

    }

    /**
     * 根据文件类型查询相应模板存放的地址空间   GLOBAL_PAYORDER_IMPORT_TEMPLATE 付款指令导入模版
     * GLOBAL_PAYORDER_EXPORT_TEMPLATE  付款指令导出模版
     * @Title: findTemplateFileId 
     * @Description:  
     * @param @param anType
     * @param @return 参数说明 
     * @return Long 返回类型 
     * @throws 
     * @author xuyp
     * @date 2017年10月17日 上午11:48:18
     */

    private Long findTemplateFileId(String anType) {

        String exportCommissionTemplate = domainAttributeDubboClientService.findString(anType);

        if (StringUtils.isNoneBlank(exportCommissionTemplate)) {

            Map<String, Object> templateMp = JsonMapper.parserJson(exportCommissionTemplate);
            Long fileId = Long.parseLong(templateMp.get("id").toString());

            return fileId;

        } else {

            BTAssert.notNull(null, "请先联系平台模版");

        }
        BTAssert.notNull(null, "请联系平台上传模版");
        return null;

    }

    /**
     * 查询当天可以付款的申请信息
     * @param anRequestPayDate
     * @return
     */
    private List<PayOrderPoolRecord> queryPayRecordList(String anRequestPayDate) {

        Map map = QueryTermBuilder.newInstance().put("requestPayDate", anRequestPayDate)
                .put("businStatus", PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_NOHANDLE)
                .put("factoryNo", queryCurrentUserCustNos()).build();
        List<PayOrderPoolRecord> recordList = recordService.queryRecordList(map);

        BTAssert.notEmpty(recordList, "没有相应的申请记录信息");

        return recordList;
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
