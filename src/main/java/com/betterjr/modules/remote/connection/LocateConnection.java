package com.betterjr.modules.remote.connection;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.remote.data.RemoteFileWorkMode;
import com.betterjr.modules.remote.entity.CustFileUpload;
import com.betterjr.modules.remote.entity.RemoteWorkFileInfo;
import com.betterjr.modules.remote.entity.WorkFarFunction;
import com.betterjr.modules.sys.service.SysConfigService;

public class LocateConnection extends RemoteConnection {
    private static final long serialVersionUID = -6837907386002264316L;

    protected File localFile;
    protected URI uri;
    protected WorkFarFunction func;
    // private RemoteInvokeMode invokeMode = null;
    private String localBasePath;

    /**
     * 如果是上传文件，则直接将结果返回到workData对象里面，如果是下载下来的文件，则放到localFile中。
     */
    /*    public StringBuilder readStream() {
        if (localFile != null && localFile.exists()) {
            try {
                return this.factory.readStream(new FileInputStream(this.localFile));
            }
            catch (FileNotFoundException e) {
    
                return new StringBuilder();
            }
        }
        else {
            return this.factory.readStream(this.workData);
        }
    }
    */
    /**
     * 创建本地的文件，如果文件存在，并且有值，则产生一个临时文件，如果存在没有值，则直接删除。
     * 
     * @param localPath
     * @return
     */
    protected File createLocalFileName(String localPath) {
        File file = new File(localPath);

        // 如果本地文件路径不存在则创建本地路径
        if (file.getParentFile().exists() == false) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            if (file.length() < 10) {
                file.delete();
            } else {
                File tmpFile;
                try {
                    tmpFile = File.createTempFile(file.getName(), ".tmp", file.getParentFile());
                    file.renameTo(tmpFile);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    @Override
    public InputStream getWorkData() {
        try {
            return new FileInputStream(this.localFile);
        }
        catch (FileNotFoundException e) {
            throw new BytterValidException(80001, "getWorkData not find work File", e);
        }
    }

    @Override
    public void sendRequest(Map<String, Object> queryParams, Map<String, Object> formParams) {
        Object obj = formParams.get("data");
        if (obj == null) {
            obj = formParams.get("anFile");
        }
        boolean bb = false;
        String remoteD = null;
        RemoteWorkFileInfo workInfo = null;
        logger.warn("remote path is : " + remoteD);
        if (obj != null && obj instanceof CustFileUpload) {
            CustFileUpload uploadInfo = (CustFileUpload) obj;
            String tmpDir = uploadInfo.getRemoteSendPath();
            if (StringUtils.isBlank(tmpDir)) {
                tmpDir = this.uri.getPath();
            }
            remoteD = BetterDateUtils.formatRemotePath(tmpDir, (String) formParams.get("workDate"));
            logger.warn("work local File is :" + this.localBasePath.concat(uploadInfo.getFilePath()));
            logger.warn("work remote FileName is :" + uploadInfo.findFileName());
            bb = this.upload(this.localBasePath.concat(uploadInfo.getFilePath()), uploadInfo.findFileName(), remoteD);
            workInfo = new RemoteWorkFileInfo(this.localBasePath.concat(uploadInfo.getFilePath()), remoteD,
                    RemoteFileWorkMode.UPLOAD, uploadInfo.findFileName());
        } else if (obj instanceof RemoteWorkFileInfo) {
            workInfo = (RemoteWorkFileInfo) obj;
            if (StringUtils.isNotBlank(workInfo.getRemotePath())) {
                remoteD = BetterDateUtils.formatRemotePath(workInfo.getRemotePath(),
                        (String) formParams.get("workDate"));
            }
            if (workInfo.getWorkMode() == RemoteFileWorkMode.DOWNLOAD) {
                bb = this.download(remoteD, workInfo.getLocalPath());
                if (bb) {
                    this.localFile = new File(workInfo.getLocalPath());
                }
            } else {
                bb = this.upload(workInfo.getLocalPath(), workInfo.getFileName(), remoteD);
            }
        }
        Map<String, Object> result = new HashMap();
        if (bb) {
            result.put("status", "0000");
            result.put("msg", "success");
        } else {
            result.put("status", "9999");
            result.put("msg", "fail");
        }
        result.put("data", workInfo);
        this.workData = new ByteArrayInputStream(JsonMapper.toJsonString(result).getBytes());
    }

    @Override
    public void init(RemoteConnectionFactory anFactory, WorkFarFunction anFunc, Object[] args) {
        super.init(anFactory, anFunc, args);
        this.func = anFunc;
        // invokeMode = RemoteInvokeMode.checking(func.getInvokeMode());
        this.localBasePath = SysConfigService.getString("LocalBasePath");

        String ftpWorkPath = func.getWorkFace().getFtpUrl();
        if (StringUtils.isBlank(ftpWorkPath)) {
            ftpWorkPath = anFactory.getString("ftp_WorkPath");
        }
        if (StringUtils.isBlank(ftpWorkPath)) {
            ftpWorkPath = func.getUrl();
        } else {
            ftpWorkPath = ftpWorkPath.concat(func.getUrl());
        }
        try {

            this.uri = new URI(ftpWorkPath);
        }
        catch (URISyntaxException e) {

        }
        String ftpUserName = anFactory.getString("ftp_UserName");
        String ftpUserPass = anFactory.getString("ftp_UserPass");
        boolean useSecue = anFactory.getBoolean("ftp_useFtpKey", false);
        int timeOut = anFactory.getInt("ftp_connectTimeout".toLowerCase(), 30);

        this.init(ftpUserName, ftpUserPass, useSecue, timeOut, func, anFactory);
    }

    public void init(String anUserName, String anPass, boolean useSecue, int anTimeOut, WorkFarFunction func,
            RemoteConnectionFactory anFactory) {

    }

    public boolean download(String remotePath, String localPath) {

        return true;
    }

    public boolean upload(String anLocalPath, String anFileName, String anRemotePath) {

        return true;
    }

    public boolean rename(String remoteOldPath, String remoteNewPath) {
        File oldFile = new File(remoteOldPath);
        File newFile = new File(remoteNewPath);
        if (oldFile.exists()) {
            return oldFile.renameTo(newFile);
        }
        return false;
    }

    public boolean deleteDir(String remotePath) {
        File dir = new File(remotePath);
        if (dir.exists()) {
            return dir.delete();
        }

        return false;
    }

    public boolean delete(String remotePath) {
        File dir = new File(remotePath);
        if (dir.exists()) {
            return dir.delete();
        }

        return false;
    }

    @Override
    public void destroy() {
        close();
    }

    @Override
    public void close() {
        super.close();
    }
}
