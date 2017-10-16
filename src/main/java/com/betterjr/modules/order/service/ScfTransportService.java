package com.betterjr.modules.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.order.dao.ScfTransportMapper;
import com.betterjr.modules.order.entity.ScfTransport;
import com.betterjr.modules.order.helper.IScfOrderInfoCheckService;

@Service
public class ScfTransportService extends BaseService<ScfTransportMapper, ScfTransport>
        implements IScfOrderInfoCheckService {

    /**
     * 订单运输单据录入
     */
    public ScfTransport addTransport(ScfTransport anTransport, String anFileList) {
        logger.info("Begin to add Product");
        // 初始化系统信息
        anTransport.initAddValue();
        this.insert(anTransport);
        return anTransport;
    }

    /**
     * 订单运输单据分页查询
     * 
     * @param anDateType 日期查询,0-发货日期 1-收货日期
     */
    public Page<ScfTransport> queryTransport(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        // 操作员只能查询本机构数据
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        // 日期查询,0-发货日期 1-收货日期
        if (StringUtils.equals((String) anMap.get("dateType"), "0")) {
            anMap.put("GTEsendDate", anMap.get("GTEDate"));
            anMap.put("LTEsendDate", anMap.get("LTEDate"));
        } else if (StringUtils.equals((String) anMap.get("dateType"), "1")) {
            anMap.put("GTEreceiveDate", anMap.get("GTEDate"));
            anMap.put("LTEreceiveDate", anMap.get("LTEDate"));
        }
        anMap.remove("GTEDate");
        anMap.remove("LTEDate");
        anMap.remove("dateType");
        Page<ScfTransport> anTransportList = this.selectPropertyByPage(anMap, anPageNum, anPageSize,
                "1".equals(anFlag));

        return anTransportList;
    }

    /**
     * 查询运输单据
     */
    public List<ScfTransport> findTransport(Map<String, Object> anMap) {
        return this.selectByClassProperty(ScfTransport.class, anMap);
    }

    @Override
    public void checkInfoExist(Long anId, String anOperOrg) {
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("id", anId);
        List<ScfTransport> transportList = this.selectByClassProperty(ScfTransport.class, anMap);
        if (Collections3.isEmpty(transportList)) {
            logger.warn("不存在相对应id,操作机构,业务状态的运输单据");
            throw new BytterTradeException(40001, "不存在相对应id,操作机构,业务状态的运输单据");
        }
    }

    /**
     * 运输单据编辑
     */
    public ScfTransport saveModifyTransport(ScfTransport anModiTransport, Long anId, String anFileList) {
        ScfTransport anTransport = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anTransport, "无法获取运输单据信息");
        anModiTransport.setId(anTransport.getId());
        anModiTransport.initModifyValue(UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(anModiTransport);
        return anModiTransport;
    }

    /**
     * 运输单据删除
     */
    public ScfTransport saveDeleteTransport(Long anId) {
        ScfTransport anTransport = this.selectByPrimaryKey(anId);
        BTAssert.notNull(anTransport, "无法获取运输单据信息");
        this.deleteByPrimaryKey(anId);
        return anTransport;
    }
}
