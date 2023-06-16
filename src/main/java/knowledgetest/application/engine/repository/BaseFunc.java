package knowledgetest.application.engine.repository;


import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BaseFunc {
     protected  static final String DIR_PATH = "../Knowledge-test-drive/src/main/assets/";

    protected static boolean tableExist(String name) {
        String filePath = DIR_PATH + name +".xlsx";
        Path path = Paths.get(filePath);
        return  Files.exists(path);
    }

    protected static void tableWriteConnection(String tableID, Workbook record) throws IOException {
        File xlsxFile = new File(DIR_PATH + tableID +".xlsx");
        FileOutputStream outputStream = new FileOutputStream(xlsxFile);
        record.write(outputStream);
        record.close();
        outputStream.close();
    }

    protected static Workbook tableReadConnection(String tableID) throws IOException {
        FileInputStream inputStream = new FileInputStream(DIR_PATH + tableID +".xlsx");
        Workbook workbook = new XSSFWorkbook(inputStream);
        inputStream.close();
        return workbook; //необходимо закрыть в принимающем методе
    }

}
