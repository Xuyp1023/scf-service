package com.betterjr.modules.commission.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.commission.dao.CommissionRecordMapper;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;
import com.betterjr.modules.commission.entity.CommissionFile;
import com.betterjr.modules.commission.entity.CommissionRecord;
import com.betterjr.modules.commission.entity.CommissionRecordAuditResult;
import com.betterjr.modules.customer.ICustMechBaseService;

@Service
public class CommissionRecordService extends BaseService<CommissionRecordMapper, CommissionRecord> {

    
    @Autowired
    private CommissionFileService fileService;
    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;
    
    /**
     * 通过佣金文件的fileId对佣金记录进行删除操作
     * @param anFileId
     * @return
     */
    public List<CommissionRecord> saveDeleteStatusByRefNo(Long anFileId) {
        
        BTAssert.notNull(anFileId,"删除佣金文件条件不符,");
        Map<String,Object> queryMap = QueryTermBuilder.newInstance()
                .put("fileId", anFileId).build();
        List<CommissionRecord> recordList = this.selectByProperty(queryMap);
        for (CommissionRecord record : recordList) {
            checkDeleteFileStatus(record, UserUtils.getOperatorInfo());
            record.setBusinStatus(CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_DELETE);
            this.updateByPrimaryKeySelective(record);
        }
        return recordList;
    }
    
    
    /**
     * 检查是否符合删除的条件
     * @param anFile
     * @param anOperatorInfo
     */
    private void checkDeleteFileStatus(CommissionRecord anRecord, CustOperatorInfo anOperatorInfo) {
        
        BTAssert.notNull(anOperatorInfo,"登录已过期，请重新登录");
        checkStatus(anRecord.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_FAILURE, true, "当前文件记录已经开始付款，不能删除");
        checkStatus(anRecord.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_SUCCESS, true, "当前文件记录已经开始付款，不能删除");
        checkStatus(anRecord.getBusinStatus(), CommissionConstantCollentions.COMMISSION_RECORD_BUSIN_STATUS_PAY, true, "当前文件记录进入付款流程中，不能删除");
        checkStatus(anRecord.getOperOrg(), anOperatorInfo.getOperOrg(), false, "你没有当前文件的删除权限");
        
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
     * 文件解析上传
     * @param anListData
     * @return
     */
    public List<CommissionRecord> saveRecordListWithMap(List<Map<String, Object>> anListData) {
        
        List<CommissionRecord> records=new ArrayList<>();
        int i=CommissionConstantCollentions.COMMISSION_FILE_BEGIN_ROW;
        try{
            
            for (Map<String, Object> dataMap : anListData) {
                i++;
                CommissionRecord record=new CommissionRecord();
                record.initResolveValue(dataMap,i,UserUtils.getOperatorInfo());
                //record.getAmount().multiply(record.getUnit())
                if(record.getBalance().compareTo(record.getUnit().multiply(new BigDecimal(record.getAmount())))!=0){
                    BTAssert.notNull(null,"第"+i+"行的数量为"+record.getAmount()+"  单价为："+record.getUnit() +" 总金额为： "+record.getBalance()+" ；数量*单价!=总金额");
                }
                this.insert(record);
                records.add(record);
            } 
            
        }catch(Exception e){
            BTAssert.notNull(null,e.getMessage());
        }
        
        return records;
    }
    
    /**
     * 佣金记录查询   
     * @param anMap 查询条件
     * @param anFlag  1：查询总的条数   2 不查询总的条数
     * @param anPageNum  当前页数
     * @param anPageSize  每页显示的总的数量
     * @return
     */
    public Page queryRecordList(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        
        BTAssert.notNull(anMap,"查询佣金记录条件为空");
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        //查询当前公司的佣金文件
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        
        Page<CommissionRecord> recordList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
       
        return recordList;
    }
    
    /**
     * 查询佣金记录详情
     * @param anRefNo
     * @return
     */
    public CommissionRecord findRecord(String anRefNo){
        
        BTAssert.notNull(anRefNo,"查询佣金记录条件为空");
        
        CommissionRecord record = this.selectOne(new CommissionRecord(anRefNo));
        
        BTAssert.notNull(record,"查询佣金记录为空");
        return   record;
        
    }
    
    public CommissionRecordAuditResult saveAuditRecordList(Map<String, Object> anMap){
        
        BTAssert.notNull(anMap,"审核佣金记录条件为空");
        BTAssert.notNull(anMap.get("importDate"),"审核佣金记录条件为空");
        BTAssert.notNull(anMap.get("custNo"),"审核佣金记录条件为空");
        CustOperatorInfo operatorInfo = UserUtils.getOperatorInfo();
        BTAssert.notNull(operatorInfo,"登录状态失效，请重新登录");
        logger.info("佣金记录批量审核 begin：  saveAuditRecordList    审核人为: "+operatorInfo.getName()); 
        String operOrg = baseService.findBaseInfo(Long.parseLong(anMap.get("custNo").toString())).getOperOrg();
        checkStatus(operOrg, operatorInfo.getOperOrg(), false, "你没有当前公司审核记录权限");
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        //查询未解析的文件(未解析文件 和解析失败未删除文件)
        List <CommissionFile> fileList=fileService.queryFileNoResolve(anMap);
        logger.info("佣金记录批量审核 ：  saveAuditRecordList  查询到未解析的文件 个数为："+fileList.size()); 
        if(!Collections3.isEmpty(fileList)){
            return CommissionRecordAuditResult.fail(fileList.size());
        }
        //查询到所有的到custNO  和导入日期并且是未审核的所有佣金记录
        anMap.put("payStatus", CommissionConstantCollentions.COMMISSION_PAY_STATUS_NO_HANDLE);
        anMap.put("businStatus",CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_NO_HANDLE);
        List<CommissionRecord> recordList = this.selectByClassProperty(CommissionRecord.class, anMap);
        if(Collections3.isEmpty(recordList)){
            return CommissionRecordAuditResult.fail("当前没有记录可供审核，当前记录已经审核完毕");
        }
        int recordAmount=0;
        BigDecimal blance=new BigDecimal(0);
        Set<Long> fileSet=new HashSet<>();
        for (CommissionRecord commissionRecord : recordList) {
            recordAmount++;
            blance= MathExtend.add(blance, commissionRecord.getBalance());
            fileSet.add(commissionRecord.getFileId());
            commissionRecord.saveAuditInit(operatorInfo);
            this.updateByPrimaryKeySelective(commissionRecord);
        }
        logger.info("佣金记录批量审核 ：  saveAuditRecordList  查询到未审核的记录 个数为："+recordAmount+"  :总的金额为："+blance+"   审核的文件数量为="+fileSet.size()); 
        //审核文件列表
      fileService.saveAuditFile(fileSet);
      
      return   CommissionRecordAuditResult.ok(fileSet.size(), recordAmount, blance);
        
    }
    
    
    /**
     * 审核单条记录信息
     * @param anRefNo
     * @return
     */
    public CommissionRecord saveAudtiRecord(String anRefNo){
        
        BTAssert.notNull(anRefNo,"审核佣金记录条件为空");
        
        CommissionRecord record = this.selectOne(new CommissionRecord(anRefNo));
        
        BTAssert.notNull(record,"审核佣金记录为空");
        //校验当前记录的状态
        checkAuditRecordStatus(record,UserUtils.getOperatorInfo());
        record.saveAuditInit(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(record);
        //尝试审核佣金文件
        attmeptAuditCommissionFile(record);
        
        return record;
        
    }


    /**
     * 当前记录的文件id  导入时间   拥有机构 没有未处理的单据则审核对应的佣金文件
     * @param anRecord
     */
    private void attmeptAuditCommissionFile(CommissionRecord anRecord) {
       
        Map<String,Object> queryParam = QueryTermBuilder.newInstance()
        .put("fileId", anRecord.getFileId())
        .put("importDate", anRecord.getImportDate())
        .put("operOrg", anRecord.getOperOrg())
        .put("NEid", anRecord.getId())
        .put("businStatus", CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_NO_HANDLE)
        .build();
        
        List<CommissionRecord> recordList = this.selectByClassProperty(CommissionRecord.class, queryParam);
        
        if(Collections3.isEmpty(recordList)){
            fileService.saveAuditFile(anRecord.getFileId());
        }
        
    }


    /**
     * 校验当前佣金记录是否符合审核条件
     * @param anRecord
     * @param anOperatorInfo
     * //业务状态 0 未处理 1 已审核(最终可供 拜特支付状态)  2已支付 3删除
     */
    private void checkAuditRecordStatus(CommissionRecord anRecord, CustOperatorInfo anOperatorInfo) {
        
        BTAssert.notNull(anOperatorInfo,"登录已过期，请重新登录");
        checkStatus(anRecord.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_FAILURE, true, "当前记录已经付款，不能重新审核");
        checkStatus(anRecord.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_SUCCESS, true, "当前记录已经付款，不能重新审核");
        checkStatus(anRecord.getBusinStatus(), CommissionConstantCollentions.COMMISSION_RECORD_BUSIN_STATUS_PAY, true, "当前记录已经付款，不能重新审核");
        checkStatus(anRecord.getBusinStatus(), CommissionConstantCollentions.COMMISSION_RECORD_BUSIN_STATUS_DELETE, true, "当前记录已经删除，不能重新审核");
        checkStatus(anRecord.getOperOrg(), anOperatorInfo.getOperOrg(), false, "你没有当前文件的审核权限");
        
    }


    public List<CommissionRecord> queryRecordListByFileId(Long anFileId) {
        
        Map<String,Object> map = QueryTermBuilder.newInstance()
        .put("fileId",anFileId)
        .build();
        return this.selectByProperty(map, "id Desc");
    }
    

    /**
     * @param anCustNo
     * @param anImportDate
     * @return
     */
    public Page<CommissionRecord> queryRecordListByImportDate(final Long anCustNo, final String anImportDate, final int anFlag, final int anPageNum, final int anPageSize) {
        final Map<String,Object> map = QueryTermBuilder.newInstance()
                .put("custNo",anCustNo)
                .put("importDate", anImportDate)
                .build();
        return this.selectPropertyByPage(map, anPageNum, anPageSize, anFlag == 1);
    }

    /**
     * @param anCustNo
     * @param anImportDate
     * @return
     */
    public Page<CommissionRecord> queryRecordListByImportDate(final Long anCustNo, final String anImportDate, final int anFlag, final int anPageNum, final int anPageSize) {
        final Map<String,Object> map = QueryTermBuilder.newInstance()
                .put("custNo",anCustNo)
                .put("importDate", anImportDate)
                .build();
        return this.selectPropertyByPage(map, anPageNum, anPageSize, anFlag == 1);
    }


}
