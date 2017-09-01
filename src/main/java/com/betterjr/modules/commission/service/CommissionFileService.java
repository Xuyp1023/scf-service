package com.betterjr.modules.commission.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.cert.dubbo.interfaces.IVerifySignCertService;
import com.betterjr.modules.commission.dao.CommissionFileMapper;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;
import com.betterjr.modules.commission.entity.CommissionFile;
import com.betterjr.modules.commission.entity.CommissionFileDown;
import com.betterjr.modules.commission.entity.CommissionRecord;
import com.betterjr.modules.config.dubbo.interfaces.IDomainAttributeService;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.service.DataStoreService;
import com.betterjr.modules.flie.data.ExcelUtils;
import com.betterjr.modules.flie.data.FileResolveConstants;
import com.betterjr.modules.flie.entity.CustFileCloumn;
import com.betterjr.modules.flie.service.CustFileCloumnService;
import com.betterjr.modules.jedis.JedisUtils;
import com.betterjr.modules.supplieroffer.BaseResolveInterface;
import com.betterjr.modules.supplieroffer.entity.ResolveResult;

@Service
public class CommissionFileService extends BaseService<CommissionFileMapper, CommissionFile> {

    
    @Autowired
    private CustAccountService custAccountService;
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileDubboService;
    
    @Reference(interfaceClass = ICustMechBaseService.class)
    private ICustMechBaseService baseService;
    
    @Autowired
    private CommissionRecordService recordService;
    
    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileService;   //查询文件详情
    @Autowired
    private DataStoreService dataStoreService; //将文件转成输入流
    
    @Autowired
    private CustFileCloumnService fileCloumnService; //查询解析文件列对应关系
    
    @Reference(interfaceClass =IVerifySignCertService.class)
    private IVerifySignCertService verifySignCertService;
    
    @Reference(interfaceClass=IDomainAttributeService.class)
    private IDomainAttributeService domainAttributeDubboClientService;
    
    /**
     * 新增佣金文件
     * @param anFile
     * @return
     */
    public synchronized CommissionFile saveAddCommissionFile(CommissionFile anFile) {
        
        BTAssert.notNull(anFile, "新增佣金文件为空");
        BTAssert.notNull(anFile.getFileId(), "新增佣金文件参数出错,文件id为空");
        BTAssert.notNull(anFile.getCustNo(), "新增佣金文件参数出错,供应商为空");
        if(!checkFilePermitOperatorSecond(anFile.getCustNo(), BetterDateUtils.getNumDate())){
            BTAssert.notNull(null, "今天已经审核了全部文件，请明天再上传解析");
        }
        if(!checkFilePermitOperator(anFile.getFileId())){
            BTAssert.notNull(null, "当前文件已经上传成功!请不要重复提交");
        }
        logger.info("Begin to add addCommissionFile"+UserUtils.getOperatorInfo().getName());
        //数字签名
        verifyCommissionFile(anFile.getCustNo(), anFile.getFileId(), anFile.getSignature());
        CustFileItem fileItem = custFileDubboService.findOne(anFile.getFileId());
        anFile.saveAddinit(UserUtils.getOperatorInfo(),fileItem);
        anFile.setCustName(custAccountService.queryCustName(anFile.getCustNo()));
        //操作机构设置为供应商
        anFile.setOperOrg(baseService.findBaseInfo(anFile.getCustNo()).getOperOrg());
        this.insert(anFile);
        logger.info("success to add addCommissionFile"+UserUtils.getOperatorInfo().getName());
        return anFile;
    }

    private void verifyCommissionFile(Long anCustNo,Long anFileId,String ansignatue){
        
        try{
            if(!verifySignCertService.verifyFile(anCustNo, anFileId, ansignatue)){
                logger.info("failer to 数字签名 verifyCommissionFile"+UserUtils.getOperatorInfo().getName()+"  文件id"+anFileId );
                BTAssert.notNull(null, "数字签名失败!"); 
            }
            
        }catch(Exception e){
            logger.info("failer to 数字签名 verifyCommissionFile"+UserUtils.getOperatorInfo().getName()+"  文件id"+anFileId +" 异常为："+e.getMessage());
            BTAssert.notNull(null, "数字签名失败!");  
        }
    }
    
