package knowledgetest.application.engine.repository;

import knowledgetest.application.engine.model.Record;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

    public static ArrayList<Record> getSelfRecord(String userLogin) throws IOException {
        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);

        ArrayList<Record> neededRecords = new ArrayList<>();
        int count = readableSheet.getLastRowNum();
        if (count != 0) {
            for (int i = 1; i < count + 1; i++) {
                Row row = readableSheet.getRow(i);
                if (row.getCell(USER_LOGIN).getStringCellValue().equals(userLogin)) {
                    neededRecords.add(getRecord(row));
                }
            }
        }
        readableWorkbook.close();
        return neededRecords;
    }

    public static ArrayList<Record> getLastRecords() throws IOException {
        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);

        ArrayList<Record> neededRecords = new ArrayList<>();
        int count = readableSheet.getLastRowNum();
        if (count != 0 && count < 20) {
            for (int i = 1; i < count + 1; i++) {
                Row row = readableSheet.getRow(i);
                neededRecords.add(getRecord(row));
            }
        } else if (count > 20){
            for (int i = count - 20; i < count + 1; i++) {
                Row row = readableSheet.getRow(i);
                neededRecords.add(getRecord(row));
            }
        }
        readableWorkbook.close();
        return neededRecords;
    }

    public static HashMap<String, int[]> medianGroupStatistics() throws IOException {
        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);

        HashMap<String, int[]> groupStatistics = selectCriterion(readableSheet, USER_GROUP);
        readableWorkbook.close();
        return groupStatistics;
    }

    public static HashMap<String, int[]> medianTestStatistics() throws IOException {
        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);

        HashMap<String, int[]> testStatistics = selectCriterion(readableSheet, TEST_NAME);
        readableWorkbook.close();
        return testStatistics;
    }

    public static void createRecord(Record rec) throws IOException {
        Workbook currentTable = tableReadConnection(TABLE_NAME);
        Sheet sheet = currentTable.getSheetAt(0);
        Row lastRow = sheet.createRow(sheet.getLastRowNum() + 1);
        addRecord(lastRow, rec);

        tableWriteConnection(TABLE_NAME, currentTable);
        currentTable.close();
    }

    private static HashMap<String, int[]> selectCriterion(Sheet sheet, int columnNum) {
        HashMap<String, Integer> countAttempts = new HashMap<>();
        HashMap<String, Integer> countBalls = new HashMap<>();
        HashMap<String, int[]> statistics = new HashMap<>();
        ArrayList<String> groups = new ArrayList<>();
        int count = sheet.getLastRowNum();

        if (count != 0) {
            for (int i = 1; i < count + 1; i++) {
                Row row = sheet.getRow(i);
                String criterion = row.getCell(columnNum).getStringCellValue();
                int ball = (int) row.getCell(USER_RESULT).getNumericCellValue();
                if (countAttempts.containsKey(criterion)){
                    countAttempts.put(criterion, countAttempts.get(criterion) + 1);
                    countBalls.put(criterion, countBalls.get(criterion) + ball);
                } else {
                    groups.add(criterion);
                    countAttempts.put(criterion, 1);
                    countBalls.put(criterion, ball);
                }
            }
            for (String groupName : groups) {
                statistics.put(groupName, new int[]{countAttempts.get(groupName), countBalls.get(groupName)});
            }
        }
        return statistics;
    }

    private static void addRecord(Row activeRow, Record record) {
        activeRow.createCell(USER_LOGIN).setCellValue(record.getUserLogin());
        activeRow.createCell(USER_NAME).setCellValue(record.getUserName());
        activeRow.createCell(USER_GROUP).setCellValue(record.getUserGroup());
        activeRow.createCell(TEST_NAME).setCellValue(record.getTestName());
        activeRow.createCell(USER_RESULT).setCellValue(record.getUserResult());
    }

    private static Record getRecord(Row activeRow) {
        Record record = new Record();
        record.setUserLogin(activeRow.getCell(USER_LOGIN).getStringCellValue());
        record.setUserName(activeRow.getCell(USER_NAME).getStringCellValue());
        record.setUserGroup(activeRow.getCell(USER_GROUP).getStringCellValue());
        record.setTestName(activeRow.getCell(TEST_NAME).getStringCellValue());
        record.setUserResult((int) activeRow.getCell(USER_RESULT).getNumericCellValue());
        return record;
    }
}
