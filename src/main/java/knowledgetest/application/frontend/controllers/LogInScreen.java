package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import knowledgetest.application.Main;
import knowledgetest.application.engine.service.Authorization;
import knowledgetest.application.engine.service.Registration;
import knowledgetest.application.frontend.common.DialogWindow;

import java.io.IOException;

public class LogInScreen {
    private Authorization logChecker = new Authorization();
    @FXML
    private Label logInLabel;
    @FXML
    private TextField login;
    @FXML
    private TextField password;
    @FXML
    private Button logInButton;
    @FXML
    private Button toLogUpButton;
    @FXML
    private Button changePasswordButton;


    public LogInScreen() throws IOException {
    }

    public void checkEnteredData() throws IOException {
        int logInStratus = logChecker.logInVerification(login.getText(), password.getText());
        final String ERROR_NAME = "Ошибка авторизации";
        switch (logInStratus) {
            case  (0):
                //успешный вход //fix me
                break;
            case (1):
                DialogWindow.createInfoDialog(ERROR_NAME, "Пользователя с таким логином не существует");
                break;
            case (2):
                DialogWindow.createInfoDialog(ERROR_NAME, "Пароль неверный");
                break;
            default:
                String enterAnswer = DialogWindow.createFormDialog(ERROR_NAME, "Вы ввели слишком много раз неверный пароль, аккаунт заморожен, введите секретное слово, выбранное при регистрации", "Ваше секретное слово");
                if (logChecker.checkSecretAnswer(login.getText(), enterAnswer)) {
                    changePasswordProcedure(login.getText());
                } else {
                    //блокировка пользователя и обращение к админу //fix me
                }
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

    private void changePasswordProcedure(String log) throws IOException {
        //изменение функционала страницы
        logInLabel.setText("Измените пароль");
        login.setEditable(false);
        password.clear();
        logInButton.setDisable(true);
        logInButton.setOpacity(0.0);
        toLogUpButton.setDisable(true);
        toLogUpButton.setOpacity(0.0);
        changePasswordButton.setOpacity(1);
        changePasswordButton.setDisable(false);

        if (logChecker.checkPassword(log, password.getText())){ //пароль совпадает со старым
            DialogWindow.createInfoDialog("Ошибка смены пароля", "Необходимо ввести пароль, отличающийся от старого");
        } else {
            if (Registration.editPassword(log, password.getText())) { //успешная смена проля
                LogUpScreen.goToAuthorization(); //перезагрузка страницы
            } else { //неверный формат проля
                DialogWindow.createInfoDialog("Ошибка смены пароля", "Пароль должен содержать строчную и заглавную латинские буквы, цифру, спец символ, минммум 8 знаков");
            }
        }
    }
}
