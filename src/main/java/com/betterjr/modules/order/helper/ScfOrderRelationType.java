package com.betterjr.modules.order.helper;

public enum ScfOrderRelationType {
        AGGREMENT("0", "合同"), 
        INVOICE("1", "发票"), 
        TRANSPORT("2", "运输单据"), 
        ACCEPTBILL("3", "汇票"), 
        RECEIVABLE("4", "应收账款");

        private String code;
        private String name;
        
        private ScfOrderRelationType(String anCode, String anName) {
            this.code = anCode;
            this.name = anName;
        }
        
        public String getCode() {
            return this.code;
        }

        public void setCode(String anCode) {
            this.code = anCode;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String anName) {
            this.name = anName;
        }



}   
