package com.betterjr.modules.agreement.service;

import java.util.List;
import java.util.Map;

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
        BTAssert.isTrue(UserUtils.platformUser(), "仅平台有权限操作合同类型！");
        anAgreementType.initAddValue(UserUtils.getOperatorInfo());
        this.insert(anAgreementType);
        return anAgreementType;
    }

    /**
     * 查询已登记合同类型
     */
    public Page<ScfAgreementType> queryRegisteredAgreementType(int anPageNum, int anPageSize, String anFlag) {
        Map<String, Object> anMap = QueryTermBuilder.newInstance().build();
        anMap.put("businStatus", "0");
        // 手动录入
        anMap.put("dataSource", "1");
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
    }

    /**
     * 编辑合同类型
     */
    public ScfAgreementType saveModifyAgreementType(ScfAgreementType anModiAgreementType, Long anId) {
        ScfAgreementType anAgreementType = this.selectByPrimaryKey(anId);
        checkOperator(anAgreementType.getOperOrg(), "当前操作员无法编辑合同类型");
        BTAssert.notNull(anAgreementType, "无法获取对应合同类型");
        // 仅登记状态下可以编辑
        if (!BetterStringUtils.equals("0", anAgreementType.getBusinStatus())) {
            throw new BytterTradeException(40001, "仅登记状态下的合同可编辑！");
        }
        anModiAgreementType.setId(anId);
        anModiAgreementType.initModifyValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(anModiAgreementType);
        return anModiAgreementType;
    }

    /**
     * 删除合同类型
     */
    public int saveDeleteAgreementType(Long anId) {
        ScfAgreementType anAgreementType = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAgreementType, "无法获取对应合同类型");
        checkOperator(anAgreementType.getOperOrg(), "当前操作员无法删除合同类型");
        // 仅登记状态下可以编辑
        if (!BetterStringUtils.equals("0", anAgreementType.getBusinStatus())) {
            throw new BytterTradeException(40001, "仅登记状态下的合同可删除！");
        }
        return this.delete(anAgreementType);
    }

    /**
     * 合同类型待审核查询
     */
    public Page<ScfAgreementType> queryUnEnableAgreementType(int anPageNum, int anPageSize, String anFlag) {
        Map<String, Object> anMap = QueryTermBuilder.newInstance().build();
        anMap.put("businStatus", "0");
        // 手动录入
        anMap.put("dataSource", "1");
        return this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag));
    }
    
    /**
     * 已启用合同类型查询
     */
    public List<ScfAgreementType> findEnableAgreementType() {
        Map<String, Object> anMap = QueryTermBuilder.newInstance().build();
        anMap.put("businStatus", "1");
        return this.selectByProperty(anMap);
    }
    
    /**
     * 启用合同类型
     */
    public ScfAgreementType saveEnableAgreementType(Long anId) {
        ScfAgreementType anAgreementType = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anAgreementType, "无法获取对应合同类型");
        checkOperator(anAgreementType.getOperOrg(), "当前操作员无法启用合同类型");
        //设置状态启用
        anAgreementType.setBusinStatus("1");
        this.updateByPrimaryKeySelective(anAgreementType);
        return anAgreementType;
    }

    /**
     * 检查用户是否有权限操作数据
     */
    private void checkOperator(String anOperOrg, String anMessage) {
        BTAssert.isTrue(UserUtils.platformUser(), "仅平台有权限操作合同类型！");
        if (BetterStringUtils.equals(UserUtils.getOperatorInfo().getOperOrg(), anOperOrg) == false) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }
    
    /**
     * 合同类型分页查询
     */
    public Page<ScfAgreementType> queryAgreementType(Map<String, Object> anMap, int anPageNum, int anPageSize, String anFlag) {
        Map<String, Object> queryMap = Collections3.fuzzyMap(anMap, new String[]{"businStatus"});
        return this.selectPropertyByPage(queryMap, anPageNum, anPageSize, "1".equals(anFlag));
    }
}