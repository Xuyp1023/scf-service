package com.betterjr.modules.joblog.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillDOService;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;

@Service
public class ScfAcceptBillExpirePlanJob extends AbstractSimpleElasticJob {

    @Autowired
    private ScfAcceptBillDOService billService;

    @Override
    public void process(JobExecutionMultipleShardingContext anShardingContext) {

        // System.out.println("123");

        billService.saveExpireEndDataList();

    }

}
