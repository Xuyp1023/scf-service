package com.betterjr.modules.remote.serializer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;

import org.apache.commons.io.IOUtils;

import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.security.CustKeyManager;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.betterjr.modules.remote.connection.RemoteConnection;
import com.betterjr.modules.remote.entity.FarConfigInfo;
import com.betterjr.modules.remote.entity.WorkFaceFieldInfo;
import com.betterjr.modules.remote.entity.WorkFarFunction;
import com.opencsv.CSVReader;

/**
 * 解析从远端下载下来的文本文件。 主要有两种情况，一种是有头信息，一种是没有头信息。 头信息主要描述了交易的情况，例如：确认金额，确认份额的合计等内容。起校验的作用。 需要在config中定义起始的记录行数或标志。 设计思路，文件都是按照
 * 
 * @author henry
 *
 */
public class TextRemoteDeSerializer extends RemoteBaseDeSerializer implements RemoteDeSerializer {

    private static final long serialVersionUID = 4975873332459749906L;
    private RemoteConnection conn = null;

    @Override
    public void init(WorkFarFunction anFunc, RemoteConnection anConn, Map<String, FarConfigInfo> anConfigInfo, CustKeyManager anKeyManager) {
        super.initParameter(anConfigInfo, anKeyManager);
        this.func = anFunc;
        this.conn = anConn;
    }

    public List<Map<String, Object>> readResult() {

        return (List) parseData(this.conn.getWorkData()).get("data");
    }

    /**
     * 根据定义的信息，获得创建的定义关系；如果存在的定义，则直接使用，如果没有则创建。
     * 
     * @param anPropMap
     * @return
     */
    protected Map<String, WorkFaceFieldInfo> buildFieldMap(Map<String, WorkFaceFieldInfo> anPropMap) {
        String defFieldOrder = this.getString("text.fieldMap."+ this.func.getFunCode());
        // 如果没有定义，直接使用fieldMap的值
        if (BetterStringUtils.isNotBlank(defFieldOrder)) {
            String[] arrStr = defFieldOrder.split(";");
            // anPropMap大于或等于表示已经处理过了。
            // if (anPropMap.size() <= arrStr.length) {
            String bfield;
            WorkFaceFieldInfo workField;
            Field refField;
            Class resultClass = this.func.getWorkReturnClass();
            for (int i = 0, k = arrStr.length; i < k; i++) {
                bfield = arrStr[i].trim();
                // 如果不需要的数据，使用空格作为占位符号
                if (BetterStringUtils.isBlank(bfield)) {
                    continue;
                }
                workField = anPropMap.get(bfield);
                if (workField == null) {
                    // 表示不存在，需要根据JAVABean的属性信息产生映射关系
                    refField = ReflectionUtils.getClassField(resultClass, bfield);
                    if (refField == null) {
                        throw new BytterValidException(87654, "declare field " + bfield + ", not find in resultClass" + resultClass.getName());
                    }
                    workField = new WorkFaceFieldInfo(i, refField, this.func);
                    anPropMap.put(workField.getBeanField(), workField);
                }
                else {
                    workField.setFieldOrder(i);
                }
            }
            // }
        }
        return anPropMap;
    }

    private WorkFaceFieldInfo findFieldInfo(Collection<WorkFaceFieldInfo> anList, int anIndex) {
        for (WorkFaceFieldInfo fieldInfo : anList) {
            if (fieldInfo.getFieldOrder().intValue() == anIndex) {
                return fieldInfo;
            }
        }
        return null;
    }

    /**
     * 解析文本文件
     */
    protected Map<String, Object> parseData(Object anObj) {
        BufferedReader reader = null;
        String tmpCharset = this.getString("text.charset", "GBK");
        int skipLine = this.getInt("text.skipLine", 1);
        String splitChar = this.getString("text.separator", "|");
        String quotechar = this.getString("text.quotechar", "\"");
        String escapeChar = this.getString("text.escapeChar", "\\");
        CSVReader dataReader = null;
        Map<String, Object> data = new HashMap();
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        data.put("data", dataList);
        try {
            reader = new BufferedReader(new InputStreamReader((InputStream)anObj, tmpCharset));
            dataReader = new CSVReader(reader, splitChar.charAt(0), quotechar.charAt(0), escapeChar.charAt(0), skipLine);
            Iterator<String[]> tmpData = dataReader.iterator();
            Map<String, WorkFaceFieldInfo> propMap = buildFieldMap(this.func.getPropMap());
            Map<String, String> tmpMapValues = null;
            WorkFaceFieldInfo fieldInfo = null;
            while (tmpData.hasNext()) {
                String[] tmpArr = tmpData.next();
                tmpMapValues = new HashMap();
                for (int i = 0, k = tmpArr.length; i < k; i++) {
                    fieldInfo = findFieldInfo(propMap.values(), i);
                    if (fieldInfo != null) {
                        tmpMapValues.put(fieldInfo.getBeanField(), tmpArr[i]);
                    }
                }
                dataList.add(tmpMapValues);
            }
        }
        catch (Exception e) {
            throw new BytterValidException(87651, "parse Text File Data hash error", e);
        }
        finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(dataReader);
        }
        return data;
    }
}
