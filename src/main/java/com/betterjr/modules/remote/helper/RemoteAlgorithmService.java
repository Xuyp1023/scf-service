package com.betterjr.modules.remote.helper;

import com.betterjr.common.config.ConfigBaseService;
import com.betterjr.common.security.CustKeyManager;

import java.util.*;

public class RemoteAlgorithmService extends ConfigBaseService {

    private static final long serialVersionUID = 7459826384239720243L;

    // 管理数字证书的工具
    protected CustKeyManager keyManager;

    public void initParameter(Map anConfigInfo, CustKeyManager anKeyManager) {
        this.addAll(anConfigInfo);
        this.keyManager = anKeyManager;
    }

}
