package com.betterjr.modules.remote.entity;

import java.util.*;
import java.io.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.FileUtils;

/**
 * 实现文件上传处理的对象handle， 屏蔽文件上传处理的细节信息
 * 
 * @author zhoucy
 *
 */

public class MultiPartDataInfo {

    private Map<String, String> itemMap = new HashMap();
    private Map<String, File> fileMap = new HashMap();

    public void addNamedItem(String anKey, String anValue) {
        if (StringUtils.isNotEmpty(anKey) && StringUtils.isNotEmpty(anValue)) {
            this.itemMap.put(anKey, anValue);
        } else {
            throw new BytterValidException(90003, " the request Key or Value is null");
        }

    }

    public String removeNamedItem(String anKey) {

        return this.itemMap.remove(anKey);
    }

    public void addFileItem(String anKey, String anFile) {

        addFileItem(anKey, new File(anFile));
    }

    public void addFileItem(String anKey, File anFile) {
        if (StringUtils.isNotBlank(anKey) && (anFile != null) && anFile.exists() && anFile.isFile()) {

            this.fileMap.put(anKey, anFile);
        } else {
            throw new BytterValidException(90003, " the request anKey is null or File Not Exists");
        }
    }

    public File removeFileItem(String anKey) {

        return this.fileMap.remove(anKey);
    }

    public static HttpEntity buildMuiltData(MultiPartDataInfo anPartData) {
        if (anPartData == null
                || (Collections3.isEmpty(anPartData.itemMap) && Collections3.isEmpty(anPartData.fileMap))) {
            throw new BytterValidException(90001, " the request MultiPartDataInfo is null or empty");
        }
        return anPartData.build();
    }

    public HttpEntity build() {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder = entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (Map.Entry<String, String> ent : this.itemMap.entrySet()) {
            entityBuilder.addTextBody(ent.getKey(), ent.getValue());
        }
        for (Map.Entry<String, File> ent : this.fileMap.entrySet()) {
            try {
                entityBuilder.addBinaryBody(ent.getKey(), org.apache.commons.io.FileUtils.openInputStream(ent.getValue()));
            }
            catch (IOException e) {
                throw new BytterValidException(90002, " the request MultiPartDataInfo request File "
                        + ent.getValue().getAbsolutePath() + ", not find");
            }
        }

        return entityBuilder.build();
    }
}
