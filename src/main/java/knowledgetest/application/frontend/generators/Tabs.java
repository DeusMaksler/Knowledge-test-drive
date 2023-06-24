package knowledgetest.application.frontend.generators;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import knowledgetest.application.engine.model.Record;
import knowledgetest.application.engine.model.User;
import knowledgetest.application.engine.repository.UsersTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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

    public static Tab createStatisticTab(ArrayList<Record> recordsList, String tabName) {
        if (recordsList.size() == 0) { return new Tab(tabName, new Label("Пройденные тесты отсутствуют"));}

        VBox recordSheet = new VBox();
        for (Record record : recordsList) {
            HBox oneRecord = new HBox();
            oneRecord.setSpacing(30);
            oneRecord.getChildren().add(new Label(record.getUserName()));
            oneRecord.getChildren().add(new Label(record.getUserGroup()));
            oneRecord.getChildren().add(new Label(record.getTestName()));
            oneRecord.getChildren().add(new Label(Integer.toString(record.getUserResult())));
            recordSheet.getChildren().add(oneRecord);
        }
        ScrollPane scrollContainer = new ScrollPane(recordSheet);
        return new Tab(tabName, scrollContainer);

    }

    public static Tab createPieChartTab(HashMap<String, int[]> recordsList, String tabName) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Set<String> partName = recordsList.keySet();

        for (String title: partName) {
            int[] values = recordsList.get(title);
            pieChartData.add(new PieChart.Data(title, values[1] / values[0]));
        }
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Средние оценки");
        return new Tab(tabName, chart);
    }

    public static Tab createBarCartTab(HashMap<String, int[]> recordsList, String tabName, String criterion) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<>(xAxis,yAxis);
        bc.setTitle("Средние оценки");
        xAxis.setLabel("Значения");
        yAxis.setLabel(criterion);

        Set<String> partName = recordsList.keySet();

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Количество прохождений");
        for (String title: partName) { series1.getData().add(new XYChart.Data(title, recordsList.get(title)[0]));}

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Средний балл");
        for (String title: partName) {
            int[] values = recordsList.get(title);
            series2.getData().add(new XYChart.Data(title, values[1] / values[0]));
        }

        bc.getData().addAll(series1, series2);
        return new Tab(tabName, bc);
    }
}
