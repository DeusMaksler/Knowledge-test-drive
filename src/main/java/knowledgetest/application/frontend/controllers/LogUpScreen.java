package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import knowledgetest.application.Main;

import java.io.IOException;

public class LogUpScreen {
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField patronymicField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField groupField;
    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField answerField;

    public void goToAuthorization() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("log-up-screen.fxml"));
        Stage stage = (Stage) nameField.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 668, 440);
        stage.setScene(scene);
        stage.setTitle("Регистрация аккаунта");
    }

}
