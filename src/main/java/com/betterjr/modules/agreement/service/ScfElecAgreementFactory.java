package com.betterjr.modules.agreement.service;

import org.springframework.aop.framework.AopProxyUtils;

import com.betterjr.common.exception.BytterDeclareException;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.modules.agreement.entity.ScfElecAgreement;
import com.betterjr.modules.sys.entity.DictItemInfo;

/**
 * 电子合同管理的工厂类，该类将负责创建合同相关信息操作的子类
 * 
 * @author henry
 *
 */

public class ScfElecAgreementFactory {
    /**
     * 根据电子合同的申请单号，创建不同的实现类。
     * 
     * @param anAppNo
     * @return
     */
    public static ScfElecAgreeLocalService create(String anAppNo) {
        ScfElecAgreementService elecAgreeService = SpringContextHolder.getBean(ScfElecAgreementService.class);
        ScfElecAgreement tmpElecAgreement = elecAgreeService.selectByPrimaryKey(anAppNo);
        ScfElecAgreeLocalService localService = null;
        if (tmpElecAgreement == null) {
            throw new BytterValidException("ScfElecAgreementFactory.create ScfElecAgreeLocalService not find request AppNo :" + anAppNo);
        }
        else {
            DictItemInfo dictItem = DictUtils.getDictItem("ElecSignContractMode", tmpElecAgreement.getAgreeType());
            try {
                localService = (ScfElecAgreeLocalService) SpringContextHolder.getBean(dictItem.getSubject());
                localService = (ScfElecAgreeLocalService) AopProxyUtils.ultimateTargetClass(localService).newInstance();
                localService.init(elecAgreeService, tmpElecAgreement);
            }
            catch (InstantiationException | IllegalAccessException e) {

                throw new BytterDeclareException(40001, "create workClass find Error ", e);
            }
        }

        return localService;
    }

    public static ScfElecAgreeLocalService create() {
        ScfElecAgreeLocalService localService = SpringContextHolder.getBean(ScfElecNoticeLocalService.class);
        localService.putService(SpringContextHolder.getBean(ScfElecAgreementService.class));
        
        return localService;
    }
}