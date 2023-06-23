package knowledgetest.application.engine.repository;

import knowledgetest.application.engine.model.QSection;
import knowledgetest.application.engine.model.Record;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class RecordsTable extends BaseFunc{
    private static final String TABLE_NAME = "Records";
    private static final  String[] TABLE_HEADERS = {"Login", "Name", "Group", "Test", "Result"};

    private static final int USER_LOGIN = 0;
    private static final int USER_NAME = 1;
    private static final int USER_GROUP = 2;
    private static final int TEST_NAME = 3;
    private static final int USER_RESULT = 4;


    public static void initialize() {
        if (!tableExist(TABLE_NAME)){
            try {
                Workbook currentTable = new XSSFWorkbook();
                Sheet sheet = currentTable.createSheet("Data");

                //указываем количество разделов и оглавляем
                Row activeRow = sheet.createRow(0);
                activeRow.createCell(USER_LOGIN).setCellValue(TABLE_HEADERS[USER_LOGIN]);
                activeRow.createCell(USER_NAME).setCellValue(TABLE_HEADERS[USER_NAME]);
                activeRow.createCell(USER_GROUP).setCellValue(TABLE_HEADERS[USER_GROUP]);
                activeRow.createCell(TEST_NAME).setCellValue(TABLE_HEADERS[TEST_NAME]);
                activeRow.createCell(USER_RESULT).setCellValue(TABLE_HEADERS[USER_RESULT]);

                // сохранение в файл специализированным методом
                BaseFunc.tableWriteConnection(TABLE_NAME, currentTable);
                currentTable.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void createRecord(Record rec) throws IOException {
        Workbook currentTable = tableReadConnection(TABLE_NAME);
        Sheet sheet = currentTable.getSheetAt(0);
        Row lastRow = sheet.createRow(sheet.getLastRowNum() + 1);
        addRecord(lastRow, rec);

        tableWriteConnection(TABLE_NAME, currentTable);
        currentTable.close();
    }

    private static void addRecord(Row activeRow, Record record) {
        activeRow.createCell(USER_LOGIN).setCellValue(record.getUserLogin());
        activeRow.createCell(USER_NAME).setCellValue(record.getUserName());
        activeRow.createCell(USER_GROUP).setCellValue(record.getUserGroup());
        activeRow.createCell(TEST_NAME).setCellValue(record.getTestName());
        activeRow.createCell(USER_RESULT).setCellValue(record.getUserResult());
    }
}
