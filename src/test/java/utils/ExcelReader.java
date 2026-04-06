package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public final class ExcelReader {
    private ExcelReader() {
    }

    public static Map<String, String> getRowData(String resourceFileName, String sheetName, int dataRowNumber) {
        try (InputStream inputStream = ExcelReader.class.getClassLoader().getResourceAsStream(resourceFileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Excel file not found in test resources: " + resourceFileName);
            }

            try (Workbook workbook = WorkbookFactory.create(inputStream)) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new IllegalArgumentException("Sheet not found: " + sheetName);
                }

                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    throw new IllegalArgumentException("Sheet does not contain a header row: " + sheetName);
                }

                if (dataRowNumber < 1) {
                    throw new IllegalArgumentException("Data row number must be 1 or greater.");
                }

                DataFormatter formatter = new DataFormatter();
                Row row = sheet.getRow(dataRowNumber);
                if (row == null) {
                    throw new IllegalArgumentException("Data row not found: " + dataRowNumber);
                }

                Map<String, String> rowData = new HashMap<>();
                for (int cellIndex = 0; cellIndex < headerRow.getLastCellNum(); cellIndex++) {
                    String header = formatter.formatCellValue(headerRow.getCell(cellIndex)).trim();
                    Cell cell = row.getCell(cellIndex);
                    rowData.put(header, formatter.formatCellValue(cell).trim());
                }
                return rowData;
            }
        } catch (IOException exception) {
            throw new RuntimeException("Unable to read test data from Excel.", exception);
        }

    }
}
