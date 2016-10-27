package com.betterjr.modules.param.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.modules.param.entity.SupplierParam;
/***
 * 参数查询
 * @author hubl
 *
 */
@Service
public class ParamService {

    private final static Logger logger=LoggerFactory.getLogger(ParamService.class);
    
    /****
     * 查询核心企业设置的参数
     * @param anCustNo
     * @return
     */
    public String queryCoreParam(String anCustNo,String endDate){
        try {
            Map<String,Object> param = DictUtils.loadObject("CoreParam", anCustNo, HashMap.class);
            String agencyCustNo=(String)param.get("agencyNo");
            // 对比一下日期是否已到期限
            double day=Double.parseDouble((String)param.get("day"));
            if(BetterDateUtils.getDistanceOfTwoDate(BetterDateUtils.parseDate(endDate),new Date())>=day){
                return agencyCustNo;
            }
        }
        catch (Exception e) {
            logger.error("参数查询异常："+e.getMessage());
        }
        return "";
    }
    
    /****
     * 检查是否接收
     * @param anSupplierNo
     * @return
     */
    public boolean checkSupplierParam(String anSupplierNo){
        boolean bool=false;
        SupplierParam param = DictUtils.loadObject("SupplierParam", anSupplierNo, SupplierParam.class);
        
        if((param == null) || ("1".equals(param.getIsPush()))){
            bool=true;
        }
        return bool;
    }
    
}
