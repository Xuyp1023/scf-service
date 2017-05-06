package com.betterjr.modules.delivery.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.commission.entity.CommissionMonthlyStatement;
import com.betterjr.modules.commission.service.CommissionMonthlyStatementService;
import com.betterjr.modules.delivery.dao.DeliveryRecordMapper;
import com.betterjr.modules.delivery.data.DeliveryConstantCollentions;
import com.betterjr.modules.delivery.entity.DeliveryRecord;
import com.betterjr.modules.delivery.entity.DeliveryRecordStatement;

@Service
public class DeliveryRecordService extends BaseService<DeliveryRecordMapper, DeliveryRecord> {
    
    @Autowired
    private DeliveryRecordStatementService recordStatementService;
    
    @Autowired
    private CommissionMonthlyStatementService montylyService;
    
    /**
     * 查询快递状态详情
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @param isPostCust  true   是青海移动    false  是平台
     * @return
     * 投递日期   postDate
     * 接受企业(操作企业)：  postCustNo
     * 状态   businStatus
     */
    public Page<DeliveryRecord> queryDeliveryRecordList(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize,boolean isPostCust){
        
        BTAssert.notNull(anMap,"查询快递账单条件为空");
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        //查询当前公司的佣金文件
        if(isPostCust){
            //是核心企业查询
            if(!(anMap.containsKey("businStatus") && anMap.get("businStatus") !=null) ){
                List<String> businStatusList=new ArrayList<>();
                businStatusList.add(DeliveryConstantCollentions.DELIVERY_RECORD_BUSIN_STATUS_EXPRESS);
                businStatusList.add(DeliveryConstantCollentions.DELIVERY_RECORD_BUSIN_STATUS_CONFIRM);
                anMap.put("businStatus", businStatusList);
            }
            anMap.put("postOperOrg", UserUtils.getOperatorInfo().getOperOrg());
        }else{
            //是平台查询操作记录
            anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        }
        
        Page<DeliveryRecord> recordList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
        for (DeliveryRecord deliveryRecord : recordList) {
            List<DeliveryRecordStatement> list = recordStatementService.queryDeliveryStatementList(deliveryRecord.getRefNo());
            deliveryRecord.setRecordStatementList(list);
        }
        return recordList;
        
    }
    
    /**
     * 查询单条传递单据的详情
     * @param anRefNo
     * @return
     */
    public DeliveryRecord findDeliveryRecord(String anRefNo){
        
        BTAssert.notNull(anRefNo,"查询快递账单条件为空");
        DeliveryRecord record = this.selectOne(new DeliveryRecord(anRefNo));
        BTAssert.notNull(record,"未查询到快递账单信息");
        List<DeliveryRecordStatement> list = recordStatementService.queryDeliveryStatementList(record.getRefNo());
        record.setRecordStatementList(list);
        return  record;
    }
    
    /**
     * 新增快递账单记录
     * @param anMap
     * @return
     */
    public DeliveryRecord saveAddDeliveryRecord(Map<String,Object> anMap){
        
        BTAssert.notNull(anMap,"请选择月账单!");
        BTAssert.notNull(anMap.get("monthList"),"请选择月账单!");
        BTAssert.notNull(anMap.get("ownCustNo"),"请选择传递的企业");
        List<Long> monthlyStatementIdList = getMonthlyStatementRefNoList(anMap.get("monthList").toString());
        //查询当前公司的佣金文件
        Map<String,Object> queryMap = QueryTermBuilder.newInstance().put("operOrg", UserUtils.getOperatorInfo().getOperOrg())
        .put("id", monthlyStatementIdList)
        .put("businStatus", "2")
        .put("ownCustNo", anMap.get("ownCustNo"))
        .build();
        //获取到月账单详情
        List<CommissionMonthlyStatement> monthlyStatementList = montylyService.saveQueryMonthlyStatement(queryMap);
        
        DeliveryRecord record=convertToDeliveryRecord(monthlyStatementList);
        List<DeliveryRecordStatement> statementByList = recordStatementService.saveAddStatementByList(record.getRecordStatementList());
        this.insert(record);
        record.setRecordStatementList(statementByList);
        return record;
        
    }
    
    /**
     * 快递详情明细账单需要移除操作
     * @param anStatementId
     * @return
     */
    public DeliveryRecord saveModifyRecordByStatementId(Long anStatementId){
        
        BTAssert.notNull(anStatementId,"修改的条件不正确");
        DeliveryRecordStatement statement = recordStatementService.selectOne(new DeliveryRecordStatement(anStatementId));
        BTAssert.notNull(statement,"修改的条件不正确");
        String refNo = statement.getDeliverRefNo();//快递主表id
        Long monthlyId = statement.getMonthlyStatementId();//月账单id
        
        //1更新快递账单详情信息
        statement.setBusinStatus(DeliveryConstantCollentions.DELIVERY_STATEMENT_BUSIN_STATUS_DELETE);
        recordStatementService.updateByPrimaryKeySelective(statement);
        
        //2更新返回前端数据快递主表信息
        DeliveryRecord record = findDeliveryRecord(refNo);
        BTAssert.notNull(record.getRecordStatementList(),"投递的账单只剩下最后一张月账单，不允许删除");
        //如果快递已经投递 或者已经确认，不允许移除
        checkStatus(record.getBusinStatus(), DeliveryConstantCollentions.DELIVERY_RECORD_BUSIN_STATUS_EXPRESS, true, "当前账单已经投递，不允许移除");
        checkStatus(record.getBusinStatus(), DeliveryConstantCollentions.DELIVERY_RECORD_BUSIN_STATUS_CONFIRM, true, "当前账单已经投递并确认，不允许移除");
        for(DeliveryRecordStatement statement2:record.getRecordStatementList()){
            if(statement2.getId().equals(anStatementId)){
                record.getRecordStatementList().remove(statement2);
                break;
            }
        }
        
        //3 更新月账单状态信息
        montylyService.saveMonthlyStatement(monthlyId, "2");
        
        return record;
        
    }
    
