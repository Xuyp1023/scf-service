package com.betterjr.modules.loan.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.modules.loan.service.ScfPayPlanService;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;

@Service
public class PayPlanJob extends AbstractSimpleElasticJob {
    private final static Logger logger = LoggerFactory.getLogger(PayPlanJob.class);
    
    @Autowired
    private ScfPayPlanService payPlanService;
    
    @Override
    public void process(JobExecutionMultipleShardingContext anParamJobExecutionMultipleShardingContext) {
        logger.info("*********定时任务：开始计算利息!");
        logger.info("*********定时任务：开始计算利息!");
        payPlanService.saveAutoUpdateOverDue();
        logger.info("*********定时任务：计算利息结束!");
        logger.info("*********定时任务：计算利息结束!");
    }

}
