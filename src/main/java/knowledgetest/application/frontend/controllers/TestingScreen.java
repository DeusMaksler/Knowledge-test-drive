package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import knowledgetest.application.Main;
import knowledgetest.application.engine.model.Question;
import knowledgetest.application.engine.repository.QuestionsTable;
import knowledgetest.application.frontend.common.DialogWindow;
import knowledgetest.application.frontend.common.PageManage;
import knowledgetest.application.frontend.generators.QBItems;

import java.io.IOException;

import static knowledgetest.application.Main.currentStage;

public class TestingScreen {
    public static boolean generalType;
    public static String selectedSectionName;
    public static int rightResults;
    public static Question[] questionsList;
    public static int iter;

    @FXML
    private AnchorPane testingScreen;
    @FXML
    private Button cancelTestButton;

    @FXML
    void initialize() throws IOException {
        if (iter < questionsList.length) {
            testingScreen.getChildren().add(0, QBItems.showTestForm(questionsList[iter]));
        } else {
            DialogWindow.createInfoDialog("Результат теста", "Правильных ответов " + countRightPercent(questionsList.length, rightResults) + "%");
            //добавление в таблицу //fix me
            PageManage.loadPage(currentStage, "home-screen.fxml", "Главный экран", 600, 400);
        };
    }

    private static int countRightPercent(int all, int right) {
        int onePercent = 100 * right;
        return Math.round(onePercent / all);
    }

    public void cancelTest() throws IOException {
        PageManage.loadPage(currentStage, "home-screen.fxml", "Главный экран", 600, 400);
    }

}
