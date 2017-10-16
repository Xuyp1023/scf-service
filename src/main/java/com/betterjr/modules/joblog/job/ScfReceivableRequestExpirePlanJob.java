package com.betterjr.modules.joblog.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.modules.joblog.data.LogConstantCollections;
import com.betterjr.modules.joblog.service.ScfJoblogService;
import com.betterjr.modules.supplieroffer.entity.ScfReceivableRequest;
import com.betterjr.modules.supplieroffer.service.ScfReceivableRequestService;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;

@Service
public class ScfReceivableRequestExpirePlanJob extends AbstractSimpleElasticJob {

    @Autowired
    private ScfReceivableRequestService requestService;

    @Autowired
    private ScfJoblogService logService;

    @Override
    public void process(JobExecutionMultipleShardingContext anShardingContext) {

        List<ScfReceivableRequest> requestEndList = requestService.queryEndedDataList();

        logService.saveExpireList(requestService, requestEndList,
                LogConstantCollections.LOG_DATA_TYPE_RECEIVABLE_REQUEST);

    }

}