    /**
     * 投递月账单信息
     * @param anRefNo
     * @return
     */
    public DeliveryRecord saveExpressRecordById(String anRefNo){
        
        BTAssert.notNull(anRefNo,"投递的账单不正确");
        
        DeliveryRecord record = findDeliveryRecord(anRefNo);
        checkStatus(record.getBusinStatus(), DeliveryConstantCollentions.DELIVERY_RECORD_BUSIN_STATUS_EXPRESS, true, "当前账单已经投递，请不要重复投递");
        checkStatus(record.getBusinStatus(), DeliveryConstantCollentions.DELIVERY_RECORD_BUSIN_STATUS_CONFIRM, true, "当前账单已经投递并确认，请不要重复投递");
        checkStatus(record.getOperOrg(), UserUtils.getOperatorInfo().getOperOrg(), false, "你没有投递当前账单的权限");
        //1 更新月账单的状态
        List<Long> monthlyIdList=getMonthlyStatementIdList(record);
        for (Long monthlyId : monthlyIdList) {
            montylyService.saveMonthlyStatement(monthlyId, "4");
        }
        record.saveExpressInit(UserUtils.getOperatorInfo());
        //更新自己的快递信息
        this.updateByPrimaryKeySelective(record);
        
        return record;
        
        
    }
    
    /**
     * 核心企业确认快递账单
     * @param anRefNo
     * @return
     */
    public DeliveryRecord saveConfirmRecordByRefNo(String anRefNo){
        
        BTAssert.notNull(anRefNo,"确认的账单不正确");
        DeliveryRecord record = this.selectOne(new DeliveryRecord(anRefNo));
        checkStatus(record.getBusinStatus(), DeliveryConstantCollentions.DELIVERY_RECORD_BUSIN_STATUS_CONFIRM, true, "当前账单已经投递并确认，请不要重复确认");
        checkStatus(record.getPostOperOrg(), UserUtils.getOperatorInfo().getOperOrg(), false, "你没有确认当前账单的权限");
        record.saveConfirmInit(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(record);
        return record;
        
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
    
    private List<Long> getMonthlyStatementIdList(DeliveryRecord anRecord) {
        
        BTAssert.notNull(anRecord,"投递的账单不正确");
        BTAssert.notNull(anRecord.getRecordStatementList(),"投递的账单不正确,没有详细月账单信息");
        List<Long> idList=new ArrayList<Long>();
        for(DeliveryRecordStatement statement:anRecord.getRecordStatementList()){
            idList.add(statement.getMonthlyStatementId());
        }
        return idList;
    }

    /**
     * 将月账单转换成快递账单pojo
     * @param anMonthlyStatementList
     * @return
     */
    private DeliveryRecord convertToDeliveryRecord(List<CommissionMonthlyStatement> anMonthlyStatementList) {
        
        DeliveryRecord record=new DeliveryRecord();
        record.saveAddInit(UserUtils.getOperatorInfo(),UserUtils.getDefCustInfo(),anMonthlyStatementList.get(0));
        List<DeliveryRecordStatement> statementList=new ArrayList<>();
        for (CommissionMonthlyStatement monthly : anMonthlyStatementList) {
            DeliveryRecordStatement statement=new DeliveryRecordStatement();
            statement.setDeliverId(record.getId());
            statement.setDeliverRefNo(record.getRefNo());
            statement.setMonthlyStatementId(monthly.getId());
            statement.setMonthlyStatementRefNo(monthly.getRefNo());
            statement.setOwnCustName(monthly.getOwnCustName());
            statement.setOwnCustNo(monthly.getOwnCustNo());
            statement.setOwnOperOrg(monthly.getOwnOperOrg());
            statement.setPayBeginDate(monthly.getPayBeginDate());
            statement.setPayEndDate(monthly.getPayEndDate());
            statement.setPayTotalFailureBlance(monthly.getPayFailureBalance());
            statement.setPayTotalFailureitems(monthly.getPayFailureAmount());
            statement.setPayTotalSuccessBlance(monthly.getPayTotalBalance());
            statement.setPayTotalSuccessitems(monthly.getPayTotalAmount());
            statement.setTotalAmount(monthly.getTotalAmount());
            statement.setTotalBlance(monthly.getTotalBalance());
            statementList.add(statement);
        }
        record.setRecordStatementList(statementList);
        return record;
    }

    public List<Long> getMonthlyStatementRefNoList(String anValue){
        
        List<Long> refNoList=new ArrayList<>();
        if(BetterStringUtils.isNotBlank(anValue)){
            
            if(anValue.contains(",")){
                String[] split = anValue.split(",");
                for (String string : split) {
                    refNoList.add(Long.parseLong(string.trim())); 
                }
            }else{
                refNoList.add(Long.parseLong(anValue.trim())); 
            }
            
        }
        return refNoList;
    }

}
