package com.betterjr.modules.loan.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.modules.loan.dao.ScfSignAgreementRelationMapper;
import com.betterjr.modules.loan.entity.ScfSignAgreementRelation;

@Service
public class ScfSignAgreementRelationService extends BaseService<ScfSignAgreementRelationMapper, ScfSignAgreementRelation> {

	public ScfSignAgreementRelation addRelation(ScfSignAgreementRelation anRelation){
		BTAssert.notNull(anRelation, "保存记录失败-anRelation不能为空");
		anRelation.init();
		this.insert(anRelation);
		return anRelation;
	}
	
	/**
	 * 根据申请编号和合同类型查询合同
	 * @param anRequestNo
	 * @param anType
	 * @return
	 */
	public ScfSignAgreementRelation findRelation(String anRequestNo, String anType){
		BTAssert.notNull(anRequestNo, "查询记录失败-anRequestNo不能为空");
		BTAssert.notNull(anType, "查询记录失败-anType不能为空");
		Map<String, Object> map = QueryTermBuilder.newInstance().put("requestNo", anRequestNo).put("type", anType).build();
		List<ScfSignAgreementRelation> list = this.selectByClassProperty(ScfSignAgreementRelation.class, map);
		if(!Collections3.isEmpty(list)){
			Collections3.getFirst(list);
		}
		return new ScfSignAgreementRelation();
	}
	
	/**
	 * 根据申请编号查询改申请的所有合同
	 * @param anRequestNo
	 * @return
	 */
	public List<ScfSignAgreementRelation> findRelationListByRequestNo(String anRequestNo){
		BTAssert.notNull(anRequestNo, "查询记录失败-anRequestNo不能为空");
		Map<String, Object> map = QueryTermBuilder.newInstance().put("requestNo", anRequestNo).build();
		return this.selectByClassProperty(ScfSignAgreementRelation.class, map);
	}
}
