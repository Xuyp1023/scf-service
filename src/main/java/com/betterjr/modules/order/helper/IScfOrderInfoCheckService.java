package com.betterjr.modules.order.helper;

import org.springframework.stereotype.Service;

public interface IScfOrderInfoCheckService {

    /**
     * 检查是否存在相应id、操作机构、业务状态的汇票信息
     * @param anId  信息id
     * @param anOperOrg 操作机构
     */
    public void checkInfoExist(Long anId, String anOperOrg);
}
