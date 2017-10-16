package com.betterjr.modules.remote.dubbo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.agreement.IScfElecAgreementService;
import com.betterjr.modules.customer.ICustRelationService;
import com.betterjr.modules.customer.entity.CustRelation;
import com.betterjr.modules.document.IAgencyAuthFileGroupService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.document.data.DownloadFileInfo;
import com.betterjr.modules.document.entity.CustFileItem;
import com.betterjr.modules.document.utils.CustFileUtils;
import com.betterjr.modules.document.utils.DownloadFileService;
import com.betterjr.modules.loan.IScfRequestService;
import com.betterjr.modules.remote.IFileWebService;

/**
 * webservice 文件下载服务，区别于平台的文件下载
 * 
 * @author zhoucy
 *
 */
@Service(interfaceClass = IFileWebService.class)

public class FileWebServiceDubboService implements IFileWebService {
    private static Logger logger = LoggerFactory.getLogger(FileWebServiceDubboService.class);

    @Reference(interfaceClass = IScfRequestService.class)
    private IScfRequestService requestService;

    @Reference(interfaceClass = ICustRelationService.class)
    private ICustRelationService factorRelService;

    @Reference(interfaceClass = IAgencyAuthFileGroupService.class)
    private IAgencyAuthFileGroupService authFileGroupService;

    @Reference(interfaceClass = ICustFileService.class)
    private ICustFileService fileService;

    @Reference(interfaceClass = IScfElecAgreementService.class)
    private IScfElecAgreementService elecAgreementService;

    /**
     * 对应接口：queryDownloadList 供应链融资业务，查询下载的文件列表
     * 
     * @param anMap
     *            需要查询的条件，客户号，在保理的客户号和需要下载的文件类型必须
     * @return
     */

    @Override
    public List<DownloadFileInfo> queryDownloadList(Long anCustNo, String anScfId, String anRequestNo,
            String anBusinType, String anFactorNo) {
        List<Long> fileBatchList = null;
        List<String> fileTypeBusinList = authFileGroupService.composeList(anFactorNo, anBusinType);
        if (StringUtils.isBlank(anRequestNo)) {
            CustRelation factorRel = factorRelService.findRelationWithCustCorp(anCustNo, anScfId, anFactorNo);
            logger.info(factorRel.toString());
            fileBatchList = fileService.findBatchNo(anCustNo, fileTypeBusinList);
        } else {
            if ("wos".equalsIgnoreCase(anFactorNo)) {
                fileBatchList = elecAgreementService.findBatchNo(anRequestNo);
                if (anCustNo == null) {
                    anCustNo = 102200334L;
                    anScfId = "102200334";
                }
            } else if ("cfgy".equalsIgnoreCase(anFactorNo)) {
                List<String> fileTypeBusinList02 = null;
                CustRelation factorRel = factorRelService.findRelationWithCustCorp(anCustNo, anScfId, anFactorNo);
                fileTypeBusinList = authFileGroupService.composeList(anFactorNo, "01");// 这里是财富共盈处理特殊方式，01代表除发票，合同，票据的其它所有文件
                fileBatchList = fileService.findBatchNo(anCustNo, fileTypeBusinList);
                List<Long> billBatchList = requestService.findVoucherBatchNo(anRequestNo); // 根据申请单查询发票，合同，票据附件
                if (billBatchList == null || billBatchList.size() <= 0) {
                    fileTypeBusinList02 = authFileGroupService.composeList(anFactorNo, "02");
                    billBatchList = fileService.findBatchNo(anCustNo, fileTypeBusinList02);
                } else {
                    fileTypeBusinList02 = authFileGroupService.composeList(anFactorNo, "02");
                }
                if (billBatchList != null) {
                    fileBatchList.addAll(billBatchList);
                    fileTypeBusinList.addAll(fileTypeBusinList02);
                }
            } else {
                fileBatchList = requestService.findVoucherBatchNo(anRequestNo);
                fileBatchList.addAll(elecAgreementService.findSignedBatchNo(anRequestNo));
            }
        }
        List<DownloadFileInfo> resultList = new ArrayList<DownloadFileInfo>();
        DownloadFileInfo resultFile;
        for (CustFileItem fileItem : fileService.findCustFilesByBatch(fileBatchList, fileTypeBusinList)) {
            logger.info("需要下载的文件是：" + fileItem);
            resultFile = CustFileUtils.sendToDownloadFileService(fileItem, anCustNo, anScfId, anBusinType);
            resultList.add(resultFile);
        }

        return resultList;
    }

    /**
     * 对应接口：downloadFile 供应链融资业务，需要下载文件的信息，需要在control层做转换
     * 
     * @param anMap
     *            需要查询的条件，文件编号，客户号，在保理的客户号和需要下载的文件类型必须
     * @return
     */
    @Override
    public DownloadFileInfo downloadFileInfo(String anToken) {

        return DownloadFileService.exactDownloadFile(anToken);
    }

}
