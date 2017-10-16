package com.betterjr.modules.supplieroffer.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.mapper.CustTimeJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.supplieroffer.data.AgreementConstantCollentions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_agreement_templent")
public class ScfAgreementTemplate implements BetterjrEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -8205890796419713924L;

    /**
     * 流水号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "流水号", comments = "流水号")
    private Long id;

    /**
     * 客户号
     */
    @Column(name = "L_CORE_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户号", comments = "客户号")
    private Long coreCustNo;

    /**
     * 客户企业名称
     */
    @Column(name = "C_CORE_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户企业名称", comments = "客户企业名称")
    private String coreCustName;

    /**
     * 合同模版存储文件Id（fileitem表）
     */
    @Column(name = "L_TEMPLE_FILE_ID", columnDefinition = "INTEGER")
    @MetaData(value = "合同模版存储文件Id（fileitem表）号", comments = "合同模版存储文件Id（fileitem表）")
    private Long templateFileId;

    /**
     * 合同模版名称
     */
    @Column(name = "C_TEMPLE_FILE_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "合同模版名称", comments = "合同模版名称")
    private String templateFileName;

    /**
     * 合同模版上传批次号
     */
    @Column(name = "N_TEMPLE_BATCHNO", columnDefinition = "INTEGER")
    @MetaData(value = "合同模版上传批次号", comments = "合同模版上传批次号")
    private Long templateBatchNo;

    /**
     * 创建日期
     */
    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "创建日期", comments = "创建日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String regDate;

    /**
     * 创建时间
     */
    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "创建时间", comments = "创建时间")
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    private String regTime;

    /**
     * 注册人
     */
    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "注册人", comments = "注册人")
    private Long regOperId;

    /**
     * 注册人名称
     */
    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "注册人名称", comments = "注册人名称")
    private String regOperName;

    /**
     * 操作机构
     */
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作机构", comments = "操作机构")
    @JsonIgnore
    private String operOrg;

    /**
     * 平台制作ftl模版存储id
     */
    @Column(name = "L_FTL_FILE_ID", columnDefinition = "INTEGER")
    @MetaData(value = "平台制作ftl模版存储id", comments = "平台制作ftl模版存储id")
    private Long ftlFileId;

    /**
     * 平台制作ftl模版存储的批次号
     */
    @Column(name = "N_FTL_BATCHNO", columnDefinition = "INTEGER")
    @MetaData(value = "平台制作ftl模版存储的批次号", comments = "平台制作ftl模版存储的批次号")
    private Long ftlBatchNo;

    /**
     * 模版的状态 0 已删除 1 刚上传模版  2 平台已经制作ftl  3已经激活
     */
    @Column(name = "C_BUSIN_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "模版的状态 0 已删除 1 刚上传模版  2 平台已经制作ftl", comments = "模版的状态 0 已删除 1 刚上传模版  2 平台已经制作ftl")
    private String businStatus;

    /**
     * 创建日期
     */
    @Column(name = "D_DEL_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "创建日期", comments = "创建日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String delDate;

    /**
     * 创建时间
     */
    @Column(name = "T_DEL_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "创建时间", comments = "创建时间")
    private String delTime;

    /**
     * 注册人
     */
    @Column(name = "L_DEL_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "注册人", comments = "注册人")
    private Long delOperId;

    /**
     * 注册人名称
     */
    @Column(name = "C_DEL_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "注册人名称", comments = "注册人名称")
    private String delOperName;

    /**
     * 制作日期
     */
    @Column(name = "D_MAKE_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "制作日期", comments = "制作日期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String makeDate;

    /**
     * 制作时间
     */
    @Column(name = "D_MAK_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "制作时间", comments = "制作时间")
    @JsonSerialize(using = CustTimeJsonSerializer.class)
    private String makeTime;

    public Long getId() {
        return this.id;
    }

    public void setId(Long anId) {
        this.id = anId;
    }

    public Long getCoreCustNo() {
        return this.coreCustNo;
    }

    public void setCoreCustNo(Long anCoreCustNo) {
        this.coreCustNo = anCoreCustNo;
    }

    public String getCoreCustName() {
        return this.coreCustName;
    }

    public void setCoreCustName(String anCoreCustName) {
        this.coreCustName = anCoreCustName;
    }

    public Long getTemplateFileId() {
        return this.templateFileId;
    }

    public void setTemplateFileId(Long anTemplateFileId) {
        this.templateFileId = anTemplateFileId;
    }

    public String getTemplateFileName() {
        return this.templateFileName;
    }

    public void setTemplateFileName(String anTemplateFileName) {
        this.templateFileName = anTemplateFileName;
    }

    public Long getTemplateBatchNo() {
        return this.templateBatchNo;
    }

    public void setTemplateBatchNo(Long anTemplateBatchNo) {
        this.templateBatchNo = anTemplateBatchNo;
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

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public Long getFtlFileId() {
        return this.ftlFileId;
    }

    public void setFtlFileId(Long anFtlFileId) {
        this.ftlFileId = anFtlFileId;
    }

    public Long getFtlBatchNo() {
        return this.ftlBatchNo;
    }

    public void setFtlBatchNo(Long anFtlBatchNo) {
        this.ftlBatchNo = anFtlBatchNo;
    }

    public String getBusinStatus() {
        return this.businStatus;
    }

    public void setBusinStatus(String anBusinStatus) {
        this.businStatus = anBusinStatus;
    }

    public String getDelDate() {
        return this.delDate;
    }

    public void setDelDate(String anDelDate) {
        this.delDate = anDelDate;
    }

    public String getDelTime() {
        return this.delTime;
    }

    public void setDelTime(String anDelTime) {
        this.delTime = anDelTime;
    }

    public Long getDelOperId() {
        return this.delOperId;
    }

    public void setDelOperId(Long anDelOperId) {
        this.delOperId = anDelOperId;
    }

    public String getDelOperName() {
        return this.delOperName;
    }

    public void setDelOperName(String anDelOperName) {
        this.delOperName = anDelOperName;
    }

    public String getMakeDate() {
        return this.makeDate;
    }

    public void setMakeDate(String anMakeDate) {
        this.makeDate = anMakeDate;
    }

    public String getMakeTime() {
        return this.makeTime;
    }

    public void setMakeTime(String anMakeTime) {
        this.makeTime = anMakeTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.businStatus == null) ? 0 : this.businStatus.hashCode());
        result = prime * result + ((this.coreCustName == null) ? 0 : this.coreCustName.hashCode());
        result = prime * result + ((this.coreCustNo == null) ? 0 : this.coreCustNo.hashCode());
        result = prime * result + ((this.ftlBatchNo == null) ? 0 : this.ftlBatchNo.hashCode());
        result = prime * result + ((this.ftlFileId == null) ? 0 : this.ftlFileId.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.operOrg == null) ? 0 : this.operOrg.hashCode());
        result = prime * result + ((this.regDate == null) ? 0 : this.regDate.hashCode());
        result = prime * result + ((this.regOperId == null) ? 0 : this.regOperId.hashCode());
        result = prime * result + ((this.regOperName == null) ? 0 : this.regOperName.hashCode());
        result = prime * result + ((this.regTime == null) ? 0 : this.regTime.hashCode());
        result = prime * result + ((this.templateBatchNo == null) ? 0 : this.templateBatchNo.hashCode());
        result = prime * result + ((this.templateFileId == null) ? 0 : this.templateFileId.hashCode());
        result = prime * result + ((this.templateFileName == null) ? 0 : this.templateFileName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ScfAgreementTemplate other = (ScfAgreementTemplate) obj;
        if (this.businStatus == null) {
            if (other.businStatus != null) return false;
        } else if (!this.businStatus.equals(other.businStatus)) return false;
        if (this.coreCustName == null) {
            if (other.coreCustName != null) return false;
        } else if (!this.coreCustName.equals(other.coreCustName)) return false;
        if (this.coreCustNo == null) {
            if (other.coreCustNo != null) return false;
        } else if (!this.coreCustNo.equals(other.coreCustNo)) return false;
        if (this.ftlBatchNo == null) {
            if (other.ftlBatchNo != null) return false;
        } else if (!this.ftlBatchNo.equals(other.ftlBatchNo)) return false;
        if (this.ftlFileId == null) {
            if (other.ftlFileId != null) return false;
        } else if (!this.ftlFileId.equals(other.ftlFileId)) return false;
        if (this.id == null) {
            if (other.id != null) return false;
        } else if (!this.id.equals(other.id)) return false;
        if (this.operOrg == null) {
            if (other.operOrg != null) return false;
        } else if (!this.operOrg.equals(other.operOrg)) return false;
        if (this.regDate == null) {
            if (other.regDate != null) return false;
        } else if (!this.regDate.equals(other.regDate)) return false;
        if (this.regOperId == null) {
            if (other.regOperId != null) return false;
        } else if (!this.regOperId.equals(other.regOperId)) return false;
        if (this.regOperName == null) {
            if (other.regOperName != null) return false;
        } else if (!this.regOperName.equals(other.regOperName)) return false;
        if (this.regTime == null) {
            if (other.regTime != null) return false;
        } else if (!this.regTime.equals(other.regTime)) return false;
        if (this.templateBatchNo == null) {
            if (other.templateBatchNo != null) return false;
        } else if (!this.templateBatchNo.equals(other.templateBatchNo)) return false;
        if (this.templateFileId == null) {
            if (other.templateFileId != null) return false;
        } else if (!this.templateFileId.equals(other.templateFileId)) return false;
        if (this.templateFileName == null) {
            if (other.templateFileName != null) return false;
        } else if (!this.templateFileName.equals(other.templateFileName)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ScfAgreementTemplate [id=" + this.id + ", coreCustNo=" + this.coreCustNo + ", coreCustName="
                + this.coreCustName + ", templateFileId=" + this.templateFileId + ", templateFileName="
                + this.templateFileName + ", templateBatchNo=" + this.templateBatchNo + ", regDate=" + this.regDate
                + ", regTime=" + this.regTime + ", regOperId=" + this.regOperId + ", regOperName=" + this.regOperName
                + ", operOrg=" + this.operOrg + ", ftlFileId=" + this.ftlFileId + ", ftlBatchNo=" + this.ftlBatchNo
                + ", businStatus=" + this.businStatus + ", delDate=" + this.delDate + ", delTime=" + this.delTime
                + ", delOperId=" + this.delOperId + ", delOperName=" + this.delOperName + ", makeDate=" + this.makeDate
                + ", makeTime=" + this.makeTime + "]";
    }

    public void saveAddValue(CustOperatorInfo anOperatorInfo) {

        BTAssert.notNull(anOperatorInfo, "无法获取登录信息,操作失败");
        this.setId(SerialGenerator.getLongValue("ScfAgreementTemplate.id"));
        this.setBusinStatus(AgreementConstantCollentions.AGREMENT_TEMPLATE_BUSIN_STATUS_NOEFFECTIVE);
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.regOperId = anOperatorInfo.getId();
        this.regOperName = anOperatorInfo.getName();

    }

    public void saveDeleteValue(CustOperatorInfo anOperatorInfo) {

        BTAssert.notNull(anOperatorInfo, "无法获取登录信息,操作失败");
        this.setBusinStatus(AgreementConstantCollentions.AGREMENT_TEMPLATE_BUSIN_STATUS_DELETE);
        this.delDate = BetterDateUtils.getNumDate();
        this.delTime = BetterDateUtils.getNumTime();
        this.delOperId = anOperatorInfo.getId();
        this.delOperName = anOperatorInfo.getName();
    }

    public void saveAllOperatorValue(CustOperatorInfo anOperatorInfo) {

        this.ftlBatchNo = this.templateBatchNo;
        this.ftlFileId = this.templateFileId;
        this.businStatus = AgreementConstantCollentions.AGREMENT_TEMPLATE_BUSIN_STATUS_ACTIVATE;
        this.makeDate = this.regDate;
        this.makeTime = this.regTime;
    }

    /**
     * 平台制作ftl
     * @param anOperatorInfo
     */
    public void saveFtlUpdateValue(CustOperatorInfo anOperatorInfo) {

        this.businStatus = AgreementConstantCollentions.AGREMENT_TEMPLATE_BUSIN_STATUS_EFFECTIVE;
        this.makeDate = this.regDate;
        this.makeTime = this.regTime;

    }

    /**
     * 平台删除已经上传的ftl文件
     * @param anOperatorInfo
     */
    public void saveDeleteFtlValue(CustOperatorInfo anOperatorInfo) {

        this.businStatus = AgreementConstantCollentions.AGREMENT_TEMPLATE_BUSIN_STATUS_NOEFFECTIVE;
        this.makeDate = "";
        this.makeTime = "";
        this.ftlFileId = null;
        this.ftlBatchNo = null;

    }

}
