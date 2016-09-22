package com.betterjr.modules.remote.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.*;

import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.security.KeyReader;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.remote.entity.WorkFarFunction;

public class RemoteNormalFtpService extends LocateConnection {
    private static final long serialVersionUID = 874256469069678514L;

    private FTPClient ftpsClient;

    @Override
    public void init(String anUserName, String anPass, boolean anUseSecue, int anTimeOut, WorkFarFunction func, RemoteConnectionFactory anFactory) {
        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
        conf.setServerLanguageCode("EN");
        conf.setDefaultDateFormatStr("yyyy-MM-dd");
        conf.setRecentDateFormatStr("yyyy-MM-dd");
        conf.setServerTimeZoneId("Asia/Shanghai");
        FTPSClient tmpFtps = null;
        int timeOut = anTimeOut * 1000;
        if (anUseSecue) {
            tmpFtps = new FTPSClient(true);
            ftpsClient = tmpFtps;
            logger.info("use secure ftps" + this.uri);
        }
        else {
            ftpsClient = new FTPClient();
        }
        try {
            ftpsClient.configure(conf);
            String userCharSet = anFactory.getString("ftp_controlCharset", "UTF-8");
            ftpsClient.setControlEncoding(userCharSet);
            ftpsClient.setBufferSize(1024 * 1024);
            if (anUseSecue) {
                tmpFtps.setKeyManager(KeyReader.getKeyManager(func.getWorkFace().getSftpKeyPath(), func.getWorkFace().getSftpPass()));
                tmpFtps.execPBSZ(0);
                tmpFtps.execPROT("P");
                tmpFtps.setUseClientMode(true);
            }
            ftpsClient.setDataTimeout(timeOut);
            ftpsClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            ftpsClient.setDefaultTimeout(timeOut);
            ftpsClient.connect(this.uri.getHost(), this.uri.getPort());
            logger.info("已经连接FTP");
            ftpsClient.setSoTimeout(timeOut);
            ftpsClient.getReplyCode();
            ftpsClient.login(anUserName, anPass);

            logger.info("has login this host:" + this.uri);
        }
        catch (Exception ex) {
            throw new BytterValidException(70901, "connect ftp server has err ", ex);
        }
    }

    public boolean checkExists(String anPath, boolean isDir) {
        if (isDir) {
            return checkDirExists(anPath);
        }
        else {
            return checkFileExists(anPath);
        }
    }

    public boolean checkDirExists(String anDir) {
        try {
            if (ftpsClient.changeWorkingDirectory(anDir)) {
                return true;
            }
        }
        catch (IOException e) {
        }
        return false;
    }

