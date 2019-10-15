package com.dan.common.util.excel;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * @version 1.0
 * @author: Bo
 * @fileName: ExcelImage
 * @createDate: 2019-10-15 10:54.
 * @description: excel导出图片属性
 */
public class ExcelImage {

    private short colHeight = 255;

    public ExcelImage(byte[] imageByte, short height, int format) {
        this(imageByte, height, format, 1, 0);
    }

    public ExcelImage(byte[] imageByte, short height, int format, int rowNum, int colNum) {
        this(imageByte, height, format, rowNum, colNum, 0, true);
    }

    public ExcelImage(byte[] imageByte, short height, int format, int rowNum, int colNum, int anchorType, boolean mergedRegionFlag) {
        if (height > 0) {
            this.height = (short) (height * colHeight);
        }
        this.rowNum = Math.max(rowNum, 1);
        this.colNum = colNum;
        this.imageByte = imageByte;
        this.format = format;
        this.anchorType = anchorType;
        this.mergedRegionFlag = mergedRegionFlag;
    }

    /**
     * JPEG format
     */
    public static final int PICTURE_TYPE_JPEG = Workbook.PICTURE_TYPE_JPEG;

    /**
     * PNG format
     */
    public static final int PICTURE_TYPE_PNG = Workbook.PICTURE_TYPE_PNG;

    /**
     * 单元格高度 * 255
     */
    private short height;

    /**
     * 占单元格几行-默认1(一行)
     */
    private int rowNum;
    /**
     * 占单元格几列-小于1自动获取列表总列
     */
    private int colNum;

    /**
     * 图片byte[]
     */
    private byte[] imageByte;

    /**
     * 图片格式
     * PICTURE_TYPE_JPEG/PICTURE_TYPE_PNG
     */
    private int format;
    /**
     * 0 =随单元移动并调整大小，2 =移动但不随单元调整大小，3 =不随单元移动或调整大小。
     * 默认:0
     */
    private int anchorType;

    /**
     * 是否合并单元格
     */
    private boolean mergedRegionFlag;

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getColNum() {
        return colNum;
    }

    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getAnchorType() {
        return anchorType;
    }

    public void setAnchorType(int anchorType) {
        this.anchorType = anchorType;
    }

    public boolean isMergedRegionFlag() {
        return mergedRegionFlag;
    }

    public void setMergedRegionFlag(boolean mergedRegionFlag) {
        this.mergedRegionFlag = mergedRegionFlag;
    }
}