    /**
     * 佣金文件查询
     * @param anQueryMap 查询条件
     * @param anFlag 1：需要查询总的条数
     * @param anPageNum 当前页数
     * @param anPageSize 每页显示总的记录数
     * @return
     */
    public Page queryFileList(Map<String, Object> anMap, String anFlag, int anPageNum, int anPageSize) {
        
        BTAssert.notNull(anMap,"查询佣金文件条件为空");
        //去除空白字符串的查询条件
        anMap = Collections3.filterMapEmptyObject(anMap);
        //查询当前公司的佣金文件
        anMap.put("operOrg", UserUtils.getOperatorInfo().getOperOrg());
        anMap.put("NEbusinStatus", CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_DELETE);
        
        Page<CommissionFile> fileList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
       
        return fileList;
    }

    /**
     * 佣金文件删除
     * @param anRefNo
     * @return
     */
    public synchronized CommissionFile saveDeleteFile(String anRefNo,Map<String, Object> anMap) {
        
        BTAssert.notNull(anRefNo,"删除佣金文件条件不符,");
        logger.info("Begin to delete 佣金文件 saveDeleteFile"+UserUtils.getOperatorInfo().getName()+"  refNo="+anRefNo);
        //Map<String,Object> queryMap = QueryTermBuilder.newInstance().put("refNo", anRefNo).build();
        CommissionFile file = this.selectOne(new CommissionFile(anRefNo));
        //校验文件状态
        checkDeleteFileStatus(file,UserUtils.getOperatorInfo());
        if(anMap !=null && anMap.containsKey("beanName")){
            String beanName = anMap.get("beanName").toString();
            Object bean = SpringContextHolder.getBean(beanName);
            if(bean instanceof BaseResolveInterface){
                BaseResolveInterface resolveInterface=(BaseResolveInterface) bean;   
                resolveInterface.invokeDelete(file.getId());
            }
        }else{
          
            //设置文件删除状态和佣金记录的删除状态
            recordService.saveDeleteStatusByRefNo(file.getId());
        }
        file.setBusinStatus(CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_DELETE);
        this.updateByPrimaryKeySelective(file);
        logger.info("success to delete 佣金文件 saveDeleteFile"+UserUtils.getOperatorInfo().getName()+"  refNo="+anRefNo);
        return file;
    }
    
    /**
     * 查找佣金文件导出模版
     * @return
     */
    public Map<String,Object> findCommissionFileExportTemplate(){
        
        String value = domainAttributeDubboClientService.findString("GLOBAL_COMMISSION_IMPORT_TEMPLATE");
        if(StringUtils.isBlank(value)){
            BTAssert.notNull(null,"删除佣金文件条件不符,");
        }
        Map<String, Object> templateMp = JsonMapper.parserJson(value);
        
        return  templateMp;
    }

