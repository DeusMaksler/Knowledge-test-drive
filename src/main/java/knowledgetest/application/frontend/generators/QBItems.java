package knowledgetest.application.frontend.generators;

import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import knowledgetest.application.engine.model.QSection;
import knowledgetest.application.engine.model.User;
import knowledgetest.application.engine.repository.QuestionsTable;
import knowledgetest.application.frontend.common.PageManage;
import knowledgetest.application.frontend.controllers.QuestionBaseScreen;

import java.io.IOException;
import java.lang.reflect.Method;

public class QBItems {
    public static Accordion showSections(QSection[] sectionsList) throws IOException {
        Accordion sections = new Accordion();

        for (QSection section: sectionsList) {
            TitledPane pane = new TitledPane();
            String paneTitle = section.getName();
            pane.setText(paneTitle);

            VBox content = new VBox();
            content.getChildren().add(new Label("Количество вопросов: " + section.getSize()));
            content.getChildren().add(new Label( section.isStatus() ? "Статус: активен": "Статус: блокирован"));

            Button del = new Button("Удалить раздел");
            del.setOnAction(event -> {
                try {
                    QuestionsTable.deleteSection(section.getName());
                    QuestionBaseScreen.rebootPage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            content.getChildren().add(del);

            Button changeStat = new Button(section.isStatus() ? "Блокировать": "Разблокировать");
            changeStat.setOnAction(event -> {
                try {
                    QuestionsTable.inverseStatusSection(section.getName());
                    QuestionBaseScreen.rebootPage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            content.getChildren().add(changeStat);

            Button openSection = new Button("Открыть раздел");
            openSection.setOnAction(event -> {
                try {
                    PageManage.createWindow("question-section-screen.fxml", "Вопросы из раздела " + section.getName(), 800, 400);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            content.getChildren().add(openSection);

            pane.setContent(content);
            sections.getPanes().add(pane);
        }
        return sections;
    }
}
