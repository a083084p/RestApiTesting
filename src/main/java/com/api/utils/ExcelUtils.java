package com.api.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtils {

    private static XSSFSheet excelSheet;
    private static XSSFWorkbook excelWorkbook;
    private static XSSFCell cell;
    private static XSSFRow row;
    private static String sheetPath = PropertiesFile.getProperty("test.data.path") + PropertiesFile.getProperty("excel.name");
    private static String sheetName = PropertiesFile.getProperty("sheet.name");
    private static final Logger LOGGER = LogManager.getLogger(ExcelUtils.class);

    private static void setExcelFile() throws IOException {
        LOGGER.info("Getting sheets from the workbook: ");
        FileInputStream fileInputStream = new FileInputStream(new File(sheetPath).getAbsolutePath());
        excelWorkbook = new XSSFWorkbook(fileInputStream);
        excelSheet = excelWorkbook.getSheet(sheetName);
    }

    private static int getDataRow (String dataKey, int dataColumn) {
        int rowCount = excelSheet.getLastRowNum();
        for (int row=0; row <= rowCount; row++) {
            if (ExcelUtils.getCellData(row, dataColumn).equalsIgnoreCase(dataKey)) {
                return row;
            }
        }
        return 0;
    }

    private static String getCellData (int rowNumb, int colNumb) {
        cell = excelSheet.getRow(rowNumb).getCell(colNumb);
        if(cell.getCellType() == CellType.NUMERIC) {
            cell.setCellType(CellType.STRING);
        }
        return cell.getStringCellValue();
    }

    public static void setCellData (String result, int rowNumb, int colNumb, String sheetPath, String sheetName) throws Exception {
        try {
            row = excelSheet.getRow(rowNumb);
            cell = row.getCell(colNumb);
            LOGGER.info("Setting results into the excel sheet.");
            if (cell==null) {
                cell = row.createCell(colNumb);
                cell.setCellValue(result);
            }
            else {
                cell.setCellValue(result);
            }

            LOGGER.info("Creating file output stream.");
            FileOutputStream fileOutputStream = new FileOutputStream(sheetPath + sheetName);
            excelWorkbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception exception) {
            LOGGER.info("Exception occurred in setCellData: " + exception);
        }
    }


    public static Map getData (String dataKey) throws Exception {
        Map<String, String> dataMap = new HashMap<>();
        setExcelFile();
        int dataRow = getDataRow(dataKey.trim(), 0);
        LOGGER.info("Test Data found in Row: " + dataRow);
        if (dataRow == 0) {
            throw new Exception("No data found for data key: " + dataKey);
        }
        int columnCount = excelSheet.getRow(dataRow).getLastCellNum();
        for (int i=0; i<columnCount; i++) {
            cell = excelSheet.getRow(dataRow).getCell(i);
            String cellData = null;
            if (cell != null) {
                if (cell.getCellType() == CellType.NUMERIC) {
                    cell.setCellType(CellType.STRING);
                }
                cellData = cell.getStringCellValue();
            }
            dataMap.put(excelSheet.getRow(0).getCell(i).getStringCellValue(), cellData);
        }
        return dataMap;
    }


    public static void main(String[] args) throws Exception {
        Map<String, String> dataMap = new HashMap<>();
        dataMap = getData("updateBooking30");
        for(Map.Entry<String, String> entry : dataMap.entrySet()) {
            LOGGER.info(entry.getKey() + "=>" + entry.getValue());
        }
    }
}
