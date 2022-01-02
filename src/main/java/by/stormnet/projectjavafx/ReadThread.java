package by.stormnet.projectjavafx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import by.stormnet.projectjavafx.models.Record;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import static by.stormnet.projectjavafx.controllers.MainController.recordTitle;
import static by.stormnet.projectjavafx.controllers.MainController.inRecordsList;


public class ReadThread implements Runnable{

    @Override
    public void run() {
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");

        System.out.printf("Поток %s запустился: чтение файла данных ...\n", Thread.currentThread().getName());
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
            recordTitle.setWorker(cells.next().getStringCellValue());
            recordTitle.setDate(cells.next().getStringCellValue());
            recordTitle.setTime(cells.next().getStringCellValue());
            recordTitle.setDepartment(cells.next().getStringCellValue());
            recordTitle.setEvent(cells.next().getStringCellValue());
            while (it.hasNext()) {
                Record<LocalDate, LocalTime> inRecord = new Record<>();
                row=it.next();
                cells = row.iterator();
                inRecord.setWorker(cells.next().getStringCellValue());
                inRecord.setDate(LocalDate.parse(cells.next().getStringCellValue(),dtfDate));
                inRecord.setTime(LocalTime.parse(cells.next().getStringCellValue(),dtfTime));
                inRecord.setDepartment(cells.next().getStringCellValue());
                inRecord.setEvent(cells.next().getStringCellValue());
                inRecordsList.add(inRecord);
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Ошибка при чтении файла данных");
            System.exit(0);

        }
        System.out.printf("Поток %s завершился \n", Thread.currentThread().getName());
    }
}

