package knowledgetest.application.engine.service;

import knowledgetest.application.engine.repository.UsersTable;

import java.io.IOException;
import java.util.HashMap;

public class Authorization {
    private final HashMap<String, String[]> accountList;
    private HashMap<String, Integer> attemptsList;
    private final int QUANTITY_ATTEMPTS = 4;

    public Authorization() throws IOException{
        this.accountList = UsersTable.getAccList();
    }

    //этот метод возвращает коды соответствующих состояний
    public int logInVerification(String log, String pass){
        if (!this.accountList.containsKey(log)) { return 1;} //Пользователь отсутствует
        if (checkPassword(log,pass)){ return 0;} //Пароль верный
        //проверяет и сохраняет кол-во неверных вводов для логинов
        int wrongAttempts = countAttempts(log);
        if (wrongAttempts == QUANTITY_ATTEMPTS){ return 3; } //Достигнут лимит неверных вводов
        else {
            this.attemptsList.put(log, countAttempts(log));
            return 2; //Пароль неверен
        }
    }

    public boolean checkPassword(String log, String pass){
       return this.accountList.get(log)[0].equals(pass);
    }

    public boolean checkSecretAnswer(String login, String answer) {
        return this.accountList.get(login)[2].equals(answer);
    }

    public String getAccRole(String login) {
        return this.accountList.get(login)[1];
    }

    private int countAttempts(String login) {
        if (this.attemptsList.containsKey(login)) {
            return attemptsList.get(login) + 1;
        } else {
            return 1;
        }
    }


}
