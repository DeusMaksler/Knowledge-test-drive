package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import knowledgetest.application.frontend.common.PageManage;

import java.io.IOException;

public class HomeScreen {
    @FXML
    private Button lkButton;
    public void goToLK() throws IOException {
        PageManage.loadPage((Stage) lkButton.getScene().getWindow(), "lk.fxml", "Личный кабинет", 700, 700) ;
    }
}