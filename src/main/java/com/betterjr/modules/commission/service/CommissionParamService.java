package com.betterjr.modules.commission.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.commission.dao.CommissionParamMapper;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;
import com.betterjr.modules.commission.entity.CommissionParam;
import com.betterjr.modules.customer.ICustMechBaseService;

@Service
public class CommissionParamService extends BaseService<CommissionParamMapper, CommissionParam> {

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;

    @Autowired
    private CustAccountService custAccountService;

    /**
     * 新增发票参数设置表信息
     * @param anParam
     * @return
     */
    public CommissionParam saveAddParam(CommissionParam anParam) {

        BTAssert.notNull(anParam, "新增佣金参数失败！数据为空");
        BTAssert.notNull(anParam.getCustNo(), "新增佣金参数失败！数据为空");
        BTAssert.notNull(anParam.getCoreCustNo(), "新增佣金参数失败！数据为空");
        BTAssert.notNull(anParam.getTaxRate(), "新增佣金参数失败！数据为空");
        BTAssert.notNull(anParam.getInterestRate(), "新增佣金参数失败！数据为空");
        logger.info("Begin to add saveAddParam 数据为：" + anParam + " 操作用户为：" + UserUtils.getOperatorInfo().getName());
        CommissionParam param = findParamByCustNo(anParam.getCustNo(), anParam.getCoreCustNo());
        BTAssert.isNull(param, "当前企业已经有生效的记录，不能重复添加");
        anParam.setCustName(custAccountService.queryCustName(anParam.getCustNo()));
        anParam.setCoreCustName(custAccountService.queryCustName(anParam.getCoreCustNo()));
        anParam.initAddValue(UserUtils.getOperatorInfo());
        this.insertSelective(anParam);
        logger.info("end to add saveAddParam 数据为：" + anParam + " 操作用户为：" + UserUtils.getOperatorInfo().getName());
        return anParam;
    }

    /**
     * 通过平台id和核心企业Id 查询参数配置信息
     * @param anCustNo  平台id
     * @param anCoreCustNo  核心企业Id
     * @return
     */
    public CommissionParam findParamByCustNo(Long anCustNo, Long anCoreCustNo) {

        Map anMap = QueryTermBuilder.newInstance().put("custNo", anCustNo).put("coreCustNo", anCoreCustNo)
                .put("businStatus", CommissionConstantCollentions.COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_OK).build();

        List<CommissionParam> list = this.selectByProperty(anMap);
        // BTAssert.notEmpty(list,"请配置企业的默认利率，税率");
        return Collections3.getFirst(list);

    }

    /**
     * 参数配置信息修改
     * @param anParam
     * @return
     */
    public CommissionParam saveUpdateParam(CommissionParam anParam) {

        BTAssert.notNull(anParam, "修改佣金参数失败！数据为空");
        BTAssert.notNull(anParam.getId(), "修改佣金参数失败！数据为空");
        BTAssert.notNull(anParam.getInterestRate(), "修改佣金参数失败！数据为空");
        BTAssert.notNull(anParam.getTaxRate(), "修改佣金参数失败！数据为空");
        CommissionParam param = this.selectByPrimaryKey(anParam.getId());
        BTAssert.notNull(anParam, "修改佣金参数失败！未查询到参数配置信息");
        logger.info("Begin to add saveUpdateParam 数据为：" + anParam + " 操作用户为：" + UserUtils.getOperatorInfo().getName());
        checkStatus(param.getBusinStatus(),
                CommissionConstantCollentions.COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_FAILER, true, "当前参数已经失效，无法修改");
        if (!getCurrentUserCustNos().contains(param.getCustNo())) {

            BTAssert.notNull(null, "修改佣金参数失败！你没有当前参数的操作权限");
        }
        param.setInterestRate(anParam.getInterestRate());
        param.setTaxRate(anParam.getTaxRate());
        param.saveUpdateValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(param);

        logger.info("end to add saveUpdateParam 数据为：" + param + " 操作用户为：" + UserUtils.getOperatorInfo().getName());
        return param;

    }

    /**
     * 删除佣金参数记录
     * @param anParamId
     * @return
     */
    public CommissionParam saveDeleteParam(Long anParamId) {

        BTAssert.notNull(anParamId, "删除佣金参数失败！数据为空");

        CommissionParam param = this.selectByPrimaryKey(anParamId);
        BTAssert.notNull(param, "删除佣金参数失败！未查询到参数配置信息");
        logger.info("Begin to add saveDeleteParam 数据为：" + param + " 操作用户为：" + UserUtils.getOperatorInfo().getName());

        if (!getCurrentUserCustNos().contains(param.getCustNo())) {

            BTAssert.notNull(null, "删除佣金参数失败！你没有当前参数的操作权限");
        }
        param.setBusinStatus(CommissionConstantCollentions.COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_FAILER);
        this.updateByPrimaryKeySelective(param);

        logger.info("end to add saveDeleteParam 数据为：" + param + " 操作用户为：" + UserUtils.getOperatorInfo().getName());
        return param;

    }

    /**
     * 查询当前平台下所有的核心企业的参数配置信息
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<CommissionParam> queryParamList(Map<String, Object> anMap, String anFlag, int anPageNum,
            int anPageSize) {

        BTAssert.notNull(anMap, "查询佣金参数失败！条件为空");
        BTAssert.notNull(anMap.get("custNo"), "查询佣金参数失败！条件为空");
        // 去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        anMap.put("businStatus", CommissionConstantCollentions.COMMISSION_INVOICE_PARAM_CUST_BUSINSTATUS_OK);
        Page<CommissionParam> page = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag),
                "id desc");

        return page;

    }

    /**
     * 检查状态信息
     */
    public void checkStatus(String anBusinStatus, String anTargetStatus, boolean anFlag, String anMessage) {
        if (StringUtils.equals(anBusinStatus, anTargetStatus) == anFlag) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

    /**
     * 获取当前登录用户所在的所有公司id集合
     * @return
     */
    private Collection<Long> getCurrentUserCustNos() {

        CustOperatorInfo operInfo = UserUtils.getOperatorInfo();
        BTAssert.notNull(operInfo, "查询参数配置失败!请先登录");
        Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
        BTAssert.notNull(custInfos, "查询参数配置失败!获取当前企业失败");
        Collection<Long> custNos = new ArrayList<>();
        for (CustInfo custInfo : custInfos) {
            custNos.add(custInfo.getId());
        }
        return custNos;
    }

}
