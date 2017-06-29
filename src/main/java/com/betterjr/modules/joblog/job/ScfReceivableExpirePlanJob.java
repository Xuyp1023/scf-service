package com.betterjr.modules.joblog.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.modules.receivable.service.ScfReceivableDOService;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;

@Service
public class ScfReceivableExpirePlanJob extends AbstractSimpleElasticJob{
    
    @Autowired
    private ScfReceivableDOService receivableService;

    @Override
    public void process(JobExecutionMultipleShardingContext anShardingContext) {
        
        //System.out.println("ScfReceivableExpirePlanJob..........");
        receivableService.saveExpireEndDataList();
        
    }

}
