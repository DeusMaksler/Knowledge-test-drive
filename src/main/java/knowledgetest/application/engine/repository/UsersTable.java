package knowledgetest.application.engine.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import knowledgetest.application.engine.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UsersTable extends BaseFunc{
    private static final String TABLE_NAME = "Persons";
    private static final  int LOGIN_CELL = 0;
    private static final  int PASSWORD_CELL = 1;
    private static final  int ROLE_CELL = 2;
    private static final  int STATUS_CELL = 3;
    private static final  int NAME_CELL = 4;
    private static final  int SURNAME_CELL = 5;
    private static final  int PATRONYMIC_CELL = 6;
    private static final  int GROUP_CELL = 7;
    private static final  int EMAIL_CELL = 8;
    private static final  int ANSWER_CELL = 9;

    private final  String[] TABLE_HEADERS = {"login", "password", "role", "status", "name", "surname", "patronymic", "group", "email", "answer"};
//    public static final User TEST_USER = new User("Tester", "ABCd9ef12*", "Тест", "Тестеров", "Тестович", "22БИБ9", "test@yandex.ru", "first");
    private final User DEFAULT_ADMIN = new User("admin", "441650", "admin", "", "", "", "", "exemple@yandex.ru", "Дорога");

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

    public static User showUser(String login) throws IOException {
        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);

        int userPos = searchUser(readableSheet, login);
        Row userPosition = readableSheet.getRow(userPos);
        User neededUser = getUser(userPosition);
        readableWorkbook.close();
        return  neededUser;
    }

    public static void  updateUser(User userData) throws IOException {
        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);

        int userPos = searchUser(readableSheet, userData.getLogin());
        Row oldUser = readableSheet.getRow(userPos);
        oldUser.getCell(NAME_CELL).setCellValue(userData.getName());
        oldUser.getCell(SURNAME_CELL).setCellValue(userData.getSurname());
        oldUser.getCell(PATRONYMIC_CELL).setCellValue(userData.getPatronymic());
        oldUser.getCell(GROUP_CELL).setCellValue(userData.getGroup());
        oldUser.getCell(EMAIL_CELL).setCellValue(userData.getEmail());
        oldUser.getCell(PASSWORD_CELL).setCellValue(userData.getPassword());

        tableWriteConnection(TABLE_NAME, readableWorkbook);
        readableWorkbook.close();
    }

    public static void remoteUser(String login) throws IOException {
        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);

        int userPos = searchUser(readableSheet, login);
        int quantity = getQuantityUsers(readableSheet);

        if (userPos != quantity) {
            User lastUser = getUser(readableSheet.getRow(quantity));
            addUser(readableSheet.getRow(userPos), lastUser);
        }
        readableSheet.removeRow(readableSheet.getRow(quantity));
        readableSheet.getRow(0).getCell(User.NUMBER_OF_FIELDS).setCellValue(quantity - 1);

        tableWriteConnection(TABLE_NAME, readableWorkbook);
        readableWorkbook.close();
    }

    public static void changeStatus(String login, boolean status) throws IOException {
        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);

        int userPos = searchUser(readableSheet, login);
        readableSheet.getRow(userPos).getCell(STATUS_CELL).setCellValue(status);
        tableWriteConnection(TABLE_NAME, readableWorkbook);
        readableWorkbook.close();
    }

    public static void changeRole(String login, String status) throws IOException {
        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);

        int userPos = searchUser(readableSheet, login);
        readableSheet.getRow(userPos).getCell(ROLE_CELL).setCellValue(status);
        tableWriteConnection(TABLE_NAME, readableWorkbook);
        readableWorkbook.close();
    }

    public static void changePassword(String login, String newPass) throws IOException {
        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);

        int userPos = searchUser(readableSheet, login);
        readableSheet.getRow(userPos).getCell(PASSWORD_CELL).setCellValue(newPass);
        tableWriteConnection(TABLE_NAME, readableWorkbook);
        readableWorkbook.close();
    }

    private static void addUser(Row userPosition, User person) throws IOException {
        // entry login
        Cell activeCell = userPosition.createCell(LOGIN_CELL);
        activeCell.setCellValue(person.getLogin());
        // entry password
        activeCell = userPosition.createCell(PASSWORD_CELL); //1 колонка
        activeCell.setCellValue(person.getPassword());
        // entry role
        activeCell = userPosition.createCell(ROLE_CELL); //2 колонка
        activeCell.setCellValue(person.getRole());
        // entry status
        activeCell = userPosition.createCell(STATUS_CELL);
        activeCell.setCellValue(person.getStatus());
        // entry name
        activeCell = userPosition.createCell(NAME_CELL);
        activeCell.setCellValue(person.getName());
        // entry surname
        activeCell = userPosition.createCell(SURNAME_CELL);
        activeCell.setCellValue(person.getSurname());
        // entry patronymic
        activeCell = userPosition.createCell(PATRONYMIC_CELL);
        activeCell.setCellValue(person.getPatronymic());
        // entry group
        activeCell = userPosition.createCell(GROUP_CELL);
        activeCell.setCellValue(person.getGroup());
        // entry email
        activeCell = userPosition.createCell(EMAIL_CELL);
        activeCell.setCellValue(person.getEmail());
        // entry answer
        activeCell = userPosition.createCell(ANSWER_CELL);
        activeCell.setCellValue(person.getAnswer());
    }

    private static User getUser(Row userRow) {
        User person = new User();
        person.setLogin(userRow.getCell(LOGIN_CELL).getStringCellValue());
        person.setPassword(userRow.getCell(PASSWORD_CELL).getStringCellValue());
        person.setRole(userRow.getCell(ROLE_CELL).getStringCellValue());
        person.setStatus(userRow.getCell(STATUS_CELL).getBooleanCellValue());
        person.setName(userRow.getCell(NAME_CELL).getStringCellValue());
        person.setSurname(userRow.getCell(SURNAME_CELL).getStringCellValue());
        person.setPatronymic(userRow.getCell(PATRONYMIC_CELL).getStringCellValue());
        person.setGroup(userRow.getCell(GROUP_CELL).getStringCellValue());
        person.setEmail(userRow.getCell(EMAIL_CELL).getStringCellValue());
        person.setAnswer(userRow.getCell(ANSWER_CELL).getStringCellValue());

        return person;
    }

    private static  int searchUser(Sheet sheet, String userLogin) {
        for (int rowNumber = 1; rowNumber < getQuantityUsers(sheet)+1; rowNumber++) {
            Cell loginCell = sheet.getRow(rowNumber).getCell(LOGIN_CELL);
            if (loginCell.getStringCellValue().equals(userLogin)){ return rowNumber; }
        }
        return 0; //ошибка поиска пользователя, исключаемая в силу контекста
    }

    private static int getQuantityUsers(Sheet sheet) {
        Cell quantityContain= sheet.getRow(0).getCell(User.NUMBER_OF_FIELDS);
        return  (int)quantityContain.getNumericCellValue();
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
            if (activeRow.getCell(STATUS_CELL).getBooleanCellValue()){ //проверка аккаунта на блокировку
                Cell login = activeRow.getCell(LOGIN_CELL);
                Cell password = activeRow.getCell(PASSWORD_CELL);
                Cell role = activeRow.getCell(ROLE_CELL);
                Cell answer = activeRow.getCell(ANSWER_CELL);
                //упаковка в массив пароля и роли, названия вопроса и секретного ответа
                String[] accInfo = new String[]{password.getStringCellValue(), role.getStringCellValue(), answer.getStringCellValue()};
                accounts.put(login.getStringCellValue(), accInfo);
            }
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
            Cell login = readableSheet.getRow(i).getCell(LOGIN_CELL);
            loginSet.add(login.getStringCellValue());
        }
        readableWorkbook.close();
        return loginSet;
    }

    public static ArrayList<User> getUserList() throws IOException {
        Workbook readableWorkbook = tableReadConnection(TABLE_NAME);
        Sheet readableSheet = readableWorkbook.getSheetAt(0);

        int quantity = getQuantityUsers(readableSheet);
        ArrayList<User> userList = new ArrayList<>();

        for (int i = 2; i < quantity +1; i++) {
            userList.add(getUser(readableSheet.getRow(i)));
        }

        readableWorkbook.close();
        return userList;
    }
}

