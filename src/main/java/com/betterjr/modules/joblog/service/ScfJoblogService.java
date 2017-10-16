package com.betterjr.modules.joblog.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.mapperhelper.EntityHelper;
import com.betterjr.mapper.mapperhelper.EntityHelper.EntityColumn;
import com.betterjr.modules.joblog.dao.ScfJoblogMapper;
import com.betterjr.modules.joblog.data.LogConstantCollections;
import com.betterjr.modules.joblog.entity.ScfJoblog;
import com.betterjr.modules.supplieroffer.BaseExpiredInterface;

@Service
public class ScfJoblogService extends BaseService<ScfJoblogMapper, ScfJoblog> {

    public ScfJoblog saveAddLog(ScfJoblog anLog) {

        anLog.initAddValue();
        this.insert(anLog);
        return anLog;

    }

    /**
     * 设置过期的对象
     * @param anInterface
     * @param anTList
     * @param anBusinType 类型
     */
    public void saveExpireList(BaseExpiredInterface anInterface, List anTList, String anBusinType) {

        if (!Collections3.isEmpty(anTList)) {

            int size = anTList.size();
            int numEach = size % LogConstantCollections.LOG_RECORD_SIZE == 0
                    ? size / LogConstantCollections.LOG_RECORD_SIZE : size / LogConstantCollections.LOG_RECORD_SIZE + 1;
            for (int i = 0; i < numEach; i++) {

                if (i + 1 == numEach) {
                    saveExpireList(anInterface, anTList.subList(LogConstantCollections.LOG_RECORD_SIZE * i, size),
                            anBusinType, (i + 1) + "");

                } else {

                    saveExpireList(anInterface, anTList.subList(LogConstantCollections.LOG_RECORD_SIZE * i,
                            LogConstantCollections.LOG_RECORD_SIZE * (i + 1)), anBusinType, (i + 1) + "");

                }

            }

        }

    }

    private void saveExpireList(BaseExpiredInterface anInterface, List anTList, String anBusinType, String anOrderBy) {

        if (!Collections3.isEmpty(anTList)) {

            String showSuccessMessage = "";
            String showFailureMessage = "";

            for (Object t : anTList) {
                try {
                    anInterface.saveExpired(t);
                    showSuccessMessage += findPrimaryKeyValue(t) + ",";

                }
                catch (Exception e) {
                    showFailureMessage += findPrimaryKeyValue(t) + ",";
                }
            }

            if (StringUtils.isNotBlank(showSuccessMessage)) {
                ScfJoblog log = new ScfJoblog();
                log.setBusinType(LogConstantCollections.LOG_BUSIN_TYPE_EXPIRE);
                log.setDataType(anBusinType);
                log.setOrderBy(anOrderBy);
                log.setBusinStatus(LogConstantCollections.LOG_BUSIN_STATUS_SUCCESS);
                log.setShowMessage(showSuccessMessage);
                log.initAddValue();
                saveAddLog(log);
            }

            if (StringUtils.isNotBlank(showFailureMessage)) {
                ScfJoblog log = new ScfJoblog();
                log.setBusinType(LogConstantCollections.LOG_BUSIN_TYPE_EXPIRE);
                log.setDataType(anBusinType);
                log.setOrderBy(anOrderBy);
                log.setBusinStatus(LogConstantCollections.LOG_BUSIN_STATUS_FAILURE);
                log.setShowMessage(showFailureMessage);
                log.initAddValue();
                saveAddLog(log);
            }

        }

    }

    private String findPrimaryKeyValue(Object anT) {

        Field[] fields = anT.getClass().getDeclaredFields();
        Set<EntityColumn> pkColumns = EntityHelper.getPKColumns(anT.getClass());
        String primaryName = "id";
        for (EntityColumn entityColumn : pkColumns) {
            primaryName = entityColumn.getProperty();
        }
        if (fields != null) {
            for (Field field : fields) {
                if (field.getName().equals(primaryName)) {
                    field.setAccessible(true);
                    try {
                        return field.get(anT).toString();
                    }
                    catch (Exception e) {

                        e.printStackTrace();
                        return "";
                    }
                }

            }
        }

        return "";
    }

}
