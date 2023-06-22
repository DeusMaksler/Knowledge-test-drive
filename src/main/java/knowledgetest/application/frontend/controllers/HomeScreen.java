package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import knowledgetest.application.frontend.common.PageManage;
import knowledgetest.application.frontend.generators.QBItems;

import java.io.IOException;

import static knowledgetest.application.Main.currentStage;

public class HomeScreen {
    @FXML
    private  ButtonBar standardBtnBar;
    @FXML
    private Button lkButton;
    @FXML
    private Button qbButton;
    @FXML
    private Button primalTestButton;
    @FXML
    private ButtonBar testBtnBar;
    @FXML
    private Button generalTestButton;
    @FXML
    private Button homeBackButton;

    public void goToLK() throws IOException {
        PageManage.loadPage(currentStage, "lk.fxml", "Личный кабинет", 650, 440) ;
    }

    public void goToQB() throws IOException {
        PageManage.loadPage(currentStage, "question-base-screen.fxml", "Управление базой вопросов", 600, 400);
    }

    public void testSwitchMode() throws IOException {
        testBtnBar.setOpacity(1);
        testBtnBar.setDisable(false);
        standardBtnBar.setOpacity(0);
        standardBtnBar.setDisable(true);
        testBtnBar.getButtons().add(0, QBItems.createTestButton());
        testBtnBar.getButtons().add(1, QBItems.createSectionMenu());
    }


}
