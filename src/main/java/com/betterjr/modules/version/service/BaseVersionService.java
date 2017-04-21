package com.betterjr.modules.version.service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.betterjr.common.dao.SqlMapper;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.mapper.pagehelper.PageHelper;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.generator.SequenceFactory;
import com.betterjr.modules.version.constant.VersionConstantCollentions;
import com.betterjr.modules.version.entity.BaseVersionEntity;

/**
 * 中转版本更替的操作，
 * @author 
 *
 * @param <D>
 * @param <T>
 */
public class BaseVersionService<D extends Mapper<T>, T extends BaseVersionEntity> extends BaseService<Mapper<T>, T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    protected D mapper;
    @Autowired
    protected SqlMapper sqlMapper;
    
    /**
     * 将对象新增版本信息插入数据库并返回该对象
     * 如果当前记录带版本信息直接新增版本然后插入，
     * 如果不带版本信息会新增版本和唯一辨识然后插入,
     * 初始化id属性为null 和isLatest为1
     * @param arg0
     * @return T
     */
    public T insertVersion(T arg0) {
        
        initInsertObjectVersionInfo(arg0);
        int result = this.insert(arg0);
        return result == VersionConstantCollentions.MODIFY_SUCCESS ? arg0 : null;

    }
    
    public T insertVersionSelective(T arg0){
        initInsertObjectVersionInfo(arg0);
        int result = this.insertSelective(arg0);
        return result == VersionConstantCollentions.MODIFY_SUCCESS ? arg0 : null;
    }
    
    /**
     * 
     * 用来更新记录信息，一切以当前对象中的所有属性为准，会创建一条新纪录并且是原来的记录设置为不是最新的状态
     * @param arg0
     * @return
     */
    public synchronized T updateVersionByPrimaryKey(T arg0){
        
        if (arg0 != null) {
            T arg1 = arg0;
            arg1.setIsLatest(VersionConstantCollentions.IS_NOT_LATEST);
            this.updateByPrimaryKey(arg1);

            try {
                long id = SerialGenerator.getLongValue(arg0.getClass().getSimpleName()+".id");
                Method method = arg1.getClass().getMethod("setId", Long.class);
                method.invoke(arg1,id);
            }catch (Exception e) {
                logger.info("初始化插入对象Id出错！"+e.getMessage());
                return null;
            } 
            return this.insertVersion(arg1);
        }

        return null;
        
    }
    
    /**
     * 
     * 用来更新记录信息，一切以当前对象中的所有属性为准，会创建一条新纪录并且是原来的记录设置为不是最新的状态
     * @param arg0
     * @param anVersion 
     * @param anRefNo 
     * @return
     */
    public synchronized T updateVersionByPrimaryKeySelective(T anT, String anRefNo, String anVersion){
        
        if (anT != null) {
            try {
                T newInstance = (T) anT.getClass().newInstance();
                newInstance.setRefNo(anRefNo);
                newInstance.setVersion(anVersion);
                T arg2 = this.selectOne(newInstance);
                checkStatus(arg2.getIsLatest(), VersionConstantCollentions.IS_NOT_LATEST, true, "当前订单已不是最新版本,不允许被编辑");
                arg2.setIsLatest(VersionConstantCollentions.IS_NOT_LATEST);
                this.updateByPrimaryKeySelective(arg2);
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                long id = SerialGenerator.getLongValue(anT.getClass().getSimpleName()+".id");
                anT.setId(id);
            }catch (Exception e) {
                logger.info("初始化插入对象Id出错！"+e.getMessage());
                return null;
            } 
            
            return this.insertVersionSelective(anT);
        }
        
        return null;
        
    }
    
    /**
     * 通过对象查询相似的对象 类似 select方法
     */
    public List<T> selectWithLatest(T arg0) {
        arg0.setIsLatest(VersionConstantCollentions.IS_LATEST);
        return this.select(arg0);
    }
    
    /**
     * 带版本查询一条记录
     */
    public T selectOneWithVersion(T arg0) {
        
        List<T> lists = selectWithLatest(arg0);

        return Collections3.getFirst(lists);
    }
    
    /**
     * 只通过refno,version查询
     */
    public T selectOneWithVersion(String refNo,String version) {
        
        Map<String,Object> paramMap= QueryTermBuilder.newInstance().put("refNo", refNo).put("version", version).build();
        List<T> lists = selectByProperty(paramMap);
        return Collections3.getFirst(lists);
    }
    
    /**
     * 类似selectAll
     */
    public List<T> selectAllWithVersion() {
        Class<T> findGenericClass = findGenericClass();
       try {
             T t = findGenericClass.newInstance();
             //t.setIsLatest(VersionConstantCollentions.IS_LATEST);
             return this.selectWithLatest(t);
        }
        catch (Exception e) {
        }
       return null;
    }
    
    /**
     * 类似 selectByProperty
     */
    public List<T> selectByMapPropertyWithVersion(Map<String, Object> paramMap, String orderBy) {
      
        paramMap.put("isLatest", VersionConstantCollentions.IS_LATEST);
        return this.selectByProperty(paramMap, orderBy);
        
    }
    
    public List selectByClassPropertyWithVersion (Class arg0, Map<String, Object> arg1) {
        return this.selectByClassPropertyWithVersion(arg0, (Map) arg1, (String) null);
    }
    
    /**
     * selectByClassProperty  通过指定条件和实体类查询最新版本的数据信息
     * @param arg0
     * @param arg1
     * @param arg2
     * @return
     */
    public List selectByClassPropertyWithVersion(Class arg0, Map<String, Object> arg1, String arg2) {
        arg1.put("isLatest", VersionConstantCollentions.IS_LATEST);
        return this.selectByClassProperty(arg0, arg1, arg2);
    }
    
    /**
     * 类似 selectByProperty
     */
    public List<T> selectByMapPropertyWithVersion(Map<String, Object> paramMap) {
      
        return selectByMapPropertyWithVersion(paramMap, (String) null);
        
    }
    
    public Page<T> selectPropertyByPageWithVersion (Map<String, Object> paramMap, int anPageNum, int anPageSize, boolean arg3) {
        //PageHelper.startPage(arg1, arg2, arg3);
        return selectPropertyByPageWithVersion(paramMap, anPageNum, anPageSize, arg3, null);
    }

    /**
     * 根据分页查询带版本的符合条件的数据
     * @param paramMap 查询条件
     * @param arg1   当前页数
     * @param arg2   每页的输了
     * @param arg3   是否跳过查询总的条数
     * @param arg4   排序字段
     * @return
     */
    public Page<T> selectPropertyByPageWithVersion (Map<String, Object> paramMap, int arg1, int arg2, boolean arg3, String arg4) {
        paramMap.put("isLatest", VersionConstantCollentions.IS_LATEST);
        PageHelper.startPage(arg1, arg2, arg3);
        return (Page) this.selectByProperty(paramMap, arg4);
    }
    
    /**
     * 根据分页查询带版本的符合条件未生效的数据INEFFECTIVE
     * C_LOCKED_STATUS  未锁定
     * businStatus 为未生效
     * docStatus   为草稿和确认，，不是废止的
     * @param paramMap 查询条件
     * @param arg1   当前页数
     * @param arg2   每页的输了
     * @param arg3   是否跳过查询总的条数
     * @param arg4   排序字段
     * @return
     */
    public Page<T> selectPropertyIneffectiveByPageWithVersion (Map<String, Object> paramMap, int arg1, int arg2, boolean arg3, String arg4) {
        paramMap.put("isLatest", VersionConstantCollentions.IS_LATEST);
        paramMap.put("businStatus", VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE);
        paramMap.put("lockedStatus", VersionConstantCollentions.LOCKED_STATUS_INlOCKED);
        paramMap.put("NEdocStatus", VersionConstantCollentions.DOC_STATUS_ANNUL);
        PageHelper.startPage(arg1, arg2, arg3);
        return (Page) this.selectByProperty(paramMap, arg4);
    }
    
    /**
     * 根据分页查询带版本的符合条件生效的数据EFFECTIVE
     * businStatus 已生效，已使用，已过期，已废止的订单
     * @param paramMap 查询条件
     * @param arg1   当前页数
     * @param arg2   每页的输了
     * @param arg3   是否跳过查询总的条数
     * @param arg4   排序字段
     * @return
     */
    public Page<T> selectPropertyEffectiveByPageWithVersion (Map<String, Object> anParamMap, int arg1, int arg2, boolean arg3, String arg4) {
       
        anParamMap.put("isLatest", VersionConstantCollentions.IS_LATEST);
        anParamMap.put("NEbusinStatus", VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE);
        if(anParamMap.containsKey("businStatus") && anParamMap.get("businStatus")!=null &&VersionConstantCollentions.BUSIN_STATUS_USED.equals(anParamMap.get("businStatus").toString())){
            
            anParamMap.put("lockedStatus", VersionConstantCollentions.LOCKED_STATUS_LOCKED);
            
        }
        PageHelper.startPage(arg1, arg2, arg3);
        return (Page) this.selectByProperty(anParamMap, arg4);
    }
    
    /**
     * 根据状态 查询符合条件的数据
     * @param anParamMap
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     * @param businStatus
     * @param docStatus
     * @param lockedStatus
     * @return
     */
    public Page<T> selectPropertyByPageWithStatus (Map<String, Object> anParamMap, int arg1, int arg2, boolean arg3, String arg4,String businStatus,String docStatus,String lockedStatus) {
        
        anParamMap.put("isLatest", VersionConstantCollentions.IS_LATEST);
        anParamMap.put("businStatus", businStatus);
        anParamMap.put("docStatus", docStatus);
        anParamMap.put("lockedStatus", lockedStatus);
        PageHelper.startPage(arg1, arg2, arg3);
        return (Page) this.selectByProperty(anParamMap, arg4);
    }
    
    public Page<T> selectPropertyCanAunulByPageWithVersion (Map<String, Object> anParamMap, int arg1, int arg2, boolean arg3, String arg4) {
        
        anParamMap.put("isLatest", VersionConstantCollentions.IS_LATEST);
        anParamMap.put("businStatus", VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE);
        anParamMap.put("docStatus", VersionConstantCollentions.DOC_STATUS_CONFIRM);
        anParamMap.put("lockedStatus", VersionConstantCollentions.LOCKED_STATUS_INlOCKED);
        PageHelper.startPage(arg1, arg2, arg3);
        return (Page) this.selectByProperty(anParamMap, arg4);
    }
    
    /**
     * 类似selectCount
     */
    public int selectCountWithVersion(T arg0) {
        arg0.setIsLatest(VersionConstantCollentions.IS_LATEST);
        return this.selectCount(arg0);
    }
    
    /**
     * 类似selectCountByProperty
     */
    public int selectCountByPropertyWithVersion(Map<String, Object> arg0) {
        arg0.put("isLatest", VersionConstantCollentions.IS_LATEST);
        Class arg1 = this.findGenericClass();
        return this.selectCountByClassProperty(arg1, arg0);
    }

    private void initInsertObjectVersionInfo(T arg0) {
        
        if (StringUtils.isNoneBlank(arg0.getVersion())) {
            arg0.setVersion(stringIncrOne(arg0.getVersion()));
        }
        else {
            String refno = assembleRefno(arg0);
            //String uuid = UUID.randomUUID().toString().replace("-", "");
            BTAssert.notNull(refno,"生成凭证编号失败");
            arg0.setRefNo(refno);
            arg0.setVersion(VersionConstantCollentions.INIT_VERSION_INFO);
        }
        
        arg0.setIsLatest(VersionConstantCollentions.IS_LATEST);
        arg0.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE);
        //arg0.setDocStatus(VersionConstantCollentions.DOC_STATUS_DRAFT);
        arg0.setLockedStatus(VersionConstantCollentions.LOCKED_STATUS_INlOCKED);
    }
    
    private String stringIncrOne(String value){
        
        try{
            return (Integer.parseInt(value)+VersionConstantCollentions.VERSION_INCR_RANGE)+"";
        }catch(Exception e){
            return value;
        }
        
    }
    
    private String assembleRefno(T arg0){
        
        BTAssert.notNull(arg0,"对象为空，操作失败");
        
        try {
            
            String simpleName = arg0.getClass().getSimpleName();
            String prefix="ScfOrderDO".equals(simpleName)?"PO":"ScfInvoiceDO".equals(simpleName)?"BI":"ScfAcceptBillDO".equals(simpleName)?"TA":"ScfReceivableDO".equals(simpleName)?"RP":"FR";
            String pattern=prefix+"#{Date:yy}#{Seq:14}";
            String generateRefNo = SequenceFactory.generate("PLAT_"+arg0.getClass().getSimpleName(), pattern);
            if(BetterStringUtils.isBlank(generateRefNo)){
                BTAssert.notNull(null,"获取凭证编号失败!");
            }
            return generateRefNo;
        }
        catch (Exception e) {
            e.printStackTrace();
            BTAssert.notNull(null,"生成凭证编号失败!");
        }
        
        return null;
    }
     
     /**
      * 检查状态信息
      */
     public void checkStatus(String anBusinStatus, String anTargetStatus, boolean anFlag, String anMessage) {
         if (BetterStringUtils.equals(anBusinStatus, anTargetStatus) == anFlag) {
             logger.warn(anMessage);
             throw new BytterTradeException(40001, anMessage);
         }
     }
     
     /**
      * 对单据进行废止
      * @param anOperatorInfo
      * @param arg0
      * @return
      */
     public T annulOperator(CustOperatorInfo anOperatorInfo,T arg0){
         
         checkOperatorModifyStatus(anOperatorInfo, arg0);
         arg0.setDocStatus(VersionConstantCollentions.DOC_STATUS_ANNUL);
         arg0.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_ANNUL);
         int result = this.updateByPrimaryKey(arg0);
         return result == VersionConstantCollentions.MODIFY_SUCCESS ? arg0 : null;
         
     }
     
     /**
      * 对已经生效的单据进行废止 操作
      * @param arg0
      * @return
      */
     public T annulEffectiveOperator(T arg0){
         
         checkEffectiveStatus(arg0);
         arg0.setDocStatus(VersionConstantCollentions.DOC_STATUS_ANNUL);
         arg0.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_ANNUL);
         int result = this.updateByPrimaryKey(arg0);
         return result == VersionConstantCollentions.MODIFY_SUCCESS ? arg0 : null;
         
     }
     
     /**
      * 检查票据生效的状态
      * @param anArg0
      */
     private void checkEffectiveStatus(T anArg0) {
         
         checkStatus(anArg0.getIsLatest(), VersionConstantCollentions.IS_NOT_LATEST, true, "当前单据已不是最新版本,不允许被废止");
         checkStatus(anArg0.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE, true, "当前单据未生效,只能由创建者废止");
         checkStatus(anArg0.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_TRANSFER, true, "当前单据已经转让,不允许被废止");
         checkStatus(anArg0.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_ANNUL, true, "当前单据已经废止,不允许被废止");
         checkStatus(anArg0.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EXPIRE, true, "当前单据已经过期,不允许被废止");
         checkStatus(anArg0.getLockedStatus(), VersionConstantCollentions.LOCKED_STATUS_LOCKED, true, "当前单据已经冻结,不允许被废止");
         checkStatus(anArg0.getDocStatus(), VersionConstantCollentions.DOC_STATUS_ANNUL, true, "当前单据已经废止,不允许被废止");
         checkStatus(anArg0.getDocStatus(), VersionConstantCollentions.DOC_STATUS_DRAFT, true, "当前单据草稿状态,只能由创建者废止");
        
    }
     
     /**
      * 单据修改校验
      * @param anOperatorInfo 当前登陆的用户信息
      * @param T 需要校验的订单信息
      * 修改者必须是新建的用户
      * businStatus=0，1，2，lockedStatus=0 docStatus=0，1 ，isLatest=1这些状态才能修改
      */
      public void checkOperatorModifyStatus(CustOperatorInfo anOperatorInfo, T anAnOrder) {
          
          checkStatus(anAnOrder.getIsLatest(), VersionConstantCollentions.IS_NOT_LATEST, true, "当前单据已不是最新版本,不允许被编辑");
          checkStatus(anOperatorInfo.getId()+"", anAnOrder.getModiOperId()+"", false, "你没有操作权限!请联系创建人修改此单据");
          checkStatus(anAnOrder.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_TRANSFER, true, "当前单据已经转让,不允许被编辑");
          checkStatus(anAnOrder.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_ANNUL, true, "当前单据已经废止,不允许被编辑");
          checkStatus(anAnOrder.getBusinStatus(), VersionConstantCollentions.BUSIN_STATUS_EXPIRE, true, "当前单据已经过期,不允许被编辑");
          checkStatus(anAnOrder.getLockedStatus(), VersionConstantCollentions.LOCKED_STATUS_LOCKED, true, "当前单据已经冻结,不允许被编辑");
          checkStatus(anAnOrder.getDocStatus(), VersionConstantCollentions.DOC_STATUS_ANNUL, true, "当前单据已经废止,不允许被编辑");
      }
      
    /**
      * 对单据进行核准
      * @param anOperatorInfo
      * @param arg0
      * @return
      */
     public T auditOperator(CustOperatorInfo anOperatorInfo,T arg0){
         
         checkStatus(arg0.getIsLatest(), VersionConstantCollentions.IS_NOT_LATEST, true, "当前订单已不是最新版本,不允许被核准");
         if(arg0.getBusinStatus().equals(VersionConstantCollentions.BUSIN_STATUS_INEFFECTIVE)&&
             arg0.getDocStatus().equals(VersionConstantCollentions.DOC_STATUS_CONFIRM) &&
             arg0.getLockedStatus().equals(VersionConstantCollentions.LOCKED_STATUS_INlOCKED)){
             
             arg0.setBusinStatus(VersionConstantCollentions.BUSIN_STATUS_EFFECTIVE);
             arg0.setAuditOperId(anOperatorInfo.getId());
             arg0.setAuditOperName(anOperatorInfo.getName());
             arg0.setAuditData(BetterDateUtils.getNumDate());
             arg0.setAuditTime(BetterDateUtils.getNumTime());
             int result = this.updateByPrimaryKey(arg0);
             return result == VersionConstantCollentions.MODIFY_SUCCESS ? arg0 : null;
         }
         BTAssert.notNull(null,"单据不符合核准的条件");
         return null;
     }
     
    
}
