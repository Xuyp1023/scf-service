package com.betterjr.modules.supplychain.service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.modules.supplychain.dao.CustCoreCorpInfoMapper;
import com.betterjr.modules.supplychain.entity.CustCoreCorpInfo;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class CustCoreCorpService extends BaseService<CustCoreCorpInfoMapper, CustCoreCorpInfo> {

    /**
     * 保存核心企业组织机构信息
     * @param anCoreCorpList
     */
    public void saveCoreCorpList(List<CustCoreCorpInfo> anCoreCorpList, Long anCoreCustNo){
        CustCoreCorpInfo tmpCorpInfo;
        logger.info("saveCoreCorpList Data Size:" + anCoreCorpList.size());
        for(CustCoreCorpInfo corpInfo : anCoreCorpList){
            tmpCorpInfo = findByCorpNo(anCoreCustNo, corpInfo.getCorpNo());
            if (tmpCorpInfo == null){
                corpInfo.initValue(null);
                corpInfo.initDefValue(anCoreCustNo);
                this.insert(corpInfo);
            }
            else{
                corpInfo.modifyValue(null, tmpCorpInfo);
                corpInfo.modifyDefValue(tmpCorpInfo);
                this.updateByPrimaryKey(corpInfo);
            }
        }
    }
    
    private CustCoreCorpInfo findByCorpNo(Long anCoreCustNo, String anCorpNo){
       Map<String, Object> termMap = QueryTermBuilder.newInstance().put("coreCustNo", anCoreCustNo).put("corpNo", anCorpNo).build();
       return Collections3.getFirst( selectByProperty(termMap));
    }
}
