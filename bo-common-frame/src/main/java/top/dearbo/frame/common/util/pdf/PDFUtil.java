package top.dearbo.frame.common.util.pdf;

import top.dearbo.util.lang.StringUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @fileName: PDFUtil
 * @author: Dan
 * @createDate: 2018-03-26 11:36.
 * @description: pdf工具类
 */
public class PDFUtil {
    Document document = new Document();// 建立一个Document对象

    //添加中文字体
    private BaseFont bfChinese;

    //设置字体样式
    public Font textFont; //正常
    public Font redTextFont; //正常,红色
    public Font boldFont; //加粗
    public Font redBoldFont; //加粗,红色
    public Font firsetTitleFont; //一级标题
    public Font secondTitleFont; //二级标题
    public Font underlineFont; //下划线斜体

    //文件路径
    private String filePath;

    private int maxWidth = 520;

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * 保存在文件流
     *
     * @param outputStream
     */
    public PDFUtil(ByteArrayOutputStream outputStream) throws DocumentException, IOException {
        document.setPageSize(PageSize.A4);// 设置页面大小
        PdfWriter.getInstance(document, outputStream);
        document.open();
        document.newPage();
        this.init();
    }

    public PDFUtil(String url) throws IOException, DocumentException {
        this(new File(url));
        this.filePath = url;
    }

    /**
     * 文成文件
     *
     * @param file 待生成的文件名
     */
    public PDFUtil(File file) throws IOException, DocumentException {
        document.setPageSize(PageSize.A4);// 设置页面大小

        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        document.newPage();
        this.init();
    }

    private void init() throws IOException, DocumentException {
        bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        textFont = new Font(bfChinese, 12, Font.NORMAL); //正常
        redTextFont = new Font(bfChinese, 12, Font.NORMAL, BaseColor.RED); //正常,红色
        boldFont = new Font(bfChinese, 12, Font.BOLD); //加粗
        redBoldFont = new Font(bfChinese, 12, Font.BOLD, BaseColor.RED); //加粗,红色
        firsetTitleFont = new Font(bfChinese, 22, Font.BOLD); //一级标题
        secondTitleFont = new Font(bfChinese, 14, Font.BOLD); //二级标题
        underlineFont = new Font(bfChinese, 12, Font.UNDERLINE); //下划线斜体
    }

    /**
     * 为表格添加一个内容
     *
     * @param value 值
     * @param font  字体
     * @param align 对齐方式
     * @return 添加的文本框
     */
    public PdfPCell createCell(String value, Font font, int align) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * 为表格添加一个内容
     *
     * @param value 值
     * @param font  字体
     * @return 添加的文本框
     */
    public PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        Phrase phrase = new Phrase();
        Chunk chunk = new Chunk(value, font);
        phrase.add(chunk);
        cell.setPhrase(phrase);

