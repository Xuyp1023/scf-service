package com.betterjr.modules.commission.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.commission.dao.CommissionFileDownMapper;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;
import com.betterjr.modules.commission.entity.CommissionFile;
import com.betterjr.modules.commission.entity.CommissionFileDown;
import com.betterjr.modules.commission.entity.CommissionRecord;

@Service
public class CommissionFileDownService extends BaseService<CommissionFileDownMapper, CommissionFileDown> {

    @Autowired
    private CommissionFileService fileService;

    @Autowired
    private CommissionRecordService recordService;

    /**
     * 佣金数据导出数据查询
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<CommissionFileDown> queryFileDownList(Map<String, Object> anMap, String anFlag, int anPageNum,
            int anPageSize) {

        BTAssert.notNull(anMap, "查询佣金文件条件为空");
        // 去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        // 查询当前公司的佣金文件
        // anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        anMap.put("confirmStatus", CommissionConstantCollentions.COMMISSION_FILE_CONFIRM_STATUS_EFFECTIVE);
        Page<CommissionFileDown> fileList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag),
                "id desc");

        return fileList;
    }

    /**
     * 查询佣金数据审核界面当天导出文件集合
     * 
     * @param anMap
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<CommissionFileDown> queryCanAuditFileList(Map<String, Object> anMap, String anFlag, int anPageNum,
            int anPageSize) {

        BTAssert.notNull(anMap, "查询佣金文件条件为空");
        BTAssert.notNull(anMap.get("GTEimportDate"), "查询佣金文件条件为空");
        BTAssert.notNull(anMap.get("LTEimportDate"), "查询佣金文件条件为空");
        // 去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        // 查询当前公司的佣金文件
        Page<CommissionFileDown> fileList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag),
                "id desc");

        return fileList;

    }

    /**
     * 
     * @param anFileId
     * @param anFlag
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<CommissionRecord> queryFileRecordByFileId(Long anFileId, String anFlag, int anPageNum, int anPageSize) {

        Page<CommissionRecord> page = new Page<>(anPageNum, anPageSize);
        List<CommissionFile> fileList = fileService.queryFileListByFileDownId(anFileId);
        if (Collections3.isEmpty(fileList)) {
            return page;
        }
        List<Long> ids = convertToFileId(fileList);
        page = recordService.queryRecordPageByFileIds(ids, anFlag, anPageNum, anPageSize);
        return page;
    }

    private List<Long> convertToFileId(List<CommissionFile> anFileList) {

        List<Long> ids = new ArrayList<>();
        for (CommissionFile commissionFile : anFileList) {
            ids.add(commissionFile.getId());
        }

        return ids;
    }

    /**
     * 审核导出文件
     * 
     * @param anQueryMap
     * @return confirmMessage confirmStatus id
     */
    public CommissionFileDown saveAuditFileDown(Map<String, Object> anMap) {

        BTAssert.notNull(anMap, "审核佣金文件条件为空");
        BTAssert.notNull(anMap.get("confirmMessage"), "审核佣金文件条件为空");
        BTAssert.notNull(anMap.get("confirmStatus"), "审核佣金文件条件为空");
        BTAssert.notNull(anMap.get("id"), "审核佣金文件条件为空");

        CommissionFileDown fileDown = this.selectByPrimaryKey(Long.parseLong(anMap.get("id").toString()));
        BTAssert.notNull(fileDown, "审核佣金文件不存在");
        checkFileDownStatus(fileDown);
        // 设置下载文件的状态
        fileDown.saveAuditInit(anMap.get("confirmStatus").toString(), anMap.get("confirmMessage").toString(),
                UserUtils.getOperatorInfo());
        this.updateByPrimaryKeySelective(fileDown);
        // 设置佣金文件的状态和 设置佣金记录的状态
        fileService.saveUpdateByFileDown(fileDown);

        return fileDown;
    }

    private void checkFileDownStatus(CommissionFileDown anFileDown) {

        checkStatus(anFileDown.getConfirmStatus(),
                CommissionConstantCollentions.COMMISSION_FILE_CONFIRM_STATUS_EFFECTIVE, true, "当前文件记录已经已经审核通过，不能重复审核");
        checkStatus(anFileDown.getConfirmStatus(),
                CommissionConstantCollentions.COMMISSION_FILE_CONFIRM_STATUS_INEFFECTIVE, true, "当前文件记录已经审核未通过，不能重复审核");

    }

    /**
     * 检查状态信息
     */
    public void checkStatus(String anBusinStatus, String anTargetStatus, boolean anFlag, String anMessage) {
        if (StringUtils.equals(anBusinStatus, anTargetStatus) == anFlag) {
            logger.warn(anMessage);
            throw new BytterTradeException(40001, anMessage);
        }
    }

}
