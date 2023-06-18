package knowledgetest.application.engine.repository;

import knowledgetest.application.engine.model.Question;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class QuestionsTable extends QuestionsSheet{
    private static final String TABLE_NAME = "Questions";
    private static final  String[] TABLE_HEADERS = {"Name", "count", "status"};

    private static final int INFO_SHEET = 0;
    private static final int QUANTITY_SECTIONS = 3;
    private static final int SECTION_NAME = 0;
    private static final int SECTION_SIZE = 1;
    private static final int SECTION_STATUS = 2;

    public static void initialize() {
        if (!tableExist(TABLE_NAME)){
            try {
                Workbook currentTable = new XSSFWorkbook();
                Sheet sheet = currentTable.createSheet("TestInfo");

                //указываем количество разделов и оглавляем
                Row activeRow = sheet.createRow(0);
                activeRow.createCell(SECTION_NAME).setCellValue(TABLE_HEADERS[SECTION_NAME]);
                activeRow.createCell(SECTION_SIZE).setCellValue(TABLE_HEADERS[SECTION_SIZE]);
                activeRow.createCell(SECTION_STATUS).setCellValue(TABLE_HEADERS[SECTION_STATUS]);
                activeRow.createCell(QUANTITY_SECTIONS).setCellValue(0);

                // сохранение в файл специализированным методом
                BaseFunc.tableWriteConnection(TABLE_NAME, currentTable);
                currentTable.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //работа с разделами
    public static void createNewSection(String sectionName) throws IOException {
        Workbook currentTable = tableReadConnection(TABLE_NAME);
        Sheet sheet = currentTable.createSheet(sectionName);
        //создание оглавления
        decorateSection(sheet);
        //добавление информации на TestInfo
        changeSectionInfo(currentTable.getSheetAt(INFO_SHEET), sectionName, 0, true);
        tableWriteConnection(TABLE_NAME, currentTable);
        currentTable.close();
    }

    public static void deleteSection(String sectionName) throws IOException {
        Workbook currentTable = tableReadConnection(TABLE_NAME);
        //находим индекс листа для удаления
        int sheetNumber =  currentTable.getSheetIndex(sectionName);
        currentTable.removeSheetAt(sheetNumber);
        //добавление информации на TestInfo
        removeSection(currentTable.getSheetAt(INFO_SHEET), sectionName);
        tableWriteConnection(TABLE_NAME, currentTable);
        currentTable.close();
    }

    public static void inverseStatusSection(String sectionName) throws IOException {
        Workbook currentTable = tableReadConnection(TABLE_NAME);
        Sheet sheet= currentTable.getSheetAt(INFO_SHEET);
        //заменяем статус на противоположныйЫ
        int sectionPos =searchSection(sheet, sectionName);
        Row section = sheet.getRow(sectionPos);
        boolean status = section.getCell(SECTION_STATUS).getBooleanCellValue();
        section.getCell(SECTION_STATUS).setCellValue(!status);
        //сохраняем изменения
        tableWriteConnection(TABLE_NAME, currentTable);
        currentTable.close();
    }

    //работа с вопросами
    public static void createQuestion(String sectionName, Question enteredQuestion) throws IOException {
        Workbook currentTable = tableReadConnection(TABLE_NAME);
        //добавили вопрос
        Sheet sectionSheet = currentTable.getSheet(sectionName);
        addQuestion(sectionSheet, enteredQuestion);
        //обновляем информацию о количестве вопросов
        Sheet sheet = currentTable.getSheetAt(INFO_SHEET);
        int sectionPos = searchSection(sheet, sectionName);
        changeSectionSize(sheet.getRow(sectionPos), 1);

        tableWriteConnection(TABLE_NAME, currentTable);
        currentTable.close();
    }

    public static void changeQuestion(String sectionName,int questionId, Question enteredQuestion) throws IOException {
        Workbook currentTable = tableReadConnection(TABLE_NAME);
        // изменили вопрос
        Sheet sectionSheet = currentTable.getSheet(sectionName);
        insertQuestion(sectionSheet, questionId, enteredQuestion);
        //сохранили изменения
        tableWriteConnection(TABLE_NAME, currentTable);
        currentTable.close();
    }

    public static void deleteQuestion(String sectionName, int questionId) throws IOException {
        Workbook currentTable = tableReadConnection(TABLE_NAME);
        //удалили вопрос
        Sheet sectionSheet = currentTable.getSheet(sectionName);
        removeQuestion(sectionSheet, questionId);
        //обновляем информацию о количестве вопросов
        Sheet sheet = currentTable.getSheetAt(INFO_SHEET);
        int sectionPos = searchSection(sheet, sectionName);
        changeSectionSize(sheet.getRow(sectionPos), -1);

        tableWriteConnection(TABLE_NAME, currentTable);
        currentTable.close();
    }

    //изменяет информацию о разделе по имени
    private static void changeSectionInfo(Sheet sheet, String sectionName, int sectionSize, boolean sectionStatus) {
        int sectionPosition = searchSection(sheet, sectionName);
        Row sectionRow;
        switch (sectionPosition) {
            case -1: { //создание первой секции
                sectionRow = sheet.createRow(1);
                changeQuantitySections(sheet, 1);
                break;
            }
            case -2: { // добавление новой секции
                int convenientPosition = getQuantitySections(sheet) + 1;
                sectionRow = sheet.createRow(convenientPosition);
                changeQuantitySections(sheet, 1);
                break;
            }
            default: { // изменение данных существующей
                sectionRow = sheet.getRow(sectionPosition);
                break;
            }
        }

        //Внесение данных о разделе
        addSectionInfo(sectionRow, sectionName, sectionSize, sectionStatus);
    }

    private static void removeSection(Sheet sheet, String sectionName) {
        int position = searchSection(sheet, sectionName);
        int quantity = getQuantitySections(sheet);
        Row row = sheet.getRow(quantity);
        if (position != quantity) { //сбор данных последнего
            String name = row.createCell(SECTION_NAME).getStringCellValue();
            int size = (int) row.createCell(SECTION_SIZE).getNumericCellValue();
            boolean status = row.createCell(SECTION_STATUS).getBooleanCellValue();
            addSectionInfo(sheet.getRow(position), name, size, status);
        }
        sheet.removeRow(sheet.getRow(quantity));
        changeQuantitySections(sheet, -1);
    }

    private static void changeQuantitySections(Sheet sheet, int delta) {
        Cell quantityCell = sheet.getRow(0).getCell(QUANTITY_SECTIONS);
        int value = (int) quantityCell.getNumericCellValue() + delta;
        quantityCell.setCellValue(value);
    }

    private static int getQuantitySections(Sheet sheet) {
        return (int) sheet.getRow(0).getCell(QUANTITY_SECTIONS).getNumericCellValue();
    }

    private static int searchSection(Sheet sheet, String sectionName) {
        int quantity = getQuantitySections(sheet);
        if (quantity == 0) {
            return -1; //список секций пуст
        } else {
            for (int i = 1; i < quantity + 1; i++) {
                Cell nameCell = sheet.getRow(i).getCell(SECTION_NAME);
                if (nameCell.getStringCellValue().equals(sectionName)) {
                    return i; //секция найдена
                }
            }
            return -2; //новая секция
        }
    }

    //находит индекс строки рездела по имени(и другие исходы)
    private static void addSectionInfo(Row row, String sectionName, int sectionSize, boolean sectionStatus){
        row.createCell(SECTION_NAME).setCellValue(sectionName);
        row.createCell(SECTION_SIZE).setCellValue(sectionSize);
        row.createCell(SECTION_STATUS).setCellValue(sectionStatus);
    }

    private static void changeSectionSize(Row row, int delta) {
        Cell sizeCell = row.getCell(SECTION_SIZE);
        int oldValue = (int) sizeCell.getNumericCellValue();
        sizeCell.setCellValue(oldValue + delta);
    }
}

