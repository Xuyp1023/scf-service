package com.betterjr.modules.remote.service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.remote.dao.FaceInfoMapper;
import com.betterjr.modules.remote.entity.FaceInfo;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class FaceInfoService extends BaseService<FaceInfoMapper, FaceInfo> {

    public List<FaceInfo> findFaceInfoWithPathCode(String anPathCode) {
        if (StringUtils.isNotBlank(anPathCode)) {
            return this.selectByProperty("faceGroup", anPathCode);
        } else {
            return new ArrayList<FaceInfo>(0);
        }
    }
}
