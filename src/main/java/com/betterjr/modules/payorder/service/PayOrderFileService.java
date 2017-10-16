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
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.payorder.dao.PayOrderFileMapper;
import com.betterjr.modules.payorder.data.PayOrderFileConstantCollentions;
import com.betterjr.modules.payorder.data.PayOrderPoolRecordConstantCollentions;
import com.betterjr.modules.payorder.entity.PayOrderFile;
import com.betterjr.modules.payorder.entity.PayOrderPoolRecord;

@Service
public class PayOrderFileService extends BaseService<PayOrderFileMapper, PayOrderFile> {

    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;
    
    @Autowired
    private PayOrderPoolRecordService recordService;
    
    /**
     * 通过付款日期生成下载文件
     * @param anRequestPayDate
     * @param anFlag 是否需要插入到数据库
     * @return
     */
    public PayOrderFile saveAddFile(String anRequestPayDate, String anFlag){
        
        BTAssert.hasLength(anRequestPayDate, "付款日期为空,操作失败");
        BTAssert.hasLength(anFlag, "查询条件为空,操作失败");
        
        List<PayOrderPoolRecord> recordList = QueryPayRecordList(anRequestPayDate);
        
        PayOrderFile file = createPayOrderFileByRecordList(recordList, anFlag, PayOrderFileConstantCollentions.PAY_FILE_INFO_TYPE_DOWNREQUESTFILE);
        
        return file;
        
        
    }

    private PayOrderFile createPayOrderFileByRecordList(List<PayOrderPoolRecord> anRecordList, String anFlag, String anInfoType) {
        
        PayOrderFile file=new PayOrderFile().saveAddInitValue(anInfoType);
        
        for (PayOrderPoolRecord payOrderPoolRecord : anRecordList) {
            
        }
        
        return null;
    }

    /**
     * 查询当天可以付款的申请信息
     * @param anRequestPayDate
     * @return
     */
    private List<PayOrderPoolRecord> QueryPayRecordList(String anRequestPayDate) {
        
        Map map = QueryTermBuilder.newInstance()
            .put("requestPayDate", anRequestPayDate)
            .put("businStatus", PayOrderPoolRecordConstantCollentions.PAY_RECORD_BUSIN_STATUS_NOHANDLE)
            .put("factoryNo", queryCurrentUserCustNos())
            .build();
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
