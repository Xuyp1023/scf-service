package com.betterjr.modules.remote.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.remote.entity.WorkFarFunction;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;

public class RemoteNormalSFtpService extends LocateConnection {

    private static final long serialVersionUID = 874256469069678514L;
    private ChannelSftp sftp = null;

    @Override
    public void init(String anUserName, String anPass, boolean anUseSecue, int anTimeOut, WorkFarFunction func,
            RemoteConnectionFactory anFactory) {
        int timeOut = anTimeOut * 1000;
        try {
            JSch jsch = new JSch();
            if (anUseSecue) {
                jsch.addIdentity(func.getWorkFace().getRealFtpKeyPath(), func.getWorkFace().getSftpPass());
            }
            Session sshSession = jsch.getSession(anUserName, this.uri.getHost(), this.uri.getPort());
            sshSession.setTimeout(timeOut);
            logger.info("Session created.");
            sshSession.setPassword(anPass);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect(anTimeOut * 200);
            logger.info("Session connected.");
            logger.info("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect(anTimeOut * 100);
            sftp = (ChannelSftp) channel;
            logger.info("Connected to " + this.uri.getHost() + ".");
        }
        catch (Exception ex) {
            throw new BytterValidException(70901, "connect ftp server has err ", ex);
        }
    }

    /**
     * 
     * @param anPath
     *            指定的文件或目录
     * @param isDir
     *            true表示目录，false表示文件
     * @return
     */
    public boolean checkExists(String anPath, boolean isDir) {
        try {
            logger.warn("checkExists is " + anPath);
            Vector<LsEntry> vv = sftp.ls(anPath);
            for (LsEntry ent : vv) {
                SftpATTRS sAttrib = ent.getAttrs();
                logger.warn("checkExists my path attribe is " + anPath + ", sAttrib.isDir()=" + sAttrib.isDir() + ", "
                        + ", isFifo =" + sAttrib.isFifo());
                if (isDir && sAttrib.isDir()) {
                    return true;
                } else if (sAttrib.isFifo()) {
                    return true;
                }
            }
            return false;
        }
        catch (SftpException ex) {
            logger.warn("check remote File " + anPath + ", hasError");
            if (isDir) {
                try {
                    sftp.mkdir(anPath);
                    return true;
                }
                catch (SftpException e) {
                    logger.warn("check remote mkdir has error " + anPath);

                }
            }
        }
        catch (Exception ex) {}
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
     * @throws IOException
     */
    @Override
    public boolean download(String remotePath, String localPath) {
        if (this.sftp == null) {
            return false;
        }
        OutputStream fileOutputStream = null;
        File file = createLocalFileName(localPath);
        File tmpFile = null;
        boolean res = false;
        try {
            tmpFile = File.createTempFile(file.getName(), ".dat", file.getParentFile());
            File remoteFile = new File(remotePath);
            String directory = remoteFile.getParent();
            if (checkExists(directory, true)) {
                sftp.cd(directory);
                fileOutputStream = new FileOutputStream(file);
                sftp.get(remoteFile.getName(), fileOutputStream);
                IOUtils.closeQuietly(fileOutputStream);
                tmpFile.renameTo(file);
                logger.info("sftp success download file " + remotePath);
                res = true;
            } else {
                logger.error("download path is not exists :" + directory);
            }
        }
        catch (SftpException e) {
            if (e.toString().endsWith("No such file")) {
                logger.error(">>>>>>>>RemoteNormalSFtpService-->download--sftp error  " + remotePath
                        + "  not exists>>>>>>>>>>>>>");
            } else {
                logger.error("download file has error, ", e);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("local path error, please checking");
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.error("create temp File " + tmpFile + " has error!");
        }
        finally {
            IOUtils.closeQuietly(fileOutputStream);
            if (res == false) {
                if (tmpFile != null) {
                    tmpFile.delete();
                }
            }
        }
        return res;
    }

    @Override
    public boolean upload(String anLocalPath, String anFileName, String anRemotePath) {
        boolean res = false;
        InputStream fileStream = null;
        File file = new File(anLocalPath);
        try {
            logger.warn("this is will upload " + anLocalPath + ", to " + anRemotePath);
            if (file.exists()) {
                checkExists(anRemotePath, true);
                fileStream = new FileInputStream(file);
                sftp.cd(anRemotePath);
                if (StringUtils.isBlank(anFileName)) {
                    anFileName = file.getName();
                }
                sftp.put(fileStream, anFileName);
                logger.warn("this is upload sueccess" + anLocalPath + ", to " + anRemotePath);
                res = true;
            } else {
                logger.warn("local file not exits");
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            logger.error("sftp upload file from " + anLocalPath + ", to " + anRemotePath + " has IOException", ex);
        }
        catch (SftpException ex) {
            ex.printStackTrace();
            logger.error("sftp upload file from " + anLocalPath + ", to " + anRemotePath + " has SftpException", ex);
        }
        finally {
            IOUtils.closeQuietly(fileStream);
        }

        return res;
    }

    @Override
    public void close() {
        try {
            if (sftp.getSession().isConnected()) {
                sftp.getSession().disconnect();
            }
            sftp.quit();
        }
        catch (JSchException e) {
            logger.error("close sftp is error", e);
        }

    }

    /**
     * 列出目录下的文件
     * 
     * @param directory
     *            要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     */
    public Vector<LsEntry> listFiles(String remotePath) {
        try {
            return sftp.ls(remotePath);
        }
        catch (SftpException ex) {

            return new Vector();
        }
    }

    @Override
    public boolean delete(String remotePath) {

        return deleteFile(remotePath) || deleteDir(remotePath);
    }

    @Override
    public boolean deleteDir(String remotePath) {
        try {
            for (LsEntry le : listFiles(remotePath)) {
                if (le.getAttrs().isDir()) {
                    deleteDir(remotePath + "/" + le.getFilename());
                } else {
                    deleteFile(remotePath + "/" + le.getFilename());
                }
            }
            sftp.rmdir(remotePath);

            return true;
        }
        catch (SftpException ex) {
            logger.error(" deleteDirectory remote Directory : " + remotePath + " has error", ex);

            return false;
        }
    }

    public boolean deleteFile(String remotePath) {
        try {
            File tmpFile = new File(remotePath);
            sftp.cd(tmpFile.getParent());
            sftp.rm(tmpFile.getName());

            return true;
        }
        catch (Exception ex) {

            logger.error(" delete remote Path : " + remotePath + " has error", ex);
            return false;
        }
    }

    @Override
    public boolean rename(String remoteOldPath, String remoteNewPath) {
        try {
            sftp.rename(remoteOldPath, remoteNewPath);

            return true;
        }
        catch (Exception ex) {
            logger.error(" rename remote file : " + remoteOldPath + " to " + remoteNewPath + " has error", ex);

            return false;
        }
    }
}