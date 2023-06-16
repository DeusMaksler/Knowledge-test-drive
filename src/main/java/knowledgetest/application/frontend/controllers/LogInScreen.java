package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import knowledgetest.application.Main;
import knowledgetest.application.engine.service.Authorization;
import knowledgetest.application.frontend.common.DialogWindow;

import java.io.IOException;

public class LogInScreen {
    private Authorization logChecker = new Authorization();
    @FXML
    private TextField login;
    @FXML
    private TextField password;

    public LogInScreen() throws IOException {
    }

    public void checkEnteredData(){
        int logInStratus = logChecker.logInVerification(login.getText(), password.getText());
        switch (logInStratus) {
            case  (0):
                //успешный вход
                break;
            case (1):
                // вывод перевведите пользователя
                break;
            case (2):
                // вывод перевведите пароль
                break;
            default:
                // запрос секретного вопроса
                break;
        }

    }

    public void goToRegistration() throws IOException {
        //сменить сцену в этом окне
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("log-up-screen.fxml"));
        Stage stage = (Stage) login.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 668, 490);
        stage.setScene(scene);
        stage.setTitle("Регистрация аккаунта");
    }
}
