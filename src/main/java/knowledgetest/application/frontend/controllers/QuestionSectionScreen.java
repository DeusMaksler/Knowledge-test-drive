package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import knowledgetest.application.Main;
import knowledgetest.application.engine.model.Question;
import knowledgetest.application.engine.repository.QuestionsTable;
import knowledgetest.application.frontend.common.PageManage;
import knowledgetest.application.frontend.generators.QBItems;

import java.io.IOException;

import static knowledgetest.application.Main.currentStage;

public class QuestionSectionScreen {
    private static final String sectionName = Main.session.getEditableSection();
    @FXML
    private Label header;
    @FXML
    private ScrollPane scrollContainer;

    @FXML
    void initialize() throws IOException {
        Question[] questions = QuestionsTable.getSectionQuestions(sectionName);
        if (questions.length == 0){
            header.setText("Разделы отсутствуют");
        } else {
            header.setText("Список вопросов");
        }
    }
    public void backWindow() throws IOException {
        PageManage.loadPage(currentStage, "question-base-screen.fxml", "Управление базой вопросов", 600, 400);
    }

    public static void rebootPage() throws IOException {
        PageManage.loadPage(currentStage, "question-section-screen.fxml", "Вопросы из раздела " + sectionName, 800, 400);
    }
}
