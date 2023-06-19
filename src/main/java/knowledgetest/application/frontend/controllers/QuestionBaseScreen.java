package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import knowledgetest.application.engine.model.QSection;
import knowledgetest.application.engine.repository.QuestionsTable;
import knowledgetest.application.frontend.common.DialogWindow;
import knowledgetest.application.frontend.common.PageManage;
import knowledgetest.application.frontend.generators.QBItems;

import java.io.IOException;

import static knowledgetest.application.Main.currentStage;

public class QuestionBaseScreen {
    @FXML
    private Label header;
    @FXML
    private AnchorPane sectionsPane;

    @FXML
    void initialize() throws IOException {
        QSection[] sectionsList = QuestionsTable.getSectionsList();
        if (sectionsList.length == 0){
            header.setText("Разделы отсутствуют");
        } else {
            header.setText("Список разделов");
            sectionsPane.getChildren().add(QBItems.showSections(sectionsList));
        }
    }

    public void goToHome() throws IOException {
        PageManage.loadPage(currentStage, "home-screen.fxml", "Главный экран", 600, 400);
    }

    public void createSectionButton() throws IOException {
        String sectionName = DialogWindow.createFormDialog("Создание раздела", "Введите название нового раздела", "Название раздела");
        QuestionsTable.createNewSection(sectionName);
        rebootPage();
    }

    public static void rebootPage() throws IOException {
        PageManage.loadPage(currentStage, "question-base-screen.fxml", "Управление базой вопросов", 600, 400);
    }
}
