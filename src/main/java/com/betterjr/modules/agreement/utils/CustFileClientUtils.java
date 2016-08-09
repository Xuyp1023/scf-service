package com.betterjr.modules.agreement.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.data.KeyAndValueObject;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.FileUtils;
import com.betterjr.modules.document.entity.CustFileItem;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public abstract class CustFileClientUtils {
    private static final Logger logger = LoggerFactory.getLogger(CustFileClientUtils.class);

    /**
     * 创建签名的文档信息
     * 
     * @param anFile
     *            签名的文件信息
     * @param anType
     *            文件类型
     * @param anFileName
     *            签名的文件名称
     * @param anFilePath
     *            签名的文件路径
     * @return
     */

    public static Long findBatchNo() {

        return SerialGenerator.getLongValue("CustFileInfo.id");
    }

    public static CustFileItem createSignDocFileItem(KeyAndValueObject anFileInfo, String anWorkType, String anFileName) {
        CustFileItem fileItem = createDefFileItem(anFileInfo, anWorkType, anFileName);
        fileItem.setBatchNo(findBatchNo());

        return fileItem;
    }

    private static CustFileItem createDefFileItem(KeyAndValueObject anFileInfo, String anWorkType, String anFileName) {
        CustFileItem fileItem = new CustFileItem();
        fileItem.setId(SerialGenerator.getLongValue("CustFileItem.id"));
        File tmpFile = (File) anFileInfo.getValue();
        fileItem.setAbsoFile(tmpFile);
        fileItem.setFileLength(tmpFile.length());
        fileItem.setFilePath(anFileInfo.getStrKey());
        fileItem.setFileInfoType(anWorkType);
        fileItem.setFileName(anFileName);
        fileItem.setBatchNo(0L);
        fileItem.setFileType(FileUtils.extractFileExt(anFileName));

        return fileItem;
    }
    
    /**
     * 根据保存的文件信息，产生PDF文件。
     * 
     * @param anSb
     * @param anFileInfo
     * @return
     */
    public static boolean exportPDF(StringBuffer anSb, KeyAndValueObject anFileInfo) {
        File tmpFile = (File) anFileInfo.getValue();
        OutputStream out = null;
        try {
            out = new FileOutputStream(tmpFile);
            exportPDF(anSb, out);
            return true;
        }
        catch (BytterTradeException ex) {

            throw ex;
        }
        catch (Exception ex) {
            logger.error("exportPDF has error", ex);
            return false;
        }
        finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 输出PDF文件
     * @param anSb
     * @param anOut
     */
    public static void exportPDF(StringBuffer anSb, OutputStream anOut) {
        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        document.setMargins(0, 0, 0, 0);
        System.out.println(anSb.toString());
        PdfWriter pdfwriter = null;
        try {
            pdfwriter = PdfWriter.getInstance(document, anOut);
            pdfwriter.setViewerPreferences(PdfWriter.HideToolbar);
            document.open();
            document.newPage();
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);

            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
            Pipeline pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, pdfwriter)));

            XMLWorker worker = new XMLWorker(pipeline, true);
            XMLParser p = new XMLParser(worker);
            StringReader reader = new StringReader(anSb.toString());
            p.parse(reader);
            p.flush();
        }
        catch (IOException | DocumentException ex) {
            throw new BytterTradeException(30002, "产生PDF报告文件出现异常，请稍后再试", ex);
        }
        finally {
            document.close();
            pdfwriter.close();
        }
    }
}