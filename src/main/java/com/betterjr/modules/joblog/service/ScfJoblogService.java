package com.betterjr.modules.joblog.service;

import org.springframework.stereotype.Service;
import com.betterjr.common.service.BaseService;
import com.betterjr.modules.joblog.dao.ScfJoblogMapper;
import com.betterjr.modules.joblog.entity.ScfJoblog;

@Service
public class ScfJoblogService extends BaseService<ScfJoblogMapper, ScfJoblog> {

    public ScfJoblog saveAddLog(ScfJoblog anLog) {

        anLog.initAddValue();
        this.insert(anLog);
        return anLog;

    }

}
