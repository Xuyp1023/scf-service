package com.betterjr.modules.order.service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.asset.data.AssetConstantCollentions;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.service.DataStoreService;
import com.betterjr.modules.flie.data.ExcelImportUtils;
import com.betterjr.modules.flie.data.FileResolveConstants;
import com.betterjr.modules.flie.entity.CustFileCloumn;
import com.betterjr.modules.flie.service.CustFileCloumnService;
import com.betterjr.modules.order.dao.ScfOrderDOMapper;
import com.betterjr.modules.order.entity.ScfOrderDO;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
import com.betterjr.modules.version.service.BaseVersionService;

@Service
public class ScfOrderDOService extends BaseVersionService<ScfOrderDOMapper, ScfOrderDO> {

    
    @Autowired
    private CustAccountService custAccountService;
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;

    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;

    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService custMechBaseService;
    
    @Autowired
    private CustFileCloumnService fileCloumnService;
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileService;
    @Autowired
    private DataStoreService dataStoreService;
    
    /**
     * 
     * @param anOrder  订单保存对象
     * @param anFileList 附件列表
     * @param anOtherFileList
     * @param confirmFlag  是否需要确认 true 为新增确认    false 为新增
     * @return
     */
     public ScfOrderDO addOrder(ScfOrderDO anOrder, String anFileList,boolean anConfirmFlag) {
         
         BTAssert.notNull(anOrder,"插入订单为空,操作失败");
         BTAssert.notNull(anOrder.getCustNo(),"请选择供应商,操作失败");
         BTAssert.notNull(anOrder.getCoreCustNo(),"请选择核心企业,操作失败");
         logger.info("Begin to add order"+UserUtils.getOperatorInfo().getName());
         anOrder.initAddValue(UserUtils.getOperatorInfo(),anConfirmFlag);
         // 操作机构设置为供应商
         anOrder.setOperOrg(baseService.findBaseInfo(anOrder.getCustNo()).getOperOrg());
         anOrder.setCustName(custAccountService.queryCustName(anOrder.getCustNo()));
         anOrder.setCoreCustName(custAccountService.queryCustName(anOrder.getCoreCustNo()));
         // 保存附件信息
         anOrder.setBatchNo(custFileDubboService.updateCustFileItemInfo(anFileList, anOrder.getBatchNo()));
         anOrder=this.insertVersion(anOrder);
         logger.info("success to add order"+UserUtils.getOperatorInfo().getName());
         return anOrder;
     }
     
