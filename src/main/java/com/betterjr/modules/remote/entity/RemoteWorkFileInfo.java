package com.betterjr.modules.remote.entity;

import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.remote.data.RemoteFileWorkMode;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * 文件上传和下载的参数信息
 * 
 * @author zhoucy
 *
 */
public class RemoteWorkFileInfo implements java.io.Serializable {
    private static final long serialVersionUID = 6976065271625411292L;
    private final String remotePath;
    private final String localPath;
    private final RemoteFileWorkMode workMode;
    private Map<String, Object> params = new HashMap();
    private final String fileName;

    public RemoteWorkFileInfo(RemoteFileWorkMode anWorkMode) {

        this(null, null, anWorkMode);
    }

    public RemoteWorkFileInfo(String anLocalPath, RemoteFileWorkMode anWorkMode) {

        this(anLocalPath, null, anWorkMode);
    }

    public RemoteWorkFileInfo(String anLocalPath, String anRemotePath, RemoteFileWorkMode anWorkMode) {

        this(anLocalPath, anRemotePath, anWorkMode, null);
    }

    public RemoteWorkFileInfo(String anLocalPath, String anRemotePath, RemoteFileWorkMode anWorkMode,
            String anFileName) {
        this.remotePath = anRemotePath;
        this.localPath = anLocalPath;
        this.workMode = anWorkMode;
        this.fileName = anFileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getRemotePath() {
        return this.remotePath;
    }

    public String getLocalPath() {
        return this.localPath;
    }

    public RemoteFileWorkMode getWorkMode() {
        return this.workMode;
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    public void addParams(Map<String, Object> anMap) {

        this.params.putAll(anMap);
    }

    public void addParam(String anKey, Object anValue) {
        if (StringUtils.isNotEmpty(anKey) && (anValue != null)) {
            this.params.put(anKey, anValue);
        } else {
            throw new BytterValidException(90003, " the request Key or Value is null");
        }
    }

    public Object removeParam(String anKey) {

        return this.params.remove(anKey);
    }
}
