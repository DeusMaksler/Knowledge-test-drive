package knowledgetest.application.engine.service;

import knowledgetest.application.engine.repository.UsersTable;

import java.io.IOException;
import java.util.HashMap;

public class Authorization {
    private final HashMap<String, String[]> accountList;

    public Authorization() throws IOException{
        this.accountList = UsersTable.getAccList();
    }

    public boolean logInVerification(String log, String pass){
        if (!this.accountList.containsKey(log)) { return false;} //Пользователь отсутствует
        else {
            return this.accountList.get(log)[0].equals(pass); //Проверка пароля
        }
    }

    public String getSecretQuestion(String login) {
        return this.accountList.get(login)[2];
    }

    public boolean checkSecretAnswer(String login, String answer) {
        return this.accountList.get(login)[3].equals(answer);
    }
}
