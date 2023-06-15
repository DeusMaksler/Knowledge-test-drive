package knowledgetest.application.engine.repository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import knowledgetest.application.engine.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UsersTable extends BaseFunc{
    private static final String TABLE_NAME = "Persons";
    private final  String[] TABLE_HEADERS = {"login", "password", "role", "status", "name", "surname", "patronymic", "group", "email", "question", "answer"};
    public static final User TEST_USER = new User("Tester", "ABCdef12*", "Тест", "Тестеров", "Тестович", "22БИБ9", "test@yandex.ru", "first", "first");
    private final User DEFAULT_ADMIN = new User("admin", "441650", "admin", "", "", "", "", "exemple@yandex.ru", "first", "Дорога");

    public UsersTable() {
        if (!tableExist(TABLE_NAME)){ initialize();}
    }

    private void initialize() {
        try {
            Workbook currentTable = new XSSFWorkbook();
            Sheet sheet = currentTable.createSheet("UserList");
            //заполнение верхней строки названиями столбцов
            int columnCount = 0;
            Row row = sheet.createRow(0);
            for (String title : TABLE_HEADERS) {
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue(title);
            }//колличество пользователей
            Cell cell = row.createCell(columnCount);
            cell.setCellValue(1);

            row = sheet.createRow(1);
            addUser(row, DEFAULT_ADMIN);

            // сохранение в файл специализированным методом
            BaseFunc.tableWriteConnection(TABLE_NAME, currentTable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createNewUser(User appendingUser) throws IOException {
        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);
        //находим первую пустую строку
        int convenientPosition = getQuantityUsers(readableSheet) + 1;
        //создаёи/берём чистую строку
        Row convenientPlace = readableSheet.getRow(convenientPosition);
        if (convenientPlace == null){ convenientPlace = readableSheet.createRow(convenientPosition); }
        //добавляем изменения
        addUser(convenientPlace, appendingUser);
        readableSheet.getRow(0).getCell(User.NUMBER_OF_FIELDS).setCellValue(convenientPosition);
        //сохряняем
        BaseFunc.tableWriteConnection(TABLE_NAME, readableWorkbook);
        readableWorkbook.close();
    }

    public static HashMap<String, String[]> getAccList() throws IOException{
        HashMap<String, String[]> accounts = new HashMap<>();

        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);

        //получаем число пользователей в таблице
        int countUsers = getQuantityUsers(readableSheet);

        //получение информации из строки пользователя
        for (int i = 1; i < countUsers + 1; i++) {
            Row activeRow = readableSheet.getRow(i);
            Cell login = activeRow.getCell(0);
            Cell password = activeRow.getCell(1);
            Cell accessLevel = activeRow.getCell(2);
            Cell question = activeRow.getCell(9);
            Cell answer = activeRow.getCell(10);
            //упаковка в массив пароля и роли, названия вопроса и секретного ответа
            String[] accInfo = new String[]{password.getStringCellValue(), accessLevel.getStringCellValue(), question.getStringCellValue(), answer.getStringCellValue()};
            accounts.put(login.getStringCellValue(), accInfo);
        }
        readableWorkbook.close();
        return accounts;
    }

    public static Set<String> getLogins() throws IOException {
        Set<String> loginSet = new HashSet<>();

        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);

        //получаем число пользователей в таблице
        int countUsers = getQuantityUsers(readableSheet);

        for (int i = 1; i < countUsers + 1; i++) {
            Cell login = readableSheet.getRow(i).getCell(0);
            loginSet.add(login.getStringCellValue());
        }
        readableWorkbook.close();
        return loginSet;
    }

    private static void addUser(Row userPosition, User person) throws IOException {
        byte colIndex = 0;
        // entry login
        Cell activeCell = userPosition.createCell(colIndex++); //0 колонка
        activeCell.setCellValue(person.getLogin());
        // entry password
        activeCell = userPosition.createCell(colIndex++); //1 колонка
        activeCell.setCellValue(person.getPassword());
        // entry role
        activeCell = userPosition.createCell(colIndex++); //2 колонка
        activeCell.setCellValue(person.getRole());
        // entry status
        activeCell = userPosition.createCell(colIndex++);
        activeCell.setCellValue(person.getStatus());
        // entry name
        activeCell = userPosition.createCell(colIndex++);
        activeCell.setCellValue(person.getName());
        // entry surname
        activeCell = userPosition.createCell(colIndex++);
        activeCell.setCellValue(person.getSurname());
        // entry patronymic
        activeCell = userPosition.createCell(colIndex++);
        activeCell.setCellValue(person.getPatronymic());
        // entry group
        activeCell = userPosition.createCell(colIndex++);
        activeCell.setCellValue(person.getGroup());
        // entry email
        activeCell = userPosition.createCell(colIndex++);
        activeCell.setCellValue(person.getEmail());
        // entry question
        activeCell = userPosition.createCell(colIndex++);
        activeCell.setCellValue(person.getQuestion());
        // entry answer
        activeCell = userPosition.createCell(colIndex);
        activeCell.setCellValue(person.getAnswer());
    }

    private static int getQuantityUsers(Sheet sheet) {
        Cell quantityContain= sheet.getRow(0).getCell(User.NUMBER_OF_FIELDS);
        return  (int)quantityContain.getNumericCellValue();
    }
}

