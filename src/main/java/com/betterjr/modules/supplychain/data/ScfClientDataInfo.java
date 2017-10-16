package com.betterjr.modules.supplychain.data;

public class ScfClientDataInfo {

    // 需要处理数据的对象名称
    private final String workClass;

    // 处理数据的服务名称（Spring)服务
    private final String workService;

    // 相对与CoreCustNo来说的，在资金管理系统中唯一索引，将对该值做处理
    private final String keyField;

    // 客户端过来的业务数据类型
    private final String dataType;

    private final String filterField;

    public ScfClientDataInfo(String anWorkClass, String anWorkService, String anKeyField, String anDataType,
            String anFilterField) {
        this.workClass = anWorkClass;
        this.workService = anWorkService;
        this.keyField = anKeyField;
        this.dataType = anDataType;
        this.filterField = anFilterField;
    }

    public String getWorkClass() {
        return this.workClass;
    }

    public String getKeyField() {
        return this.keyField;
    }

    public String getDataType() {
        return this.dataType;
    }

    public String getWorkService() {
        return this.workService;
    }

    public String getFilterField() {
        return this.filterField;
    }

    public Object findCondition(Object anBtNo, Object anBankAccount) {
        if (this.filterField.endsWith("btNo")) {
            return anBtNo;
        } else if (this.filterField.endsWith("BankAccount")) {
            return anBankAccount;
        }
        return null;
    }
}