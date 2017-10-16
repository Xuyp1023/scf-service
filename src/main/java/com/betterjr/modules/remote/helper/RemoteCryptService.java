package com.betterjr.modules.remote.helper;

import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.data.DataEncoding;
import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.security.CustKeyManager;
import com.betterjr.common.security.SignHelper;

/**
 * 数据加密处理
 * 
 * @author zhoucy
 *
 */
public class RemoteCryptService extends RemoteAlgorithmService implements java.io.Serializable {

    private static final Logger logger = LoggerFactory.getLogger(RemoteCryptService.class);

    private static final long serialVersionUID = 7459826384239720243L;
    // 算法为DES的密码必须是8的倍数
    protected String algorithm = "RSA";
    protected byte[] workKey = null;
    protected String workCharSet = "UTF-8";
    protected DataEncoding dataEncodeing = DataEncoding.BASE64;
    protected String algorithmCBC;
    protected boolean fileBase64 = false;
    // 密钥必须是16位的；Initialization vector (IV) 必须是16位
    protected String algorithmCBCIV;

    @Override
    public void initParameter(Map anConfigInfo, CustKeyManager anKeyManager) {
        super.initParameter(anConfigInfo, anKeyManager);
        this.algorithm = this.getString("encrypt_algorithm".toLowerCase(), algorithm);
        this.algorithmCBC = this.getString("encrypt_algorithm_cbc".toLowerCase(), null);
        this.algorithmCBCIV = this.getString("encrypt_algorithm_iv".toLowerCase(), null);

        this.workCharSet = this.getString("encrypt_charset".toLowerCase(), workCharSet);
        this.dataEncodeing = DataEncoding.checking(this.getString("encrypt_encoding", null));
        if (dataEncodeing == null) {
            this.dataEncodeing = DataEncoding.BASE64;
        }

        String tmpKey = this.getString("encrypt_key".toLowerCase(), null);
        if (StringUtils.isNotBlank(tmpKey)) {
            this.workKey = dataEncodeing.decodeData(tmpKey);
        }

        this.fileBase64 = this.getBoolean("fileBase64", false);
    }

    public String encrypt(String source) {
        try {
            byte[] bbs = source.getBytes(workCharSet);

            byte[] bytes = encryptAlgorithm(Cipher.ENCRYPT_MODE, bbs);
            return this.dataEncodeing.encodeData(bytes);
        }
        catch (UnsupportedEncodingException e) {

        }
        return "";
    }

    public String decrypt(String source) {
        try {
            byte[] bbs = this.dataEncodeing.decodeData(source);

            byte[] bytes = encryptAlgorithm(Cipher.DECRYPT_MODE, bbs);
            return new String(bytes, this.workCharSet);

        }
        catch (UnsupportedEncodingException e) {

        }
        return "";
    }

    public String bosDecode(String source, Key privateKey) {

        Cipher cipher;
        try {
            cipher = Cipher.getInstance(this.algorithm);

            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] data = this.dataEncodeing.decodeData(source);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int MAX_DECRYPT_BLOCK = 256;
            int inputLen = data.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();

            return new String(decryptedData, "UTF-8");
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | UnsupportedEncodingException e) {

            e.printStackTrace();
            return null;
        }
    }

    private byte[] encryptAlgorithm(int anMode, byte[] anBytes) {

        Cipher cipher;
        try {
            cipher = createCipher(anMode);

            int offSet = 0;
            byte cache[];
            final int keyDiv = findKeyDiv(anMode);
            int inputLen = anBytes.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for (int i = 0; inputLen - offSet > 0; offSet = ++i * keyDiv) {
                if (inputLen - offSet > keyDiv) {
                    cache = cipher.doFinal(anBytes, offSet, keyDiv);
                } else {
                    cache = cipher.doFinal(anBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
            }

            byte enData[] = out.toByteArray();
            out.close();

            return enData;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new BytterSecurityException(20203, "SignHelper encrypt has error", e);
        }
    }

    protected int findKeyDiv(int anMode) {

        final int keyDiv;
        if (anMode == Cipher.DECRYPT_MODE) {
            keyDiv = (SignHelper.findKeySize(this.keyManager.getPrivKey()) >> 3);
        } else {
            keyDiv = ((SignHelper.findKeySize(this.keyManager.getMatchKey()) >> 3) - 11);
        }
        return keyDiv;
    }

    public boolean encryptFile(File anInFile, File anOutFile) {

        return encryptFile(Cipher.ENCRYPT_MODE, anInFile, anOutFile, this.fileBase64);
    }

    public boolean decryptFile(File anInFile, File anOutFile) {

        return encryptFile(Cipher.DECRYPT_MODE, anInFile, anOutFile, this.fileBase64);
    }

    protected boolean encryptFile(int anMode, File anInFile, File anOutFile, boolean useBase64) {
        InputStream in = null;
        OutputStream out = null;
        if (anInFile.exists() && anInFile.isFile()) {
            try {
                in = new FileInputStream(anInFile);
                out = new FileOutputStream(anOutFile);
                if (anMode == Cipher.DECRYPT_MODE) {
                    if (useBase64) {
                        in = new Base64InputStream(in);
                    }
                } else {
                    if (useBase64) {
                        out = new Base64OutputStream(out);
                    }
                }

                return encryptFile(anMode, in, out);
            }
            catch (Exception ex) {
                logger.warn("encryptFile has error ", ex);
                return false;
            }
            finally {
                IOUtils.closeQuietly(out);
                IOUtils.closeQuietly(in);
            }
        } else {
            logger.warn("encryptFile file :" + anInFile.getAbsolutePath() + ", not exists");
            return false;
        }
    }

    protected Cipher createCipher(int anMode) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher;
        if ("RSA".equalsIgnoreCase(this.algorithm)) {
            cipher = Cipher.getInstance(this.algorithm);
            if (anMode == Cipher.ENCRYPT_MODE) {
                cipher.init(anMode, this.keyManager.getMatchKey());
            } else {
                cipher.init(anMode, this.keyManager.getPrivKey());
            }
        } else {
            SecretKey secretKey = new SecretKeySpec(this.workKey, this.algorithm);
            if (StringUtils.isNotBlank(this.algorithmCBC) && StringUtils.isNotBlank(this.algorithmCBCIV)) {
                cipher = Cipher.getInstance(this.algorithmCBC);
                IvParameterSpec ivSpec = new IvParameterSpec(this.dataEncodeing.decodeData(this.algorithmCBCIV));
                cipher.init(anMode, secretKey, ivSpec);
            } else {
                cipher = Cipher.getInstance(this.algorithm);
                cipher.init(anMode, secretKey);
            }
        }

        return cipher;
    }

    protected boolean encryptFile(int anMode, InputStream anInStream, OutputStream anOutStream) {
        Cipher cipher;
        try {
            cipher = createCipher(anMode);
            final int keyDiv = findKeyDiv(anMode);
            byte[] bytes = new byte[keyDiv];
            int readCount = 0;
            byte cache[];
            while (true) {
                readCount = anInStream.read(bytes);
                if (readCount < 0) {
                    break;
                }
                cache = cipher.doFinal(bytes, 0, readCount);
                anOutStream.write(cache, 0, cache.length);
            }

            anOutStream.close();

            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new BytterSecurityException(20203, "SignHelper encrypt has error", e);
        }
    }

}
