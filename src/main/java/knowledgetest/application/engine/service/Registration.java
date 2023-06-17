package knowledgetest.application.engine.service;

import knowledgetest.application.engine.model.User;
import knowledgetest.application.engine.repository.UsersTable;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration {
    private final Set<String> loginList;
    private static final Pattern ONLY_LETTERS = Pattern.compile("[A-zА-яЁё]{2,}");
    private static final Pattern GROUP_PATTERN = Pattern.compile("[0-9]{2}+[А-Я]{2,4}+[0-9]{1}");
    private static final Pattern LOGIN_PATTERN = Pattern.compile("[A-z0-9_]{5,}");
    //пароль должен содержать строчные, заглавные буквы, цифры, символы и быть минимум 8 символов
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("(?:(?:[A-z0-9]{1,}+[@]{1}+[a-z]{1,})+(?:(?:[.]{1}+[a-z]{1,}){1,}))");

    public Registration() throws IOException {
        this.loginList = UsersTable.getLogins();
    }

    public boolean uniqueLogin(String enteredLogin) {
        return !(this.loginList.contains(enteredLogin));
    }

    //Метод выводит коды состояний
    public int logUpVerification( String login, String password, String name, String surname, String patronymic, String group, String email) {
        if(!fitThePattern(login, LOGIN_PATTERN)) { return 1;} //неверный логин
        if (!checkPassword(password)) {return 2;} //неверный пароль
        if (!fitThePattern(email, EMAIL_PATTERN)) { return 3;}
        if (!fitThePattern(group, GROUP_PATTERN)) { return 4;}
        if (!(fitThePattern(name, ONLY_LETTERS) && fitThePattern(surname, ONLY_LETTERS) && fitThePattern(patronymic, ONLY_LETTERS))) { return 5;}
        return 0;
    }

    public void createAcc( String login, String password, String name, String surname, String patronymic, String group, String email, String answer) throws IOException {
        User newUser = new User(login, password, name, surname, patronymic, group, email, answer);
        UsersTable.createNewUser(newUser);
    }

    private static boolean fitThePattern(String enteredString, Pattern selectPattern) {
        Matcher validString = selectPattern.matcher(enteredString);
        return validString.matches();
    }

    private static boolean checkPassword(String pas){
        Matcher validString = PASSWORD_PATTERN.matcher(pas);
        return validString.matches();
    }

    public static boolean editPassword(String userLogin, String userPassword) throws IOException {
        if (checkPassword(userPassword)) {
            UsersTable.changePassword(userLogin, userPassword);
            return true;
        } else {
            return false;
        }
    }
}
