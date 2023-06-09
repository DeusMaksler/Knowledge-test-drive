package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import knowledgetest.application.Main;
import knowledgetest.application.engine.model.User;
import knowledgetest.application.engine.repository.RecordsTable;
import knowledgetest.application.engine.repository.UsersTable;
import knowledgetest.application.engine.service.Registration;
import knowledgetest.application.frontend.common.DialogWindow;
import knowledgetest.application.frontend.common.PageManage;
import knowledgetest.application.frontend.generators.Tabs;

import java.io.IOException;

import static knowledgetest.application.Main.currentStage;

public class Lk {
    private final String ERROR_NAME = "Ошибка изменения";
    private User currentAcc;

    @FXML
    private TabPane lkTabPane;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField groupField;
    @FXML
    private TextField patronymicField;
    @FXML
    private TextField surnameField;
    @FXML
    private Button activeFieldsButton;
    @FXML
    private Button saveChangesButton;
    @FXML
    void initialize() throws IOException {
        currentAcc = Main.session.getActingUser();
        nameField.setText(currentAcc.getName());
        surnameField.setText(currentAcc.getSurname());
        patronymicField.setText(currentAcc.getPatronymic());
        groupField.setText(currentAcc.getGroup());
        emailField.setText(currentAcc.getEmail());

        if (Main.session.getActingUser().getRole().equals("admin")) {
            lkTabPane.getTabs().add(Tabs.createUsersTab()); //функционал админа
            lkTabPane.getTabs().add(Tabs.createStatisticTab(RecordsTable.getSelfRecord(Main.session.getActingUser().getLogin()), "Результаты"));//функционал пользователя
        } else if (Main.session.getActingUser().getRole().equals("user")) {
            lkTabPane.getTabs().add(Tabs.createStatisticTab(RecordsTable.getSelfRecord(Main.session.getActingUser().getLogin()), "Результаты"));//функционал пользователя
        }
    }

    public void activeFields() {
        nameField.setEditable(true);
        surnameField.setEditable(true);
        patronymicField.setEditable(true);
        groupField.setEditable(true);
        emailField.setEditable(true);
        passwordField.setEditable(true);
        activeFieldsButton.setDisable(true);
        saveChangesButton.setDisable(false);
    }

    private void disactiveFields() {
        nameField.setEditable(false);
        surnameField.setEditable(false);
        patronymicField.setEditable(false);
        groupField.setEditable(false);
        emailField.setEditable(false);
        passwordField.setEditable(false);
        activeFieldsButton.setDisable(false);
        saveChangesButton.setDisable(true);
    }

    public void saveChanges() throws IOException {
        int validFields = Registration.validFields(passwordField.getText(), nameField.getText(), surnameField.getText(), patronymicField.getText(), groupField.getText(), emailField.getText());
        switch (validFields) {
            case  (0):
                //изменение данных
                currentAcc.setPassword(passwordField.getText());
                currentAcc.setName(nameField.getText());
                currentAcc.setSurname(surnameField.getText());
                currentAcc.setPatronymic(patronymicField.getText());
                currentAcc.setGroup(groupField.getText());
                currentAcc.setEmail(emailField.getText());
                //сохранение изменений
                UsersTable.updateUser(currentAcc);
                disactiveFields();
                DialogWindow.createInfoDialog("Успешное изменение", "Пользовательские данные изменены");
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
    }

    public void remoteUserButton() throws IOException {
        String deleteAnswer = DialogWindow.createFormDialog("Удаление аккаунта", "Чтобы удалить аккаунт введите: удалить", "Введите подтверждение");
        if (deleteAnswer.equals("удалить")){
            DialogWindow.createInfoDialog("Удаление аккаунта", "Аккаунт успешно удалён");
            UsersTable.remoteUser(currentAcc.getLogin());
            PageManage.loadPage(currentStage, "log-in-screen.fxml", "Войдите в приложение", 555, 365);
        }
    }

    public void goToHome() throws IOException {
        PageManage.loadPage(currentStage, "home-screen.fxml", "Главный экран", 600, 400);
    }

}
