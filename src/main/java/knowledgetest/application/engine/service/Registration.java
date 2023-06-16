package knowledgetest.application.engine.service;

import knowledgetest.application.engine.repository.UsersTable;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration {
    private final Set<String> loginList;
    private static final Pattern ONLY_LETTERS = Pattern.compile("[A-zА-я]{2,}");
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

    public boolean logUpVerification( String login, String password, String name, String surname, String patronymic, String group, String email) {
        if(!fitThePattern(login, LOGIN_PATTERN)) { return false;}
        if (!fitThePattern(password, PASSWORD_PATTERN)) {return false;}
        if (!fitThePattern(email, EMAIL_PATTERN)) { return false;}
        if (!fitThePattern(group, GROUP_PATTERN)) { return false;}
        if (!(fitThePattern(name, ONLY_LETTERS) && fitThePattern(surname, ONLY_LETTERS) && fitThePattern(patronymic, ONLY_LETTERS))) { return false;}
        return true;
    }

    private static boolean fitThePattern(String enteredString, Pattern selectPattern) {
        Matcher validString = selectPattern.matcher(enteredString);
        return validString.matches();
    }
}
