package knowledgetest.application.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import knowledgetest.application.Main;
import knowledgetest.application.engine.repository.RecordsTable;
import knowledgetest.application.frontend.common.PageManage;
import knowledgetest.application.frontend.generators.Tabs;

import java.io.IOException;
import java.util.HashMap;

import static knowledgetest.application.Main.currentStage;

public class AnalystScreen {
    @FXML
    private TabPane statisticTabs;
    @FXML
    void initialize() throws IOException {
        HashMap<String, int[]> data;
        String criterion;
        statisticTabs.getTabs().add(Tabs.createStatisticTab(RecordsTable.getLastRecords(), "Последние тесты"));
        if (Main.session.isTestStatistic()) {
            criterion = "Тест";
            data = RecordsTable.medianTestStatistics();
        }
        else {
            criterion = "Группа";
            data = RecordsTable.medianGroupStatistics();
        }
        statisticTabs.getTabs().add(Tabs.createPieChartTab(data, "Круговая диаграмма"));
        statisticTabs.getTabs().add(Tabs.createBarCartTab(data, "Столбчатая диаграмма", criterion));
    }

    public void goToHome() throws IOException {
        PageManage.loadPage(currentStage, "home-screen.fxml", "Главный экран", 600, 400);
    }
}
