package com.betterjr.modules.agreement.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.betterjr.modules.agreement.dao.ScfAgreementStandardMapper;
import com.betterjr.modules.agreement.entity.ScfAgreementStandard;
import com.betterjr.modules.agreement.entity.ScfAgreementType;

/**
 * 标准合同
 * 
 * @author wudy
 *
 */
@Service
public class ScfAgreementStandardService extends BaseService<ScfAgreementStandardMapper, ScfAgreementStandard> {

    @Autowired
    private ScfAgreementTypeService agreementTypeService;

    /**
     * 标准合同登记
     */
    public ScfAgreementStandard addAgreementStandard(ScfAgreementStandard anAgreementStandard) {
        logger.info("标准合同登记");
        BTAssert.isTrue(UserUtils.platformUser(), "无权限进行操作！");
        anAgreementStandard.initAddValue(UserUtils.getOperatorInfo());
        this.insert(anAgreementStandard);
        return anAgreementStandard;
    }

    /**
     * 查询已登记标准合同
     */
    public Page<ScfAgreementStandard> queryRegisteredAgreementStandard(int anPageNum, int anPageSize, String anFlag) {
        Map<String, Object> anMap = QueryTermBuilder.newInstance().build();
        // 状态 0登记 1生效
        anMap.put("businStatus", "0");
        Page<ScfAgreementStandard> resultList = this.selectPropertyByPage(anMap, anPageNum, anPageSize,
                "1".equals(anFlag), "agreementStandardNo");
        // 填充合同类型名称
        for (ScfAgreementStandard anAgreement : resultList) {
            ScfAgreementType anAgreementType = agreementTypeService
                    .selectByPrimaryKey(anAgreement.getAgreementTypeId());
            anAgreement.setAgreementTypeName(anAgreementType.getAgreementTypeName());
        }
        return resultList;
    }

    /**
     * 标准合同修改
     */
    public ScfAgreementStandard saveModifyAgreementStandard(ScfAgreementStandard anModiAgreementStandard, Long anId) {
        ScfAgreementStandard anAgreementStandard = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAgreementStandard, "无法获取对应标准合同");
        checkOperator(anAgreementStandard.getOperOrg(), "无权限进行操作！");
        // 仅登记状态下可以编辑
        if (!StringUtils.equals("0", anAgreementStandard.getBusinStatus())) {
            throw new BytterTradeException(40001, "无权限进行操作！");
        }
        anAgreementStandard.initModifyValue(anModiAgreementStandard, UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(anAgreementStandard);
        return anAgreementStandard;
    }

    /**
     * 删除标准合同
     */
    public int saveDeleteAgreementStandard(Long anId) {
        ScfAgreementStandard agreementStandard = this.selectByPrimaryKey(anId);
        BTAssert.notNull(agreementStandard, "无法获取对应标准合同");
        checkOperator(agreementStandard.getOperOrg(), "无权限进行操作！");
        // 仅登记状态下可以编辑
        if (!StringUtils.equals("0", agreementStandard.getBusinStatus())) {
            throw new BytterTradeException(40001, "无权限进行操作！");
        }
        return this.delete(agreementStandard);
    }

    /**
     * 标准合同审核查询
     */
    public Page<ScfAgreementStandard> queryAgreementStandardByStatus(String anBusinStatus, int anPageNum,
            int anPageSize, String anFlag) {
        Map<String, Object> anMap = QueryTermBuilder.newInstance().build();
        // 状态 0登记 1生效
        anMap.put("businStatus", anBusinStatus);
        anMap = Collections3.fuzzyMap(anMap, new String[] { "businStatus" });
        Page<ScfAgreementStandard> resultList = this.selectPropertyByPage(anMap, anPageNum, anPageSize,
                "1".equals(anFlag), "agreementStandardNo");
        // 填充合同类型名称
        for (ScfAgreementStandard anAgreement : resultList) {
            ScfAgreementType anAgreementType = agreementTypeService
                    .selectByPrimaryKey(anAgreement.getAgreementTypeId());
            anAgreement.setAgreementTypeName(anAgreementType.getAgreementTypeName());
        }
        return resultList;
    }

    /**
     * 启用标准合同
     */
    public ScfAgreementStandard saveEnableAgreementStandard(Long anId) {
        ScfAgreementStandard agreementStandard = this.selectByPrimaryKey(anId);
        BTAssert.notNull(agreementStandard, "无法获取对应标准合同");
        checkOperator(agreementStandard.getOperOrg(), "无权限进行操作！");
        // 仅登记、停用状态下可以编辑
        if (!StringUtils.equals("0", agreementStandard.getBusinStatus())
                && !StringUtils.equals("2", agreementStandard.getBusinStatus())) {
            throw new BytterTradeException(40001, "无权限进行操作！");
        }
        // 设置状态启用,状态 0登记 1生效
        agreementStandard.setBusinStatus("1");
        // 审核日期
        agreementStandard.initAuditValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(agreementStandard);
        return agreementStandard;
    }

    /**
     * 停用标准合同
     */
    public ScfAgreementStandard saveDisableAgreementStandard(Long anId) {
        ScfAgreementStandard agreementStandard = this.selectByPrimaryKey(anId);
        BTAssert.notNull(agreementStandard, "无法获取对应标准合同");
        checkOperator(agreementStandard.getOperOrg(), "无权限进行操作！");
        // 仅登记、停用状态下可以编辑
        if (!StringUtils.equals("1", agreementStandard.getBusinStatus())) {
            throw new BytterTradeException(40001, "无权限进行操作！");
        }
        // 设置状态启用 状态 0登记 1生效
        agreementStandard.setBusinStatus("2");
        agreementStandard.initDisableValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(agreementStandard);
        return agreementStandard;
    }

    /**
     * 标准合同查询
     */
    public Page<ScfAgreementStandard> queryAgreementStandard(Map<String, Object> anMap, int anPageNum, int anPageSize,
            String anFlag) {
        Map<String, Object> queryMap = Collections3.fuzzyMap(anMap, new String[] { "businStatus", "agreementTypeId" });
        Page<ScfAgreementStandard> resultList = this.selectPropertyByPage(queryMap, anPageNum, anPageSize,
                "1".equals(anFlag), "agreementStandardNo");
        // 填充合同类型名称
        for (ScfAgreementStandard anAgreement : resultList) {
            ScfAgreementType anAgreementType = agreementTypeService
                    .selectByPrimaryKey(anAgreement.getAgreementTypeId());
            anAgreement.setAgreementTypeName(anAgreementType.getAgreementTypeName());
        }
        return resultList;
    }

    /**
     * 检查用户是否有权限操作数据
     */
    private void checkOperator(String anOperOrg, String anMessage) {
        BTAssert.isTrue(UserUtils.platformUser(), "无权限进行操作！");
        if (StringUtils.equals(UserUtils.getOperatorInfo().getOperOrg(), anOperOrg) == false) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }
}