    /**
     * 检查是否符合删除的条件
     * @param anFile
     * @param anOperatorInfo
     */
    private void checkDeleteFileStatus(CommissionFile anFile, CustOperatorInfo anOperatorInfo) {
        
        checkStatus(anFile.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_FAILURE, true, "当前文件记录已经开始付款，不能删除");
        checkStatus(anFile.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_SUCCESS, true, "当前文件记录已经开始付款，不能删除");
        checkStatus(anFile.getBusinStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_AUDIT, true, "当前文件记录进入付款流程中，不能删除");
        checkStatus(anFile.getBusinStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_DELETE, true, "当前文件记录已经删除，不能重复删除");
        checkStatus(anFile.getOperOrg(), anOperatorInfo.getOperOrg(), false, "你没有当前文件的删除权限");
        
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
     * 文件解析
     * @param anRefNo
     * @return
     */
    public CommissionFile saveResolveFile(String anRefNo,Map<String, Object> anAnMap) {
        
        BTAssert.notNull(anRefNo,"删除佣金文件条件不符,");
        logger.info("Begin to resolve 佣金文件 saveResolveFile"+UserUtils.getOperatorInfo().getName()+"  refNo="+anRefNo);
        //Map<String,Object> queryMap = QueryTermBuilder.newInstance().put("refNo", anRefNo).build();
        JedisUtils.acquireLock(CommissionConstantCollentions.COMMISSION_FILE_RESOLVE_SUFFIX_KEY+anRefNo, CommissionConstantCollentions.COMMISSION_FILE_RESOLVE_SLEEP_TIME);
        CommissionFile file = this.selectOne(new CommissionFile(anRefNo));
        checkResolveFileStatus(file,UserUtils.getOperatorInfo());
        try {
            if(anAnMap !=null && anAnMap.containsKey("beanName")){
                String beanName = anAnMap.get("beanName").toString();
                Object bean = SpringContextHolder.getBean(beanName);
                if(bean instanceof BaseResolveInterface){
                    BaseResolveInterface resolveInterface=(BaseResolveInterface) bean;   
                    file=resolveCommissionFile(file,resolveInterface);
                }
            }else{
                
                //解析文件
                file=resolveCommissionFile(file,null);
            }
        }
        catch (Exception e) {
            //BTAssert.notNull(null,"解析的佣金文件失败"+file.getShowMessage());
            setResolveFailure(e.getMessage(), file);
        }
        this.updateByPrimaryKeySelective(file);
        logger.info("finsh to resolve 佣金文件 saveResolveFile"+UserUtils.getOperatorInfo().getName()+"  refNo="+anRefNo);
        JedisUtils.releaseLock(CommissionConstantCollentions.COMMISSION_FILE_RESOLVE_SUFFIX_KEY+anRefNo);
        return file;
    }

    /**
     * 解析佣金文件
     * @param anFile
     * @return
     */
    private CommissionFile resolveCommissionFile(CommissionFile anFile,BaseResolveInterface resolveInterface) {
        
        try{
            
            BTAssert.notNull(anFile,"解析的佣金文件不存在");
            BTAssert.notNull(anFile.getFileId(),"解析的佣金文件不存在");
            BTAssert.isNotShorString(anFile.getFileName(),"解析的文件名为空,请重新上传");
            String fileName = anFile.getFileName();
            if( ! (fileName.endsWith("xls") || fileName.endsWith("xlsx")) ){
                BTAssert.notNull(null,"文件读取格式失败,请上传解析文件为excel");
            }
            
            //根据类型查找对应的列
            List<CustFileCloumn> fileCloumnList=fileCloumnService.queryFileCloumnByInfoType(anFile.getInfoType(),"1");
            CustFileItem fileItem = custFileService.findOne(anFile.getFileId());//文件上次详情
            InputStream is = dataStoreService.loadFromStore(fileItem);//得到文件输入流
            if(is==null){
                setResolveFailure("文件解析失败：文件读取失败!", anFile);
                BTAssert.notNull(null,"文件解析失败：文件读取失败!");
            }
            if(Collections3.isEmpty(fileCloumnList)){
                setResolveFailure("文件解析失败：请预先配置模版参数", anFile);
                BTAssert.notNull(null,"文件解析失败：请预先配置模版参数");
            }
            //得到佣金记录的数据集合
            List<Map<String,Object>> listData=resolveFileToListMap(is,fileCloumnList,anFile);
            if(resolveInterface==null){
                
                //插入佣金记录信息
                List<CommissionRecord> recordList = recordService.saveRecordListWithMap(listData);
                int recordAmount=0;
                BigDecimal blance=new BigDecimal(0);
                for (CommissionRecord commissionRecord : recordList) {
                    recordAmount++;
                    blance= MathExtend.add(blance, commissionRecord.getBalance());
                    //blance=MathExtend  //blance+commissionRecord.getBalance();
                }
                anFile.setTotalAmount(recordAmount);
                anFile.setTotalBlance(blance);
            }else{
                ResolveResult invokeResolve = resolveInterface.invokeResolve(listData);
                anFile.setTotalAmount(invokeResolve.getRecordAmount());
                anFile.setTotalBlance(invokeResolve.getBlance());
            }
            setResolveSuccess("文件解析成功", anFile);
        }
        catch(IOException e){
            logger.debug("文件解析失败 ,解析日志记录id 为："+anFile.getId()+"   "+e.getMessage()); 
            setResolveFailure(e.getMessage(), anFile);
        }
        catch(Exception e){
            logger.debug("文件解析失败 ,解析日志记录id 为："+anFile.getId()+"   "+e.getMessage()); 
            setResolveFailure(e.getMessage(), anFile);
        }
        return anFile;
    }

    /**
     * 解析文件封装参数
     * @param anIs
     * @param anFileCloumnList
     * @param anFile
     * @return
     * @throws IOException 
     */
    private List<Map<String, Object>> resolveFileToListMap(InputStream anIs, List<CustFileCloumn> anFileCloumnList, CommissionFile anFile) throws IOException {
        
       
        //将佣金文件中需要存储到拥挤记录的信息存储到map中
        Map<String,Object> appendMap=anFile.resolveToRecordMap(anFile);
        //文件后缀
        //FileUtils.copyInputStreamToFile(anIs, new File("d:\\123.xlsx"));
        String fileType=anFile.getFileName().substring(anFile.getFileName().indexOf(".")+1);
        Integer beginRow = anFileCloumnList.get(0).getBeginRow();
        if(beginRow ==null || beginRow==0){
            beginRow=1; 
        }
        Integer endRow = anFileCloumnList.get(0).getEndRow();
        if(endRow==null){
            endRow=0;
        }
        Sheet sheet = ExcelUtils.parseFileToSheet(anIs, fileType);
        Iterator<Row> rows = sheet.rowIterator();
        int lastRowNum = sheet.getLastRowNum();
        //File file = ExcelUtils.createFile(anIs, fileType);
        //Iterator<Row> rows = ExcelUtils.parseFile(file, fileType);
             BTAssert.notNull(rows,"文件读取失败");
            List<Map<String, Object>> listMap=new ArrayList<>();
            while (rows.hasNext()) {
                Map<String, Object> map=new HashMap<>();
                Row currentRow = rows.next();
                // 模板里的表头，该行跳过
                if (currentRow.getRowNum() < beginRow) {
                    continue;
                }
                
                /*if(currentRow.getRowNum() > CommissionConstantCollentions.COMMISSION_FILE_BEGIN_ROW ){
                    
                    if(StringUtils.isBlank(ExcelUtils.getStringCellValue(currentRow.getCell(0))) 
                            || StringUtils.isBlank(ExcelUtils.getStringCellValue(currentRow.getCell(1)))
                            || StringUtils.isBlank(ExcelUtils.getStringCellValue(currentRow.getCell(2)))){
                        break;
                    }
                    
                }*/
                int rowNum = currentRow.getRowNum() + 1;
                if (rowNum > lastRowNum-endRow+1) {
                    continue;
                }
                for (CustFileCloumn  fileColumn : anFileCloumnList) {
                   
                    String fileColumnProperties=fileColumn.getCloumnProperties();
                    String fileColumnName=fileColumn.getCloumnName();
                    int fileColumnOrder=fileColumn.getCloumnOrder();
                    int isMust=fileColumn.getIsMust();
                    String cloumnType = fileColumn.getCloumnType();
                    String stringCellValue = ExcelUtils.getStringCellValue(currentRow.getCell(fileColumnOrder));
                    if(StringUtils.isBlank(stringCellValue) && isMust==FileResolveConstants.RESOLVE_FILE_IS_MUST){
                        setResolveFailure("第"+rowNum+"行的"+fileColumnName+"不能为空", anFile);
                        BTAssert.notNull(null,"第"+rowNum+"行的"+fileColumnName+"不能为空"); 
                    }
                    if(StringUtils.isNotBlank(stringCellValue) && "n".equals(cloumnType) ){
                        if(! isNumber(stringCellValue)){
                            setResolveFailure("第"+rowNum+"行的"+fileColumnName+"必须为数字类型", anFile);
                            BTAssert.notNull(null,"第"+rowNum+"行的"+fileColumnName+"必须为数字类型"); 
                        }
                    }
                    map.put(fileColumnProperties, stringCellValue);
               }
                map.putAll(appendMap);
                listMap.add(map); 
            }
        
            return listMap;
    }
    
    
    private boolean isNumber(String anValue){
        if (!anValue.matches("\\d+(.\\d+)?[fF]?")) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 佣金文件作废
     * @param anFileId
     * @return
     */
    public CommissionFile saveCannulFile(Long anFileId){
        
        BTAssert.notNull(anFileId,"作废佣金文件失败!条件不正确");
        CustOperatorInfo operatorInfo = UserUtils.getOperatorInfo();
        BTAssert.notNull(operatorInfo,"作废佣金文件失败!请先登录");
        CommissionFile file = this.selectByPrimaryKey(anFileId);
        BTAssert.notNull(file,"作废佣金文件失败!条件不正确");
        checkCannulFileStatus(file,operatorInfo);
        //设置文件删除状态和佣金记录的删除状态
        recordService.saveCannulStatusByFileId(file.getId());
        file.setBusinStatus(CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_CANNUL);
        this.updateByPrimaryKeySelective(file);
        logger.info("success to cannul 佣金文件 saveCannulCommissionFile"+UserUtils.getOperatorInfo().getName()+"  anFileId="+anFileId);
        
        return file;
        
    }
    
    /**
     * 校验当前文件是否符合作废的条件
     * @param anFile
     * @param anOperatorInfo
     */
    private void checkCannulFileStatus(CommissionFile anFile, CustOperatorInfo anOperatorInfo) {
        
        checkStatus(anFile.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_FAILURE, true, "当前文件记录已经付款，不能作废");
        checkStatus(anFile.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_SUCCESS, true, "当前文件记录已经付款，不能作废");
        checkStatus(anFile.getBusinStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_DELETE, true, "当前文件记录已经删除，不能作废");
        checkStatus(anFile.getBusinStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_CANNUL, true, "当前文件记录已经作废，不能重复作废");
        checkStatus(anFile.getConfirmStatus(), CommissionConstantCollentions.COMMISSION_FILE_CONFIRM_STATUS_EFFECTIVE, true, "当前文件记录已经确认通过，不能作废");
        checkStatus(anFile.getOperOrg(), anOperatorInfo.getOperOrg(), false, "你没有当前文件的作废权限");
   
        
    }

    public CommissionFile setResolveFailure(String anShowMessage,CommissionFile anFile){
        
        anFile.setShowMessage(anShowMessage);
        anFile.setBusinStatus(CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_IS_HANDLE);
        anFile.setResolveStatus(CommissionConstantCollentions.COMMISSION_RESOLVE_STATUS_FAILURE);
        
        return anFile;
   
    }
    
    public CommissionFile setResolveSuccess(String anShowMessage,CommissionFile anFile){
        
        anFile.setShowMessage(anShowMessage);
        anFile.setBusinStatus(CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_IS_HANDLE);
        anFile.setResolveStatus(CommissionConstantCollentions.COMMISSION_RESOLVE_STATUS_SUCCESS);
        
        return anFile;
        
    }

    /**
     * 检查符合解析文件的条件
     * @param anFile
     * @param anOperatorInfo
     */
    private void checkResolveFileStatus(CommissionFile anFile, CustOperatorInfo anOperatorInfo) {
        
        checkStatus(anFile.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_FAILURE, true, "当前文件记录已经开始付款，不能解析");
        checkStatus(anFile.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_SUCCESS, true, "当前文件记录已经开始付款，不能解析");
        checkStatus(anFile.getBusinStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_AUDIT, true, "当前文件记录已经审核完成中，不能重复解析");
        checkStatus(anFile.getBusinStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_IS_HANDLE, true, "当前文件记录已经解析完成中，不能重复解析");
        checkStatus(anFile.getBusinStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_DELETE, true, "当前文件记录已经删除，不能重复解析");
        checkStatus(anFile.getOperOrg(), anOperatorInfo.getOperOrg(), false, "你没有当前文件的删除权限");
   
        
    }

    /**
     * 文件审核 并生成下载文件路径
     * @param anFileId
     * @param anFileDownId 
     * @return
     */
    public CommissionFile saveAuditFile(Long anFileId, Long anFileDownId) {

        logger.info("佣金记录批量审核 ：  saveAuditRecordList  审核佣金记录文件  saveAuditFile 开始  审核人："+UserUtils.getOperatorInfo().getName()+"   文件id="+anFileId);
        CommissionFile file = this.selectOne(new CommissionFile(anFileId));
        checkAuditFileStatus(file, UserUtils.getOperatorInfo());
        file.saveAuditInit(UserUtils.getOperatorInfo());
        file.setFileDownId(anFileDownId);
        this.updateByPrimaryKey(file);
        
        logger.info("佣金记录批量审核 ：  saveAuditRecordList  审核佣金记录文件  saveAuditFile  成功 审核人："+UserUtils.getOperatorInfo().getName());
        return file;
        
    }
    
    public CommissionFile saveAuditFile(Long anFileId) {
        
        logger.info("佣金记录批量审核 ：  saveAuditRecordList  审核佣金记录文件  saveAuditFile 开始  审核人："+UserUtils.getOperatorInfo().getName()+"   文件id="+anFileId);
        CommissionFile file = this.selectOne(new CommissionFile(anFileId));
        checkAuditFileStatus(file, UserUtils.getOperatorInfo());
        file.saveAuditInit(UserUtils.getOperatorInfo());
        this.updateByPrimaryKey(file);
        
        logger.info("佣金记录批量审核 ：  saveAuditRecordList  审核佣金记录文件  saveAuditFile  成功 审核人："+UserUtils.getOperatorInfo().getName());
        return file;
        
    }
    
    /**
     * 根据佣金记录生成佣金记录文件
     * @param anRecordList
     * @return
     */
   

    private void checkAuditFileStatus(CommissionFile anFile, CustOperatorInfo anOperatorInfo) {
        
        checkStatus(anFile.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_FAILURE, true, "当前文件记录已经付款，不能重新审核");
        checkStatus(anFile.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_SUCCESS, true, "当前文件记录已经付款，不能重新审核");
        checkStatus(anFile.getBusinStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_DELETE, true, "当前文件记录已经删除，不能重新审核");
        checkStatus(anFile.getOperOrg(), anOperatorInfo.getOperOrg(), false, "你没有当前文件的删除权限");
   
        
    }

    /**
     * 查询未解析的文件  (未解析文件 和解析失败未删除文件和已经审核但是确认未通过的文件)
     * @param anMap
     * @return
     */
    public List<CommissionFile> queryFileNoResolve(Map<String, Object> anMap) {
        
        BTAssert.notNull(anMap,"查询未解析的文件条件为空");
        BTAssert.notNull(anMap.get("importDate"),"查询未解析的文件条件为空");
        BTAssert.notNull(anMap.get("custNo"),"查询未解析的文件为空");
        anMap.put("businStatus", CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_NO_HANDLE);
        List<CommissionFile> noResolvelist = this.selectByProperty(anMap);
        if(Collections3.isEmpty(noResolvelist)){
            noResolvelist=new ArrayList<CommissionFile>();
        }
        anMap.put("businStatus", CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_IS_HANDLE);
        anMap.put("resolveStatus", CommissionConstantCollentions.COMMISSION_RESOLVE_STATUS_FAILURE);
        List<CommissionFile> falilureResolveList = this.selectByClassProperty(CommissionFile.class, anMap);
        noResolvelist.addAll(falilureResolveList);
        anMap.remove("resolveStatus");
        anMap.put("businStatus", CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_AUDIT);
        anMap.put("confirmStatus", CommissionConstantCollentions.COMMISSION_FILE_CONFIRM_STATUS_INEFFECTIVE);
        List<CommissionFile> inEffeciveResolveList = this.selectByClassProperty(CommissionFile.class, anMap);
        noResolvelist.addAll(inEffeciveResolveList);
        anMap.remove("confirmStatus");
        anMap.remove("businStatus");
        return noResolvelist;
    }

    /**
     * 审核多个文件
     * @param anFileSet
     * @param anFileDownId 
     * @return
     */
    public List<CommissionFile> saveAuditFile(Set<Long> anFileSet, Long anFileDownId) {
        
        List<CommissionFile> fileList=new ArrayList<CommissionFile>();
        for (Long fileId : anFileSet) {
            CommissionFile file = saveAuditFile(fileId,anFileDownId);
            fileList.add(file);
        }
        
        return fileList;
        
    }
    
    /**
     * 检查当前企业的解析记录是否审核完全，如果已经审核完成，不允许再次上传文件解析
     * true  表明今天还可以上传文件
     * false  表明今天已经不可以上传文件解析
     * @param anCustNo
     * @param anImportDate
     * @return
     */
    public boolean checkFilePermitOperatorSecond(Long anCustNo,String anImportDate){
        
        Map queryMap = QueryTermBuilder.newInstance()
        .put("custNo", anCustNo)
        .put("importDate", anImportDate)
        .build();
        List<CommissionFile> fileList = this.selectByProperty(queryMap);
        for (CommissionFile file : fileList) {
            
            //已经付款失败，不允许再次上传
            if(CommissionConstantCollentions.COMMISSION_PAY_STATUS_FAILURE.equals(file.getPayStatus())){
                return false;
            }
            
            //已经付款成功，不允许再次上传
            if(CommissionConstantCollentions.COMMISSION_PAY_STATUS_FAILURE.equals(file.getPayStatus())){
                return false;
            }
            
            //已经审核，但是确认状态是确认通过或者未确认状态不允许再次上传
            if(CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_AUDIT.equals(file.getBusinStatus())){
                
                if(!CommissionConstantCollentions.COMMISSION_FILE_CONFIRM_STATUS_INEFFECTIVE.equals(file.getConfirmStatus())){
                    return false;
                    
                }
                
            }
            
        }
        return true;
    }
    
    @Deprecated
    public boolean checkFilePermitOperator(Long anCustNo,String anImportDate){
        
        Map queryMap = QueryTermBuilder.newInstance()
        .put("custNo", anCustNo)
        .put("importDate", anImportDate)
        .put("businStatus", CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_AUDIT)
        .build();
        List<CommissionFile> fileList = this.selectByProperty(queryMap);
        if(Collections3.isEmpty(fileList)){
            List<String> payList=new ArrayList<>();
            payList.add(CommissionConstantCollentions.COMMISSION_PAY_STATUS_SUCCESS);
            payList.add(CommissionConstantCollentions.COMMISSION_PAY_STATUS_FAILURE);
            
            Map queryMap2 = QueryTermBuilder.newInstance()
            .put("custNo", anCustNo)
            .put("importDate", anImportDate)
            .put("payStatus", payList)
            .build();
            List<CommissionFile> fileList2 = this.selectByProperty(queryMap2);
            
            if(Collections3.isEmpty(fileList2)){
                return true;
            }
            
        }
        return false;
    }
    
    private boolean checkFilePermitOperator(Long anFileId) {
        
        Map queryMap = QueryTermBuilder.newInstance()
                .put("fileId", anFileId)
                .build();
                List<CommissionFile> fileList = this.selectByProperty(queryMap);
                if(Collections3.isEmpty(fileList)){
                    return true;
                }
                for (CommissionFile commissionFile : fileList) {
                    if(! commissionFile.getBusinStatus().equals(CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_DELETE)){
                        
                        return false;
                        
                    }
                }
                return true;
    }
    
    /**
     * 如果企业的所有文件已经审核完成，返回true
     * 
     * @param anCustNo
     * @param anImportDate
     * @return
     */
    public boolean checkFileAuditFinish(Long anCustNo,String anImportDate){
        
        
        Map queryMap = QueryTermBuilder.newInstance()
                .put("custNo", anCustNo)
                .put("importDate", anImportDate)
                .put("businStatus", CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_AUDIT)
                .build();
        List<CommissionFile> fileList = this.selectByProperty(queryMap);
        
        return Collections3.isEmpty(fileList)?false:true;
    }

    /**
     * 通过当天总文件id查询当天所有的文件信息
     * @param anFileId
     * @return
     */
    public List<CommissionFile> queryFileListByFileDownId(Long anFileId) {

        Map queryMap = QueryTermBuilder.newInstance()
                .put("fileDownId", anFileId)
                .build();
        List<CommissionFile> fileList = this.selectByProperty(queryMap,"id desc");
        
        return fileList;
    }

    /**
     * 通过导出文件更新佣金文件和佣金记录的状态
     * @param anFileDown
     */
    public List<CommissionFile> saveUpdateByFileDown(CommissionFileDown anFileDown) {
        
        BTAssert.notNull(anFileDown,"审核导出文件不存在！");
        BTAssert.notNull(anFileDown.getId(),"审核导出文件为空");
        BTAssert.notNull(anFileDown.getConfirmStatus(),"审核导出文件条件为空");
        BTAssert.notNull(anFileDown.getConfirmMessage(),"审核导出文件条件为空");
        
        List<CommissionFile> fileList = queryFileListByFileDownId(anFileDown.getId());
        BTAssert.notNull(fileList,"审核导出文件不存在！");
        for (CommissionFile file : fileList) {
            checkOperatorAuditFileStatus(file);
            file.setConfirmDate(anFileDown.getConfirmDate());
            file.setConfirmMessage(anFileDown.getConfirmMessage());
            file.setConfirmStatus(anFileDown.getConfirmStatus());
            file.setConfirmTime(anFileDown.getConfirmTime());
            file.setAuditOperId(anFileDown.getAuditOperId());
            file.setAuditOperName(anFileDown.getAuditOperName());
            this.updateByPrimaryKeySelective(file);
            //更新明细记录信息
            recordService.saveUpdateByCommissionFile(file);
        }
        
        return  fileList;
        
    }

    private void checkOperatorAuditFileStatus(CommissionFile anFile) {

        checkStatus(anFile.getConfirmStatus(), CommissionConstantCollentions.COMMISSION_FILE_CONFIRM_STATUS_EFFECTIVE, true, "当前文件记录已经已经审核通过，不能重复审核");
        checkStatus(anFile.getConfirmStatus(), CommissionConstantCollentions.COMMISSION_FILE_CONFIRM_STATUS_INEFFECTIVE, true, "当前文件记录已经审核未通过，不能重复审核");
        checkStatus(anFile.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_FAILURE, true, "当前文件记录已经付款，不能审核");
        checkStatus(anFile.getPayStatus(), CommissionConstantCollentions.COMMISSION_PAY_STATUS_SUCCESS, true, "当前文件记录已经付款，不能审核");
        checkStatus(anFile.getBusinStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_DELETE, true, "当前文件记录已经删除，不能审核");
        checkStatus(anFile.getBusinStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_CANNUL, true, "当前文件记录已经作废，不能审核");
        checkStatus(anFile.getBusinStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_NO_HANDLE, true, "当前文件记录还未达到审核条件，不能审核");
        checkStatus(anFile.getBusinStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_IS_HANDLE, true, "当前文件记录还未达到审核条件，不能审核");
        
    }


}
