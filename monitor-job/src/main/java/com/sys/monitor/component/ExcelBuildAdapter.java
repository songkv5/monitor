package com.sys.monitor.component;

import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sys.monitor.annotation.FieldQualifier;
import com.sys.monitor.util.ClassUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author willis
 * @desc
 * @since 2020年02月26日 17:52
 */
public class ExcelBuildAdapter {
    private static Logger logger = LoggerFactory.getLogger(ExcelBuildAdapter.class);
    private static final Integer DEFAULT_MAX_ROW_PER_SHEET = 65535;
    private File file;

    private ExcelBuildAdapter() {
    }

    public File getFile() {
        return file;
    }
    public static <T> ExcelBuilder<T> newBuilder(String filePath) {
        return new ExcelBuilder<T>(filePath);
    }

    public static class ExcelBuilder<T>{
        private String filePath;
        private HSSFWorkbook wb;
        private Integer maxRows;
        private int startColumnNum;
        private FileType finalType = FileType.EXCEL;
        private File pdfFile;

        public ExcelBuilder(String filePath) {
            this.filePath = filePath;
            wb = new HSSFWorkbook();
            maxRows = DEFAULT_MAX_ROW_PER_SHEET;
            startColumnNum = 0;
        }

        public ExcelBuildAdapter get() {
            ExcelBuildAdapter adapter = new ExcelBuildAdapter();
            File desFile = new File(filePath.concat(finalType.getSuffix()));
            File parentFile = desFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdir();
            }
            try {
                if (!desFile.exists()) {
                    desFile.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(desFile);
                switch (finalType) {
                    case EXCEL:{
                        wb.write(fileOutputStream);
                        break;
                    }
                    case PDF:{
                        break;
                    }
                }

                fileOutputStream.close();
                wb.close();
                adapter.file = desFile;
            } catch (Exception e) {
            }
            return adapter;
        }

        //TODO 有问题
        private ExcelBuilder<T> convertPdf() {
            HSSFSheet s = wb.getSheetAt(0);
            Iterator<Row> iterator = s.iterator();
            Document xls2Pdf = new Document();
            try {
                FileOutputStream pdfOs = new FileOutputStream(filePath.concat(FileType.PDF.getSuffix()));
                PdfWriter pdfWriter = PdfWriter.getInstance(xls2Pdf, pdfOs);
                xls2Pdf.open();
                PdfPTable table = new PdfPTable(11);
                PdfPCell pCell;

                while(iterator.hasNext()) {
                    Row row = iterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while(cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch(cell.getCellType()) {
                            case STRING:
                                pCell = new PdfPCell(new Phrase(cell.getStringCellValue()));
                                table.addCell(pCell);
                                break;
                        }
                        //next line
                    }
                }
                xls2Pdf.add(table);
                xls2Pdf.close();

                finalType = FileType.PDF;
                pdfWriter.close();
                pdfOs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }
        public ExcelBuilder<T> startColumnNum(int startColumnNum) {
            this.startColumnNum = startColumnNum;
            return this;
        }
        public ExcelBuilder<T> maxRowPerSheet(Integer maxRows) {
            this.maxRows = maxRows;
            return this;
        }
        public ExcelBuilder<T> append(List<T> datas) {
            this.append(datas, false);
            return this;
        }

        private List<Field> suitableField(Class<T> clazz) {
            List<Field> fieldsTmp = ClassUtil.getAllField(clazz);
            List<Field> fields = new ArrayList<Field>();
            for (Field field : fieldsTmp) {
                FieldQualifier annotation = field.getAnnotation(FieldQualifier.class);
                if (annotation == null) {
                    fields.add(field);
                } else {
                    if (!annotation.exclude()) {
                        fields.add(field);
                    }
                }
            }
            // 排序字段
            sort(fields);
            return fields;
        }

        private HSSFSheet newestSheet() {
            // sheet 页数量
            int numberOfSheets = wb.getNumberOfSheets();
            HSSFSheet s = null;
            if (numberOfSheets == 0) {
                s = wb.createSheet();
            } else {
                // 取得当前sheet页
                s= wb.getSheetAt(numberOfSheets - 1);
            }
            return s;
        }

        /**
         * 合并单元格
         * @param frow
         * @param lrow
         * @param fcol
         * @param lcol
         * @param txt
         * @return
         */
        public ExcelBuilder<T> mergeCells(int frow, int lrow, int fcol, int lcol, String txt) {
            CellRangeAddress region  = new CellRangeAddress(frow, lrow, fcol, lcol);
            HSSFSheet sheet = newestSheet();
            sheet.addMergedRegion(region);
            HSSFCell cell = sheet.getRow(frow).createCell(fcol);
            sheet.autoSizeColumn(fcol, true);

            HSSFCellStyle style = wb.createCellStyle();
            Font font = wb.createFont();
            font.setFontName("微软雅黑");
            font.setBold(true);
            style = wb.createCellStyle();
            style.setFont(font);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            // 自动换行
            style.setWrapText(true);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(txt));
            return this;
        }
        public ExcelBuilder<T> append(List<T> datas, boolean genTitle) {
            if (datas == null || datas.size() == 0) {
                return this;
            }
            Class clazz = datas.get(0).getClass();
            // 拿到最终的列表字段
            List<Field> fields = suitableField(clazz);
            // 总列数
            int columnCounts = fields.size();
            // 最新一个sheet
            HSSFSheet s = newestSheet();
            // 最后一行
            int rowNum = s.getLastRowNum();

            try {
                // 是否生成表头
                if (rowNum == 0) {
                    createHead(s, rowNum, fields);
                } else {
                    if (genTitle) {
                        createHead(s, ++ rowNum, fields);
                    }
                }

                for (T data : datas) {
                    if (rowNum >= this.maxRows) {
                        s = wb.createSheet();
                        rowNum = 0;
                        createHead(s, rowNum, fields);
                    }
                    //写数据
                    writeRowData(s, ++ rowNum, fields, data);
                }
            } catch (Exception e) {
                logger.error("出错了", e);
            }
            return this;
        }

