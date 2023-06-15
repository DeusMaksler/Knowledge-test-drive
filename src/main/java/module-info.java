module solidapplication.knowledgetestdrive {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    opens knowledgetest.application to javafx.fxml;
    exports knowledgetest.application;
    exports knowledgetest.application.frontend.controllers;
    opens knowledgetest.application.frontend.controllers to javafx.fxml;
}