        return cell;
    }

    /**
     * 为表格添加一个内容
     *
     * @param value   值
     * @param font    字体
     * @param align   对齐方式
     * @param colspan 占多少列
     * @return 添加的文本框
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * 为表格添加一个内容
     *
     * @param value     值
     * @param font      字体
     * @param align     对齐方式
     * @param colspan   占多少列
     * @param boderFlag 是否有有边框
     * @return 添加的文本框
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);
        if (!boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        }
        return cell;
    }

    /**
     * 创建一个表格对象
     *
     * @param colNumber 表格的列数
     * @return 生成的表格对象
     */
    public PdfPTable createTable(int colNumber) {
        PdfPTable table = new PdfPTable(colNumber);
        try {
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    public PdfPTable createTable(float[] widths) {
        PdfPTable table = new PdfPTable(widths);
        try {
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    public PdfPTable createBlankTable() {
        PdfPTable table = new PdfPTable(1);
        table.getDefaultCell().setBorder(0);
        table.addCell(createCell("", secondTitleFont));
        table.setSpacingAfter(20.0f);
        table.setSpacingBefore(20.0f);
        return table;
    }

    /**
     * 加水印（字符串）
     *
     * @param inputFile     需要加水印的PDF路径
     * @param inputFile     输出生成PDF的路径
     * @param waterMarkName 水印字符
     */
    public static ByteArrayOutputStream stringWaterMark(ByteArrayOutputStream inputFile, String waterMarkName) {

        ByteArrayOutputStream outputFile = new ByteArrayOutputStream();
        try {
            PdfReader reader = new PdfReader(inputFile.toByteArray());

            PdfStamper stamper = new PdfStamper(reader, outputFile);
            //添加中文字体
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

            int total = reader.getNumberOfPages() + 1;

            PdfContentByte under;
            int j = waterMarkName.length();
            char c = 0;
            int rise = 0;
            //给每一页加水印
            for (int i = 1; i < total; i++) {
                rise = 400;
                under = stamper.getUnderContent(i);
                under.beginText();
                under.setFontAndSize(bfChinese, 30);
                under.setTextMatrix(200, 120);
                for (int k = 0; k < j; k++) {
                    under.setTextRise(rise);
                    c = waterMarkName.charAt(k);
                    under.showText(c + "");
                }

                // 添加水印文字
                under.endText();
            }
            stamper.close();
            inputFile = outputFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputFile;
    }

    /**
     * 加水印（图片）
     *
     * @param inputFile 需要加水印的PDF路径
     * @param imageFile 输出生成PDF的路径
     * @param imageFile 水印图片路径
     */
    public static ByteArrayOutputStream imageWaterMark(ByteArrayOutputStream inputFile, String imageFile) {
        ByteArrayOutputStream outputFile = new ByteArrayOutputStream();
        try {
            PdfReader reader = new PdfReader(inputFile.toByteArray());
            PdfStamper stamper = new PdfStamper(reader, outputFile);

            int total = reader.getNumberOfPages() + 1;

            Image image = Image.getInstance(imageFile);
            image.setAbsolutePosition(-100, 0);//坐标
            image.scaleAbsolute(800, 1000);//自定义大小
            //image.setRotation(-20);//旋转 弧度
            //image.setRotationDegrees(-45);//旋转 角度
            //image.scalePercent(50);//依照比例缩放

            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.2f);// 设置透明度为0.2
            PdfContentByte under;
            //给每一页加水印
            for (int i = 1; i < total; i++) {
                under = stamper.getUnderContent(i);
                under.beginText();
                // 添加水印图片
                under.addImage(image);
                under.setGState(gs);
            }
            stamper.close();
            inputFile = outputFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputFile;
    }

    public <T> void exportPDFTable(Map<String, String> headMap, List<T> list, List<PdfPCell> titleCellList) {
        int columnsLength = headMap.size();
        String[] head = new String[headMap.size()];

        List<String> strList = new ArrayList<>();
        {
            int i = 0;
            for (Map.Entry<String, String> stringStringEntry : headMap.entrySet()) {
                strList.add(stringStringEntry.getKey());
                head[i] = stringStringEntry.getKey();
                i++;
            }
        }
        // 创建一个只有表头长度列的表格
        PdfPTable table = createTable(columnsLength);

        // 添加备注,靠左，不显示边框
        if (titleCellList != null && titleCellList.size() > 0) {
            for (PdfPCell pdfPCell : titleCellList) {
                table.addCell(pdfPCell);
            }
        } else {
            table.addCell(createCell("列表：", secondTitleFont, Element.ALIGN_CENTER, columnsLength, false));
        }

        //设置表头
        for (int i = 0; i < columnsLength; i++) {
            table.addCell(createCell(StringUtils.isBlank(headMap.get(head[i])) ? head[i] : headMap.get(head[i]), secondTitleFont, Element.ALIGN_CENTER));
        }

        if (null != list && list.size() > 0) {
            Class classType = list.get(0).getClass();//获得该对对象的class实例
            int size = list.size();
            for (int i = 0; i < size; i++) {
                Object obj = list.get(i);
                for (int j = 0; j < columnsLength; j++) {
                    //获得首字母
                    String firstLetter = head[j].substring(0, 1).toUpperCase();

                    //获得get方法,getName,getAge等
                    String getMethodName = "get" + firstLetter + head[j].substring(1);

                    try {
                        //通过反射获得相应的get方法，用于获得相应的属性值
                        //报错.
                        //method = classType.getDeclaredMethod(getMethodName);
                        Method method = classType.getMethod(getMethodName, new Class[]{});
                        try {
                            //System.out.print(getMethodName + ":" + method.invoke(t, new Class[]{}) + ",");
                            //添加数据
                            table.addCell(createCell(method.invoke(obj) == null ? "" : String.valueOf(method.invoke(obj)), textFont));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        Object o = StringUtil.judgeConversionType(((Map) obj).get(head[j]));

                        table.addCell(createCell(String.valueOf(o), textFont));


                        System.err.println(getMethodName + "方法不存在!" + e.getMessage());

                    }
                }
                //System.out.println("");
            }
        }
        try {
            //将表格添加到文档中
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //关闭流
        if (document != null) {
            document.close();
        }
    }


    /**
     * 提供外界调用的接口，生成以head为表头，list为数据的pdf
     *
     * @param headMap //数据表头
     * @param list    //数据
     * @return //excel所在的路径
     */
    public <T> void exportPDFTable(Map<String, String> headMap, List<T> list) {
        exportPDFTable(headMap, list, new ArrayList<>());
    }

    public <T> void exportPDFTable(Map<String, String> headMap, List<T> list, String titleStr) {
        List<PdfPCell> arrayList = new ArrayList<>();
        arrayList.add(createCell(titleStr, secondTitleFont, Element.ALIGN_CENTER, headMap.size(), false));
        exportPDFTable(headMap, list, arrayList);
    }

    /*public static void main(String[] args) throws IOException, DocumentException {
        //String[] head = {"account", "password", "name", "mail", "qq", "phone"};

        *//*Map<String, String> linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("account", "账号");
        linkedHashMap.put("password", "密码");
        linkedHashMap.put("name", "昵称");
        linkedHashMap.put("mail", "邮箱");
        linkedHashMap.put("qq", "QQ");
        linkedHashMap.put("phone", "电话");
        linkedHashMap.put("birthDay", "生日");
        List<UserTestDto> userInfoList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            UserTestDto userInfo = new UserTestDto();
            userInfo.setAccount("account-" + i);
            userInfo.setPassword("password" + i);
            userInfo.setName("名称" + i);
            userInfo.setMail("邮箱" + i);
            userInfo.setPhone("电话:123655" + i);
            userInfo.setQq("qq=" + i);
            userInfo.setBirthDay(new Date());

            userInfoList.add(userInfo);
        }
        String str = "P:/pdf/test123456789.pdf";
        PDFUtil pdfUtil = new PDFUtil(new File(str));
        List<PdfPCell> arrayList = new ArrayList<>();
        arrayList.add(pdfUtil.createCell("测试表头", pdfUtil.secondTitleFont, Element.ALIGN_CENTER, linkedHashMap.size(), false));
        arrayList.add(pdfUtil.createCell("测试表头1", pdfUtil.secondTitleFont, Element.ALIGN_LEFT, linkedHashMap.size(), false));
        arrayList.add(pdfUtil.createCell("测试表头2", pdfUtil.secondTitleFont, Element.ALIGN_RIGHT, linkedHashMap.size(), false));
        pdfUtil.exportPDFTable(linkedHashMap, userInfoList, arrayList);*//*
    }*/

}
