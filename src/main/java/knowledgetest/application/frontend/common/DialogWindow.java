package knowledgetest.application.frontend.common;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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

    public static String createFormDialog(String title, String message, String prompt) {
        Dialog dialogForm = new Dialog();
        dialogForm.setTitle(title);

        Label label = new Label(message);
        TextField form = new TextField();
        form.setPromptText(prompt);

        VBox content = new VBox(label, form);
        VBox.setMargin(form, new Insets(10, 0, 0, 0));

        DialogPane pane = new DialogPane();
        pane.setContent(content);
        pane.getButtonTypes().addAll(ButtonType.OK);
        dialogForm.setDialogPane(pane);

        dialogForm.showAndWait();
        return form.getText();
    }
}
