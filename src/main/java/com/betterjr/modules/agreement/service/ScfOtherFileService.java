package com.betterjr.modules.agreement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.service.BaseService;
import com.betterjr.modules.agreement.dao.ScfOtherFileMapper;
import com.betterjr.modules.agreement.entity.ScfOtherFile;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;

@Service
public class ScfOtherFileService extends BaseService<ScfOtherFileMapper, ScfOtherFile> {

    public final static Logger logger = LoggerFactory.getLogger(ScfOtherFileService.class);
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileService;

    public boolean addOtherFile(Map<String, Object> anMap) {
        ScfOtherFile otherFile = new ScfOtherFile();
        otherFile.initDef((String) anMap.get("requestNo"), (String) anMap.get("node"),
                Long.parseLong((String) anMap.get("fileId")));
        return this.insert(otherFile) > 0;
    }

    public List<ScfOtherFile> queryOtherFile(String anRequestNo) {
        List<ScfOtherFile> otherFileList = new ArrayList<ScfOtherFile>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("requestNo", anRequestNo);
        map.put("businStatus", "1");
        for (ScfOtherFile otherFile : this.selectByProperty(map)) {
            String filesName = findFileName(otherFile.getFileId());
            otherFile.setFileName(filesName);
            otherFileList.add(otherFile);
        }
        return otherFileList;
    }

    public String findFileName(Long anFileId) {
        CustFileItem custFileItem = custFileService.findOne(anFileId);
        return custFileItem.getFileName();
    }

    public boolean delOtherFile(Long anOtherFileId) {
        return this.deleteByPrimaryKey(anOtherFileId) > 0;
    }
}
