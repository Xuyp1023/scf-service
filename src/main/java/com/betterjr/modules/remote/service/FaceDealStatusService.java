package com.betterjr.modules.remote.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.remote.dao.FaceDealStatusMapper;
import com.betterjr.modules.remote.entity.FaceDealStatus;

@Service
public class FaceDealStatusService extends BaseService<FaceDealStatusMapper, FaceDealStatus> {

    public void saveDealStatus(FaceDealStatus anStatus) {
        Map map = new HashMap();
        map.put("faceNo", anStatus.getFaceNo());
        map.put("pathCode", anStatus.getPathCode());
        map.put("status", anStatus.getStatus());
        List list = this.selectByProperty(map);
        if (Collections3.isEmpty(list)) {
            this.insert(anStatus);
        }
        else {
            this.updateByPrimaryKey(anStatus);
        }
    }
}
