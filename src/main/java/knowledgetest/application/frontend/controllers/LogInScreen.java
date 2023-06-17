package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import knowledgetest.application.Main;
import knowledgetest.application.engine.service.Authorization;
import knowledgetest.application.engine.service.Registration;
import knowledgetest.application.frontend.common.DialogWindow;

import java.io.IOException;

public class LogInScreen {
    private Authorization logChecker = new Authorization();
    private  final String ERROR_NAME = "Ошибка авторизации";
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


    public LogInScreen() throws IOException {}

    public void checkEnteredData() throws IOException {
        if (signIpFieldsNotEmpty()){ //валидация полей формы
            int logInStratus = logChecker.logInVerification(login.getText(), password.getText());
            switch (logInStratus) {
                case (0):
                    DialogWindow.createInfoDialog("Успех!", "Вы вошли бы, если бы я это прописал");
                    //успешный вход //fix me
                    break;
                case (1):
                    DialogWindow.createInfoDialog(ERROR_NAME, "Пользователя с таким логином не существует или аккаунт заблокирован");
                    break;
                case (2):
                    DialogWindow.createInfoDialog(ERROR_NAME, "Пароль неверный");
                    break;
                default:
                    String enterAnswer = DialogWindow.createFormDialog(ERROR_NAME, "Вы ввели слишком много раз неверный пароль, аккаунт заморожен, введите секретное слово, выбранное при регистрации", "Ваше секретное слово");
                    if (logChecker.checkSecretAnswer(login.getText(), enterAnswer)) {
                        changePasswordProcedure();
                    } else {
                        logChecker.banUser(login.getText());
                        DialogWindow.createInfoDialog(ERROR_NAME, "Ваш аккаунт заблогирован");
                        //обращение к админу //fix me
                    }
                    break;
            }
        } else {
            DialogWindow.createInfoDialog(ERROR_NAME, "Необходимо заполнить все поля!");
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

    public void changePasswordAction() throws IOException {
        String log = login.getText();
        if (logChecker.checkPassword(log, password.getText())){ //пароль совпадает со старым
            DialogWindow.createInfoDialog("Ошибка смены пароля", "Необходимо ввести пароль, отличающийся от старого");
        } else {
            if (Registration.editPassword(log, password.getText())) { //успешная смена проля
                pageReload(); //перезагрузка страницы
            } else { //неверный формат проля
                DialogWindow.createInfoDialog("Ошибка смены пароля", "Пароль должен содержать строчную и заглавную латинские буквы, цифру, спец символ, минммум 8 знаков");
            }
        }
    }

    private void changePasswordProcedure() {
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
    }

    private void pageReload() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("log-in-screen.fxml"));
        Stage currentStage = (Stage) login.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 555, 365);
        currentStage.setScene(scene);
        currentStage.setTitle("Войдите в приложение");
    }

    private boolean signIpFieldsNotEmpty() {
        if (login.getText().isEmpty() || password.getText().isEmpty()) {
            return false;
        }
        return true;
    }
}
