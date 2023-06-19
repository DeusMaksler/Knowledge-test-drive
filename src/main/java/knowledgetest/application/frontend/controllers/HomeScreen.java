package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import knowledgetest.application.frontend.common.PageManage;

import java.io.IOException;

import static knowledgetest.application.Main.currentStage;

public class HomeScreen {
    @FXML
    private Button lkButton;
    @FXML
    private Button qbButton;

    public void goToLK() throws IOException {
        PageManage.loadPage(currentStage, "lk.fxml", "Личный кабинет", 650, 440) ;
    }

    public void goToQB() throws IOException {
        PageManage.loadPage(currentStage, "question-base-screen.fxml", "Управление базой вопросов", 600, 400);
    }
}