    public boolean checkFileExists(String anFile) {
        File ff = new File(anFile);
        if (checkDirExists(ff.getParent())) {
            FTPFile[] files;
            try {
                files = ftpsClient.listFiles();
                for (FTPFile fff : files) {
                    if (fff.isFile() && fff.getName().equalsIgnoreCase(ff.getName())) {
                        return true;
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 下载
     * 
     * @param remotePath
     *            ftp路径
     * @param localPath
     *            本地路径
     * @return 下载是否成功
     */
    public boolean download(String remotePath, String localPath) {
        if (ftpsClient == null) {
            return false;
        }
        OutputStream fileOutputStream = null;
        File file = createLocalFileName(localPath);
        File tmpFile = null;
        boolean res = false;
        try {
            File remoteFile = new File(remotePath);
            String directory = remoteFile.getParent();
            if (checkDirExists(directory)) {
                tmpFile = File.createTempFile(file.getName(), ".dat", file.getParentFile());
                fileOutputStream = new FileOutputStream(tmpFile);
                res = ftpsClient.retrieveFile(remotePath, fileOutputStream);
                fileOutputStream.flush();
                IOUtils.closeQuietly(fileOutputStream);
                tmpFile.renameTo(file);
                logger.info("ftp success download file " + remotePath);
                res = true;
            }
            else {
                logger.error("ftp download path is not exists :" + directory);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            logger.error("ftp local path error, please checking :" + remotePath);
        }
        finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
        return res;
    }

    /**
     * 创建目录
     * 
     * @param remotePath
     * @throws IOException
     */
    public boolean makeDirectory(String remotePath) throws IOException {
        if (ftpsClient == null) return false;

        String[] item = remotePath.split("/");
        String currentPath = "";
        for (int i = 0; i < item.length - 1; i++) {
            currentPath = currentPath + "/" + item[i];
            if (checkDirExists(currentPath) == false) {
                ftpsClient.makeDirectory(currentPath);
            }
        }

        return ftpsClient.makeDirectory(remotePath);
    }

    /**
     * 上传
     * 
     * @param localPath
     *            本地路径
     * @param remotePath
     *            ftp路径
     * @return 上传是否成功
     */
    public boolean upload(String anLocalPath, String anFileName, String anRemotePath) {
        if (ftpsClient == null) {
            return false;
        }

        boolean res = false;
        FileInputStream fileStream = null;
        try {
            File file = new File(anLocalPath);

            // 检查ftp服务器上的目录是否存在，如果不存在则层级的创建目录.
            if (checkDirExists(anRemotePath ) == false) {
                makeDirectory(anRemotePath);
            }

            if (file.exists()) {
                fileStream = new FileInputStream(file);
                ftpsClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpsClient.changeWorkingDirectory(anRemotePath);
                if (BetterStringUtils.isBlank(anFileName)){
                    anFileName= file.getName();
                }
                res = ftpsClient.storeFile(file.getName(), fileStream);
            }
        }
        catch (IOException ex) {
            logger.error("ftp upload file from " + anLocalPath + ", to " + anRemotePath + " has error", ex);
        }
        finally {
            IOUtils.closeQuietly(fileStream);
        }

        return res;
    }

    /**
     * 退出登录
     * 
     * @throws IOException
     */
    public void close() {
        if (ftpsClient != null) {
            try {
                ftpsClient.logout();
            }
            catch (IOException ex) {

            }
            finally {
                try {
                    ftpsClient.disconnect();
                }
                catch (IOException e) {

                }
            }
        }
    }

    /**
     * 删除文件
     * 
     * @param remotePath
     *            ftp端路径
     * @return
     * @throws IOException
     */
    public boolean delete(String remotePath) {
        try {
            return ftpsClient.deleteFile(remotePath) || deleteDir(remotePath);
        }
        catch (Exception ex) {
            logger.error(" delete remote Path : " + remotePath + " has error", ex);
            return false;
        }
    }

    /**
     * 删除文件
     * 
     * @param remotePath
     *            ftp端路径
     * @return
     * @throws IOException
     */
    public boolean deleteDir(String remotePath) {
        try {
            for (FTPFile ftpFile : listFiles(remotePath)) {
                if (ftpFile.isDirectory()) {
                    deleteDir(remotePath + "/" + ftpFile.getName());
                }
                else {
                    ftpsClient.deleteFile(remotePath + "/" + ftpFile.getName());
                }
            }
            return ftpsClient.removeDirectory(remotePath);
        }
        catch (IOException ex) {

            logger.error(" deleteDirectory remote Directory : " + remotePath + " has error", ex);
            return false;
        }
    }

    /**
     * 重命名
     * 
     * @param remoteOldPath
     * @param remoteNewPath
     * @return
     * @throws IOException
     */
    public boolean rename(String remoteOldPath, String remoteNewPath) {
        try {

            return ftpsClient.rename(remoteOldPath, remoteNewPath);
        }
        catch (Exception ex) {
            logger.error(" rename remote file : " + remoteOldPath + " to " + remoteNewPath + " has error", ex);
            return false;
        }
    }

    /**
     * 得到所有目录
     * 
     * @param remotePath
     * @return
     * @throws IOException
     */
    public FTPFile[] listFiles(String remotePath) {
        try {
            ftpsClient.changeWorkingDirectory(remotePath);
            return ftpsClient.listFiles();
        }
        catch (Exception ex) {
            logger.error(" listFiles : " + remotePath + " has error", ex);
            return new FTPFile[] {};
        }
    }
}