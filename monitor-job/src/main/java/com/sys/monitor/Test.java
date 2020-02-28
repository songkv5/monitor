package com.sys.monitor;

import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sys.monitor.component.FileType;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

/**
 * @Author willis
 * @desc
 * @since 2020年02月28日 09:39
 */
public class Test {
    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream("E:\\7c86750dc48c0e07809e7f7cc137dc5400dc538cffd66dc7dc6ab117d5c765ea.xls");
            HSSFWorkbook wb = new HSSFWorkbook(fis);
            HSSFSheet s = wb.getSheetAt(0);
            Iterator<Row> iterator = s.iterator();
            Document xls2Pdf = new Document();
            FileOutputStream pdfOs = new FileOutputStream("E:\\7c86750dc48c0e07809e7f7cc137dc5400dc538cffd66dc7dc6ab117d5c765ea".concat(FileType.PDF.getSuffix()));
            PdfWriter pdfWriter = PdfWriter.getInstance(xls2Pdf, pdfOs);
            xls2Pdf.open();
            PdfPTable table = new PdfPTable(12);
            PdfPCell pCell;

            while(iterator.hasNext()) {
                Row row = iterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while(cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    CellStyle cellStyle = cell.getCellStyle();

                    CellType cellType = cell.getCellType();
                    switch(cellType) {
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

//            finalType = FileType.PDF;
            pdfWriter.close();
            pdfOs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
