package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import knowledgetest.application.Main;
import knowledgetest.application.frontend.common.PageManage;
import knowledgetest.application.frontend.generators.QBItems;

import java.io.IOException;

import static knowledgetest.application.Main.currentStage;

public class HomeScreen {
    @FXML
    private  ButtonBar standardBtnBar;
    @FXML
    private Button primalAnalystButton;
    @FXML
    private Button qbButton;
    @FXML
    private Button primalTestButton;
    @FXML
    private ButtonBar testBtnBar;
    @FXML
    private ButtonBar analystBtnBar;
    @FXML
    void initialize() {
        switch (Main.session.getActingUser().getRole()) {
            case ("analyst"):
                primalTestButton.setDisable(true);
                primalTestButton.setOpacity(0);
                break;
            case ("user"):
                qbButton.setDisable(true);
                qbButton.setOpacity(0);
                primalAnalystButton.setDisable(true);
                primalAnalystButton.setOpacity(0);
            default:
                break;
        }
    }

    public void goToLK() throws IOException {
        PageManage.loadPage(currentStage, "lk.fxml", "Личный кабинет", 650, 440) ;
    }

    public void goToQB() throws IOException {
        PageManage.loadPage(currentStage, "question-base-screen.fxml", "Управление базой вопросов", 600, 400);
    }

    public void rebootPage() throws IOException {
        PageManage.loadPage(currentStage, "home-screen.fxml", "Главный экран", 600, 400);
    }

    public void analystSwitchMode() {
        analystBtnBar.setOpacity(1);
        analystBtnBar.setDisable(false);
        standardBtnBar.setOpacity(0);
        standardBtnBar.setDisable(true);
    }

    public void selectGroupStatistic() throws IOException {
        Main.session.setTestStatistic(false);
        PageManage.loadPage(currentStage, "analyst-screen.fxml", "Главный экран", 600, 400);
    }

    public void selectTestStatistic() throws IOException {
        Main.session.setTestStatistic(false);
        PageManage.loadPage(currentStage, "analyst-screen.fxml", "Главный экран", 600, 400);
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
