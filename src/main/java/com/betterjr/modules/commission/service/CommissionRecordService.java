package com.betterjr.modules.commission.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.commission.dao.CommissionRecordMapper;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;
import com.betterjr.modules.commission.entity.CommissionRecord;

@Service
public class CommissionRecordService extends BaseService<CommissionRecordMapper, CommissionRecord> {

    
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

}
