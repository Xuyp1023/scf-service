package com.betterjr.modules.commission.entity;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.mapper.CustTimeJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.generator.SequenceFactory;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cps_file")
public class CommissionFile implements BetterjrEntity {
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    private Long id;

    //凭证编号
    @Column(name = "C_REFNO",  columnDefinition="VARCHAR" )
    private String refNo;

    //文件上传批次号
    @Column(name = "N_BATCHNO",  columnDefinition="DECIMAL" )
    private Long batchNo;

    //文件摘要
    @Column(name = "C_DIGEST",  columnDefinition="VARCHAR" )
    private String digest;

    //文件签名
    @Column(name = "C_SIGNATURE",  columnDefinition="VARCHAR" )
    private String signature;

    //文件上传id
    @Column(name = "L_FILE_ID",  columnDefinition="INTEGER" )
    private Long fileId;
    
    //文件下载id
    @Column(name = "L_DOWNFILE_ID",  columnDefinition="INTEGER" )
    private Long downFileId;

    //文件上传名称
    @Column(name = "C_FILE_NAME",  columnDefinition="VARCHAR" )
    private String fileName;

    //文件导入日期
    @Column(name = "D_IMPORT_DATE",  columnDefinition="VARCHAR" )
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String importDate;

    //文件导入时间
    @Column(name = "T_IMPORT_TIME",  columnDefinition="VARCHAR" )
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    private String importTime;

    //企业id
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    private Long custNo;

    //当前企业名称
    @Column(name = "C_CUSTNAME",  columnDefinition="VARCHAR" )
    private String custName;

    //操作机构
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    private String operOrg;

    //付款状态 0 未处理 2 支付成功  1 支付失败
    @Column(name = "C_PAY_STATUS",  columnDefinition="VARCHAR" )
    private String payStatus;

