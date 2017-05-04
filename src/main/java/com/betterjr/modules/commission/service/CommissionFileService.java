package com.betterjr.modules.commission.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.MathExtend;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.commission.dao.CommissionFileMapper;
import com.betterjr.modules.commission.data.CommissionConstantCollentions;
import com.betterjr.modules.commission.entity.CommissionFile;
import com.betterjr.modules.commission.entity.CommissionRecord;
import com.betterjr.modules.customer.ICustMechBaseService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.service.DataStoreService;
import com.betterjr.modules.flie.data.ExcelUtils;
import com.betterjr.modules.flie.data.FileResolveConstants;
import com.betterjr.modules.flie.entity.CustFileCloumn;
import com.betterjr.modules.flie.service.CustFileCloumnService;

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
    
    /**
     * 新增佣金文件
     * @param anFile
     * @return
     */
    public CommissionFile saveAddCommissionFile(CommissionFile anFile) {
        
        BTAssert.notNull(anFile, "新增佣金文件为空");
        BTAssert.notNull(anFile.getFileId(), "新增佣金文件参数出错,文件id为空");
        BTAssert.notNull(anFile.getCustNo(), "新增佣金文件参数出错,供应商为空");
        logger.info("Begin to add addCommissionFile"+UserUtils.getOperatorInfo().getName());
        CustFileItem fileItem = custFileDubboService.findOne(anFile.getFileId());
        anFile.saveAddinit(UserUtils.getOperatorInfo(),fileItem);
        anFile.setCustName(custAccountService.queryCustName(anFile.getCustNo()));
        //操作机构设置为供应商
        anFile.setOperOrg(baseService.findBaseInfo(anFile.getCustNo()).getOperOrg());
        this.insert(anFile);
        logger.info("success to add addCommissionFile"+UserUtils.getOperatorInfo().getName());
        return anFile;
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
        
        Page<CommissionFile> fileList = this.selectPropertyByPage(anMap, anPageNum, anPageSize, "1".equals(anFlag), "id desc");
       
        return fileList;
    }

    /**
     * 佣金文件删除
     * @param anRefNo
     * @return
     */
    public synchronized CommissionFile saveDeleteFile(String anRefNo) {
        
        BTAssert.notNull(anRefNo,"删除佣金文件条件不符,");
        logger.info("Begin to delete 佣金文件 saveDeleteFile"+UserUtils.getOperatorInfo().getName()+"  refNo="+anRefNo);
        //Map<String,Object> queryMap = QueryTermBuilder.newInstance().put("refNo", anRefNo).build();
        CommissionFile file = this.selectOne(new CommissionFile(anRefNo));
        //校验文件状态
        checkDeleteFileStatus(file,UserUtils.getOperatorInfo());
        //设置文件删除状态和佣金记录的删除状态
        recordService.saveDeleteStatusByRefNo(file.getId());
        file.setBusinStatus(CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_DELETE);
        this.updateByPrimaryKeySelective(file);
        logger.info("success to delete 佣金文件 saveDeleteFile"+UserUtils.getOperatorInfo().getName()+"  refNo="+anRefNo);
        return file;
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
        checkStatus(anFile.getPayStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_DELETE, true, "当前文件记录已经删除，不能重复删除");
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
    public CommissionFile saveResolveFile(String anRefNo) {
        
        BTAssert.notNull(anRefNo,"删除佣金文件条件不符,");
        logger.info("Begin to resolve 佣金文件 saveResolveFile"+UserUtils.getOperatorInfo().getName()+"  refNo="+anRefNo);
        //Map<String,Object> queryMap = QueryTermBuilder.newInstance().put("refNo", anRefNo).build();
        CommissionFile file = this.selectOne(new CommissionFile(anRefNo));
        try {
            checkResolveFileStatus(file,UserUtils.getOperatorInfo());
            //解析文件
            file=resolveCommissionFile(file);
        }
        catch (Exception e) {
            //BTAssert.notNull(null,"解析的佣金文件失败"+file.getShowMessage());
            setResolveFailure(e.getMessage(), file);
        }
        this.updateByPrimaryKeySelective(file);
        logger.info("finsh to resolve 佣金文件 saveResolveFile"+UserUtils.getOperatorInfo().getName()+"  refNo="+anRefNo);
        return file;
    }

    /**
     * 解析佣金文件
     * @param anFile
     * @return
     */
    private CommissionFile resolveCommissionFile(CommissionFile anFile) {
        
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
        Iterator<Row> rows = ExcelUtils.parseFile(anIs, fileType);
        //File file = ExcelUtils.createFile(anIs, fileType);
        //Iterator<Row> rows = ExcelUtils.parseFile(file, fileType);
             BTAssert.notNull(rows,"文件读取失败");
            List<Map<String, Object>> listMap=new ArrayList<>();
            while (rows.hasNext()) {
                Map<String, Object> map=new HashMap<>();
                Row currentRow = rows.next();
                // 模板里的表头，该行跳过
                if (currentRow.getRowNum() < CommissionConstantCollentions.COMMISSION_FILE_BEGIN_ROW) {
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
        checkStatus(anFile.getPayStatus(), CommissionConstantCollentions.COMMISSION_BUSIN_STATUS_DELETE, true, "当前文件记录已经删除，不能重复解析");
        checkStatus(anFile.getOperOrg(), anOperatorInfo.getOperOrg(), false, "你没有当前文件的删除权限");
   
        
    }

}
