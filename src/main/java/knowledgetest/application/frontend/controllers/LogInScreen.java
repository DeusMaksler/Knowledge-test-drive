package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import knowledgetest.application.Main;
import knowledgetest.application.engine.model.Session;
import knowledgetest.application.engine.repository.UsersTable;
import knowledgetest.application.engine.service.Authorization;
import knowledgetest.application.engine.service.Registration;
import knowledgetest.application.frontend.common.DialogWindow;
import knowledgetest.application.frontend.common.PageManage;

import java.io.IOException;

import static knowledgetest.application.Main.currentStage;

public class LogInScreen {
    private Authorization logChecker;
    private  final String ERROR_NAME = "Ошибка авторизации";
    @FXML
    private Label logInLabel;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Button logInButton;
    @FXML
    private Button toLogUpButton;
    @FXML
    private Button changePasswordButton;
    @FXML
    void initialize() throws IOException {
        logChecker = new Authorization();
    }

    public void checkEnteredData() throws IOException {
        if (signIpFieldsNotEmpty()){ //валидация полей формы
            int logInStratus = logChecker.logInVerification(login.getText(), password.getText());
            switch (logInStratus) {
                case (0):
                    Main.session = new Session(login.getText(), logChecker.getAccRole(login.getText())); //сохранение информации о сессии
                    PageManage.loadPage(currentStage, "home-screen.fxml", "Главный экран", 600, 400);
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
                        UsersTable.changeStatus(login.getText(), false);
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
        PageManage.loadPage(currentStage, "log-up-screen.fxml", "Регистрация аккаунта", 668, 490);
    }

    public void changePasswordAction() throws IOException {
        String log = login.getText();
        if (logChecker.checkPassword(log, password.getText())){ //пароль совпадает со старым
            DialogWindow.createInfoDialog("Ошибка смены пароля", "Необходимо ввести пароль, отличающийся от старого");
        } else {
            if (Registration.editPassword(log, password.getText())) { //успешная смена проля
                //перезагрузка страницы
                DialogWindow.createInfoDialog("Уведомление", "Пароль успешно изменён");
                PageManage.loadPage(currentStage, "log-in-screen.fxml", "Войдите в приложение", 555, 365);
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

    private boolean signIpFieldsNotEmpty() {
        if (login.getText().isEmpty() || password.getText().isEmpty()) {
            return false;
        }
        return true;
    }
}
