package com.betterjr.modules.supplychain.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.modules.agreement.service.ScfFactorRemoteHelper;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;

/**
 * 查询远端的资金方的产品信息
 * @author zhoucy
 *
 */
@Service
public class QueryProductInfoJob extends AbstractSimpleElasticJob {

    @Autowired
    private ScfFactorRemoteHelper remoteHelper;
    @Override
    public void process(JobExecutionMultipleShardingContext anParam) {
        
        remoteHelper.queryProductInfo();
    }

}
