package com.betterjr.modules.remote.helper;

import java.util.Map;

import com.betterjr.common.security.CustKeyManager;
import com.betterjr.common.security.SignHelper;
 
/**
 * 数据签名实现类，不同的签名继承不同的对象
 * 
 * @author zhoucy
 *
 */
public class RemoteSignService extends RemoteAlgorithmService implements java.io.Serializable {

    private static final long serialVersionUID = -4685964766838180595L;
    protected String algorithm = "SHA1WithRSA";

    @Override
    public void initParameter(Map anConfigInfo, CustKeyManager anKeyManager) {
        super.initParameter(anConfigInfo, anKeyManager);
        this.algorithm = this.getString("sign_algorithm".toLowerCase(), algorithm);
    }

    public String signData(String anData) {
        anData = beforeSignData(anData);
        return SignHelper.signData(anData, keyManager.getPrivKey(), algorithm, this.keyManager.getWorkCharSet());
    }

    protected String beforeSignData(String anData) {

        return anData;
    }

    public boolean verifySign(String anContent, String anSignData) {
        if (this.keyManager.getMatchKey() != null) {
            return SignHelper.verifySign(anContent, anSignData, keyManager.getMatchKey(), algorithm, this.keyManager.getWorkCharSet());
        }
        else {
            return false;
        }
    }
}