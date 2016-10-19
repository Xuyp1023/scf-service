package com.betterjr.modules.loan.service;

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
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfRequestApprovedMapper;
import com.betterjr.modules.loan.entity.ScfRequest;
import com.betterjr.modules.loan.entity.ScfRequestScheme;
import com.betterjr.modules.loan.helper.RequestTradeStatus;
import com.betterjr.modules.loan.helper.RequestType;

@Service
public class ScfRequestSchemeService extends BaseService<ScfRequestApprovedMapper, ScfRequestScheme> {

    @Autowired
    private ScfRequestService requestService;

    @Autowired
    private CustAccountService custAccountService;

    /**
     * 保存贷款方案
     * 
     * @param anScheme
     * @return
     */
    public ScfRequestScheme addScheme(ScfRequestScheme anScheme) {
        BTAssert.notNull(anScheme, "保存贷款方案失败-anScheme不能为空");

        // 将原有方案 设置为作废再 添加新的方案
        List<ScfRequestScheme> oldSchemeList = this.selectByProperty("requestNo", anScheme.getRequestNo());
        for (ScfRequestScheme scheme : oldSchemeList) {
            scheme.setCustAduit("2");
            scheme.setCoreCustAduit("2");
            scheme.initModify(scheme);
            this.updateByPrimaryKeySelective(scheme);
        }

        // 初始化相关企业编号
        ScfRequest request = requestService.findRequestDetail(anScheme.getRequestNo());
        BTAssert.notNull(request.getRequestNo(), "保存贷款方案失败-没找到相应的申请单");
        anScheme.setFactorNo(request.getFactorNo());
        anScheme.setCoreCustNo(request.getCoreCustNo());
        anScheme.setCustNo(request.getCustNo());
        anScheme.init(anScheme);
        this.insert(anScheme);

        return findSchemeDetail(anScheme.getRequestNo());
    }

    /**
     * 获取融资审批详情
     * 
     * @param anRequestNo
     * @param anFactorNo
     * @return
     */
    public ScfRequestScheme findSchemeDetail(String anRequestNo) {
        List<ScfRequestScheme> schemeList = this.selectByProperty("requestNo", anRequestNo);
        if (Collections3.isEmpty(schemeList)) {
            logger.debug("无数据！");
            return new ScfRequestScheme();
        }

        // 设置相关企业名称
        ScfRequestScheme scheme = Collections3.getFirst(schemeList);
        fillCustName(scheme);
        return scheme;
    }

    /**
     * 获取融资审批详情(无数据返回null)
     * 
     * @param anRequestNo
     * @param anFactorNo
     * @return
     */
    public ScfRequestScheme findSchemeDetail2(String anRequestNo) {
        List<ScfRequestScheme> schemeList = this.selectByProperty("requestNo", anRequestNo);
        if (Collections3.isEmpty(schemeList) || schemeList.size() == 0) {
            logger.debug("无数据！");
            return null;
        }

        ScfRequestScheme scheme = Collections3.getFirst(schemeList);
        fillCustName(scheme);
        return scheme;
    }

    /**
     * 修改贷款方案
     * 
     * @param rquestNo
     * @param factorNo
     * @return
     */
    public ScfRequestScheme saveModifyScheme(ScfRequestScheme anScheme) {
        BTAssert.notNull(anScheme, "修改贷款方案失败-anScheme不能为空");

        List<ScfRequestScheme> list = this.selectByProperty("requestNo", anScheme.getRequestNo());
        if (Collections3.isEmpty(list)) {
            throw new BytterTradeException(40001, "修改贷款方案失败，找不到源数据-requestNo:" + anScheme.getRequestNo());
        }

        // 修改最新的一条
        anScheme.setId(Collections3.getFirst(list).getId());
        this.updateByPrimaryKeySelective(anScheme);
        return findSchemeDetail(anScheme.getRequestNo());
    }

    /**
     * (申请企业/核心企业)查询融资审批
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<ScfRequestScheme> querySchemeList(Map<String, Object> anMap, int anFlag, int anPageNum, int anPageSize) {
        String[] queryTerm = new String[] {"custNo", "requestType", "factorNo", "coreCustNo"};
        anMap = Collections3.filterMap(anMap, queryTerm);
        Map<String, Object> qyReqyestMap = QueryTermBuilder.newInstance().addAll(anMap).build();
        Map<String, Object> qySchemeMap = QueryTermBuilder.newInstance().addAll(anMap).build();
        //查询待批 并且 指定类型（经销商/其它），(属于该核心企业要审批，申请单状态状态为：背景确认)
        if(BetterStringUtils.equals(RequestType.SELLER.getCode(), anMap.get("requestType").toString())){
            qyReqyestMap.put("requestType", RequestType.SELLER.getCode());
        }else{
            String[] requestType = new String[]{RequestType.ORDER.getCode(),RequestType.BILL.getCode(), RequestType.RECEIVABLE.getCode()};
            qyReqyestMap.put("requestType", requestType);
        }
        
        // 核心企业查询时
        if (UserUtils.coreUser()) {
            qyReqyestMap.put("GTEtradeStatus", RequestTradeStatus.CONFIRM_TRADING.getCode());
           
            // 去除未到审批时的数据
            qySchemeMap.put("coreCustAduit", new String[]{"0", "1"} );
        }
        
        List<ScfRequest> requestList =  requestService.selectByClassProperty(ScfRequest.class, qyReqyestMap);
        if(Collections3.isEmpty(requestList)){
            return new Page(anPageNum, anPageSize, 1 == anFlag);
        }
        
        List<String> requestsNo = new ArrayList<String>();
        for (ScfRequest scfRequest : requestList) {
            requestsNo.add(scfRequest.getRequestNo());
        }
        
        qySchemeMap.put("requestNo", requestsNo.toArray());
        qySchemeMap.remove("requestType");
        Page<ScfRequestScheme> page = this.selectPropertyByPage(qySchemeMap, anPageNum, anPageSize, 1 == anFlag);
        for (ScfRequestScheme scheme : page) {
            // 设置相关企业名
            fillCustName(scheme);
        }

        return page;
    }

    /**
     * 设置相关企业名称
     * 
     * @param scheme
     */
    private void fillCustName(ScfRequestScheme scheme) {
        scheme.setCustName(custAccountService.queryCustName(scheme.getCustNo()));
        scheme.setCoreCustName(custAccountService.queryCustName(scheme.getCoreCustNo()));
        scheme.setFactorName(custAccountService.queryCustName(scheme.getFactorNo()));
    }

}
