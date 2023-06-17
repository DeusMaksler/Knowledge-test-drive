package knowledgetest.application.frontend.common;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import knowledgetest.application.Main;

import java.io.IOException;

public class PageManage {
    public static void loadPage(Stage currentStage, String pageName, String pageTitle, int pageWidth, int pageHeight) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(pageName));
        Scene scene = new Scene(fxmlLoader.load(), pageWidth, pageHeight);
        currentStage.setScene(scene);
        currentStage.setTitle(pageTitle);
    }
}
