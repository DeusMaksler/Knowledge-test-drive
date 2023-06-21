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

            Label roleLabel = new Label( (person.getRole().equals("user") ? "Роль: Пользователь" : "Роль: Аналитик"));
            content.getChildren().add(roleLabel);

            Label  statusLabel= new Label(person.getStatus() ? "Статус: активен" : "Статус: заблокирован");
            content.getChildren().add(statusLabel);

            HBox btns = new HBox();

            //Функционал назначения роли
            Button changeRoleButton = new Button(person.getRole().equals("user") ? "Сделать аналитиком" : "Сделать пользователем");
            changeRoleButton.setOnAction(event -> {
                try {
                    if (roleLabel.getText().equals("Роль: Пользователь")) {
                    UsersTable.changeRole(person.getLogin(), "analyst");
                    roleLabel.setText("Роль: Аналитик");
                    changeRoleButton.setText("Сделать пользователем");
                    } else {
                        UsersTable.changeRole(person.getLogin(), "user");
                        roleLabel.setText("Роль: Пользователь");
                        changeRoleButton.setText("Сделать аналитиком");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            //Функционал (раз)блокировки
            Button changeStatusButton = new Button(person.getStatus() ? "Заблокировать" : "Разблокировать");
            changeStatusButton.setOnAction(event -> {
                try {
                    UsersTable.changeStatus(person.getLogin());
                    statusLabel.setText(statusLabel.getText().equals("Статус: активен") ? "Статус: заблокирован" : "Статус: активен");
                    changeStatusButton.setText(statusLabel.getText().equals("Статус: активен") ? "Заблокировать" : "Разблокировать");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

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
