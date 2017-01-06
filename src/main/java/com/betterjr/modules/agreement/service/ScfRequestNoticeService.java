package com.betterjr.modules.agreement.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.mapper.CustDecimalJsonSerializer;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.service.FreemarkerService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.NumberToCN;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.acceptbill.entity.ScfAcceptBill;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.agreement.dao.ScfRequestNoticeMapper;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.agreement.entity.ScfRequestNotice;
import com.betterjr.modules.agreement.utils.SupplyChainUtil;

/**
 * 应收账款转让通知书管理
 * 
 * @author zhoucy
 *
 */
@Service
public class ScfRequestNoticeService extends BaseService<ScfRequestNoticeMapper, ScfRequestNotice> {

    private static final Logger logger = LoggerFactory.getLogger(ScfRequestNoticeService.class);

    @Autowired
    private FreemarkerService markerService;

    @Autowired
    private ScfElecAgreementService elecAgreeService;
    @Autowired
    private CustAccountService custAccountService;

    /**
     * 更新转让通知书信息，只有未签署合同的转让通知书才能更新
     * 
     * @param anRequest
     * @return
     */
    public String updateTransNotice(ScfRequestNotice anRequest, String anSuppiler,BigDecimal anBalance) {
        ScfRequestNotice tmpNotice = this.selectByPrimaryKey(anRequest.getRequestNo());
        boolean result = true;

        if (tmpNotice == null) {
            anRequest.setBuyer(custAccountService.queryCustName(anRequest.getBuyerNo()));
            anRequest.setFactorName(custAccountService.queryCustName(Long.parseLong(anRequest.getFactorNo())));
            this.insert(anRequest);
        }
        else if ("0".equals(tmpNotice.getTransStatus())) {
            anRequest.setBuyer(custAccountService.queryCustName(anRequest.getBuyerNo()));
            anRequest.setFactorName(tmpNotice.getFactorName());
            this.updateByPrimaryKey(anRequest);
        }
        else {
            result = false;
            logger.warn("updateTransNotice  the request " + anRequest + ", has used!");
        }

        // 表示需要更新电子合同签署信息
        if (result) {
            ScfElecAgreement elecAgreement = ScfElecAgreement.createByNotice(anRequest.getAgreeName(), anRequest.getNoticeNo(),
                    anBalance);
            elecAgreement.setSupplierNo(anRequest.getSupplierNo());
            elecAgreement.setRequestNo(anRequest.getRequestNo());
            elecAgreement.setBuyerNo(anRequest.getBuyerNo());
            elecAgreement.setFactorNo(anRequest.getFactorNo());
            elecAgreement.setSupplier(anSuppiler);
            elecAgreeService.addElecAgreementInfo(elecAgreement, Arrays.asList(elecAgreement.getSupplierNo()));

            return elecAgreement.getAgreeNo();
        }
        return null;
    }

    /**
     * 检查转移申请书是否可以更新
     * 
     * @param anRequestNo
     * @return
     */
    public boolean allowUpdate(String anRequestNo) {
        ScfRequestNotice requestNotice = this.selectByPrimaryKey(anRequestNo);
        if (requestNotice == null) {

            return true;
        }

        return "0".equals(requestNotice.getTransStatus());
    }
    
    /***
     * 根据申请单号查询转让通知书
     * @param requestNo
     * @return
     */
    public ScfRequestNotice findTransNotice(String requestNo){
        return this.selectByPrimaryKey(requestNo);
    }
    
    /***
     * 查询设置保理合同信息
     * @param noticeInfo
     * @return
     */
    public ScfRequestNotice findFactorAgreement(ScfRequestNotice noticeInfo){
        ScfElecAgreement elecAgreement= elecAgreeService.findFactorAgreementBySupplierNo(noticeInfo.getSupplierNo(), Long.parseLong(noticeInfo.getFactorNo()), "3");
        if(elecAgreement!=null){
            noticeInfo.setFactorAgreementNo(elecAgreement.getAgreeNo());
            noticeInfo.setFactorAgreementName(elecAgreement.getAgreeName());
        }
        noticeInfo.setSupplierName(custAccountService.queryCustName(noticeInfo.getSupplierNo()));
        if(BetterStringUtils.isBlank(noticeInfo.getBuyer())){
            noticeInfo.setBuyer(custAccountService.queryCustName(noticeInfo.getBuyerNo()));
        }
        return noticeInfo;
    }
    
    /***
     * 查询汇票信息
     * @param anRequestNo
     * @return
     */
    public Map<String, Object> findBillListByRequestNo(String anRequestNo){
        Map<String, Object> map = new HashMap();
        List<Map<String, Object>> billList=new ArrayList<Map<String,Object>>();
        List list=elecAgreeService.findBillListByRequestNo(anRequestNo);
        BigDecimal totalBalance=new BigDecimal(0);// 总金额 
        for(int i=0;i<list.size();i++){
            Map<String, Object> billMap=new HashMap<String, Object>();
            ScfAcceptBill scfAcceptBill=(ScfAcceptBill)list.get(i);
            billMap.put("billNo", scfAcceptBill.getBillNo());
            billMap.put("balance", CustDecimalJsonSerializer.format(scfAcceptBill.getBalance()));
            billMap.put("CapitalBalance", NumberToCN.number2CNMontrayUnit(scfAcceptBill.getBalance()));
            totalBalance=MathExtend.add(totalBalance,scfAcceptBill.getBalance());
            billList.add(billMap);
        }
        map.put("billList", billList);
        map.put("totalBalance", CustDecimalJsonSerializer.format(totalBalance));
        map.put("CapitalTotalBalance", NumberToCN.number2CNMontrayUnit(totalBalance));
        return map;
    }

}