        /**
         * 写入一行数据
         * @param s
         * @param rowNum
         * @param fields
         * @param data
         */
        private void writeRowData(HSSFSheet s, int rowNum, List<Field> fields, T data) {
            try {
                //新建一行
                HSSFRow dataRow = s.createRow(rowNum);
                int columnCounts = fields.size();
                //统一字体
                for (int j_ = 0 ; j_ < columnCounts ; j_++) {//列
                    //新建一格
                    HSSFCell c = dataRow.createCell(j_ + startColumnNum);
                    c.setCellStyle(cellStyleData());
                    // FieldEntry fieldEntry = fields.get(j_);
                    Field field = fields.get(j_);
                    FieldQualifier annotation = field.getAnnotation(FieldQualifier.class);
                    Class z = data.getClass();
                    Class fz = z.getSuperclass();

                    Field f = null;
                    try {
                        f = z.getDeclaredField(field.getName());
                    } catch (NoSuchFieldException e) {
                        f = fz.getDeclaredField(field.getName());
                    }
                    f.setAccessible(true);
                    //取值
                    Object cellValue = f.get(data);
                    if (cellValue instanceof Date) {
                        String dateFmt = annotation.dateFmt();
                        SimpleDateFormat sdf = new SimpleDateFormat(dateFmt);
                        cellValue = sdf.format((Date) cellValue);
                    }
                    c.setCellValue(cellValue == null ? "" : cellValue.toString());
                }
            } catch (Exception e) {
                logger.error("数据写入错误", e);
            }
        }
        /**
         * 添加表头
         * @param s
         * @param rowNum
         * @param fields
         */
        private void createHead(HSSFSheet s, int rowNum, List<Field> fields) {
            int columnCounts = fields.size();
            HSSFRow r = s.createRow(rowNum);
            r.setHeight((short) 256);
            for (int i_ = 0 ; i_ < columnCounts ; i_++) {
                HSSFCell c = r.createCell(i_ + startColumnNum);
                c.setCellStyle(cellStyleTitle(wb));
                Field field = fields.get(i_);
                FieldQualifier annotation = field.getAnnotation(FieldQualifier.class);
                if (annotation == null) {
                    c.setCellValue(field.getName());
                } else {
                    String alias = annotation.alias();
                    boolean b = annotation.autoSize();
                    int width = annotation.width();
                    //字段别名
                    c.setCellValue(alias);
                    s.autoSizeColumn(i_, b);
                    if (width > 0) {
                        s.setColumnWidth(i_, width);
                    }
                }
            }
        }
        /**
         * 字段排序
         * @param fields
         */
        private static void sort(List<Field> fields) {
            if (fields == null || fields.isEmpty()) {
                return ;
            }
            Collections.sort(fields, (f1, f2) -> {
                FieldQualifier annotation1 = f1.getAnnotation(FieldQualifier.class);
                FieldQualifier annotation2 = f2.getAnnotation(FieldQualifier.class);
                int seq1 = 0;
                int seq2 = 0;
                if (annotation1 != null) {
                    seq1 = annotation1.sequence();
                }
                if (annotation2 != null) {
                    seq2 = annotation2.sequence();
                }
                return seq1 - seq2;
            });
        }

        /**
         * 数据样式
         * @return
         */
        private CellStyle cellStyleData() {
            //内容样式

            HSSFCellStyle cellStyle = this.wb.createCellStyle();
            Font fontData = wb.createFont();
            fontData.setFontName("微软雅黑");
            cellStyle = wb.createCellStyle();
            cellStyle.setFont(fontData);
            //自动换行
            cellStyle.setWrapText(true);
            return cellStyle;
        }

        /**
         * 标题样式
         * @param wb
         * @return
         */
        private static CellStyle cellStyleTitle(HSSFWorkbook wb) {
            if (wb != null) {
                //表头样式
                CellStyle cellStyleTitle = wb.createCellStyle();
                Font fontTitle = wb.createFont();
                fontTitle.setFontName("微软雅黑");
                fontTitle.setColor(IndexedColors.WHITE.getIndex());
                fontTitle.setBold(true);
                cellStyleTitle.setFont(fontTitle);
                cellStyleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyleTitle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
                return cellStyleTitle;
            }
            return null;
        }

    }
}
