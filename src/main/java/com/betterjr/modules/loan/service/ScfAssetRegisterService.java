package com.betterjr.modules.loan.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.loan.dao.ScfAssetRegisterMapper;
import com.betterjr.modules.loan.entity.ScfAssetRegister;

@Service
public class ScfAssetRegisterService extends BaseService<ScfAssetRegisterMapper, ScfAssetRegister> {

    @Autowired
    private CustAccountService custAccountService;

    public ScfAssetRegister addRegister(ScfAssetRegister anRegister) {
        BTAssert.notNull(anRegister, "保存记录失败-anRegister不能为空");
        if (StringUtils.isNotEmpty(anRegister.getRequestNo())) {
            // 删除原有的登记
            ScfAssetRegister oldRegister = findAssestRegisterByRequestNo(anRegister.getRequestNo());
            if (oldRegister != null) {
                this.delete(anRegister);
            }
        }

        anRegister.init();
        anRegister.setRegisterCustName(custAccountService.queryCustName(anRegister.getRegisterCustNo()));
        this.insert(anRegister);
        return anRegister;
    }

    public ScfAssetRegister findAssestRegister(Long anId) {
        BTAssert.notNull(anId, "查询失败anId不能为空");
        return this.selectByPrimaryKey(anId);
    }

    public ScfAssetRegister findAssestRegisterByRequestNo(String anRequestNo) {
        BTAssert.notNull(anRequestNo, "查询失败anId不能为空");
        Map<String, Object> map = QueryTermBuilder.newInstance().put("requestNo", anRequestNo).build();
        List<ScfAssetRegister> list = this.selectByClassProperty(ScfAssetRegister.class, map, "id desc");
        if (!Collections3.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
