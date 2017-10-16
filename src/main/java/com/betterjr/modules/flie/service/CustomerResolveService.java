package com.betterjr.modules.flie.service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.mq.annotation.RocketMQListener;
import com.betterjr.common.mq.message.MQMessage;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.acceptbill.service.ScfAcceptBillDOService;
import com.betterjr.modules.asset.data.AssetConstantCollentions;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.entity.CustResolveFile;
import com.betterjr.modules.document.service.DataStoreService;
import com.betterjr.modules.flie.data.ExcelUtils;
import com.betterjr.modules.flie.data.FileResolveConstants;
import com.betterjr.modules.flie.entity.CustFileCloumn;
import com.betterjr.modules.order.service.ScfInvoiceDOService;
import com.betterjr.modules.order.service.ScfOrderDOService;
import com.betterjr.modules.receivable.service.ScfReceivableDOService;

@Service
public class CustomerResolveService {

    private final static Logger logger = LoggerFactory.getLogger(CustomerResolveService.class);

    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService custFileService;

    @Autowired
    private DataStoreService dataStoreService;

    @Autowired
    private ScfOrderDOService orderService;

    @Autowired
    private ScfReceivableDOService receivableService;

    @Autowired
    private ScfAcceptBillDOService billService;

    @Autowired
    private ScfInvoiceDOService invoiceService;

    @Autowired
    private CustFileCloumnService fileCloumnService;

    @RocketMQListener(topic = "FILE_RESOLVE_CUST_TOPIC", consumer = "betterConsumer")
    public void processNotification(final Object anMessage) {

        final MQMessage message = (MQMessage) anMessage;
        logger.debug("文件开始解析,解析日志记录id 为：" + message.getHead("id"));
        // 得到解析日志记录对象
        CustResolveFile resolveFile = (CustResolveFile) message.getObject();
        String[] filterProperties = new String[] { "regDate", "regTime", "modiOperId", "modiOperName", "operOrg",
                "custNo", "coreCustNo", "batchNo" };
        Map<String, Object> resolveFileMap = transBean2Map(resolveFile, filterProperties);
        resolveFileMap.put("resolveFileId", resolveFile.getId());
        try {
            String fileType = resolveFile.getFileType();
            if (!("xls".equals(fileType) || "xlsx".equals(fileType))) {
                BTAssert.notNull(null, "文件读取格式失败,请上传excel文件");
            }
            // 得到文件类型 1订单 2票据 3应收账款 4发票 5 合同
            String infoType = (String) message.getHead("infoType");
            // 根据类型查找对应的列
            List<CustFileCloumn> fileCloumnList = fileCloumnService.queryFileCloumnByInfoType(infoType,
                    FileResolveConstants.FILE_USED_UOLOAD);
            CustFileItem fileItem = custFileService.findOne(resolveFile.getFileItemId());// 文件上次详情
            InputStream is = dataStoreService.loadFromStore(fileItem);
            // FileUtils.copyInputStreamToFile(is, new File("d:\\788945.xlsx"));
            if (AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_ORDER.equals(infoType)) {
                // 获取
                List<Map<String, Object>> listMap = convertListMap(is, fileCloumnList, resolveFileMap, fileType,
                        FileResolveConstants.BEGIN_ROW_ORDER);
                orderService.saveResolveFile(listMap);
            }
            if (AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_BILL.equals(infoType)) {
                List<Map<String, Object>> listMap = convertListMap(is, fileCloumnList, resolveFileMap, fileType,
                        FileResolveConstants.BEGIN_ROW_ORDER);
                billService.saveResolveFile(listMap);
            }
            if (AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_RECEIVABLE.equals(infoType)) {
                List<Map<String, Object>> listMap = convertListMap(is, fileCloumnList, resolveFileMap, fileType,
                        FileResolveConstants.BEGIN_ROW_ORDER);
                receivableService.saveResolveFile(listMap);
            }
            if (AssetConstantCollentions.ASSET_BASEDATA_INFO_TYPE_INVOICE.equals(infoType)) {
                List<Map<String, Object>> listMap = convertListMap(is, fileCloumnList, resolveFileMap, fileType,
                        FileResolveConstants.BEGIN_ROW_ORDER);
                invoiceService.saveResolveFile(listMap);
            }

            // 更新解析日志记录
            resolveFile.setBusinStatus("2");
            resolveFile.setShowMessage("文件解析成功");
            custFileService.saveModifyResolveFile(resolveFile);
            logger.debug("文件解析完成 ,解析日志记录id 为：" + message.getHead("id"));
        }
        catch (Exception e) {

            logger.debug("文件解析失败 ,解析日志记录id 为：" + message.getHead("id"));
            resolveFile.setBusinStatus("1");
            resolveFile.setShowMessage("解析失败，请稍后重试!" + e.getMessage());
            custFileService.saveModifyResolveFile(resolveFile);
        }

    }

