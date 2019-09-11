package com.spring.mvc2.view;

import com.spring.mvc2.model.PageRank;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class PageRankView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook hssfWorkbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setHeader("Content-Dispostion", "attachment; filename=\"pagerank.xls\";");

        HSSFSheet sheet = createFirstSheet(hssfWorkbook);
        createColumnLabel(sheet);

        List<PageRank> pageRanks = (List<PageRank>) map.get("pageRankList");
        int rowNum = 1;
        for (PageRank rank : pageRanks) {
            createPageRankRow(sheet, rank, rowNum++);
        }
    }

    private HSSFSheet createFirstSheet(HSSFWorkbook workbook) {
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "페이지 순위");
        sheet.setColumnWidth(1, 256 * 20);
        return sheet;
    }

    private void createColumnLabel(HSSFSheet sheet) {
        HSSFRow firstRow = sheet.createRow(0);
        HSSFCell cell = firstRow.createCell(0);
        cell.setCellValue("순위");

        cell = firstRow.createCell(1);
        cell.setCellValue("페이지");
    }

    private void createPageRankRow(HSSFSheet sheet, PageRank rank, int rowNum) {
        HSSFRow row = sheet.createRow(rowNum);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(rank.getRank());

        cell = row.createCell(1);
        cell.setCellValue(rank.getPage());
    }
}
