package com.betterjr.modules.supplychain.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.modules.supplychain.dao.CustCoreCorpInfoMapper;
import com.betterjr.modules.supplychain.entity.CustCoreCorpInfo;

@Service
public class CustCoreCorpService extends BaseService<CustCoreCorpInfoMapper, CustCoreCorpInfo> {

    /**
     * 保存核心企业组织机构信息
     * @param anCoreCorpList
     */
    public void saveCoreCorpList(final List<CustCoreCorpInfo> anCoreCorpList, final String anOperOrg){
        CustCoreCorpInfo tmpCorpInfo;
        logger.info("saveCoreCorpList Data Size:" + anCoreCorpList.size());
        for(final CustCoreCorpInfo corpInfo : anCoreCorpList){
            tmpCorpInfo = findByCorpNo(anOperOrg, corpInfo.getCorpNo());
            if (tmpCorpInfo == null){
                corpInfo.initValue(null);
                corpInfo.initDefValue(anOperOrg);
                this.insert(corpInfo);
            }
            else{
                corpInfo.modifyValue(null, tmpCorpInfo);
                corpInfo.modifyDefValue(tmpCorpInfo);
                this.updateByPrimaryKey(corpInfo);
            }
        }
    }

    public CustCoreCorpInfo findByCorpNo(final String anOperOrg, final String anCorpNo){
        final Map<String, Object> termMap = QueryTermBuilder.newInstance().put("operOrg", anOperOrg).put("corpNo", anCorpNo).build();
        return Collections3.getFirst( selectByProperty(termMap));
    }

}
