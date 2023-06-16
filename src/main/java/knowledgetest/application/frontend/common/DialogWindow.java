package knowledgetest.application.frontend.common;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class DialogWindow {
    public static void createInfoDialog(String title, String content) {
        Dialog dialog = new Dialog();
        dialog.setTitle(title);
        DialogPane pane = new DialogPane();
        pane.setContentText(content);
        pane.getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.setDialogPane(pane);
        dialog.showAndWait();
    }
}
