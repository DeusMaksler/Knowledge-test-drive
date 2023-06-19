package knowledgetest.application.frontend.generators;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import knowledgetest.application.engine.model.User;
import knowledgetest.application.engine.repository.UsersTable;
import knowledgetest.application.frontend.common.DialogWindow;

import java.io.IOException;
import java.util.ArrayList;

public class Tabs {
    public static Tab createUsersTab() throws IOException {
        ArrayList<User> users = UsersTable.getUserList();
        Accordion usersView = new Accordion();

        for (User person: users) {
            TitledPane pane = new TitledPane();
            String paneTitle = person.getName() + " " + person.getSurname() + " " + person.getPatronymic() + " " + person.getGroup();
            pane.setText(paneTitle);

            VBox content = new VBox();
            content.getChildren().add(new Label("Логин: " + person.getLogin()));

            Label roleLabel = new Label("Роль: " + (person.getRole().equals("user") ? "Пользователь" : "Аналитик"));
            content.getChildren().add(roleLabel);

            Label  statusLabel= new Label(person.getStatus() ? "Статус: активен" : "Статус: заблокирован");
            content.getChildren().add(statusLabel);
            //Функционал назначения роли

            HBox btns = new HBox();
            Button changeRoleButton;
            if (person.getRole().equals("user")) {
                changeRoleButton = new Button("Сделать аналитиком");
                changeRoleButton.setOnAction(event -> {
                    try {
                        UsersTable.changeRole(person.getLogin(), "analyst");
                        roleLabel.setText("Аналитик");
                        DialogWindow.createInfoDialog("Смена роли", "Роль пользователя успешно изменена");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                changeRoleButton = new Button("Сделать пользователем");
                changeRoleButton.setOnAction(event -> {
                    try {
                        UsersTable.changeRole(person.getLogin(), "user");
                        roleLabel.setText("Пользователь");
                        DialogWindow.createInfoDialog("Смена роли", "Роль пользователя успешно изменена");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            //Функционал (раз)блокировки
            Button changeStatusButton;
            if (person.getStatus()) {
                changeStatusButton = new Button("Заблокировать пользователя");
                changeStatusButton.setOnAction(event -> {
                    try {
                        UsersTable.changeStatus(person.getLogin(), false);
                        DialogWindow.createInfoDialog("Блокировка пользователя", "Пользователь успешно заблокирован");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                changeStatusButton = new Button("Разблокировать");
                changeStatusButton.setOnAction(event -> {
                    try {
                        UsersTable.changeStatus(person.getLogin(), true);
                        DialogWindow.createInfoDialog("Разблокировка пользователя", "Пользователь успешно разблокирован");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            //упаковка кнопок
            btns.getChildren().addAll(changeRoleButton, changeStatusButton);
            btns.setSpacing(10);
            content.getChildren().add(btns);

            //добавление раздела одного пользователя
            pane.setContent(content);
            usersView.getPanes().add(pane);
        }
        ScrollPane scrollContainer = new ScrollPane(usersView);

        return new Tab("Пользователи", scrollContainer);
    }
}
