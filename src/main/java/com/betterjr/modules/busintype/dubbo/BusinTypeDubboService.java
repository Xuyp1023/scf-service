package com.betterjr.modules.busintype.dubbo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.businType.IScfBusinTypeService;
import com.betterjr.modules.busintype.Service.ScfBusinTypeService;

@Service(interfaceClass = IScfBusinTypeService.class)
public class BusinTypeDubboService implements IScfBusinTypeService {

    @Autowired
    private ScfBusinTypeService businTypeService;

    @Override
    public String webQueryBusinType(Map<String, Object> anMap) {

        return AjaxObject.newOk("查询成功", businTypeService.queryEffectiveBusinType(anMap)).toJson();
    }

}
