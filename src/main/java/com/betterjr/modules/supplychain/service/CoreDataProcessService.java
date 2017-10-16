package com.betterjr.modules.supplychain.service;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.supplychain.dao.CoreDataProcessInfoMapper;
import com.betterjr.modules.supplychain.entity.CoreDataProcessInfo;

import java.util.*;

import org.springframework.stereotype.Service;

@Service("coreDataProcessService")
public class CoreDataProcessService extends BaseService<CoreDataProcessInfoMapper, CoreDataProcessInfo> {

    /**
     * 查询核心企业所需要处理的信息；状态为0未处理，2部分处理的需要继续处理
     * 
     * @param anOperOrg
     *            1086
     * @return
     */
    public List<CoreDataProcessInfo> findCoreDataProcess(String anOperOrg) {
        Map<String, Object> workCondition = new HashMap();
        workCondition.put("workStatus", new String[] { "0", "2" });
        workCondition.put("LTEworkDate", BetterDateUtils.getNumDate());
        workCondition.put("operOrg", anOperOrg);

        return this.selectByProperty(workCondition);
    }

    /**
     * 更新处理状态，如果处理成功，则切换到下一个日期，每天处理一次。
     * 
     * @param anProcess
     */
    private CoreDataProcessInfo saveProcessStatus(CoreDataProcessInfo anProcess) {
        this.updateByPrimaryKey(anProcess);
        if ("1".equals(anProcess.getWorkStatus())) {
            anProcess = CoreDataProcessInfo.switchNext(anProcess);
        }
        this.updateByPrimaryKey(anProcess);

        return anProcess;
    }

    public void processWorkStatus(String anOpenOrg, String anProcessType, String anStatus) {
        Map<String, Object> termMap = new HashMap();
        termMap.put("processType", anProcessType);
        termMap.put("operOrg", anOpenOrg);
        CoreDataProcessInfo processInfo = Collections3.getFirst(this.selectByProperty(termMap));
        processInfo.setWorkStatus(anStatus);
        this.saveProcessStatus(processInfo);
    }

    /**
     * 改变数据处理状态
     */
    public void changeWorkStatus(String anWorkParam) {
        SimpleDataEntity data = new SimpleDataEntity(anWorkParam);
        System.out.println("this is anWorkParam :" + data);
        this.processWorkStatus(data.getValue(), data.getName(), "0");
    }
}
