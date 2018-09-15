package com.github.automain.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtil {

    private static final String TYPE_STRING = "String";
    private static final String TYPE_INTEGER = "Integer";
    private static final String TYPE_LONG = "Long";
    private static final String TYPE_BIG_DECIMAL = "BigDecimal";
    private static final String TYPE_DATE = "Date";
    public static final String FORMAT_NAME_JPG = "jpg";
    public static final String FORMAT_NAME_PNG = "png";

    /**
     * 读取工作簿
     *
     * @param path
     * @return
     */
    private static Workbook readWorkbook(String path) throws Exception {
        File file = new File(path);
        if (SystemUtil.checkFileAvailable(file)) {
            try (FileInputStream fis = new FileInputStream(path)) {
                return WorkbookFactory.create(fis);
            }
        }
        return null;
    }

    /**
     * 读取XLSX
     *
     * @param path
     * @return
     */
    public static XSSFWorkbook readXLSX(String path) throws Exception {
        Workbook wb = readWorkbook(path);
        return wb == null ? null : (XSSFWorkbook) wb;
    }

    /**
     * 读取XLS
     *
     * @param path
     * @return
     */
    public static HSSFWorkbook readXLS(String path) throws Exception {
        Workbook wb = readWorkbook(path);
        return wb == null ? null : (HSSFWorkbook) wb;
    }

    /**
     * 导出工作簿
     *
     * @param path
     * @param wb
     * @return
     */
    public static void writeWorkbook(String path, Workbook wb) throws IOException {
        if (path != null && wb != null) {
            File file = new File(path);
            if (UploadUtil.mkdirs(file.getParentFile())) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    wb.write(fos);
                }
            }
        }
    }

    /**
     * 根据行坐标获取行
     *
     * @param sheet
     * @param rowNum
     * @return
     */
    public static Row getRow(Sheet sheet, int rowNum) {
        if (sheet != null && rowNum >= 0) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                sheet.createRow(rowNum);
                row = sheet.getRow(rowNum);
            }
            return row;
        }
        return null;
    }

    /**
     * 根据行和列坐标获取单元格
     *
     * @param row
     * @param colNum
     * @return
     */
    public static Cell getCell(Row row, int colNum) {
        if (row != null && colNum >= 0) {
            Cell cell = row.getCell(colNum);
            if (cell == null) {
                row.createCell(colNum);
                cell = row.getCell(colNum);
            }
            return cell;
        }
        return null;
    }

    /**
     * 根据行坐标和列坐标获取单元格
     *
     * @param sheet
     * @param rowNum
     * @param colNum
     * @return
     */
    public static Cell getCell(Sheet sheet, int rowNum, int colNum) {
        if (sheet != null && rowNum >= 0 && colNum >= 0) {
            return getCell(getRow(sheet, rowNum), colNum);
        }
        return null;
    }

    /**
     * 设置单元格边框加居中格式
     *
     * @param wb
     * @return
     */
    public static CellStyle borderAndCenter(Workbook wb) {
        if (wb == null) {
            return null;
        }
        CellStyle style = wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 切换多行隐藏/显示
     *
     * @param sheet
     * @param firstRow
     * @param lastRow
     * @return
     */
    public static void toggleRowsVisible(Sheet sheet, int firstRow, int lastRow, boolean hide) {
        if (sheet != null && firstRow >= 0 && lastRow >= firstRow) {
            for (int rowNum = firstRow; rowNum <= lastRow; rowNum++) {
                sheet.getRow(rowNum).setZeroHeight(hide);
            }
        }
    }

    /**
     * 切换多列隐藏/显示
     *
     * @param sheet
     * @param firstCol
     * @param lastCol
     * @return
     */
    public static void toggleColsVisible(Sheet sheet, int firstCol, int lastCol, boolean hide) {
        if (sheet != null && firstCol >= 0 && lastCol >= firstCol) {
            for (int colNum = firstCol; colNum <= lastCol; colNum++) {
                sheet.setColumnHidden(colNum, hide);
            }
        }
    }

    /**
     * 批量设置行高
     *
     * @param sheet
     * @param firstRow
     * @param lastRow
     * @param height   要设置的行高(单位:字符)
     * @return
     */
    public static void setHeightInPoints(Sheet sheet, int firstRow, int lastRow, float height) {
        if (sheet != null && firstRow >= 0 && lastRow >= firstRow) {
            for (int rowNum = firstRow; rowNum <= lastRow; rowNum++) {
                sheet.getRow(rowNum).setHeightInPoints(height);
            }
        }
    }

    /**
     * 设置所有行高
     *
     * @param sheet
     * @param height 要设置的行高(单位:字符)
     * @return
     */
    public static void setAllHeightInPoints(Sheet sheet, float height) {
        if (sheet != null) {
            setHeightInPoints(sheet, 0, sheet.getLastRowNum(), height);
        }
    }

    /**
     * 批量设置列宽
     *
     * @param sheet
     * @param firstCol
     * @param lastCol
     * @param width    要设置的列宽(单位:字符)
     * @return
     */
    public static void setColumnWidth(Sheet sheet, int firstCol, int lastCol, int width) {
        if (sheet != null && firstCol >= 0 && lastCol >= firstCol) {
            for (int colNum = firstCol; colNum <= lastCol; colNum++) {
                // 列宽设置，1个单位为1个字符的256分之一
                sheet.setColumnWidth(colNum, 256 * width);
            }
        }
    }

    /**
     * 批量设置单元格样式
     *
     * @param sheet
     * @param style
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol
     * @return
     */
    public static void setCellStyle(Sheet sheet, CellStyle style, int firstRow, int lastRow, int firstCol, int lastCol) {
        if (sheet != null && firstRow >= 0 && firstCol >= 0 && lastRow >= firstRow && lastCol >= firstCol) {
            for (int i = firstRow; i <= lastRow; i++) {
                for (int j = firstCol; j <= lastCol; j++) {
                    getCell(sheet, i, j).setCellStyle(style);
                }
            }
        }
    }

    /**
     * 根据颜色值获取对应前景色的单元格格式
     *
     * @param wb
     * @param colorIndex 要设置的颜色值，如：IndexedColors.GREY_40_PERCENT.getIndex()
     * @return
     */
    public static CellStyle getForegroundColorStyle(Workbook wb, short colorIndex) {
        if (wb != null) {
            CellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(colorIndex);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);// 前景色
            return style;
        }
        return null;
    }

    private static List initListByType(String type, int initialCapacity) {
        if (initialCapacity <= 0) {
            initialCapacity = 10;
        }
        List retList = null;
        switch (type) {
            case TYPE_STRING:
                retList = new ArrayList<String>(initialCapacity);
                break;
            case TYPE_INTEGER:
                retList = new ArrayList<Integer>(initialCapacity);
                break;
            case TYPE_BIG_DECIMAL:
                retList = new ArrayList<BigDecimal>(initialCapacity);
                break;
            case TYPE_LONG:
                retList = new ArrayList<Long>(initialCapacity);
                break;
            case TYPE_DATE:
                retList = new ArrayList<Date>(initialCapacity);
                break;
            default:
                retList = new ArrayList(initialCapacity);
        }
        return retList;
    }

    /**
     * 根据类型获取一行
     *
     * @param sheet
     * @param rowNum
     * @param firstCol
     * @param lastCol
     * @param type
     * @return
     */
    private static List readRow(Sheet sheet, int rowNum, int firstCol, int lastCol, String type, boolean notNull) {
        if (sheet != null && rowNum >= 0 && firstCol >= 0) {
            int initialCapacity = 10;
            if (!notNull) {
                if (lastCol < firstCol) {
                    return null;
                } else {
                    initialCapacity = lastCol - firstCol + 1;
                }
            } else {
                lastCol = Integer.MAX_VALUE;
            }
            List retList = initListByType(type, initialCapacity);
            Row row = getRow(sheet, rowNum);
            for (int i = firstCol; i <= lastCol; i++) {
                Cell cell = getCell(row, i);
                if (!addCellToList(type, notNull, retList, cell)) {
                    break;
                }
            }
            return retList;
        }
        return null;
    }

    /**
     * 根据类型获取一列
     *
     * @param sheet
     * @param colNum
     * @param firstRow
     * @param lastRow
     * @param type
     * @param notNull
     * @return
     */
    private static List readCol(Sheet sheet, int colNum, int firstRow, int lastRow, String type, boolean notNull) {
        if (sheet != null && colNum >= 0 && firstRow >= 0) {
            int initialCapacity = 10;
            if (!notNull) {
                if (lastRow < firstRow) {
                    return null;
                } else {
                    initialCapacity = lastRow - firstRow + 1;
                }
            } else {
                lastRow = Integer.MAX_VALUE;
            }
            List retList = initListByType(type, initialCapacity);
            for (int i = firstRow; i <= lastRow; i++) {
                Cell cell = getCell(sheet, i, colNum);
                if (!addCellToList(type, notNull, retList, cell)) {
                    break;
                }
            }
            return retList;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static boolean addCellToList(String type, boolean notNull, List retList, Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                if (TYPE_STRING.equals(type)) {
                    retList.add(cell.getStringCellValue());
                }
                break;
            case FORMULA:
            case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (TYPE_DATE.equals(type)) {
                        retList.add(date);
                    } else if (TYPE_STRING.equals(type)) {
                        retList.add(DateUtil.convertDateToString(date, DateUtil.SIMPLE_DATE_TIME_PATTERN));
                    }
                } else {
                    BigDecimal cellValue = BigDecimal.valueOf(cell.getNumericCellValue());
                    switch (type) {
                        case TYPE_INTEGER:
                            retList.add(cellValue.intValue());
                            break;
                        case TYPE_LONG:
                            retList.add(cellValue.longValue());
                            break;
                        case TYPE_BIG_DECIMAL:
                            retList.add(cellValue);
                            break;
                        default:
                            retList.add(cellValue.toPlainString());
                            break;
                    }
                }
                break;
            case BOOLEAN:
                if (TYPE_STRING.equals(type)) {
                    retList.add(cell.getBooleanCellValue() ? "TRUE" : "FALSE");
                }
                break;
            default:
                if (notNull) {
                    return false;
                }
                switch (type) {
                    case TYPE_INTEGER:
                        retList.add(0);
                        break;
                    case TYPE_LONG:
                        retList.add(0L);
                        break;
                    case TYPE_BIG_DECIMAL:
                        retList.add(BigDecimal.ZERO);
                        break;
                    case TYPE_STRING:
                        retList.add("");
                        break;
                    default:
                        break;
                }
        }
        return true;
    }

    /**
     * 读取一行字符串格式数据
     *
     * @param sheet
     * @param rowNum
     * @param firstCol
     * @param lastCol
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> readRowString(Sheet sheet, int rowNum, int firstCol, int lastCol) {
        return readRow(sheet, rowNum, firstCol, lastCol, TYPE_STRING, false);
    }

    /**
     * 读取一行非空的字符串格式数据
     *
     * @param sheet
     * @param rowNum
     * @param firstCol
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> readNotNullRowString(Sheet sheet, int rowNum, int firstCol) {
        return readRow(sheet, rowNum, firstCol, 0, TYPE_STRING, true);
    }

    /**
     * 读取一行整型格式数据
     *
     * @param sheet
     * @param rowNum
     * @param firstCol
     * @param lastCol
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> readRowInteger(Sheet sheet, int rowNum, int firstCol, int lastCol) {
        return readRow(sheet, rowNum, firstCol, lastCol, TYPE_INTEGER, false);
    }

    /**
     * 读取一行非空的整型格式数据
     *
     * @param sheet
     * @param rowNum
     * @param firstCol
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> readNotNullRowInteger(Sheet sheet, int rowNum, int firstCol) {
        return readRow(sheet, rowNum, firstCol, 0, TYPE_INTEGER, true);
    }

    /**
     * 读取一行双精度格式数据
     *
     * @param sheet
     * @param rowNum
     * @param firstCol
     * @param lastCol
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<BigDecimal> readRowBigDecimal(Sheet sheet, int rowNum, int firstCol, int lastCol) {
        return readRow(sheet, rowNum, firstCol, lastCol, TYPE_BIG_DECIMAL, false);
    }

    /**
     * 读取一行非空的双精度格式数据
     *
     * @param sheet
     * @param rowNum
     * @param firstCol
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<BigDecimal> readRowBigDecimal(Sheet sheet, int rowNum, int firstCol) {
        return readRow(sheet, rowNum, firstCol, 0, TYPE_BIG_DECIMAL, true);
    }

    /**
     * 读取一行长整型格式数据
     *
     * @param sheet
     * @param rowNum
     * @param firstCol
     * @param lastCol
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Long> readRowLong(Sheet sheet, int rowNum, int firstCol, int lastCol) {
        return readRow(sheet, rowNum, firstCol, lastCol, TYPE_LONG, false);
    }

    /**
     * 读取一行非空的长整型格式数据
     *
     * @param sheet
     * @param rowNum
     * @param firstCol
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Long> readNotNullRowLong(Sheet sheet, int rowNum, int firstCol) {
        return readRow(sheet, rowNum, firstCol, 0, TYPE_LONG, true);
    }

    /**
     * 读取一行日期格式数据
     *
     * @param sheet
     * @param rowNum
     * @param firstCol
     * @param lastCol
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Date> readRowDate(Sheet sheet, int rowNum, int firstCol, int lastCol) {
        return readRow(sheet, rowNum, firstCol, lastCol, TYPE_DATE, false);
    }

    /**
     * 读取一行非空的日期格式数据
     *
     * @param sheet
     * @param rowNum
     * @param firstCol
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Date> readNotNullRowDate(Sheet sheet, int rowNum, int firstCol) {
        return readRow(sheet, rowNum, firstCol, 0, TYPE_DATE, true);
    }

    /**
     * 写入一行多种格式数据
     *
     * @param sheet
     * @param rowNum
     * @param firstCol
     * @param list
     * @return
     */
    @SuppressWarnings("unchecked")
    public static void writeRow(Sheet sheet, int rowNum, int firstCol, List list) {
        if (sheet != null && list != null && rowNum >= 0 && firstCol >= 0) {
            Row row = getRow(sheet, rowNum);
            for (Object o : list) {
                getCell(row, firstCol).setCellValue(String.valueOf(o));
                firstCol++;
            }
        }
    }

    /**
     * 读取一列字符串格式数据
     *
     * @param sheet
     * @param colNum
     * @param firstRow
     * @param lastRow
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> readColString(Sheet sheet, int colNum, int firstRow, int lastRow) {
        return readCol(sheet, colNum, firstRow, lastRow, TYPE_STRING, false);
    }

    /**
     * 读取一列非空的字符串格式数据
     *
     * @param sheet
     * @param colNum
     * @param firstRow
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> readNotNullColString(Sheet sheet, int colNum, int firstRow) {
        return readCol(sheet, colNum, firstRow, 0, TYPE_STRING, true);
    }

    /**
     * 读取一列整型格式数据
     *
     * @param sheet
     * @param colNum
     * @param firstRow
     * @param lastRow
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> readColInteger(Sheet sheet, int colNum, int firstRow, int lastRow) {
        return readCol(sheet, colNum, firstRow, lastRow, TYPE_INTEGER, false);
    }

    /**
     * 读取一列非空的整型格式数据
     *
     * @param sheet
     * @param colNum
     * @param firstRow
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> readNotNullColInteger(Sheet sheet, int colNum, int firstRow) {
        return readCol(sheet, colNum, firstRow, 0, TYPE_INTEGER, true);
    }

    /**
     * 读取一列双精度格式数据
     *
     * @param sheet
     * @param colNum
     * @param firstRow
     * @param lastRow
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<BigDecimal> readColBigDecimal(Sheet sheet, int colNum, int firstRow, int lastRow) {
        return readCol(sheet, colNum, firstRow, lastRow, TYPE_BIG_DECIMAL, false);
    }

    /**
     * 读取一列非空的双精度格式数据
     *
     * @param sheet
     * @param colNum
     * @param firstRow
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<BigDecimal> readNotNullColBigDecimal(Sheet sheet, int colNum, int firstRow) {
        return readCol(sheet, colNum, firstRow, 0, TYPE_BIG_DECIMAL, true);
    }

    /**
     * 读取一列长整型格式数据
     *
     * @param sheet
     * @param colNum
     * @param firstRow
     * @param lastRow
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Long> readColLong(Sheet sheet, int colNum, int firstRow, int lastRow) {
        return readCol(sheet, colNum, firstRow, lastRow, TYPE_LONG, false);
    }

    /**
     * 读取一列非空的长整型格式数据
     *
     * @param sheet
     * @param colNum
     * @param firstRow
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Long> readNotNullColLong(Sheet sheet, int colNum, int firstRow) {
        return readCol(sheet, colNum, firstRow, 0, TYPE_LONG, true);
    }

    /**
     * 读取一列日期格式数据
     *
     * @param sheet
     * @param colNum
     * @param firstRow
     * @param lastRow
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Date> readColDate(Sheet sheet, int colNum, int firstRow, int lastRow) {
        return readCol(sheet, colNum, firstRow, lastRow, TYPE_DATE, false);
    }

    /**
     * 读取一列非空的日期格式数据
     *
     * @param sheet
     * @param colNum
     * @param firstRow
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Date> readNotNullColDate(Sheet sheet, int colNum, int firstRow) {
        return readCol(sheet, colNum, firstRow, 0, TYPE_DATE, true);
    }

    /**
     * 写入一列多种格式数据
     *
     * @param sheet
     * @param colNum
     * @param firstRow
     * @param list
     * @return
     */
    @SuppressWarnings("unchecked")
    public static void writeCol(Sheet sheet, int colNum, int firstRow, List list) {
        if (sheet != null && list != null && colNum >= 0 && firstRow >= 0) {
            for (Object o : list) {
                getCell(sheet, firstRow, colNum).setCellValue(String.valueOf(o));
                firstRow++;
            }
        }
    }

    /**
     * 向单元格写入公式
     *
     * @param cell
     * @param formula
     * @return
     */
    public static void setCellFormula(Cell cell, String formula) {
        if (cell != null && StringUtils.isNotBlank(formula)) {
            cell.setCellType(CellType.FORMULA);
            cell.setCellFormula(formula);
        }
    }

    /**
     * 用于将Excel表格中列号字母转成列索引，从1对应A开始
     *
     * @param colNum
     * @return
     */
    public static int colNumToIndex(String colNum) {
        int index = 0;
        if (colNum.matches("[A-Z]+")) {
            char[] chars = colNum.toUpperCase().toCharArray();
            int size = chars.length;
            for (int i = 0; i < size; i++) {
                index += ((int) chars[i] - (int) 'A' + 1) * (int) Math.pow(26, size - i - 1);
            }
        }
        return index;
    }

    /**
     * 用于将excel表格中列索引转成列号字母，从A对应1开始
     *
     * @param index
     * @return
     */
    public static String indexToColNum(int index) {
        String colNum = "";
        if (index > 0) {
            index--;
            do {
                if (colNum.length() > 0) {
                    index--;
                }
                colNum = ((char) (index % 26 + (int) 'A')) + colNum;
                index = (int) ((index - index % 26) / 26);
            } while (index > 0);
        }
        return colNum;
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet
     * @param textList
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastRow
     * @return
     */

    public static void setXLSValidation(HSSFSheet sheet, String[] textList, int firstRow, int lastRow, int firstCol, int lastCol) {
        if (sheet != null && textList != null && firstRow >= 0 && firstCol >= 0 && lastRow >= firstRow && lastCol >= firstCol) {
            // 加载下拉列表内容
            DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(textList);
            // 设置应用范围
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
            HSSFDataValidation validation = new HSSFDataValidation(addressList, dvConstraint);
            sheet.addValidationData(validation);
        }
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet
     * @param textList
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol
     * @return
     */

    public static void setXLSXValidation(XSSFSheet sheet, String[] textList, int firstRow, int lastRow, int firstCol, int lastCol) {
        if (sheet != null && textList != null && firstRow >= 0 && firstCol >= 0 && lastRow >= firstRow && lastCol >= firstCol) {
            XSSFDataValidationHelper helper = new XSSFDataValidationHelper(sheet);
            // 加载下拉列表内容
            XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) helper.createExplicitListConstraint(textList);
            // 设置应用范围
            CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
            XSSFDataValidation validation = (XSSFDataValidation) helper.createValidation(dvConstraint, addressList);
            sheet.addValidationData(validation);
        }
    }

    /**
     * 向XLS工作簿中添加图片
     *
     * @param wb
     * @param patriarch   调用sheet.createDrawingPatriarch()得到，插入多张图片只创建一次该对象
     * @param picturePath
     * @param formatName  要插入的图片格式(本类中静态常量，可选为JPG或PNG)
     * @param firstRow
     * @param firstCol
     * @param lastRow
     * @param lastCol
     * @return
     */
    public static void insertPictureXLS(HSSFWorkbook wb, HSSFPatriarch patriarch, String picturePath, String formatName, int firstRow, int firstCol, int lastRow, int lastCol) throws IOException {
        if (wb != null && patriarch != null && picturePath != null && formatName != null && firstRow >= 0 && firstCol >= 0 && lastRow >= firstRow && lastCol >= firstCol) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                File file = new File(picturePath);
                if (SystemUtil.checkFileAvailable(file)) {
                    BufferedImage bufferedImage = ImageIO.read(file);
                    ImageIO.write(bufferedImage, formatName, bos);
                    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) firstCol, firstRow, (short) (lastCol + 1), lastRow + 1);
                    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                    if (formatName.equals(FORMAT_NAME_JPG)) {
                        patriarch.createPicture(anchor, wb.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                    } else if (formatName.equals(FORMAT_NAME_PNG)) {
                        patriarch.createPicture(anchor, wb.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
                    }
                }
            }
        }
    }

    /**
     * 向XLSX工作簿中添加图片
     *
     * @param wb
     * @param patriarch   调用sheet.createDrawingPatriarch()得到，插入多张图片只创建一次该对象
     * @param picturePath
     * @param formatName  要插入的图片格式(本类中静态常量，可选为JPG或PNG)
     * @param firstRow
     * @param firstCol
     * @param lastRow
     * @param lastCol
     * @return
     */
    public static void insertPictureXLSX(XSSFWorkbook wb, Drawing patriarch, String picturePath, String formatName, int firstRow, int firstCol, int lastRow, int lastCol) throws IOException {
        if (wb != null && patriarch != null && picturePath != null && formatName != null && firstRow >= 0 && firstCol >= 0 && lastRow >= firstRow && lastCol >= firstCol) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                File file = new File(picturePath);
                if (SystemUtil.checkFileAvailable(file)) {
                    BufferedImage bufferedImage = ImageIO.read(file);
                    ImageIO.write(bufferedImage, formatName, bos);
                    XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, firstCol, firstRow, lastCol + 1, lastRow + 1);
                    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                    if (formatName.equals(FORMAT_NAME_JPG)) {
                        patriarch.createPicture(anchor, wb.addPicture(bos.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));
                    } else if (formatName.equals(FORMAT_NAME_PNG)) {
                        patriarch.createPicture(anchor, wb.addPicture(bos.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG));
                    }
                }
            }
        }
    }

    /**
     * 合并单元格
     *
     * @param sheet
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol
     * @return
     */
    public static void addMergedRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        if (sheet != null && firstRow >= 0 && firstCol >= 0 && lastRow >= firstRow && lastCol >= firstCol) {
            sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
        }
    }

    /**
     * 合并单元格(根据列号)
     *
     * @param sheet
     * @param indexBegin
     * @param indexEnd
     * @return
     */
    public static void addMergedRegion(Sheet sheet, String indexBegin, String indexEnd) {
        if (sheet != null && StringUtils.isNotBlank(indexBegin) && StringUtils.isNotBlank(indexEnd)) {
            sheet.addMergedRegion(CellRangeAddress.valueOf(indexBegin + ":" + indexEnd));
        }
    }

    /**
     * 向单元格中添加超链接(linkType为HyperLink类的枚举常量)
     *
     * @param wb
     * @param sheet
     * @param cell
     * @param linkType
     * @param linkAddress
     * @return
     */
    public static void addHyperlinkToCell(Workbook wb, Sheet sheet, Cell cell, HyperlinkType linkType, String linkAddress) {
        if (wb != null && sheet != null && cell != null && StringUtils.isNotBlank(linkAddress)) {
            CreationHelper createHelper = wb.getCreationHelper();
            CellStyle hlink_style = wb.createCellStyle();
            Font hlink_font = wb.createFont();
            hlink_font.setUnderline(Font.U_SINGLE);
            hlink_font.setColor(IndexedColors.BLUE.getIndex());
            hlink_style.setFont(hlink_font);
            Hyperlink link = createHelper.createHyperlink(linkType);
            link.setAddress(linkAddress);
            cell.setHyperlink(link);
            cell.setCellStyle(hlink_style);
        }
    }

}
