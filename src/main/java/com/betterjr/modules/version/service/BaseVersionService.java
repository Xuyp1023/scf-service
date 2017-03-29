package com.betterjr.modules.version.service;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.betterjr.common.dao.SqlMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.mapper.pagehelper.PageHelper;
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
    public T updateVersionByPrimaryKey(T arg0){
        
        if (arg0 != null) {
            T arg1 = arg0;
            arg1.setIsLatest(VersionConstantCollentions.IS_NOT_LATEST);
            this.updateByPrimaryKey(arg1);
            return this.insertVersion(arg1);
        }

        return null;
        
    }
    
    /**
     * 
     * 用来更新记录信息，一切以当前对象中的所有属性为准，会创建一条新纪录并且是原来的记录设置为不是最新的状态
     * @param arg0
     * @return
     */
    public T updateVersionByPrimaryKeySelective(T arg0){
        
        if (arg0 != null) {
            T arg1 = arg0;
            arg1.setIsLatest(VersionConstantCollentions.IS_NOT_LATEST);
            this.updateByPrimaryKeySelective(arg1);
            return this.insertVersionSelective(arg1);
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
        if(lists !=null && lists.size()>0){
            return lists.get(0);
        }
        return null;
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
        arg0.put("isLatest", "1");
        Class arg1 = this.findGenericClass();
        return this.selectCountByClassProperty(arg1, arg0);
    }

    private void initInsertObjectVersionInfo(T arg0) {
        
        if (StringUtils.isNoneBlank(arg0.getVersion())) {
            arg0.setVersion(stringIncrOne(arg0.getVersion()));
        }
        else {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            arg0.setRefNo(uuid);
            arg0.setVersion(VersionConstantCollentions.INIT_VERSION_INFO);
        }
        
        arg0.setIsLatest(VersionConstantCollentions.IS_LATEST);
        
        try {
            Method method = arg0.getClass().getMethod("setId", Long.class);
            method.invoke(arg0,null);
        }catch (Exception e) {
            logger.info("初始化插入对象Id出错！"+e.getMessage());
        }
    }
    
    private String stringIncrOne(String value){
        
        try{
            return (Integer.parseInt(value)+VersionConstantCollentions.VERSION_INCR_RANGE)+"";
        }catch(Exception e){
            return value;
        }
        
    }

    
}
