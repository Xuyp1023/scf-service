package com.betterjr.modules.supplieroffer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.supplieroffer.entity.ScfSupplierOffer;

@BetterjrMapper
public interface ScfSupplierOfferMapper extends Mapper<ScfSupplierOffer>{
    
    
    @Select("select DISTINCT L_CUSTNO from t_scf_supplier_offer where L_CORE_CUSTNO=#{coreCustNo} and C_BUSIN_STATUS='1'")
    @ResultType(List.class)
    public List<Long> countAllPayResultRecord(@Param("coreCustNo") Long coreCustNo);
    
    @Select("select  * from t_scf_supplier_offer where C_OPER_ROLE=3 and C_BUSIN_STATUS='1' and L_CUSTNO=#{custNo} ORDER BY l_corecust_rate ASC")
    @ResultType(ScfSupplierOffer.class)
    public List<ScfSupplierOffer> queryAllFactoryByCustNo(@Param("custNo") Long custNo);

}
