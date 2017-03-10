package com.betterjr.modules.template.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_scf_contract_template")
public class ScfContractTemplate implements BetterjrEntity {
    @Id
    @Column(name = "ID", columnDefinition = "BIGINT")
    @MetaData(value = "", comments = "")
    @OrderBy("desc")
    private Long id;

    /**
     * 保理公司编号
     */
    @Column(name = "L_FACTORNO", columnDefinition = "BIGINT")
    @MetaData(value = "保理公司编号", comments = "保理公司编号")
    private Long factorNo;

    /**
     * 合同名称
     */
    @Column(name = "C_TEMPLATE_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "合同名称", comments = "合同名称")
    private String templateName;

    /**
     * 合同类型
     */
    @Column(name = "C_TEMPLATE_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "合同类型", comments = "合同类型 billTransNotice：应该收账款转让通知书，buyerConfirm：应该收账款转让确认意见书， threePartProtocol：三方协议")
    private String templateType;

    /**
     * 保理公司编号
     */
    @Column(name = "C_TEMPLATE_STATUS", columnDefinition = "CHAR")
    @MetaData(value = "合同状态", comments = "合同状态 0:禁用， 1启用")
    private String templateStatus;

    /**
     * 上传的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO", columnDefinition = "INT")
    @MetaData(value = "上传的批次号", comments = "上传的批次号，对应fileinfo中的ID")
    private Long batchNo;

    /**
     * 合同存储路径
     */
    @Column(name = "C_TEMPLATE_PATH", columnDefinition = "VARCHAR")
    @MetaData(value = "合同存储路径", comments = "合同存储路径")
    private String templatePath;

    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String regDate;

    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "", comments = "")
    private String regTime;

    @Transient
    private String factorName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFactorNo() {
        return factorNo;
    }

    public void setFactorNo(Long factorNo) {
        this.factorNo = factorNo;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTemplateStatus() {
        return templateStatus;
    }

    public void setTemplateStatus(String templateStatus) {
        this.templateStatus = templateStatus;
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((batchNo == null) ? 0 : batchNo.hashCode());
        result = prime * result + ((factorNo == null) ? 0 : factorNo.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((regDate == null) ? 0 : regDate.hashCode());
        result = prime * result + ((regTime == null) ? 0 : regTime.hashCode());
        result = prime * result + ((templateName == null) ? 0 : templateName.hashCode());
        result = prime * result + ((templatePath == null) ? 0 : templatePath.hashCode());
        result = prime * result + ((templateStatus == null) ? 0 : templateStatus.hashCode());
        result = prime * result + ((templateType == null) ? 0 : templateType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ScfContractTemplate other = (ScfContractTemplate) obj;
        if (batchNo == null) {
            if (other.batchNo != null)
                return false;
        } else if (!batchNo.equals(other.batchNo))
            return false;
        if (factorNo == null) {
            if (other.factorNo != null)
                return false;
        } else if (!factorNo.equals(other.factorNo))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (regDate == null) {
            if (other.regDate != null)
                return false;
        } else if (!regDate.equals(other.regDate))
            return false;
        if (regTime == null) {
            if (other.regTime != null)
                return false;
        } else if (!regTime.equals(other.regTime))
            return false;
        if (templateName == null) {
            if (other.templateName != null)
                return false;
        } else if (!templateName.equals(other.templateName))
            return false;
        if (templatePath == null) {
            if (other.templatePath != null)
                return false;
        } else if (!templatePath.equals(other.templatePath))
            return false;
        if (templateStatus == null) {
            if (other.templateStatus != null)
                return false;
        } else if (!templateStatus.equals(other.templateStatus))
            return false;
        if (templateType == null) {
            if (other.templateType != null)
                return false;
        } else if (!templateType.equals(other.templateType))
            return false;
        return true;
    }

    public void initValue() {
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.id = SerialGenerator.getLongValue("ContractTemplate.id");
	}

	public String getFactorName() {
		return factorName;
	}

	public void setFactorName(String factorName) {
		this.factorName = factorName;
	}

}