    //业务状态 0 未处理 1 已处理 2已审核 3删除
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="VARCHAR" )
    private String businStatus;

    //文件处理状态 1：解析成功  0 解析失败
    @Column(name = "C_RESOLVE_STATUS",  columnDefinition="VARCHAR" )
    private String resolveStatus;

    //文件解析结果提示信息
    @Column(name = "C_SHOW_MESSAGE",  columnDefinition="VARCHAR" )
    private String showMessage;

    //当前文件总金额
    @Column(name = "F_TOTAL_BLANCE",  columnDefinition="DECIMAL" )
    private BigDecimal totalBlance;

    //当前解析文件总的行数
    @Column(name = "L_TOTAL_AMOUNT",  columnDefinition="INTEGER" )
    private Integer totalAmount;

    //解析的文件类型1订单 2票据  3应收账款 4发票 5 合同 6佣金
    @Column(name = "C_INFO_TYPE",  columnDefinition="VARCHAR" )
    private String infoType;
    
    @Column(name = "L_REG_OPERID",  columnDefinition="INTEGER" )
    private Long regOperId;
    
    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    private String regOperName;
    
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;
    
    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    private String regTime;
    
    @Column(name = "L_MODI_OPERID",  columnDefinition="INTEGER" )
    private Long modiOperId;
    
    @Column(name = "C_MODI_OPERNAME",  columnDefinition="VARCHAR" )
    private String modiOperName;
    
    @Column(name = "D_MODI_DATE",  columnDefinition="VARCHAR" )
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String modiDate;
    
    @Column(name = "T_MODI_TIME",  columnDefinition="VARCHAR" )
    private String modiTime;
    
    @Column(name = "N_VERSION",  columnDefinition="VARCHAR" )
    private String version;
    
    private static final long serialVersionUID = -7738651669430689381L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo == null ? null : refNo.trim();
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest == null ? null : digest.trim();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getImportDate() {
        return importDate;
    }

    public void setImportDate(String importDate) {
        this.importDate = importDate == null ? null : importDate.trim();
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime == null ? null : importTime.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus == null ? null : payStatus.trim();
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public String getResolveStatus() {
        return resolveStatus;
    }

    public void setResolveStatus(String resolveStatus) {
        this.resolveStatus = resolveStatus == null ? null : resolveStatus.trim();
    }

    public String getShowMessage() {
        return showMessage;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage == null ? null : showMessage.trim();
    }

    public BigDecimal getTotalBlance() {
        return this.totalBlance;
    }

    public void setTotalBlance(BigDecimal anTotalBlance) {
        this.totalBlance = anTotalBlance;
    }

    public Integer getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(Integer anTotalAmount) {
        this.totalAmount = anTotalAmount;
    }

    public Long getRegOperId() {
        return this.regOperId;
    }

    public void setRegOperId(Long anRegOperId) {
        this.regOperId = anRegOperId;
    }

    public String getRegOperName() {
        return this.regOperName;
    }

    public void setRegOperName(String anRegOperName) {
        this.regOperName = anRegOperName;
    }

    public String getRegDate() {
        return this.regDate;
    }

    public void setRegDate(String anRegDate) {
        this.regDate = anRegDate;
    }

    public String getRegTime() {
        return this.regTime;
    }

    public void setRegTime(String anRegTime) {
        this.regTime = anRegTime;
    }

    public Long getModiOperId() {
        return this.modiOperId;
    }

    public void setModiOperId(Long anModiOperId) {
        this.modiOperId = anModiOperId;
    }

    public String getModiOperName() {
        return this.modiOperName;
    }

    public void setModiOperName(String anModiOperName) {
        this.modiOperName = anModiOperName;
    }

    public String getModiDate() {
        return this.modiDate;
    }

    public void setModiDate(String anModiDate) {
        this.modiDate = anModiDate;
    }

    public String getModiTime() {
        return this.modiTime;
    }

    public void setModiTime(String anModiTime) {
        this.modiTime = anModiTime;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String anVersion) {
        this.version = anVersion;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType == null ? null : infoType.trim();
    }

    public Long getDownFileId() {
        return this.downFileId;
    }

    public void setDownFileId(Long anDownFileId) {
        this.downFileId = anDownFileId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.batchNo == null) ? 0 : this.batchNo.hashCode());
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.custName == null) ? 0 : this.custName.hashCode());
        result = prime * result + ((this.custNo == null) ? 0 : this.custNo.hashCode());
        result = prime * result + ((this.digest == null) ? 0 : this.digest.hashCode());
        result = prime * result + ((this.fileId == null) ? 0 : this.fileId.hashCode());
        result = prime * result + ((this.fileName == null) ? 0 : this.fileName.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.importDate == null) ? 0 : this.importDate.hashCode());
        result = prime * result + ((this.importTime == null) ? 0 : this.importTime.hashCode());
        result = prime * result + ((this.infoType == null) ? 0 : this.infoType.hashCode());
        result = prime * result + ((this.modiDate == null) ? 0 : this.modiDate.hashCode());
        result = prime * result + ((this.modiOperId == null) ? 0 : this.modiOperId.hashCode());
        result = prime * result + ((this.modiOperName == null) ? 0 : this.modiOperName.hashCode());
        result = prime * result + ((this.modiTime == null) ? 0 : this.modiTime.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.payStatus == null) ? 0 : this.payStatus.hashCode());
        result = prime * result + ((this.refNo == null) ? 0 : this.refNo.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regOperId == null) ? 0 : this.regOperId.hashCode());
        result = prime * result + ((this.regOperName == null) ? 0 : this.regOperName.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.resolveStatus == null) ? 0 : this.resolveStatus.hashCode());
        result = prime * result + ((this.showMessage == null) ? 0 : this.showMessage.hashCode());
        result = prime * result + ((this.signature == null) ? 0 : this.signature.hashCode());
        result = prime * result + ((this.totalAmount == null) ? 0 : this.totalAmount.hashCode());
        result = prime * result + ((this.totalBlance == null) ? 0 : this.totalBlance.hashCode());
        result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CommissionFile other = (CommissionFile) obj;
        if (this.batchNo == null) {
            if (other.batchNo != null) return false;
        }
        else if (!this.batchNo.equals(other.batchNo)) return false;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        }
        else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.custName == null) {
            if (other.custName != null) return false;
        }
        else if (!this.custName.equals(other.custName)) return false;
        if (this.custNo == null) {
            if (other.custNo != null) return false;
        }
        else if (!this.custNo.equals(other.custNo)) return false;
        if (this.digest == null) {
            if (other.digest != null) return false;
        }
        else if (!this.digest.equals(other.digest)) return false;
        if (this.fileId == null) {
            if (other.fileId != null) return false;
        }
        else if (!this.fileId.equals(other.fileId)) return false;
        if (this.fileName == null) {
            if (other.fileName != null) return false;
        }
        else if (!this.fileName.equals(other.fileName)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        }
        else if (!this.id.equals(other.id)) return false;
        if (this.importDate == null) {
            if (other.importDate != null) return false;
        }
        else if (!this.importDate.equals(other.importDate)) return false;
        if (this.importTime == null) {
            if (other.importTime != null) return false;
        }
        else if (!this.importTime.equals(other.importTime)) return false;
        if (this.infoType == null) {
            if (other.infoType != null) return false;
        }
        else if (!this.infoType.equals(other.infoType)) return false;
        if (this.modiDate == null) {
            if (other.modiDate != null) return false;
        }
        else if (!this.modiDate.equals(other.modiDate)) return false;
        if (this.modiOperId == null) {
            if (other.modiOperId != null) return false;
        }
        else if (!this.modiOperId.equals(other.modiOperId)) return false;
        if (this.modiOperName == null) {
            if (other.modiOperName != null) return false;
        }
        else if (!this.modiOperName.equals(other.modiOperName)) return false;
        if (this.modiTime == null) {
            if (other.modiTime != null) return false;
        }
        else if (!this.modiTime.equals(other.modiTime)) return false;
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        }
        else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.payStatus == null) {
            if (other.payStatus != null) return false;
        }
        else if (!this.payStatus.equals(other.payStatus)) return false;
        if (this.refNo == null) {
            if (other.refNo != null) return false;
        }
        else if (!this.refNo.equals(other.refNo)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        }
        else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regOperId == null) {
            if (other.regOperId != null) return false;
        }
        else if (!this.regOperId.equals(other.regOperId)) return false;
        if (this.regOperName == null) {
            if (other.regOperName != null) return false;
        }
        else if (!this.regOperName.equals(other.regOperName)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        }
        else if (!this.regTime.equals(other.regTime)) return false;
        if (this.resolveStatus == null) {
            if (other.resolveStatus != null) return false;
        }
        else if (!this.resolveStatus.equals(other.resolveStatus)) return false;
        if (this.showMessage == null) {
            if (other.showMessage != null) return false;
        }
        else if (!this.showMessage.equals(other.showMessage)) return false;
        if (this.signature == null) {
            if (other.signature != null) return false;
        }
        else if (!this.signature.equals(other.signature)) return false;
        if (this.totalAmount == null) {
            if (other.totalAmount != null) return false;
        }
        else if (!this.totalAmount.equals(other.totalAmount)) return false;
        if (this.totalBlance == null) {
            if (other.totalBlance != null) return false;
        }
        else if (!this.totalBlance.equals(other.totalBlance)) return false;
        if (this.version == null) {
            if (other.version != null) return false;
        }
        else if (!this.version.equals(other.version)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "CommissionFile [id=" + this.id + ", refNo=" + this.refNo + ", batchNo=" + this.batchNo + ", digest=" + this.digest + ", signature="
                + this.signature + ", fileId=" + this.fileId + ", downFileId=" + this.downFileId + ", fileName=" + this.fileName + ", importDate="
                + this.importDate + ", importTime=" + this.importTime + ", custNo=" + this.custNo + ", custName=" + this.custName + ", operOrg="
                + this.operOrg + ", payStatus=" + this.payStatus + ", businStatus=" + this.businStatus + ", resolveStatus=" + this.resolveStatus
                + ", showMessage=" + this.showMessage + ", totalBlance=" + this.totalBlance + ", totalAmount=" + this.totalAmount + ", infoType="
                + this.infoType + ", regOperId=" + this.regOperId + ", regOperName=" + this.regOperName + ", regDate=" + this.regDate + ", regTime="
                + this.regTime + ", modiOperId=" + this.modiOperId + ", modiOperName=" + this.modiOperName + ", modiDate=" + this.modiDate
                + ", modiTime=" + this.modiTime + ", version=" + this.version + "]";
    }

    public CommissionFile saveAddinit(CustOperatorInfo anOperatorInfo, CustFileItem anFileItem) {
        
        BTAssert.notNull(anOperatorInfo, "新增佣金当前未登录");
        BTAssert.notNull(anFileItem, "当前文件未上传成功");
        this.setBusinStatus(CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_NO_HANDLE);
        this.setPayStatus(CommissionConstantCollentions.COMMISSION_PAY_STATUS_NO_HANDLE);
        this.setImportDate(BetterDateUtils.getNumDate());
        this.setImportTime(BetterDateUtils.getNumTime());
        this.setInfoType(CommissionConstantCollentions.COMMISSION_FILE_INFO_TYPE);
        this.setOperOrg(anOperatorInfo.getOperOrg());
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setRegTime(BetterDateUtils.getNumTime());
        this.setRegOperId(anOperatorInfo.getId());
        this.setRegOperName(anOperatorInfo.getName());
        this.setVersion("1");
        this.setBatchNo(anFileItem.getBatchNo());
        checkFileName(anFileItem.getFileName());
        this.setFileName(anFileItem.getFileName());
        this.refNo= SequenceFactory.generate("PLAT_COMMON", "#{Date:yyyyMMdd}#{Seq:12}","D");
        this.id=SerialGenerator.getLongValue("CommissionFile.id");
        return this;
        
    }
    
    private void checkFileName(String anFileName){
        
        if(StringUtils.isBlank(anFileName)){
            BTAssert.notNull(null, "上传的文件应该是excel文件");
        }
        
        if(!( anFileName.endsWith("xls") || anFileName.endsWith("xlsx"))){
            BTAssert.notNull(null, "上传的文件应该是excel文件");
        }
        
        
    }

    public CommissionFile(String anRefNo) {
        super();
        this.refNo = anRefNo;
    }

    public CommissionFile() {
        super();
    }
    
    public CommissionFile(Long anId) {
        super();
        this.id = anId;
    }

    public Map<String, Object> resolveToRecordMap(CommissionFile anFile) {
        
        Map<String,Object> appendMap=QueryTermBuilder.newInstance()
                .put("custNo", anFile.getCustNo())
                .put("custName", anFile.getCustName())
                .put("operOrg", anFile.getOperOrg())
                .put("importDate", anFile.getImportDate())
                .put("importTime", anFile.getImportTime())
                .put("fileId", anFile.getId())
                .build();
        return appendMap;
    }

    public CommissionFile saveAuditInit(CustOperatorInfo anOperatorInfo) {

        this.setBusinStatus(CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_AUDIT);
        this.setModiDate(BetterDateUtils.getNumDate());
        this.setModiTime(BetterDateUtils.getNumTime());
        this.setModiOperId(anOperatorInfo.getId());
        this.setModiTime(anOperatorInfo.getName());
        return this;
        
    }
    
    

}