    /**
     * 将excel文件输入流和需要位置属性封装excel中的值到对象中
     * 
     * @param anIs
     *            excel文件输入流
     * @param anFileCloumnList;
     * @param anFileType;
     *            文件的类型
     * @param beginRow;
     *            从哪行开始解析
     * @return
     * @throws IOException
     */
    private List<Map<String, Object>> convertListMap(InputStream anIs, List<CustFileCloumn> anFileCloumnList,
            Map<String, Object> appendMap, String anFileType, int beginRow) throws IOException {

        BTAssert.notNull(anIs, "文件读取失败");
        BTAssert.notNull(anFileCloumnList, "请预先配置模版参数");
        Iterator<Row> rows = ExcelUtils.parseFile(anIs, anFileType);
        BTAssert.notNull(rows, "文件读取失败");
        List<Map<String, Object>> listMap = new ArrayList<>();
        while (rows.hasNext()) {
            Map<String, Object> map = new HashMap<>();
            Row currentRow = rows.next();
            // 模板里的表头，该行跳过
            if (currentRow.getRowNum() < beginRow) {
                continue;
            }
            if (currentRow.getRowNum() > beginRow) {

                if (StringUtils.isBlank(ExcelUtils.getStringCellValue(currentRow.getCell(0)))
                        || StringUtils.isBlank(ExcelUtils.getStringCellValue(currentRow.getCell(1)))
                        || StringUtils.isBlank(ExcelUtils.getStringCellValue(currentRow.getCell(2)))) {
                    break;
                }

            }
            int rowNum = currentRow.getRowNum() + 1;

            for (CustFileCloumn fileColumn : anFileCloumnList) {

                String fileColumnProperties = fileColumn.getCloumnProperties();
                String fileColumnName = fileColumn.getCloumnName();
                int fileColumnOrder = fileColumn.getCloumnOrder();
                int isMust = fileColumn.getIsMust();
                String cloumnType = fileColumn.getCloumnType();
                String stringCellValue = ExcelUtils.getStringCellValue(currentRow.getCell(fileColumnOrder));
                if (StringUtils.isBlank(stringCellValue) && isMust == FileResolveConstants.RESOLVE_FILE_IS_MUST) {
                    BTAssert.notNull(null, "第" + rowNum + "行的" + fileColumnName + "不能为空，请修改重试");
                }
                if (StringUtils.isNotBlank(stringCellValue) && "n".equals(cloumnType)) {
                    if (!isNumber(stringCellValue)) {
                        BTAssert.notNull(null, "第" + rowNum + "行的" + fileColumnName + "必须为数字类型");
                    }
                }
                map.put(fileColumnProperties, stringCellValue);
            }
            map.putAll(appendMap);
            listMap.add(map);
        }

        return listMap;
    }

    private boolean isNumber(String anValue) {
        if (!anValue.matches("\\d+(.\\d+)?[fF]?")) {
            return false;
        }

        return true;
    }

    // Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
    public static Map<String, Object> transBean2Map(Object obj, String[] anTerms) {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }

            }
        }
        catch (Exception e) {
            logger.debug("transBean2Map Error " + e);
        }

        return Collections3.filterMap(map, anTerms);

    }

}
