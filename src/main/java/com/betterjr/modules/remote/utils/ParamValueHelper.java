package com.betterjr.modules.remote.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.service.SelectKeyGenService;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.modules.remote.data.ParamDataSource;
import com.betterjr.modules.remote.entity.*;
import com.betterjr.modules.rule.RuleContext;
import com.betterjr.modules.rule.service.QlExpressUtil;

public class ParamValueHelper {
    private static ThreadLocal<Map<ParamDataSource, Object>> context = new ThreadLocal();;

    public static void init(WorkFarFaceInfo anFaceInfo, WorkFarFunction anFunc, Object anObj) {
        Map<ParamDataSource, Object> map = new HashMap();
        map.put(ParamDataSource.FACE, anFaceInfo);
        map.put(ParamDataSource.FUNC, anFunc);
        map.put(ParamDataSource.CONFIG, anFaceInfo.getConfig());
        map.put(ParamDataSource.OUTCONVER, anFaceInfo.getOutConverMap());
        map.put(ParamDataSource.INCONVER, anFaceInfo.getInConvertMap());
        map.put(ParamDataSource.CLASS, anObj);
        map.put(ParamDataSource.SELECTKEY, SelectKeyGenService.class);
        map.put(ParamDataSource.DICT, DictUtils.class);

        context.set(map);
    }

    public static Map<String, RemoteFieldInfo> prepareHeadValues(Map<String, FaceHeaderInfo> anHeadMap) {
        Map<String, RemoteFieldInfo> map = new LinkedHashMap();
        FaceHeaderInfo headInfo;
        for (Map.Entry<String, FaceHeaderInfo> ent : anHeadMap.entrySet()) {
            headInfo = ent.getValue();
            headInfo.setHeadValue(getValue(headInfo.getDefValue()));
            map.put(ent.getKey(), headInfo);
        }

        return map;
    }

    public static String getValue(String anKey) {

        if (StringUtils.isNotBlank(anKey)) {
            if (anKey.indexOf("@@") > 0) {
                return anKey.replace("@@", "@");
            }

            String[] arrStr = anKey.split("@");
            if (arrStr.length == 1) {

                return anKey;
            }

            ParamDataSource workSource = ParamDataSource.checking(arrStr[1]);
            Map<ParamDataSource, Object> map = context.get();
            if (workSource == null || map == null) {

                return anKey;
            }

            Object obj = map.get(workSource);
            if (workSource == ParamDataSource.CUSTOM) {
                try {
                    RuleContext ruleC = RuleContext.create("obj", map.get(ParamDataSource.CLASS));
                    Object workObj = QlExpressUtil.simpleInvoke(arrStr[0], ruleC);
                    if (workObj != null) {
                        return workObj.toString();
                    }
                }
                catch (Exception e) {
                }
            }
            else if (arrStr.length == 3) {
                if (obj instanceof Map) {
                    Map tmpMap = (Map) obj;
                    obj = tmpMap.get(arrStr[2]);
                }
            }
            else if (workSource == ParamDataSource.SELECTKEY) {
                arrStr = arrStr[0].split(":");
                if (arrStr.length == 1) {
                obj = SelectKeyGenService.getValue(arrStr[0], arrStr[0]);
                }
                else {
                    obj = SelectKeyGenService.getValue(arrStr[0], arrStr[1]);
                }
                if (obj != null) {
                    return obj.toString();
                }
                return anKey;
            }
            else if (obj instanceof Map) {
                Map tmpMap = (Map) obj;
                obj = tmpMap.get(arrStr[0]);
                if (obj != null) {
                    return obj.toString();
                }
            }
            else if (workSource == ParamDataSource.DICT) {

                obj = DictUtils.getDictLabel(arrStr[1], arrStr[0]);
                if (obj != null) {
                    return obj.toString();
                }
            }
            else {
                String mName = "get" + arrStr[0].substring(0, 1).toUpperCase() + arrStr[0].substring(1);
                Method mm;
                try {
                    mm = obj.getClass().getMethod(mName);

                    obj = mm.invoke(obj);
                    if (obj != null) {

                        return obj.toString();
                    }

                }
                catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return anKey;
    }
}
