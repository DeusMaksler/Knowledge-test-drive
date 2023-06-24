package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import knowledgetest.application.Main;
import knowledgetest.application.engine.model.Question;
import knowledgetest.application.engine.repository.QuestionsTable;
import knowledgetest.application.frontend.common.DialogWindow;

import java.io.IOException;

import static java.lang.Integer.parseInt;

public class QuestionFormScreen {
    private String sectionName;
    private int quantityVariants;
    @FXML
    private TextArea phrasingArea;
    @FXML
    private CheckBox ynBox;
    @FXML
    private TextField rightField;
    @FXML
    private TextField firstField;
    @FXML
    private TextField secondField;
    @FXML
    private TextField thirdField;
    @FXML
    private TextField fourthField;
    @FXML
    private Label thirdLabel;
    @FXML
    private Label fourthLabel;
    @FXML
    void initialize() {
        sectionName = Main.session.getEditableSection();
        quantityVariants = 4;
    }

    public void changeQuestionType() {
        if (ynBox.isSelected()) {
            thirdField.setEditable(false);
            thirdField.setOpacity(0);
            thirdLabel.setOpacity(0);
            fourthField.setEditable(false);
            fourthField.setOpacity(0);
            fourthLabel.setOpacity(0);
            quantityVariants = 2;
        } else {
            thirdField.setEditable(true);
            thirdField.setOpacity(1);
            thirdLabel.setOpacity(1);
            fourthField.setEditable(true);
            fourthField.setOpacity(1);
            fourthLabel.setOpacity(1);
            quantityVariants = 4;
        }

    }

    public void saveQuestion() throws IOException {
        if (!fieldsEmpty()) {
            int right = parseInt(rightField.getText());
            if (checkRightField(right)){
                String[] fields;
                if (quantityVariants == 2) {
                    fields = new String[] { firstField.getText(), secondField.getText()};
                } else {
                    fields = new String[] { firstField.getText(), secondField.getText(), thirdField.getText(), fourthField.getText()};
                }

                Question question = new Question(ynBox.isSelected(), phrasingArea.getText(), fields, right, countControlDigit());
                QuestionsTable.createQuestion(sectionName, question);
                phrasingArea.getScene().getWindow().hide();
                QuestionSectionScreen.rebootPage();
                DialogWindow.createInfoDialog("Создание вопроса", "Вопрос успешно создан");
            } else {
                DialogWindow.createInfoDialog("Ошибка создания вопроса", "Некорректный номер верного ответа");
            }
        } else {
           DialogWindow.createInfoDialog("Ошибка создания вопроса", "Заполнены не все поля");
        }
    }

    private boolean fieldsEmpty() {
        if (ynBox.isSelected()){
            return  (phrasingArea.getText().isEmpty() || rightField.getText().isEmpty() || firstField.getText().isEmpty() || secondField.getText().isEmpty());
        } else {
            return  (phrasingArea.getText().isEmpty() || rightField.getText().isEmpty() || firstField.getText().isEmpty() || secondField.getText().isEmpty() || thirdField.getText().isEmpty() || fourthField.getText().isEmpty());
        }
    }

    private boolean checkRightField(int rightCell){
        if (ynBox.isSelected()){
            return (rightCell == 1 || rightCell == 2);
        } else {
            return (rightCell > 0 && rightCell < 5);
        }
    }
    private int countControlDigit() {
        return phrasingArea.getText().length() / 25 + 1;
    }
}
