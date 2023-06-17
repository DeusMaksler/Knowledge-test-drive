package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import knowledgetest.application.Main;
import knowledgetest.application.engine.service.Registration;
import knowledgetest.application.frontend.common.DialogWindow;
import knowledgetest.application.frontend.common.PageManage;

import java.io.IOException;

public class LogUpScreen {
    private Registration reg = new Registration();
    private final String ERROR_NAME= "Ошибка регистрации";
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField patronymicField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField groupField;
    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField answerField;

    public LogUpScreen() throws IOException {}

    public void goToAuthorization() throws IOException {
        PageManage.loadPage((Stage) loginField.getScene().getWindow(), "log-in-screen.fxml", "Войдите в приложение", 555, 365);
    }

    public void registerUser() throws IOException {
        if (signUpFieldsNotEmpty()){  //валидация полей формы
            if (reg.uniqueLogin(loginField.getText())){
                int logUpStatus = reg.logUpVerification(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(), patronymicField.getText(), groupField.getText(), emailField.getText());
                //проверка всех введённых данных
                switch (logUpStatus) {
                    case  (0):
                        reg.createAcc(loginField.getText(), passwordField.getText(), nameField.getText(), surnameField.getText(), patronymicField.getText(), groupField.getText(), emailField.getText(), answerField.getText());
                        DialogWindow.createInfoDialog("Регистрация", "Пользователь успешно зарегистрирован");
                        //goToAuthorization((Stage) loginField.getScene().getWindow());//fix me
                        break;
                    case  (1):
                        DialogWindow.createInfoDialog(ERROR_NAME, "Логин неприемлемого формата, введите новый");
                        break;
                    case  (2):
                        DialogWindow.createInfoDialog(ERROR_NAME, "Пароль неприемлемого формата, введите новый");
                        break;
                    case  (3):
                        DialogWindow.createInfoDialog(ERROR_NAME, "Email неприемлемого формата, введите новый");
                        break;
                    case  (4):
                        DialogWindow.createInfoDialog(ERROR_NAME, "Название группы неприемлемого формата");
                        break;
                    case  (5):
                        DialogWindow.createInfoDialog(ERROR_NAME, "ФИО неприемлемого формата");
                        break;
                    default:
                        break;
                }
            } else {
                DialogWindow.createInfoDialog(ERROR_NAME, "Такой логин уже существует");
            }
        } else {
            DialogWindow.createInfoDialog(ERROR_NAME, "Необходимо заполнить все поля!");
        }
    }

    private boolean signUpFieldsNotEmpty() {
        if (loginField.getText().isEmpty() || passwordField.getText().isEmpty() || nameField.getText().isEmpty() || surnameField.getText().isEmpty() || patronymicField.getText().isEmpty() || emailField.getText().isEmpty() || groupField.getText().isEmpty() || answerField.getText().isEmpty()) {
            return false;
        }
        return true;
    }

}
