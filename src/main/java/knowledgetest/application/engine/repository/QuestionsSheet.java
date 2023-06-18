package knowledgetest.application.engine.repository;

import knowledgetest.application.engine.model.Question;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class QuestionsSheet extends BaseFunc{
    private static final  String[] SHEET_HEADERS = {"ID", "yesNoType", "title", "rightVariant", "checkDigit"};
    private static final int ORDER = 9;
    private static final int ID = 0;
    private static final int YN_TYPE = 1;
    private static final int TITLE = 2;
    private static final int RIGHT_CHOICE = 3;
    private static final int CHECK_DIGIT = 4;

    protected static void decorateSection(Sheet sheet) {
        //заполнение верхней строки названиями столбцов
        int columnCount = 0;
        Row row = sheet.createRow(0);
        for (String title : SHEET_HEADERS) {
            Cell cell = row.createCell(columnCount++);
            cell.setCellValue(title);
        }
        for (int j = 0; j < 4; j++) {
            Cell cell = row.createCell(columnCount + j);
            cell.setCellValue("variant " + (j + 1));
        }
        row.createCell(ORDER).setCellValue(0);
    }

    //взаимодействие с пользователями
    protected static void addQuestion(Sheet sheet, Question question) {
        int id = updateOrder(sheet, 1);
        Row place = sheet.createRow(id);
        place.createCell(ID).setCellValue(id);
        place.createCell(YN_TYPE).setCellValue(question.isYnType());
        place.createCell(TITLE).setCellValue(question.getPhrasing());
        place.createCell(RIGHT_CHOICE).setCellValue(question.getRightChoice());
        place.createCell(CHECK_DIGIT).setCellValue(question.getCheckDigit());

        int count;
        if (question.isYnType()) { count = 2;}
        else { count = 4;}

        for (int i = 0; i < count; i++) {
            place.createCell(5 + i).setCellValue(question.getVariants()[i]);
        }
    }

    protected static void insertQuestion(Sheet sheet, int id, Question question) {
        Row place = sheet.getRow(id);
        place.getCell(YN_TYPE).setCellValue(question.isYnType());
        place.getCell(TITLE).setCellValue(question.getPhrasing());
        place.getCell(RIGHT_CHOICE).setCellValue(question.getRightChoice());
        place.getCell(CHECK_DIGIT).setCellValue(question.getCheckDigit());

        int count;
        if (question.isYnType()) { count = 2;}
        else { count = 4;}

        for (int i = 0; i < count; i++) {
            place.createCell(5 + i).setCellValue(question.getVariants()[i]);
        }
    }

    protected static void removeQuestion(Sheet sheet, int id) {
        int capacity = getOrder(sheet);
        if (capacity != id) {
            Question savedQuestion = getQuestion(sheet.getRow(capacity));
            insertQuestion(sheet, id, savedQuestion);
        }
        sheet.removeRow(sheet.getRow(id));
        updateOrder(sheet, -1);
    }

    //изменяет order и возвращает id
    private static int updateOrder(Sheet sheet, int delta) {
        Cell orderCell = sheet.getRow(0).getCell(ORDER);
        int id = (int) orderCell.getNumericCellValue() + delta;
        orderCell.setCellValue(id);
        return id;
    }

    private static Question getQuestion(Row row){
        Question question = new Question();

        question.setYnType(row.getCell(YN_TYPE).getBooleanCellValue());
        question.setPhrasing(row.getCell(TITLE).getStringCellValue());
        question.setCheckDigit( (int) row.getCell(CHECK_DIGIT).getNumericCellValue());
        question.setRightChoice( (int) row.getCell(RIGHT_CHOICE).getNumericCellValue());

        int count;
        if (question.isYnType()) { count = 2;}
        else { count = 4;}

        String[] variants = new String[count];
        for (int i = 0; i < count; i++) {
            variants[i] = row.getCell(5+i).getStringCellValue();
        }
        question.setVariants(variants);

        return question;
    }

    private static int getOrder(Sheet sheet) {
        return (int) sheet.getRow(0).getCell(ORDER).getNumericCellValue();
    }
}
