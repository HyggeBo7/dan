package com.dan.common.util.excel;

import com.dan.utils.StringUtil;
import com.dan.utils.exception.AppException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @fileName: ExcelWriteUtils
 * @author: Dan
 * @createDate: 2018-01-12 12:32.
 * @description: 导出，导入
 */
public class ExcelWriteUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExcelWriteUtils.class);

    /**
     * 私有构造方法
     *
     * @param writeOrReadFlag 导出:true,导入false
     */
    private ExcelWriteUtils(boolean writeOrReadFlag) {
        if (writeOrReadFlag) {
            writeWorkbook = new HSSFWorkbook();
        }
    }

    //导出(写入)-write  设置顺序作用 构造->当前

    /**
     * 创建sheet页面-导出设置
     */
    private HSSFWorkbook writeWorkbook;

    /**
     * 文件保存路径
     */
    private String filePathWrite;

    /**
     * 单元格导出宽度  [单元格宽度最小值] * 256
     */
    private static final int rowStandardWidth = 256;

    /**
     * 最大宽度-最大不能超过 256 * 256
     */
    private static final int rowWidthMax = 256 * 256 - 100;

    /**
     * 导出时间格式 默认 yyyy-MM-dd
     */
    private String exportDataFormat = "yyyy-MM-dd HH:mm:ss";
    /**
     * 表头字体
     */
    private String titleFontType = "宋体";
    /**
     * 表头背景色
     */
    private String titleBackColor = "C1FBEE";
    /**
     * 表头字号
     */
    private short titleSize = 16;
    /**
     * 表头字号
     */
    private short titleFontSize = 14;
    /**
     * 正文字体
     */
    private String contentFontType = "宋体";
    /**
     * 正文字号
     */
    private short contentFontSize = 12;

    /**
     * 自定义默认正文样式
     */
    private boolean defaultColumnStyleFlag = false;

    /**
     * 定义默认样式
     */
    private CellStyle defaultColumnStyle;

    /**
     * 添加自动筛选的列 如 A:M
     */
    private String address;

    /**
     * 设置列的公式
     */
    private String[] colFormula = null;

    /**
     * Float类型数据小数位
     */
    private String floatDecimal = ".00";

    /**
     * Double类型数据小数位
     */
    private String doubleDecimal = ".00";

    /**
     * BigDecimal类型数据小数位
     */
    private String bigDecimalDecimal = ".00";

    /**
     * BigDecimalDecimal/Float/Double 类型小数位是否需要格式化 注：为true时DecimalStyle不会生效
     */
    private boolean decimalFormatFlag = true;

    /**
     * BigDecimalDecimal/Float/Double 类型是否格式化成指定格式类型 注：为true时生效,但DecimalFormatFlag必须为false
     */
    private boolean decimalStyleFlag = false;

    /**
     * 导出标题合并列数-默认表头列数
     */
    private int titleRowSize = -1;

    /**
     * 单元格宽度最小值 10 * [单元格导出宽度]
     */
    private int rowMinStandardWidth = 10;

    /**
     * 导出多个页配置项
     */
    private List<WriteCriteria> addWriteCriteriaList;

    /**
     * 全部导出表头
     */
    private Map<String, String> defaultWriteTitleColumnAndNameMap;

    /**
     * 设置头部大小
     */
    private Integer[] defaultWriteTitleSize;

    /**
     * 设置导出标题
     */
    private String defaultWriteTitle;

    /**
     * 是否导出表头
     */
    private boolean defaultTitleHeaderFlag = true;

    //导入(读取)-import

    //private List<ReadCriteria> addReadCriteriaList;

    /**
     * 读取的行
     */
    private int startSheet;
    /**
     * 去除最后读取的行
     */
    private int endSheet;
    /**
     * 工作薄对象-导入设置
     */
    private Workbook readWorkBook;

    /**
     * 读取是否去除当一行数据全都为null或者""，就删除
     */
    private boolean emptyRowFlag = false;

    /**
     * 显示读取行
     */
    private boolean debugNumFlag = false;

    /**
     * 排除读取行
     */
    private List<Integer> excludeLineList;

    //公用

    /**
     * sheet 读取,写入单个 名称
     */
    private String sheetName;

    /**
     * 查询的sheet
     */
    private int sheetIndex;

    /**
     * 文件名
     */
    private String fileName;

    //导出方法

    /**
     * 设置是否导出表头
     */
    public void setDefaultTitleHeaderFlag(boolean defaultTitleHeaderFlag) {
        this.defaultTitleHeaderFlag = defaultTitleHeaderFlag;
    }

    /**
     * 创建一个样式
     */
    public CellStyle getDefaultColumnStyle() {
        return writeWorkbook.createCellStyle();
    }

    public void setContentFontSize(short contentFontSize) {
        this.contentFontSize = contentFontSize;
    }

    public void setDefaultColumnStyleFlag(boolean defaultColumnStyleFlag) {
        this.defaultColumnStyleFlag = defaultColumnStyleFlag;
    }

    public void setDefaultColumnStyle(CellStyle defaultColumnStyle) {
        this.defaultColumnStyle = defaultColumnStyle;
    }

    public void setDefaultWriteTitle(String defaultWriteTitle) {
        this.defaultWriteTitle = defaultWriteTitle;
    }

    public void setDefaultWriteTitleSize(Integer[] defaultWriteTitleSize) {
        this.defaultWriteTitleSize = defaultWriteTitleSize;
    }

    public void setDefaultWriteTitleColumnAndNameMap(Map<String, String> defaultWriteTitleColumnAndNameMap) {
        this.defaultWriteTitleColumnAndNameMap = defaultWriteTitleColumnAndNameMap;
    }

    public List<WriteCriteria> getAddWriteCriteriaList() {
        return addWriteCriteriaList;
    }

    public void setRowMinStandardWidth(int rowMinStandardWidth) {
        this.rowMinStandardWidth = rowMinStandardWidth;
    }

    public void setTitleRowSize(int titleRowSize) {
        this.titleRowSize = titleRowSize;
    }

    public void setDecimalStyleFlag(boolean decimalStyleFlag) {
        this.decimalStyleFlag = decimalStyleFlag;
    }

    public String getBigDecimalDecimal() {
        return bigDecimalDecimal;
    }

    public void setBigDecimalDecimal(String bigDecimalDecimal) {
        this.bigDecimalDecimal = bigDecimalDecimal;
    }

    public String getFloatDecimal() {
        return floatDecimal;
    }

    public void setFloatDecimal(String floatDecimal) {
        this.floatDecimal = floatDecimal;
    }

    public String getDoubleDecimal() {
        return doubleDecimal;
    }

    public void setDoubleDecimal(String doubleDecimal) {
        this.doubleDecimal = doubleDecimal;
    }

    public void setDecimalFormatFlag(boolean decimalFormatFlag) {
        this.decimalFormatFlag = decimalFormatFlag;
    }

    public String getExportDataFormat() {
        return exportDataFormat;
    }

    public void setExportDataFormat(String exportDataFormat) {
        this.exportDataFormat = exportDataFormat;
    }

    public String getTitleBackColor() {
        return titleBackColor;
    }

    public void setTitleBackColor(String titleBackColor) {
        this.titleBackColor = titleBackColor;
    }

    /**
     * 新增一个页数据
     *
     * @param newCriteria
     */
    public void addCriteria(WriteCriteria newCriteria) {
        if (this.addWriteCriteriaList == null) {
            this.addWriteCriteriaList = new ArrayList<>();
        }
        this.addWriteCriteriaList.add(newCriteria);
    }

    //导入方法

    public void setExcludeLineList(String excludeLine) {
        String[] split = excludeLine.split(",");
        List<Integer> integerList = new ArrayList<>();
        if (split.length > 0) {
            for (String s : split) {
                integerList.add(Integer.parseInt(s));
            }
        }
        this.excludeLineList = integerList;
    }

    public void setExcludeLineList(List<Integer> excludeLineList) {
        this.excludeLineList = excludeLineList;
    }

    public void setEmptyRowFlag(boolean emptyRowFlag) {
        this.emptyRowFlag = emptyRowFlag;
    }

    public void setDebugNumFlag(boolean debugNumFlag) {
        this.debugNumFlag = debugNumFlag;
    }

    public static ExcelWriteUtils importExcel(String filePath, int startSheet, int endSheet) throws IOException {
        return importExcelByFile(new File(filePath), startSheet, endSheet);
    }

    public static ExcelWriteUtils importExcel(String filePath, String sheetName) throws IOException {
        return importExcelByFile(new File(filePath), sheetName);
    }

    public static ExcelWriteUtils importExcelByFile(File file, String sheetName) throws IOException {
        return importExcel(file.getName(), new FileInputStream(file), sheetName);
    }

    public static ExcelWriteUtils importExcelByFile(File file, int startSheet, int endSheet) throws IOException {
        return importExcel(file.getName(), new FileInputStream(file), startSheet, endSheet);
    }

    /*public static ExcelWriteUtils importExcelByMultipartFile(MultipartFile multipartFile, int startSheet, int endSheet) throws IOException {
        return importExcel(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), startSheet, endSheet);
    }

    public static ExcelWriteUtils importExcelByMultipartFile(MultipartFile multipartFile, String sheetName) throws IOException {
        return importExcel(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), 1, 1, sheetName);
    }*/

    public static ExcelWriteUtils importExcel(String fileName, InputStream is, String sheetName) throws IOException {
        if (StringUtils.isBlank(sheetName)) {
            AppException.throwEx("工作表页名称不能为空!");
        }
        return importExcel(fileName, is, 0, 0, sheetName);
    }

    private static ExcelWriteUtils importExcel(String fileName, InputStream is, int startSheet, int endSheet) throws IOException {
        return importExcel(fileName, is, startSheet, endSheet, null);
    }

    private static ExcelWriteUtils importExcel(String fileName, InputStream is, int startSheet, int endSheet, String sheetName) throws IOException {
        ExcelWriteUtils excelWriteUtils = new ExcelWriteUtils(false);
        if (StringUtils.isBlank(fileName)) {
            AppException.throwEx("导入文档名称为空!");
        } else if (fileName.toLowerCase().endsWith("xls")) {
            excelWriteUtils.readWorkBook = new HSSFWorkbook(is);
        } else if (fileName.toLowerCase().endsWith("xlsx")) {
            excelWriteUtils.readWorkBook = new XSSFWorkbook(is);
        } else {
            AppException.throwEx("读取的不是excel文件!");
        }
        if (StringUtils.isNotBlank(sheetName)) {
            excelWriteUtils.startSheet = 1;
            excelWriteUtils.endSheet = excelWriteUtils.readWorkBook.getNumberOfSheets();
            excelWriteUtils.sheetName = sheetName;
        } else {
            if (startSheet > endSheet) {
                AppException.throwEx("开始页不能大于结束页!");
            }
            if (endSheet > excelWriteUtils.readWorkBook.getNumberOfSheets() || (endSheet - startSheet) < 0 || (endSheet - startSheet) >= excelWriteUtils.readWorkBook.getNumberOfSheets()) {
                AppException.throwEx("文档中没有指定的工作表!");
            }
            excelWriteUtils.startSheet = startSheet;
            excelWriteUtils.endSheet = endSheet;
        }
        excelWriteUtils.fileName = fileName;
        if (is != null) {
            is.close();
        }
        return excelWriteUtils;
    }

    /**
     * 读取excel文件
     *
     * @param titleFlag   是否设置自定义标题
     * @param titleNum    标题列数
     * @param scienceFlag 是否保留科学计数法
     * @return
     */
    public List<List<Map<String, String>>> readExcelWithoutTitle(boolean titleFlag, int titleNum, boolean scienceFlag) {
        // 对应excel文件
        List<List<Map<String, String>>> result = new ArrayList<>();
        try {
            titleNum = titleNum < -1 ? -1 : titleNum - 1;
            List<Map<String, String>> sheetList = null;
            Map<String, String> rowMap = null;
            //当前读取行
            int thisNum = 0;
            // 遍历sheet页
            for (int i = startSheet - 1; i <= endSheet - 1; i++) {
                Sheet sheet = readWorkBook.getSheetAt(i);
                //对应sheet页
                sheetList = new ArrayList<>();
                if (StringUtils.isNotBlank(sheetName) && !sheet.getSheetName().equals(sheetName)) {
                    continue;
                }
                for (Row row : sheet) {
                    int rowNum = row.getRowNum();
                    //手动设置排除行
                    if (excludeLineList != null && excludeLineList.size() > 0) {
                        if (getExcludeLineList(excludeLineList, rowNum)) {
                            continue;
                        }
                    }
                    if (rowNum != titleNum) {
                        if (row != null && Objects.nonNull(row)) {
                            if (emptyRowFlag && !judgmentEmptyRowFlag(row)) {
                                continue;
                            }
                            //对应一个数据行
                            rowMap = new LinkedHashMap<>();
                            for (Cell c : row) {
                                // 判断是否具有合并单元格
                                boolean isMerge = isMergedRegion(sheet, rowNum, c.getColumnIndex());
                                String key = "";
                                if (titleFlag) {
                                    key = String.valueOf(sheet.getRow(titleNum < 0 ? 0 : titleNum).getCell(c.getColumnIndex()));
                                } else {
                                    key = String.valueOf(CellReference.convertNumToColString(c.getColumnIndex()));
                                }
                                if (StringUtils.isBlank(key)) {
                                    continue;
                                }
                                String value = null;
                                if (isMerge) {
                                    value = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex(), scienceFlag);
                                } else {
                                    //c.getRichStringCellValue() + "";
                                    value = this.getCellValue(c, scienceFlag);
                                }
                                rowMap.put(key, value);
                            }
                            sheetList.add(rowMap);
                        }
                    }
                    if (debugNumFlag) {
                        logger.debug("thisNum:{}", thisNum);
                        thisNum++;
                    }
                }
                result.add(sheetList);
            }
            return result;

        } catch (Exception e) {
            throw e;
        }
    }

    public static ExcelWriteUtils createWriteExcel() {
        return new ExcelWriteUtils(true);
    }

    /**
     * 导出多个sheet页面excel-导出到文件和文件流只能选择其一
     *
     * @param sheetAndDataList 多个页面excel
     * @param file             导出到指定文件
     * @param outputStream     导出到文件流
     */
    private void writeExcel(List<WriteCriteria> sheetAndDataList, File file, ByteArrayOutputStream outputStream) {
        if (writeWorkbook == null) {
            writeWorkbook = new HSSFWorkbook();
        }
        this.filePathWrite = file != null ? file.getPath() : "";
        for (int i = 0; i < sheetAndDataList.size(); i++) {
            WriteCriteria writeCriteria = sheetAndDataList.get(i);
            String thisSheetName = StringUtils.isNotBlank(writeCriteria.getSheetName()) ? writeCriteria.getSheetName() : "sheet" + i;
            writeCriteria.setSheetName(thisSheetName);
            //导出
            writeExcel(writeWorkbook, writeCriteria);
        }
        //新建文件
        OutputStream out = null;
        try {
            if (file != null) {
                //有文件路径
                try {
                    out = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    AppException.throwEx("导出文件路径错误!" + e.getMessage());
                }
                this.filePathWrite = file.getPath();
                writeWorkbook.write(out);
            } else {
                //否则，直接写到输出流中
                writeWorkbook.write(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
            AppException.throwEx("导出excel失败!" + e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeExcel(Workbook thisWorkBoot, WriteCriteria writeCriteria) {
        //excel 实体类属性名称 别名
        String[] thisTitleName = null;
        //实体类属性名称
        String[] thisTitleColumn = null;
        //设置excel表头与实体对应属性的名称
        if (writeCriteria.getWriteTitleColumnAndNameMap() != null && writeCriteria.getWriteTitleColumnAndNameMap().size() > 0) {
            int length = writeCriteria.getWriteTitleColumnAndNameMap().size();
            thisTitleColumn = writeCriteria.getWriteTitleColumnAndNameMap().keySet().toArray(new String[length]);
            thisTitleName = writeCriteria.getWriteTitleColumnAndNameMap().values().toArray(new String[length]);
        } else if (writeCriteria.getTitleColumn() != null && writeCriteria.getTitleColumn().length > 0) {
            thisTitleColumn = writeCriteria.getTitleColumn();
        } else if (defaultWriteTitleColumnAndNameMap != null && defaultWriteTitleColumnAndNameMap.size() > 0) {
            int length = defaultWriteTitleColumnAndNameMap.size();
            thisTitleColumn = defaultWriteTitleColumnAndNameMap.keySet().toArray(new String[length]);
            thisTitleName = defaultWriteTitleColumnAndNameMap.values().toArray(new String[length]);
        } else {
            AppException.throwEx("属性名称不能为空!");
        }

        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        Sheet sheet = thisWorkBoot.createSheet(writeCriteria.getSheetName());
        if (defaultColumnStyleFlag) {
            //设置默认sheet样式
            if (defaultColumnStyle == null) {
                defaultColumnStyle = setFontAndBorder(getCellStyleNew(), contentFontType, contentFontSize, false, false);
            } else {
                defaultColumnStyle = setFontAndBorder(defaultColumnStyle, contentFontType, contentFontSize, false, false);
            }
            for (int i = 0; i < thisTitleColumn.length; i++) {
                sheet.setDefaultColumnStyle(i, defaultColumnStyle);
            }
        }

        //当前行下标
        int rowNum = 0;
        //判断是否有标题,写入标题
        String thisTitleWrite = StringUtils.isNotBlank(writeCriteria.getTitleWrite()) ? writeCriteria.getTitleWrite() : defaultWriteTitle;
        if (StringUtils.isNotBlank(thisTitleWrite)) {
            Row row = sheet.createRow(rowNum);
            // 合并行列,四个参数分别是：起始行，起始列，结束行，结束列
            int thisTitleRowSize = titleRowSize == -1 ? thisTitleColumn.length - 1 : titleRowSize - 1;
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, thisTitleRowSize));
            Cell cell = row.createCell(0);
            cell.setCellValue(thisTitleWrite);
            CellStyle cellStyle = setFontAndBorder(getCellStyleNew(), titleFontType, titleSize, true, true);
            //居中显示
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cell.setCellStyle(cellStyle);
            rowNum++;
        }
        //获取自定义宽度
        Integer[] thisTitleSize = writeCriteria.getTitleSize() == null ? defaultWriteTitleSize : writeCriteria.getTitleSize();
        //获取当前列总行数
        int thisColumnSize = thisTitleName != null && thisTitleName.length > 0 ? thisTitleName.length : thisTitleColumn.length;
        //自动获取每列宽度,当前自定义列宽度为null时,自动获取每列宽度
        int[] columnIndexWidth = null;
        if (thisTitleSize == null) {
            columnIndexWidth = new int[thisColumnSize];
        }
        //是否导出表头
        boolean thisTitleHeaderFlag = writeCriteria.isTitleHeaderFlag() == null ? defaultTitleHeaderFlag : writeCriteria.isTitleHeaderFlag();
        if (thisTitleHeaderFlag) {
            //写入excel的表头
            Row titleNameRow = sheet.createRow(rowNum);
            rowNum++;
            if (thisTitleName != null && thisTitleName.length > 0) {
                setTitleHeader(titleNameRow, thisTitleName, columnIndexWidth);
            } else {
                setTitleHeader(titleNameRow, thisTitleColumn, columnIndexWidth);
            }
        }
        //为表头添加自动筛选
        if (StringUtils.isNotBlank(address)) {
            CellRangeAddress c = (CellRangeAddress) CellRangeAddress.valueOf(address);
            sheet.setAutoFilter(c);
        }
        //bigDecimal 类型样式
        CellStyle bigDecimalCellStyle = null;
        //float类型样式
        CellStyle floatCellStyle = null;
        //double 类型样式
        CellStyle doubleCellStyle = null;
        //通过反射获取数据并写入到excel中
        List<?> dataList = writeCriteria.getDataList();
        if (dataList != null && dataList.size() > 0) {
            if (thisTitleColumn.length > 0) {
                for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
                    //获得该对象
                    Object obj = dataList.get(rowIndex);
                    //获得该对对象的class实例
                    Class<?> ownerClass = obj.getClass();
                    Row dataRow = writeWorkbook.getSheet(writeCriteria.getSheetName()).createRow((rowIndex + rowNum));
                    for (int columnIndex = 0; columnIndex < thisTitleColumn.length; columnIndex++) {
                        String thisTableHeadName = thisTitleColumn[columnIndex].trim();
                        //字段不为空
                        if (StringUtils.isNotBlank(thisTableHeadName)) {
                            //使首字母大写
                            String UTitle = Character.toUpperCase(thisTableHeadName.charAt(0)) + thisTableHeadName.substring(1, thisTableHeadName.length());
                            String methodName = "get" + UTitle;

                            // 设置要执行的方法
                            Object data = null;
                            try {
                                //报错.
                                //Method method = clsss.getDeclaredMethod(methodName);
                                Method method = ownerClass.getMethod(methodName, new Class[0]);
                                Object invoke = method.invoke(obj);
                                data = invoke == null ? "" : invoke;
                            } catch (NoSuchMethodException en) {
                                //data = ((LinkedTreeMap) obj).get(title);
                                data = StringUtil.judgeConversionType(((Map) obj).get(thisTableHeadName));
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                                logger.info("==执行方法错误!obj:【{}】,writeExcel:method.invoke:【{}】", e.getMessage(), e);
                                continue;
                            }
                            Cell cell = dataRow.createCell(columnIndex);
                            if (data != null) {
                                if (data instanceof Boolean) {
                                    cell.setCellValue(String.valueOf(data));
                                } else if (data instanceof Integer) {
                                    cell.setCellValue(Integer.parseInt(String.valueOf(data)));
                                       /* HSSFCellStyle cellStyle = writeWorkbook.createCellStyle();
                                        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
                                        cell.setCellStyle(cellStyle);*/
                                } else if (data instanceof Float) {
                                    // 此处设置数据格式
                                    if (decimalStyleFlag && floatDecimal != null) {
                                        if (floatCellStyle == null) {
                                            floatCellStyle = getCellStyleNew(floatDecimal);
                                        }
                                        // 设置单元格格式
                                        cell.setCellStyle(floatCellStyle);
                                    }
                                    if (decimalFormatFlag && floatDecimal != null) {
                                        DecimalFormat floatDecimalFormat = new DecimalFormat(floatDecimal);
                                        cell.setCellValue(floatDecimalFormat.format(Float.parseFloat(String.valueOf(data))));
                                    } else {
                                        cell.setCellValue(Float.parseFloat(String.valueOf(data)));
                                    }
                                } else if (data instanceof Double || data instanceof BigDecimal) {
                                    String decimalDecimal = null;
                                    if (data instanceof BigDecimal) {
                                        // 此处设置数据格式
                                        if (decimalStyleFlag && bigDecimalDecimal != null) {
                                            if (bigDecimalCellStyle == null) {
                                                //cell.getCellStyle(),
                                                bigDecimalCellStyle = getCellStyleNew(bigDecimalDecimal);
                                            }
                                            // 设置单元格格式
                                            cell.setCellStyle(bigDecimalCellStyle);
                                        }
                                        if (decimalFormatFlag && bigDecimalDecimal != null) {
                                            decimalDecimal = bigDecimalDecimal;
                                        }
                                    } else {
                                        // 此处设置数据格式
                                        if (decimalStyleFlag && doubleDecimal != null) {
                                            if (doubleCellStyle == null) {
                                                doubleCellStyle = getCellStyleNew(doubleDecimal);
                                            }
                                            // 设置单元格格式
                                            cell.setCellStyle(doubleCellStyle);
                                        }
                                        if (decimalFormatFlag && doubleDecimal != null) {
                                            decimalDecimal = doubleDecimal;
                                        }
                                    }
                                    if (StringUtils.isNotBlank(decimalDecimal)) {
                                        DecimalFormat decimalFormat = new DecimalFormat(decimalDecimal);
                                        cell.setCellValue(decimalFormat.format(Double.parseDouble(String.valueOf(data))));
                                    } else {
                                        cell.setCellValue(Double.parseDouble(String.valueOf(data)));
                                    }
                                } else if (data instanceof Long) {
                                    cell.setCellValue(Long.parseLong(String.valueOf(data)));
                                } else if (data instanceof Date) {
                                    if (data != null && StringUtils.isNotBlank(exportDataFormat)) {
                                        String format = new SimpleDateFormat(exportDataFormat).format(((Date) data));
                                        cell.setCellValue(format);
                                    } else {
                                        cell.setCellValue(String.valueOf(data));
                                    }
                                } else {
                                    cell.setCellValue(String.valueOf(data));
                                }
                                if (columnIndexWidth != null) {
                                    int length = StringUtil.calculatePlaces(String.valueOf(data), 1.9, 1.3);
                                    if (columnIndexWidth[columnIndex] < length) {
                                        columnIndexWidth[columnIndex] = length;
                                    }
                                }
                            }
                        } else {   //字段为空 检查该列是否是公式
                            if (colFormula != null) {
                                String sixBuf = colFormula[columnIndex].replace("@", (rowIndex + 1) + "");
                                Cell cell = dataRow.createCell(columnIndex);
                                cell.setCellFormula(String.valueOf(sixBuf));
                            }
                        }
                    }
                }

            }
        }
        //设置自动宽度
        for (int i = 0; i < thisColumnSize; i++) {
            if (thisTitleSize == null) {
                //如果当前宽度小于了，最小单元格宽度,设置默认最小值宽度
                int thisWidth = columnIndexWidth[i] < rowMinStandardWidth ? rowMinStandardWidth : columnIndexWidth[i];
                if (thisWidth > 0) {
                    // * 2 * 256 中文适用
                    int maxWidth = thisWidth * rowStandardWidth;
                    if (defaultColumnStyle != null) {
                        //默认字体大小是10,乘上当前字体大小
                        maxWidth = maxWidth / 10 * contentFontSize;
                    }
                    maxWidth = maxWidth > rowWidthMax ? rowWidthMax : maxWidth;
                    sheet.setColumnWidth(i, maxWidth);
                }
            } else {
                //保证自定义的长度下标和列总行数下标对应
                if (thisTitleSize.length > 0 && i < thisTitleSize.length) {
                    int thisWidth = thisTitleSize[i] == null ? rowMinStandardWidth : thisTitleSize[i];
                    //设置宽度
                    sheet.setColumnWidth(i, thisWidth * rowStandardWidth);
                }
            }
        }
    }

    //导出重载方法

    /**
     * excel 导出到文件
     *
     * @param writeCriteria
     * @param file          文件路径
     */
    public void writeExcel(WriteCriteria writeCriteria, File file) {
        writeExcel(writeCriteriaToList(writeCriteria), file);
    }

    public void writeExcel(WriteCriteria writeCriteria, String fileUrl) {
        writeExcel(writeCriteria, new File(fileUrl));
    }

    public void writeExcel(List<WriteCriteria> writeCriteriaList, File file) {
        writeExcel(writeCriteriaList, file, null);
    }

    public void writeExcelFileUrl(String sheetName, String[] titleColumn, List<?> dataList, String filePath) {
        writeExcelFile(sheetName, titleColumn, dataList, new File(filePath));
    }

    public void writeExcelFile(String sheetName, String[] titleColumn, List<?> dataList, File file) {
        WriteCriteria writeCriteria = new WriteCriteria(sheetName, titleColumn, dataList, null, null);
        writeExcel(writeCriteria, file);
    }

    public void writeExcelFileUrl(String sheetName, Map<String, String> headMap, List<?> dataList, String filePath) {
        writeExcelFileUrl(sheetName, headMap, dataList, filePath, null);
    }

    public void writeExcelFileUrl(String sheetName, Map<String, String> headMap, List<?> dataList, String filePath, Integer titleSize[]) {
        WriteCriteria writeCriteria = new WriteCriteria(sheetName, headMap, dataList, titleSize, null);
        writeExcel(writeCriteria, filePath);
    }

    /**
     * 导出excel到文件流
     *
     * @param writeCriteria
     * @return ByteArrayOutputStream
     */
    public ByteArrayOutputStream writeExcel(WriteCriteria writeCriteria) {
        return writeExcel(writeCriteriaToList(writeCriteria));
    }

    public ByteArrayOutputStream writeExcel(List<WriteCriteria> writeCriteriaList) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writeExcel(writeCriteriaList, null, outputStream);
        return outputStream;
    }

    public ByteArrayOutputStream writeExcel(String sheetName, String[] titleColumn, List<?> dataList) {
        return writeExcel(sheetName, titleColumn, dataList, null);
    }

    public ByteArrayOutputStream writeExcel(String sheetName, String[] titleColumn, List<?> dataList, Integer[] titleSize) {
        WriteCriteria writeCriteria = new WriteCriteria(sheetName, titleColumn, dataList, titleSize, null);
        return writeExcel(writeCriteria);
    }

    public ByteArrayOutputStream writeExcel(String sheetName, Map<String, String> headMap, List<?> dataList, Integer[] titleSize) {
        WriteCriteria writeCriteria = new WriteCriteria(sheetName, headMap, dataList, titleSize, null);
        return writeExcel(writeCriteria);
    }

    //导出私有方法

    /**
     * 设置导出标题
     *
     * @param titleNameRow 标题行数
     * @param titleHeader  标题
     * @return 每一列宽度
     */
    private void setTitleHeader(Row titleNameRow, String[] titleHeader, int[] columnIndexWidth) {
        //设置样式
        HSSFCellStyle titleStyle = getCellStyleNew();
        titleStyle = (HSSFCellStyle) setFontAndBorder(titleStyle, titleFontType, titleFontSize);
        //居中显示
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        if (StringUtils.isNotBlank(titleBackColor)) {
            titleStyle = (HSSFCellStyle) setColor(titleStyle, titleBackColor, (short) 10);
        }
        for (int i = 0; i < titleHeader.length; i++) {
            //如果columnIndexWidth不为null,说明没有自定义宽度
            if (columnIndexWidth != null) {
                //中文一个相当于2个,其他相当于1.3
                int length = StringUtil.calculatePlaces(titleHeader[i], 2.0, 1.3);
                columnIndexWidth[i] = length;
            }
            Cell cell = titleNameRow.createCell(i);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(titleHeader[i]);
        }
    }

    //导入私有方法

    /**
     * 判断指定的单元格是否是合并单元格
     *
     * @param sheet
     * @param row    行下标
     * @param column 列下标
     * @return
     */
    private boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 手动设置排除行
     *
     * @param excludeLineList 排除行数据
     * @param index           行的下标
     */
    private boolean getExcludeLineList(List<Integer> excludeLineList, Integer index) {
        for (Integer integer : excludeLineList) {
            if (integer.equals(index)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    public String getCellValue(Cell cell, boolean scienceFlag) {
        if (null == cell) {
            return "";
        }
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            CellValue evaluate = null;
            try {
                evaluate = readWorkBook.getCreationHelper().createFormulaEvaluator().evaluate(cell);
            } catch (FormulaParseException e) {
                String rawValue = ((XSSFCell) cell).getRawValue();
                logger.error("((XSSFCell) cell).getRawValue():【{}】,cell.toString():【{}】,readWorkBook.getCreationHelper().createFormulaEvaluator().evaluate(cell):", rawValue, cell.toString(), e);
                return rawValue;
            }
            if (evaluate.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                return dealFormulaWithNum(evaluate.getNumberValue(), scienceFlag);
            } else if (evaluate.getCellType() == Cell.CELL_TYPE_STRING) {
                return String.valueOf(evaluate.getStringValue());
            }
            return cell.getCellFormula();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            // 判断是否是日期格式
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                return dealFormulaDate(cell);
            } else {
                return dealFormulaWithNum(cell.getNumericCellValue(), scienceFlag);
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
            //错误格式 cell.toString()
            return ((XSSFCell) cell).getRawValue();
        }
        return cell.toString();
    }

    /**
     * 处理时间格式
     */
    private String dealFormulaDate(Cell cell) {
        // 获取单元格的样式值，即获取单元格格式对应的数值
        int style = cell.getCellStyle().getDataFormat();
        Date date = cell.getDateCellValue();
        String value = null;
        // 对不同格式的日期类型做不同的输出，与单元格格式保持一致
        switch (style) {
            case 14:
                value = new SimpleDateFormat("yyyy/MM/dd").format(date);
                break;
            case 22:
                value = new SimpleDateFormat(" yyyy/MM/dd HH:mm:ss").format(date);
                break;
            case 178:
                value = new SimpleDateFormat("yyyy'年'M'月'd'日'").format(date);
                break;
            case 179:
                value = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);
                break;
            case 180:
                value = new SimpleDateFormat("dd/MM/yyyy").format(date);
                break;
            case 181:
                value = new SimpleDateFormat("yyyy/MM/dd HH:mm a").format(date);
                break;
            default:
                value = cell.toString();
                break;
        }
        return value;
    }

    /**
     * 处理公式+科学计数法
     *
     * @param doubleValue 值
     * @param scienceFlag 是否保留科学计数法 false:不保留
     */
    private String dealFormulaWithNum(double doubleValue, boolean scienceFlag) {
        DecimalFormat df = new DecimalFormat("0");
        String value = String.valueOf(doubleValue);
        if (value.contains("E") || value.contains("e") || value.contains("+")) {
            if (scienceFlag) {
                return new BigDecimal(value).toString();
            } else {
                DecimalFormat decimalFormat = new DecimalFormat("#.###");
                //最多保留几位(10)
                decimalFormat.setMaximumFractionDigits(10);
                //最少保留几位(最小保留,不够补0显示), 可以是0 就是 取整数->  0.653==>0.653
                decimalFormat.setMinimumFractionDigits(0);
                return decimalFormat.format(doubleValue);
            }
        }
        return value;
    }

    /**
     * 判断当前行是否全都为Null或者""
     *
     * @param row
     */
    private static boolean judgmentEmptyRowFlag(Row row) {
        if (Objects.isNull(row)) {
            return false;
        }
        for (Cell cell : row) {
            if (cell != null && StringUtils.isNotBlank(cell.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取合并单元格的值
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private String getMergedRegionValue(Sheet sheet, int row, int column, boolean scienceFlag) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {

                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell, scienceFlag);
                }
            }
        }

        return null;
    }

    /**
     * 判断sheet页中是否含有合并单元格
     *
     * @param sheet
     * @return
     */
    private boolean hasMerged(Sheet sheet) {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }

    /**
     * 合并单元格
     *
     * @param sheet
     * @param firstRow 开始行
     * @param lastRow  结束行
     * @param firstCol 开始列
     * @param lastCol  结束列
     */
    private void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    //公共方法

    /**
     * 设置字体并加外边框
     *
     * @param style      样式
     * @param fontName   字体名
     * @param size       大小
     * @param boldFlag   是否加粗
     * @param borderFlag 是否加边框
     * @return
     */
    public CellStyle setFontAndBorder(CellStyle style, String fontName, short size, boolean boldFlag, boolean borderFlag) {
        Font font = writeWorkbook.createFont();
        font.setFontHeightInPoints(size);
        font.setFontName(fontName);
        if (boldFlag) {
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        }
        style.setFont(font);
        if (borderFlag) {
            //下边框
            style.setBorderBottom(CellStyle.BORDER_THIN);
            //左边框
            style.setBorderLeft(CellStyle.BORDER_THIN);
            //上边框
            style.setBorderTop(CellStyle.BORDER_THIN);
            //右边框
            style.setBorderRight(CellStyle.BORDER_THIN);
        }
        return style;
    }

    /**
     * 设置字体并加外边框
     *
     * @param style    样式
     * @param fontName 字体名
     * @param size     大小
     */
    public CellStyle setFontAndBorder(CellStyle style, String fontName, short size) {
        return setFontAndBorder(style, fontName, size, false, true);
    }

    /**
     * 将16进制的颜色代码写入样式中来设置颜色
     *
     * @param style 保证style统一
     * @param color 颜色：66FFDD
     * @param index 索引 8-64 使用时不可重复
     * @return
     */
    public CellStyle setColor(CellStyle style, String color, short index) {
        if (StringUtils.isNotBlank(color)) {
            //转为RGB码
            //转为16进制
            int r = Integer.parseInt((color.substring(0, 2)), 16);
            int g = Integer.parseInt((color.substring(2, 4)), 16);
            int b = Integer.parseInt((color.substring(4, 6)), 16);
            //自定义cell颜色
            HSSFPalette palette = writeWorkbook.getCustomPalette();
            palette.setColorAtIndex((short) index, (byte) r, (byte) g, (byte) b);

            // 填充方式
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            //颜色样式
            style.setFillForegroundColor(index);
        }
        return style;
    }

    /**
     * 创建一个样式
     *
     * @return HSSFCellStyle
     */
    private HSSFCellStyle getCellStyleNew() {
        return getCellStyleNew(null);
    }

    /**
     * 根据格式创建一个样式(避免重复创建)
     *
     * @param decimalFormat 格式
     */
    private HSSFCellStyle getCellStyleNew(String decimalFormat) {
        HSSFCellStyle cellStyle = writeWorkbook.createCellStyle();
        if (StringUtils.isNotBlank(decimalFormat)) {
            //保根据格式保留
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(decimalFormat));
        }
        return cellStyle;
    }

    private CellStyle getCellStyleNew(CellStyle cellStyle, String decimalFormat) {
        if (cellStyle == null) {
            cellStyle = writeWorkbook.createCellStyle();
        }
        if (StringUtils.isNotBlank(decimalFormat)) {
            //保根据格式保留
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(decimalFormat));
        }
        return cellStyle;
    }

    private List<WriteCriteria> writeCriteriaToList(WriteCriteria writeCriteria) {
        List<WriteCriteria> list = new ArrayList<>();
        list.add(writeCriteria);
        return list;
    }

    public WriteCriteria createWriteCriteria() {
        return new WriteCriteria();
    }

    public ReadCriteria createReadCriteria() {
        return new ReadCriteria();
    }

    public static class WriteCriteria {
        protected WriteCriteria() {
            super();
        }

        protected WriteCriteria(String sheetName, String[] titleColumn, List<?> dataList, Integer[] titleSize, String titleWrite) {
            this.sheetName = sheetName;
            this.titleColumn = titleColumn;
            this.dataList = dataList;
            this.titleWrite = titleWrite;
            this.titleSize = titleSize;
        }

        protected WriteCriteria(String sheetName, Map<String, String> writeTitleColumnAndNameMap, List<?> dataList, Integer[] titleSize, String titleWrite) {
            this.sheetName = sheetName;
            this.writeTitleColumnAndNameMap = writeTitleColumnAndNameMap;
            this.dataList = dataList;
            this.titleWrite = titleWrite;
            this.titleSize = titleSize;
        }

        private List<?> dataList;

        /**
         * 设置导出excel标题
         */
        private String titleWrite;

        /**
         * 对应实体类属性列 name
         */
        private String[] titleColumn;
        /**
         * excel列的别名 name:名称
         */
        private Map<String, String> writeTitleColumnAndNameMap;

        /**
         * 自定义列的宽度
         */
        private Integer[] titleSize;

        /**
         * 当前页名称
         */
        private String sheetName;

        /**
         * 设置是否导出表头
         */
        private Boolean titleHeaderFlag;

        public Boolean isTitleHeaderFlag() {
            return titleHeaderFlag;
        }

        public void setTitleHeaderFlag(Boolean titleHeaderFlag) {
            this.titleHeaderFlag = titleHeaderFlag;
        }

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public String[] getTitleColumn() {
            return titleColumn;
        }

        public void setTitleColumn(String[] titleColumn) {
            this.titleColumn = titleColumn;
        }

        public Map<String, String> getWriteTitleColumnAndNameMap() {
            return writeTitleColumnAndNameMap;
        }

        public void setWriteTitleColumnAndNameMap(Map<String, String> writeTitleColumnAndNameMap) {
            this.writeTitleColumnAndNameMap = writeTitleColumnAndNameMap;
        }

        public Integer[] getTitleSize() {
            return titleSize;
        }

        public void setTitleSize(Integer[] titleSize) {
            this.titleSize = titleSize;
        }

        public void setTitleWrite(String titleWrite) {
            this.titleWrite = titleWrite;
        }

        public void setDataList(List<?> dataList) {
            this.dataList = dataList;
        }

        public String getTitleWrite() {
            return titleWrite;
        }

        public List<?> getDataList() {
            return dataList;
        }

    }

    public static class ReadCriteria {
        protected ReadCriteria() {
            super();
        }
        //导入(读取)-import
    }
}
