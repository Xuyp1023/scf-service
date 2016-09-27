package com.betterjr.modules.remote.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.modules.remote.utils.RemoteBaseConverter;
import com.betterjr.modules.remote.utils.XmlUtils;

import java.util.*;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_FACE_CONVERT")
public class FaceConvertInfo implements BetterjrEntity {
    /**
     * 接口，可以是文件和在线接口
     */
    @Column(name = "C_FACE", columnDefinition = "VARCHAR")
    @MetaData(value = "接口", comments = "接口，可以是文件和在线接口")
    private String faceNo;

    /**
     * 数据项TXT名称
     */
    @Column(name = "C_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项TXT名称", comments = "数据项TXT名称")
    private String fieldName;

    /**
     * 数据项读写方向，0相互映射，1转出，即数据往外写，2转入，即数据从外面进入系统；9表示在系统中使用CLASS来转换
     */
    @Column(name = "C_IO",  columnDefinition="VARCHAR" )
    @MetaData( value="数据项读写方向", comments = "数据项读写方向，0相互映射，1转出，即数据往外写，2转入，即数据从外面进入系统；9表示在系统中使用CLASS来转换")
    private String direction;

    /**
     * 数据项源项，如果IO定义为9，则为 数据项内容解析的CLASS全称包括路径
     */
    @Column(name = "C_SRC", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项源项", comments = "数据项源项，如果IO定义为9，则为 数据项内容解析的CLASS全称包括路径")
    private String source;

    /**
     * 数据项目的项，如果IO定义为9，则为 数据项内容生成的CLASS全称包括路径
     */
    @Column(name = "C_DEST", columnDefinition = "VARCHAR")
    @MetaData(value = "数据项目的项", comments = "数据项目的项，如果IO定义为9，则为 数据项内容生成的CLASS全称包括路径")
    private String target;

    /**
     * 描述
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "描述", comments = "描述")
    private String description;

    /**
     * 修改时间
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改时间", comments = "修改时间")
    private String modiDate;

    /**
     * 处理数据转换的JAVA CLASS实现转换接口
     */
    @Column(name = "C_CLASS", columnDefinition = "VARCHAR")
    @MetaData(value = "处理数据转换的JAVA CLASS实现转换接口", comments = "处理数据转换的JAVA CLASS实现转换接口")
    private String workClass;

    private static final long serialVersionUID = 1441077082484L;
    private RemoteBaseConverter converter;
    // private WorkFarFaceInfo workFace;
    private Map<String, String> map = new HashMap();

    public String convert(WorkFaceFieldInfo fieldInfo, String anFaceFieldName, String anValue) {
        if (this.converter == null) {
            String tmpStr = map.get(anValue);
            if (StringUtils.isNotBlank(tmpStr)) {
                return tmpStr;
            }
            else {
                return anValue;
            }
        }
        else {
            return converter.convert(fieldInfo, anFaceFieldName, anValue);
        }
    }

    private void addItemValue(String anSource, String anDest) {
        this.map.put(anSource, anDest);
    }

    public void mergeConvert(Map<String, FaceConvertInfo> converMap) {
        FaceConvertInfo tmpFace = converMap.get(this.getFieldName());
        if (tmpFace == null) {
            tmpFace = this;
            converMap.put(tmpFace.getFieldName(), tmpFace);
        }

        tmpFace.addItemValue(this.getSource(), this.getTarget());
        if (StringUtils.isNotBlank(this.workClass)) {
            tmpFace.setWorkClass(this.workClass);
        }

    }

    public void init(WorkFarFaceInfo anFaceInfo) {
        // this.workFace = anFaceInfo;
        String tmpClassName = workClass;
        // 如果没有包体名称，则认为和在一个目录
        if (StringUtils.isNotBlank(tmpClassName)) {
            if (XmlUtils.split(tmpClassName, ".").size() == 1) {
                tmpClassName = RemoteBaseConverter.class.getPackage().getName().concat(".").concat(tmpClassName);
            }
            try {
                this.converter = (RemoteBaseConverter) Class.forName(tmpClassName).newInstance();
            }
            catch (ClassNotFoundException e) {
                throw new BytterValidException(25067, "FaceConvertInfo init fund workClass ClassNotFoundException", e);
            }
            catch (InstantiationException e) {
                throw new BytterValidException(25068, "FaceConvertInfo init fund workClass InstantiationException", e);

            }
            catch (IllegalAccessException e) {
                throw new BytterValidException(25069, "FaceConvertInfo init fund workClass IllegalAccessException", e);

            }
        }
    }

    public void swapSource(String anInutDirect) {
        if ("2".equalsIgnoreCase(anInutDirect) && "0".equalsIgnoreCase(this.direction)) {
            String tmpStr = this.source;
            this.source = this.target;
            this.target = tmpStr;
        }
    }

    public String getFaceNo() {
        return faceNo;
    }

    public void setFaceNo(String faceNo) {
        this.faceNo = faceNo == null ? null : faceNo.trim();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction == null ? null : direction.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target == null ? null : target.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getWorkClass() {
        return workClass;
    }

    public void setWorkClass(String workClass) {
        this.workClass = workClass == null ? null : workClass.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", faceNo=").append(faceNo);
        sb.append(", fieldName=").append(fieldName);
        sb.append(", direction=").append(direction);
        sb.append(", source=").append(source);
        sb.append(", target=").append(target);
        sb.append(", description=").append(description);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", workClass=").append(workClass);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FaceConvertInfo other = (FaceConvertInfo) that;
        return (this.getFaceNo() == null ? other.getFaceNo() == null : this.getFaceNo().equals(other.getFaceNo()))
                && (this.getFieldName() == null ? other.getFieldName() == null : this.getFieldName().equals(other.getFieldName()))
                && (this.getDirection() == null ? other.getDirection() == null : this.getDirection().equals(other.getDirection()))
                && (this.getSource() == null ? other.getSource() == null : this.getSource().equals(other.getSource()))
                && (this.getTarget() == null ? other.getTarget() == null : this.getTarget().equals(other.getTarget()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getWorkClass() == null ? other.getWorkClass() == null : this.getWorkClass().equals(other.getWorkClass()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFaceNo() == null) ? 0 : getFaceNo().hashCode());
        result = prime * result + ((getFieldName() == null) ? 0 : getFieldName().hashCode());
        result = prime * result + ((getDirection() == null) ? 0 : getDirection().hashCode());
        result = prime * result + ((getSource() == null) ? 0 : getSource().hashCode());
        result = prime * result + ((getTarget() == null) ? 0 : getTarget().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getWorkClass() == null) ? 0 : getWorkClass().hashCode());
        return result;
    }
}