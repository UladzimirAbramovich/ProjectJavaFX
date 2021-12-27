package by.stormnet.projectjavafx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import static by.stormnet.projectjavafx.MainController.inRecordTitle;
import static by.stormnet.projectjavafx.MainController.inRecordsList;


public class ReadThread implements Runnable{

    @Override
    public void run() {
        System.out.printf("Поток %s запустился \n", Thread.currentThread().getName());
        final String reportsFolder = "C:\\ClockHouse\\in";
        String reportFileName = reportsFolder + File.separator + "ClockHouseIn" + ".xlsx";
        File fileIn = new File(reportFileName);
        if (!fileIn.exists()) {
            System.out.println("Ошибка: Недоступен файл данных");
            System.exit(0);
        }
       try (FileInputStream input = new FileInputStream(fileIn)) {
            XSSFWorkbook workbook = new XSSFWorkbook(input);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            inRecordTitle.setWorker(cells.next().getStringCellValue());
            inRecordTitle.setDate(cells.next().getStringCellValue());
            inRecordTitle.setTime(cells.next().getStringCellValue());
            inRecordTitle.setDepartment(cells.next().getStringCellValue());
            inRecordTitle.setEvent(cells.next().getStringCellValue());
            while (it.hasNext()) {
                InRecord inRecord = new InRecord();
                row=it.next();
                cells = row.iterator();
                inRecord.setWorker(cells.next().getStringCellValue());
                inRecord.setDate(cells.next().getStringCellValue());
                inRecord.setTime(cells.next().getStringCellValue());
                inRecord.setDepartment(cells.next().getStringCellValue());
                inRecord.setEvent(cells.next().getStringCellValue());
                inRecordsList.add(inRecord);
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Ошибка при чтении файла данных");
            System.exit(0);

        }
        System.out.printf("Поток %s завершился \n", Thread.currentThread().getName();
    }
}

