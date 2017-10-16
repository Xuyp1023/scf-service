package com.betterjr.modules.agreement.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.agreement.dao.ScfAgreementTypeMapper;
import com.betterjr.modules.agreement.entity.ScfAgreementType;

/**
 * 合同类型Service
 * 
 * @author wudy
 */
@Service
public class ScfAgreementTypeService extends BaseService<ScfAgreementTypeMapper, ScfAgreementType> {

    /**
     * 合同类型登记
     */
    public ScfAgreementType addAgreementType(ScfAgreementType anAgreementType) {
        logger.info("合同类型登记");
        BTAssert.isTrue(UserUtils.platformUser(), "无权限进行操作！");
        anAgreementType.initAddValue(UserUtils.getOperatorInfo());
        this.insert(anAgreementType);
        return anAgreementType;
    }

    /**
     * 查询已登记合同类型
     */
    public Page<ScfAgreementType> queryRegisteredAgreementType(int anPageNum, int anPageSize, String anFlag) {
        Map<String, Object> anMap = QueryTermBuilder.newInstance().build();
        // 状态 0登记 1生效
        anMap.put("businStatus", "0");
        // 数据来源:0:默认,1:手动录入
        anMap.put("dataSource", "1");
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "agreementTypeNo");
    }

    /**
     * 编辑合同类型
     */
    public ScfAgreementType saveModifyAgreementType(ScfAgreementType anModiAgreementType, Long anId) {
        ScfAgreementType anAgreementType = this.selectByPrimaryKey(anId);
        checkOperator(anAgreementType.getOperOrg(), "无权限进行操作！");
        BTAssert.notNull(anAgreementType, "无法获取对应合同类型！");
        // 仅登记状态下可以编辑
        if (!StringUtils.equals("0", anAgreementType.getBusinStatus())) {
            throw new BytterTradeException(40001, "无权限进行操作！！");
        }
        anAgreementType.initModifyValue(anModiAgreementType, UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(anAgreementType);
        return anAgreementType;
    }

    /**
     * 删除合同类型
     */
    public int saveDeleteAgreementType(Long anId) {
        ScfAgreementType anAgreementType = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAgreementType, "无法获取对应合同类型！");
        checkOperator(anAgreementType.getOperOrg(), "无权限进行操作！");
        // 仅登记状态下可以编辑
        if (!StringUtils.equals("0", anAgreementType.getBusinStatus())) {
            throw new BytterTradeException(40001, "无权限进行操作！");
        }
        return this.delete(anAgreementType);
    }

    /**
     * 合同类型待审核查询
     */
    public Page<ScfAgreementType> queryUnEnableAgreementType(int anPageNum, int anPageSize, String anFlag) {
        Map<String, Object> anMap = QueryTermBuilder.newInstance().build();
        // 状态 0登记 1生效
        anMap.put("businStatus", "0");
        // 数据来源:0:默认,1:手动录入
        anMap.put("dataSource", "1");
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "agreementTypeNo");
    }

    /**
     * 已启用合同类型查询
     */
    public List<ScfAgreementType> findEnableAgreementType() {
        Map<String, Object> anMap = QueryTermBuilder.newInstance().build();
        // 状态 0登记 1生效
        anMap.put("businStatus", "1");
        return this.selectByProperty(anMap);
    }

    /**
     * 启用合同类型
     */
    public ScfAgreementType saveEnableAgreementType(Long anId) {
        ScfAgreementType anAgreementType = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAgreementType, "无法获取对应合同类型！");
        checkOperator(anAgreementType.getOperOrg(), "无权限进行操作！");
        // 设置状态启用, 0登记 1生效
        anAgreementType.setBusinStatus("1");
        anAgreementType.initAuditValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(anAgreementType);
        return anAgreementType;
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

    /**
     * 合同类型分页查询
     */
    public Page<ScfAgreementType> queryAgreementType(Map<String, Object> anMap, int anPageNum, int anPageSize,
            String anFlag) {
        Map<String, Object> queryMap = Collections3.fuzzyMap(anMap, new String[] { "businStatus" });
        return this.selectPropertyByPage(queryMap, anPageNum, anPageSize, "1".equals(anFlag), "agreementTypeNo");
    }
}