     /**
      * 订单信息编辑
      */
     public ScfOrderDO saveModifyOrder(ScfOrderDO anModiOrder,String anFileList,boolean anConfirmFlag) {
         
         BTAssert.notNull(anModiOrder,"订单为空,操作失败");
         BTAssert.notNull(anModiOrder.getRefNo(),"凭证编号为空,操作失败");
         BTAssert.notNull(anModiOrder.getVersion(),"订单信息不全,操作失败");
         logger.info("Begin to modify order");
         ScfOrderDO order = this.selectOneWithVersion(anModiOrder.getRefNo(),anModiOrder.getVersion());
         BTAssert.notNull(order, "无法获取订单信息");
         //校验当前操作员是否是创建此订单的人 并且校验当前订单是否允许修改
         checkOperatorModifyStatus(UserUtils.getOperatorInfo(),order);
         // 应收账款信息变更迁移初始化
         //anModiOrder.setId(anOrder.getId());
         order.setCustName(custAccountService.queryCustName(anModiOrder.getCustNo()));
         order.setCoreCustName(custAccountService.queryCustName(anModiOrder.getCoreCustNo()));
         // 操作机构设置为供应商
         order.setOperOrg(baseService.findBaseInfo(order.getCustNo()).getOperOrg());
         order=anModiOrder.initModifyValue(order);
         // 保存附件信息
         if(StringUtils.isNotBlank(anFileList)){
             order.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo(anFileList, order.getBatchNo()));
         }else{
             order.setBatchNo(custFileDubboService.updateAndDelCustFileItemInfo("", order.getBatchNo())); 
         }
         // 初始版本更改 直接返回
         if(order.getBusinStatus().equals(VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE)&&
                 order.getDocStatus().equals(VersionConstantCollentions.DOC_STATUS_DRAFT)){
             if(anConfirmFlag){
                 order.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM); 
             }
              // 数据存盘
             this.updateByPrimaryKeySelective(order);
             return order;
         }
         // 需要升级版本的修改
           order.setIsLatest(VersionConstantCollentions.IS_NOT_LATEST);
           order.setLockedStatus(VersionConstantCollentions.LOCKED_STATUS_LOCKED);
           order.setDocStatus(VersionConstantCollentions.DOC_STATUS_DRAFT);
         //this.updateByPrimaryKeySelective(anOrder);
         if(anConfirmFlag){
             order.setDocStatus(VersionConstantCollentions.DOC_STATUS_CONFIRM); 
         }
         //ScfOrderDO order2 = this.selectOneWithVersion(order.getRefNo(),order.getVersion());
         anModiOrder=this.updateVersionByPrimaryKeySelective(order,order.getRefNo(),order.getVersion());
         BTAssert.notNull(anModiOrder, "修改订单失败");
         logger.info("success to modify order"+UserUtils.getOperatorInfo().getName());
         return anModiOrder;
     }

     /**
      * 作废当前订单
      * @param refNo
      * @param version
      * @return
      */
     public ScfOrderDO saveAnnulOrder(String anRefNo,String anVersion){
         
         BTAssert.notNull(anRefNo, "订单凭证单号为空!操作失败");
         BTAssert.notNull(anVersion, "操作异常为空!操作失败");
         ScfOrderDO order = this.selectOneWithVersion(anRefNo, anVersion);
         BTAssert.notNull(order, "此订单异常!操作失败");
         order=this.annulOperator(UserUtils.getOperatorInfo(), order);
         return order;
         
     }
     
     /**
      * 查询当前订单
      * @param refNo
      * @param version
      * @return
      */
     public ScfOrderDO findOrder(String anRefNo,String anVersion){
         
         BTAssert.notNull(anRefNo, "订单凭证单号为空!操作失败");
         BTAssert.notNull(anVersion, "操作异常为空!操作失败");
         ScfOrderDO order = this.selectOneWithVersion(anRefNo, anVersion);
         BTAssert.notNull(order, "此订单异常!操作失败");
         return order;
     }
     
     /**
      * 
      * @param anRefNo
      * @param anVersion
      * @return
      */
     public ScfOrderDO saveAuditOrder(String anRefNo,String anVersion){
         
         BTAssert.notNull(anRefNo, "订单凭证单号为空!操作失败");
         BTAssert.notNull(anVersion, "操作异常为空!操作失败");
         ScfOrderDO order = this.selectOneWithVersion(anRefNo, anVersion);
         BTAssert.notNull(order, "此订单异常!操作失败");
         Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
         BTAssert.notNull(custInfos, "获取当前企业失败!操作失败");
         if(! getCustNoList(custInfos).contains(order.getCoreCustNo())){
             BTAssert.notNull(order, "您没有审核权限!操作失败"); 
         }
         this.auditOperator(UserUtils.getOperatorInfo(), order);
         return order;
         
     }
     
     /**
      * 批量审核订单文件
      * @param ids
      * @return
      */
     public List<ScfOrderDO> saveAuditOrders(String ids){
         
         BTAssert.notNull(ids, "审核文件对象查询失败!操作失败");
         if(StringUtils.isBlank(ids)){
             BTAssert.notNull(null, "审核文件对象查询失败!操作失败"); 
         }
         List<Long> idList=covernStringToLongList(ids,",");
         List<ScfOrderDO> orders=new ArrayList<>(idList.size());
         for (Long id : idList) {
            ScfOrderDO order = this.selectByPrimaryKey(id);
            BTAssert.notNull(order, "此订单异常!操作失败");
            Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
            BTAssert.notNull(custInfos, "获取当前企业失败!操作失败");
            if(! getCustNoList(custInfos).contains(order.getCoreCustNo())){
                BTAssert.notNull(order, "您没有审核权限!操作失败"); 
            }
            this.auditOperator(UserUtils.getOperatorInfo(), order);
            orders.add(order);
        }
         
         return orders;
     }
     
    private List<Long> covernStringToLongList(String anIds, String anString) {
        
        List<Long> idList=new ArrayList<>();
        if(StringUtils.isNoneBlank(anIds) && StringUtils.isNoneBlank(anString)){
            if(anIds.contains(anString)){
                
                String[]  ids= anIds.split(anString);
                for (String id : ids) {
                    if(ExcelImportUtils.isDecimal(id)){
                        idList.add(Long.parseLong(id));
                    }
                }
            }else{
                idList.add(Long.parseLong(anIds));
            }
        }
        
        return idList;
    }

    /**
     * 订单信息分页查询
     * @param anMap 查询条件封装
     * @param anFlag 是否需要查询总的数量。1 需要       不为1： 为不需要
     * @param anPageNum 当前页数
     * @param anPageSize 每页显示的数量
     * anIsOnlyNormal 1 数据来源与新增
     * @return
     */
     public Page<ScfOrderDO> queryOrder(Map<String, Object> anMap,String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {
         
         // 操作员只能查询本机构数据
         // anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
         // 只查询数据非自动生成的数据来源
         if("1".equals(anIsOnlyNormal)){
             anMap.put("dataSource", "1");
         }
         
         if(anMap.containsKey("businStatus") && anMap.get("businStatus")!=null &&VersionConstantCollentions.BUSIN_STATUS_USED.equals(anMap.get("businStatus").toString())){
             
             anMap.put("lockedStatus", VersionConstantCollentions.LOCKED_STATUS_LOCKED);
             
         }
         anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
         
         Page<ScfOrderDO> anOrderList = this.selectPropertyByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "refNo desc");

         return anOrderList;
     }
     
     /**
      * 订单信息未生效分页查询
      * 查询是否编辑和确认 废止的订单  必须是当前用户的自己创建修改的
      * 是查询需要审核的订单   必须是自己公司的订单
      * @param anMap 查询条件封装
      * @param anFlag 是否需要查询总的数量。1 需要       不为1： 为不需要
      * @param anPageNum 当前页数
      * @param anPageSize 每页显示的数量
      * anIsOnlyNormal 1 数据来源与新增
      * anIsAudit true 是查询需要审核的订单     false 查询是否编辑和确认 废止的订单
      * @return
      */
     public Page<ScfOrderDO> queryIneffectiveOrder(Map<String, Object> anMap,String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize,boolean anIsAudit) {
         
         BTAssert.notNull(anMap, "查询条件为空!操作失败");
         // 操作员只能查询本机构数据
         // anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
         // 只查询数据非自动生成的数据来源
         if("1".equals(anIsOnlyNormal)){
             anMap.put("dataSource", "1");
         }
         
         //去除空白字符串的查询条件
         anMap = Collections3.filterMapEmptyObject(anMap);
         
         if(anIsAudit){
             
             if (! anMap.containsKey("coreCustNo") ||  anMap.get("coreCustNo") ==null || StringUtils.isBlank(anMap.get("coreCustNo").toString())) {
                 
                 Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
                 anMap.put("coreCustNo", getCustNoList(custInfos));
             }
             anMap.put("docStatus", VersionConstantCollentions.DOC_STATUS_CONFIRM);
             
         }else{
             anMap.put("modiOperId", UserUtils.getOperatorInfo().getId());
         }
         
         Page<ScfOrderDO> orderList = this.selectPropertyIneffectiveByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
         
         return orderList;
     }
     
     /**
      * 订单信息已生效分页查询
      * isCustNo true 是供应商的已经生效的查询    false 是核心企业的已经生效的查询
      * 是查询需要审核的订单   必须是自己公司的订单
      * @param anMap 查询条件封装
      * @param anFlag 是否需要查询总的数量。1 需要       不为1： 为不需要
      * @param anPageNum 当前页数
      * @param anPageSize 每页显示的数量
      * anIsOnlyNormal 1 数据来源与新增
      * 
      * @return
      */
     public Page<ScfOrderDO> queryEffectiveOrder(Map<String, Object> anMap,String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize,boolean anIsCust) {
         
         BTAssert.notNull(anMap, "查询条件为空!操作失败");
         // 操作员只能查询本机构数据
         // anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
         // 只查询数据非自动生成的数据来源
         if("1".equals(anIsOnlyNormal)){
             anMap.put("dataSource", "1");
         }
         
         //去除空白字符串的查询条件
         anMap = Collections3.filterMapEmptyObject(anMap);
         
         Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
         
         if(anIsCust){
             
             if (! anMap.containsKey("custNo") ||  anMap.get("custNo") ==null || StringUtils.isBlank(anMap.get("custNo").toString())) {
                 anMap.put("custNo", getCustNoList(custInfos));
             }
             //anMap.put("custNo", getCustNoList(custInfos));
             anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
           //供应商去除已经废止的票据信息
             anMap.put("NEbusinStatus",VersionConstantCollentions.BUSIN_STATUS_ANNUL);
             
         }else{
             
             if (! anMap.containsKey("coreCustNo") ||  anMap.get("coreCustNo") ==null || StringUtils.isBlank(anMap.get("coreCustNo").toString())) {
                 anMap.put("coreCustNo", getCustNoList(custInfos));
             }
             //anMap.put("coreCustNo", getCustNoList(custInfos));
             return this.selectPropertyByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
         }
         
         Page<ScfOrderDO> orderList = this.selectPropertyEffectiveByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
         
         return orderList;
     }
     
     /**
      * 将给定的企业集合中提取企业的id
      * @param custInfos
      * @return
      */
     private List<Long> getCustNoList(Collection<CustInfo> custInfos) {
         
         List<Long> custNos=new ArrayList<>();
         for (CustInfo custInfo : custInfos) {
             custNos.add(custInfo.getCustNo());
        }
         
         return custNos;
         
     }

     /**
      * 查询能够作废的单据列表
      * @param anAnQueryConditionMap
      * @param anIsOnlyNormal
      * @param anFlag
      * @param anPageNum
      * @param anPageSize
      * @return
      */
    public Page<ScfOrderDO> queryCanAnnulBill(Map<String, Object> anMap, String anIsOnlyNormal, String anFlag, int anPageNum, int anPageSize) {
        BTAssert.notNull(anMap, "查询条件为空!操作失败");
        // 操作员只能查询本机构数据
        // anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        // 只查询数据非自动生成的数据来源
        if("1".equals(anIsOnlyNormal)){
            anMap.put("dataSource", "1");
        }
        
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        
        if (! anMap.containsKey("coreCustNo") ||  anMap.get("coreCustNo") ==null || StringUtils.isBlank(anMap.get("coreCustNo").toString())) {
            
            Collection<CustInfo> custInfos = custMechBaseService.queryCustInfoByOperId(UserUtils.getOperatorInfo().getId());
            anMap.put("coreCustNo", getCustNoList(custInfos));
        }
        
        Page<ScfOrderDO> orderList = this.selectPropertyCanAunulByPageWithVersion(anMap, anPageNum, anPageSize, "1".equals(anFlag), "refNo desc");
        
        return orderList;
    }

    /**
     * 解析文件插入订单数据  
     * regDate","regTime","modiOperId","modiOperName","operOrg"
                                                ,"custNo","coreCustNo","batchNo" +模版中的属性
     * @return
     */
    public List<ScfOrderDO> saveResolveFile(List<Map<String,Object>> listMap) {
        List<ScfOrderDO> list=new ArrayList<>();
        List<CustFileCloumn> fileCloumnList=fileCloumnService.queryFileCloumnByInfoTypeIsMust(AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER,"1");
        int i=FileResolveConstants.BEGIN_ROW_ORDER;
        for (Map<String, Object> map : listMap) {
            i++;
            BTAssert.notNull(map.get("custNo"), "第选择供应商!再上传");
            BTAssert.notNull(map.get("coreCustNo"), "第选择核心企业!再上传");
            for(CustFileCloumn fileCloumn:fileCloumnList){
                BTAssert.notNull(map.get(fileCloumn.getCloumnProperties()), "第"+i+"行的"+fileCloumn.getCloumnName()+"值为空,请重新上传"); 
            }
            map=initAddOrderWithMap(map);
            ScfOrderDO order=new ScfOrderDO();
            try {
                BeanUtils.populate(order, map);
                order=this.insertVersion(order);
                BTAssert.notNull(order,"第"+i+"行的数据有问题,请重新上传"); 
                list.add(order);
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                logger.info("文件上次map转对象订单失败"+map+" "+e.getMessage());
            }
            catch (Exception e) {
                logger.info("第"+i+"行的数据有问题,请重新上传"+" "+e.getMessage());
                BTAssert.notNull(null,"第"+i+"行的数据有问题,请重新上传"+e.getMessage()); 
            }
        }
        
        return list;
    }

    private Map<String, Object> initAddOrderWithMap(Map<String, Object> anMap) {
        
        Long custNo=Long.parseLong(anMap.get("custNo").toString());
        anMap.put("custName",custAccountService.queryCustName(custNo));
        Long coreCustNo=Long.parseLong(anMap.get("coreCustNo").toString());
        anMap.put("coreCustName", custAccountService.queryCustName(coreCustNo));
        anMap.put("dataSource", 2);
        anMap.put("description","excel 导入");
        anMap.put("id", SerialGenerator.getLongValue("ScfOrderDO.id"));
        anMap.put("businStatus",VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE);
        anMap.put("lockedStatus",VersionConstantCollentions.LOCKED_STATUS_INlOCKED);
        anMap.put("docStatus",VersionConstantCollentions.DOC_STATUS_DRAFT);
        return anMap;
    }

    public Page<ScfOrderDO> queryExportOrderRecordList(Long anResolveFileid, String anFlag, int anPageNum, int anPageSize) {
        
        BTAssert.notNull(anResolveFileid, "查询导入数据失败");
        
        Map<String,Object> paramMap= QueryTermBuilder.newInstance()
                .put("resolveFileId", anResolveFileid)
                .build();
        
        Page<ScfOrderDO> orderList = this.selectPropertyByPage(paramMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
        
        return orderList;
    }

    public List<ScfOrderDO> saveResolveOrderFile(Map<String, Object> anAnMap) {

        BTAssert.notNull(anAnMap, "解析的文件为空");
        BTAssert.notNull(anAnMap.get("custNo"), "请选择供应商!再上传");
        BTAssert.notNull(anAnMap.get("coreCustNo"), "请选择核心企业!再上传");
        BTAssert.notNull(anAnMap.get("id"), "请选择解析文件再上传");
        
        CustFileItem fileItem = custFileService.findOne(Long.parseLong(anAnMap.get("id").toString()));//文件上次详情
        InputStream is = dataStoreService.loadFromStore(fileItem);
        List<ScfOrderDO> orders=new ArrayList<>();
        try {
            orders = ExcelImportUtils.importObj(ScfOrderDO.class, is, fileItem.getFileType());
            for (ScfOrderDO scfOrderDO : orders) {
                scfOrderDO.setDescription("excel文件导入");
                scfOrderDO.setCoreCustNo(Long.parseLong(anAnMap.get("coreCustNo").toString()));
                scfOrderDO.setCustNo(Long.parseLong(anAnMap.get("custNo").toString()));
                scfOrderDO.setResolveFileId(fileItem.getId());
                addOrder(scfOrderDO, "", true);
            }
        }
        catch (Exception e) {
            logger.info(UserUtils.getOperatorInfo().getName()+"文件导入失败! 文件Id为："+fileItem.getId()+"  产生错误信息："+e.getMessage());
            BTAssert.notNull(null, e.getMessage());
        }
        return orders;
    }

}
