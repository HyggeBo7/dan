package top.dearbo.frame.common.util.pdf;

import top.dearbo.frame.common.util.pdf.view.AsianFontProvider;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.*;
import java.nio.charset.Charset;

/**
 * 生成PDF文件
 */
public class PDFFileUtils {

    public static void savePdf(OutputStream out, String html) {
        Document document = new Document(PageSize.A4, 50, 50, 60, 60);
        //Document document = new Document(PageSize.A4, 110, 110, 120, 140);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(html));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    public static void saveChinesePdf(OutputStream out, String html) {
        Document document = new Document(PageSize.A4, 50, 50, 60, 60);
        //Document document = new Document(PageSize.A4, 110, 110, 120, 140);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(html.getBytes()), Charset.forName("UTF-8"), new AsianFontProvider());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    public static void saveChinesePdf(String outPath, String html) throws FileNotFoundException {
        saveChinesePdf(new FileOutputStream(outPath), html);
    }
}
