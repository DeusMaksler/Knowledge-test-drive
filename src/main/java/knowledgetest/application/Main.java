package knowledgetest.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import knowledgetest.application.engine.repository.UsersTable;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        new UsersTable();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("log-in-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 555, 365);
        stage.setTitle("Войдите в приложение");
        stage.getIcons().add(new Image("icon.png"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}