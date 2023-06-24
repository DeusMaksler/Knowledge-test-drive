package knowledgetest.application.frontend.generators;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import knowledgetest.application.Main;
import knowledgetest.application.engine.model.QSection;
import knowledgetest.application.engine.model.Question;
import knowledgetest.application.engine.repository.QuestionsTable;
import knowledgetest.application.frontend.common.PageManage;
import knowledgetest.application.frontend.controllers.QuestionBaseScreen;
import knowledgetest.application.frontend.controllers.QuestionSectionScreen;
import knowledgetest.application.frontend.controllers.TestingScreen;

import java.io.IOException;

import static knowledgetest.application.Main.currentStage;

public class QBItems {
    public static Accordion showSections(QSection[] sectionsList){
        Accordion sections = new Accordion();

        for (QSection section: sectionsList) {
            TitledPane pane = new TitledPane();
            String paneTitle = section.getName();
            pane.setText(paneTitle);

            VBox content = new VBox();
            content.getChildren().add(new Label("Количество вопросов: " + section.getSize()));
            content.getChildren().add(new Label( section.isStatus() ? "Статус: активен": "Статус: блокирован"));

            HBox btns = new HBox();
            Button del = new Button("Удалить раздел");
            del.setOnAction(event -> {
                try {
                    QuestionsTable.deleteSection(section.getName());
                    QuestionBaseScreen.rebootPage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Button changeStat = new Button(section.isStatus() ? "Блокировать": "Разблокировать");
            changeStat.setOnAction(event -> {
                try {
                    QuestionsTable.inverseStatusSection(section.getName());
                    QuestionBaseScreen.rebootPage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Button openSection = new Button("Открыть раздел");
            openSection.setOnAction(event -> {
                try {
                    Main.session.setEditableSection(section.getName());
                    PageManage.loadPage(currentStage, "question-section-screen.fxml", "Вопросы из раздела " + section.getName(), 830, 400);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            btns.getChildren().addAll(del, changeStat, openSection);
            btns.setSpacing(10);

            content.getChildren().add(btns);
            pane.setContent(content);
            sections.getPanes().add(pane);
        }
        AnchorPane.setTopAnchor(sections, 10d);
        AnchorPane.setBottomAnchor(sections, 10d);
        AnchorPane.setLeftAnchor(sections, 10d);
        AnchorPane.setRightAnchor(sections, 10d);
        return sections;
    }

    public static Accordion showQuestions(String sectionName, Question[] questionList) {
        Accordion questions = new Accordion();

        for (int i = 0; i < questionList.length; i++) {
            Question question = questionList[i];
            TitledPane pane = new TitledPane();
            int questionId = i + 1;
            pane.setText("ID: " + questionId);

            VBox content = new VBox();
            content.getChildren().add(new Label("Формулировка вопроса:"));
            TextArea phrasing= new TextArea(question.getPhrasing());
            phrasing.setEditable(false);
            phrasing.setPrefRowCount(5);
            content.getChildren().add(phrasing);

            HBox digitContain = new HBox();
            digitContain.getChildren().add(new Label("Время для решения: "));
            TextField digitField = new TextField();
            digitField.setText(Integer.toString(question.getCheckDigit()));
            digitField.setEditable(false);
            digitContain.getChildren().add(digitField);
            digitContain.setSpacing(10);
            content.getChildren().add(digitContain);

            HBox rightContain = new HBox();
            rightContain.getChildren().add(new Label("Правильный ответ: "));
            TextField rightField = new TextField();
            rightField.setText(Integer.toString(question.getRightChoice()));
            rightField.setEditable(false);
            rightContain.getChildren().add(rightField);
            rightContain.setSpacing(10);
            content.getChildren().add(rightContain);

            //создание полей вопросов под варианты ответов
            HBox firstVarContain = new HBox();
            TextField firstField = new TextField();
            firstField.setText(question.getVariants()[0]);
            firstField.setEditable(false);

            TextField secondField = new TextField();
            secondField.setText(question.getVariants()[1]);
            secondField.setEditable(false);

            firstVarContain.getChildren().addAll(firstField, secondField);
            firstVarContain.setSpacing(10);
            content.getChildren().add(firstVarContain);

            TextField thirdField = new TextField();
            TextField fourthField = new TextField();

            if (!question.isYnType()) {
                HBox secondVarContain = new HBox();
                thirdField.setText(question.getVariants()[2]);
                thirdField.setEditable(false);

                fourthField.setText(question.getVariants()[3]);
                fourthField.setEditable(false);

                secondVarContain.getChildren().addAll(thirdField, fourthField);
                secondVarContain.setSpacing(10);
                content.getChildren().add(secondVarContain);
            }

            HBox btns = new HBox();
            Button del = new Button("Удалить вопрос");
            del.setOnAction(event -> {
                try {
                    QuestionsTable.deleteQuestion(sectionName, questionId);
                    QuestionSectionScreen.rebootPage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Button saveData = new Button("Сохранить изменения");
            saveData.setOnAction(click -> {
                question.setPhrasing(phrasing.getText());
                question.setCheckDigit(Integer.parseInt(digitField.getText()));
                question.setRightChoice(Integer.parseInt(rightField.getText()));
                if (question.isYnType()) {
                    question.setVariants(new String[]{firstField.getText(), secondField.getText()});
                } else {
                    question.setVariants(new String[]{firstField.getText(), secondField.getText(), thirdField.getText(), fourthField.getText()});
                }
                try {
                    QuestionsTable.changeQuestion(sectionName, questionId, question);
                    QuestionSectionScreen.rebootPage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            saveData.setDisable(true);
            saveData.setOpacity(0);

            Button changeData = new Button("Изменить данные");
            changeData.setOnAction(event -> {
                phrasing.setEditable(true);
                digitField.setEditable(true);
                rightField.setEditable(true);
                firstField.setEditable(true);
                secondField.setEditable(true);
                if (!question.isYnType()) {
                    thirdField.setEditable(true);
                    fourthField.setEditable(true);
                }
                saveData.setDisable(false);
                saveData.setOpacity(1);
            });

            btns.getChildren().addAll(del, changeData, saveData);
            btns.setSpacing(10);

            content.getChildren().add(btns);
            pane.setContent(content);
            questions.getPanes().add(pane);
        }
        return questions;
    }

    public static MenuButton createSectionMenu() throws IOException {
        QSection[] sections = QuestionsTable.getSectionsList();
        MenuButton sectionMenu = new MenuButton("Тест по разделу");
        if (sections.length != 0){
            for (QSection section : sections) {
                if(section.getSize() != 0) {
                    MenuItem item = new MenuItem(section.getName());
                    item.setOnAction(event -> {
                        try {
                            TestingScreen.generalType = false;
                            TestingScreen.questionsList = QuestionsTable.getSectionQuestions(section.getName());
                            TestingScreen.iter = 0;
                            TestingScreen.rightResults = 0;
                            TestingScreen.selectedSectionName = section.getName();
                            PageManage.loadPage(currentStage, "testing-screen.fxml", "Тест по разделу " + section.getName(), 522, 400);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    sectionMenu.getItems().add(item);
                }
            }
        }
        return sectionMenu;
    }

    public static Button createTestButton() throws IOException {
        Button generalTestButton = new Button("Общий тест");

        QSection[] sectionList = QuestionsTable.getSectionsList();
        int count = 0;
        for (QSection section : sectionList) {
            if (section.getSize() >= 15){ count++;}
        }

        if (count >= 5){
            generalTestButton.setOnAction(event -> {
                //fix me изменить размеры
                try {
                    TestingScreen.generalType = true;
                    PageManage.loadPage(currentStage, "testing-screen.fxml", "Полный тест", 400, 600);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            generalTestButton.setTooltip(new Tooltip("В данный момент прохождение теста не доступно"));
            generalTestButton.setDisable(true);
        }


        return generalTestButton;
    }

    public static AnchorPane showTestForm(Question question){
        int right = question.getRightChoice() - 1;

        AnchorPane formPane = new AnchorPane();
        formPane.setPrefHeight(300);
        formPane.setPrefWidth(500);
        formPane.setLayoutX(11);
        formPane.setLayoutY(50);
        //формулировка вопроса
        TextArea questionWording = new TextArea(question.getPhrasing());
        questionWording.setPrefWidth(472);
        questionWording.setPrefHeight(135);
        questionWording.setLayoutX(14);
        questionWording.setLayoutX(14);
        formPane.getChildren().add(questionWording);
        //первые две кнопки
        formPane.getChildren().add(createAnswerButtons(0,  165, question.getVariants(), right));
        //вторые две кнопки(опционально)
        if (!question.isYnType()){
            formPane.getChildren().add(createAnswerButtons(2,  236, question.getVariants(), right));
        }
        return formPane;
    }

    private static ButtonBar createAnswerButtons(int startIndex, int layoutY, String[] variants, int right) {
        ButtonBar btnBar = new ButtonBar();
        btnBar.setPrefHeight(42);
        btnBar.setPrefWidth(472);
        btnBar.setPadding(new Insets(0, 0,0, -20));
        btnBar.setLayoutX(14);
        btnBar.setLayoutY(layoutY);

        for (int i = startIndex; i < startIndex + 2; i++) {
            Button btnAnswer = new Button(variants[i]);
            btnAnswer.setPrefWidth(230);
            btnAnswer.setPrefHeight(42);
            if (i == right) { //правильный вариант
                btnAnswer.setOnAction(event -> {
                    TestingScreen.rightResults++;
                    TestingScreen.iter++;
                    try {
                        PageManage.loadPage(currentStage, "testing-screen.fxml", "Тест по разделу " + TestingScreen.selectedSectionName, 522, 400);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                btnAnswer.setOnAction(event -> {
                    TestingScreen.iter++;
                    try {
                        PageManage.loadPage(currentStage, "testing-screen.fxml", "Тест по разделу " + TestingScreen.selectedSectionName, 522, 400);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            btnBar.getButtons().add(btnAnswer);
        }
        return btnBar;
    }
}
