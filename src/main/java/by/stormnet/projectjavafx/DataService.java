package by.stormnet.projectjavafx;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DataService {

    public static boolean isDateWrong(ComboBox<String> comboBoxPeriod, DatePicker datePicker1, DatePicker datePicker2) {
        LocalDate today = LocalDate.now();
        if (comboBoxPeriod.getValue().equals("На дату") && (datePicker1.getValue() == null ||
            datePicker1.getValue().isAfter(today))) {
            return true;
        } else return (comboBoxPeriod.getValue().equals("За период")) &&
                      ((datePicker1.getValue() == null || datePicker1.getValue().isAfter(today)) &&
                      (datePicker2.getValue() == null || datePicker2.getValue().isAfter(today)));
    }

    public static void writeFile(Record<String,String> recordTitle, List<Record<LocalDate, LocalTime>> outRecordsList) {
        System.out.println("Начинается запись файла ...");
        final String outReportsFolder = "C:\\ClockHouse\\out";
        String outReportFileName = outReportsFolder + File.separator + "ClockHouseOut" + ".xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Отчет по проходной");
        sheet.setDefaultColumnWidth(15);
        sheet.setColumnWidth(3,10000);
        int rowNum = 0;
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(recordTitle.getWorker());
        row.createCell(1).setCellValue(recordTitle.getDate());
        row.createCell(2).setCellValue(recordTitle.getTime());
        row.createCell(3).setCellValue(recordTitle.getDepartment());
        row.createCell(4).setCellValue(recordTitle.getEvent());

        XSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        XSSFFont font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        style.setFont(font);
        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            row.getCell(i).setCellStyle(style);
        }
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        for (Record<LocalDate,LocalTime> outRecord : outRecordsList) {
            row = sheet.createRow(++rowNum);
            row.createCell(0).setCellValue(outRecord.getWorker());
            row.createCell(1).setCellValue(dtfDate.format(outRecord.getDate()));
            row.createCell(2).setCellValue(dtfTime.format(outRecord.getTime()));
            row.createCell(3).setCellValue(outRecord.getDepartment());
            row.createCell(4).setCellValue(outRecord.getEvent());
        }
        boolean noErrorWrite = true;
        try (FileOutputStream out = new FileOutputStream(outReportFileName)) {
            workbook.write(out);
        } catch (IOException e) {
            //e.printStackTrace();
            noErrorWrite = false;
        }
        if(noErrorWrite) {
            System.out.println("Excel файл успешно создан!");
        } else {
            System.out.println("Ошибка при записи Excel файла");
        }
    }

    public static List<Record<LocalDate, LocalTime>> makeOutRecordsList (List<Record<LocalDate, LocalTime>> inRecordsList,
                    WorkingTime <String,String> workingTime, ComboBox<String> comboBoxType,
                    ComboBox<String> comboBoxDepartament,
                    ComboBox<String> comboBoxWorker, ComboBox<String> comboBoxPeriod,
                    DatePicker datePicker1, DatePicker datePicker2) {

        LocalDate dateStart;
        LocalDate dateEnd;
        if(comboBoxPeriod.getValue().equals("На дату")) {
            dateStart = datePicker1.getValue();
            dateEnd = dateStart;
        } else if(datePicker1.getValue() == null) {
            dateStart = datePicker2.getValue();
            dateEnd = dateStart;
        } else if(datePicker2.getValue() == null) {
            dateStart = datePicker1.getValue();
            dateEnd = dateStart;
        } else if(datePicker1.getValue().isBefore(datePicker2.getValue())) {
            dateStart = datePicker1.getValue();
            dateEnd = datePicker2.getValue();
        } else {
            dateStart = datePicker2.getValue();
            dateEnd = datePicker1.getValue();
        }
        List<Record<LocalDate, LocalTime>> checkTimeRecordsList = inRecordsList.stream()
                .filter(inRecord -> (inRecord.getDate().isAfter(dateStart) || inRecord.getDate().isEqual(dateStart)) &&
                                    (inRecord.getDate().isBefore(dateEnd) || inRecord.getDate().isEqual(dateEnd)))
                .collect(Collectors.toList());
        if(comboBoxType.getValue().equals("Аналитика по опозданиям и переработкам")) {
            WorkingTime<LocalTime,Integer> workingLocalTime = makeWorkingLocalTime(workingTime);
            System.out.println(workingLocalTime);
            checkTimeRecordsList = checkTimeRecordsList.stream()
                    .filter(inRecord -> (inRecord.getEvent().equals("Вход") &&
                                        inRecord.getTime().isAfter(workingLocalTime.getStartWorkingDay()) &&
                                        inRecord.getTime().isBefore(workingLocalTime.getStartWorkingDay().plusMinutes(workingLocalTime.getInterval()))) ||
                                        (inRecord.getEvent().equals("Выход") &&
                                        inRecord.getTime().isAfter(workingLocalTime.getStartLunch().minusMinutes(workingLocalTime.getInterval())) &&
                                        inRecord.getTime().isBefore(workingLocalTime.getStartLunch())) ||
                                        (inRecord.getEvent().equals("Вход") &&
                                        inRecord.getTime().isAfter(workingLocalTime.getEndLunch()) &&
                                        inRecord.getTime().isBefore(workingLocalTime.getEndLunch().plusMinutes(workingLocalTime.getInterval()))) ||
                                        (inRecord.getEvent().equals("Выход") &&
                                        inRecord.getTime().isAfter(workingLocalTime.getEndWorkingDay().minusMinutes(workingLocalTime.getInterval())) &&
                                        inRecord.getTime().isBefore(workingLocalTime.getEndWorkingDay())) ||
                                        (inRecord.getEvent().equals("Выход") &&
                                        inRecord.getTime().isAfter(workingLocalTime.getHardWorkingTime())))
                                        .collect(Collectors.toList());
        }
        if(!comboBoxDepartament.isDisable()) {
            System.out.println(comboBoxDepartament.getValue());
            checkTimeRecordsList = checkTimeRecordsList.stream()
                    .filter(inRecord -> inRecord.getDepartment().equals(comboBoxDepartament.getValue()))
                    .collect(Collectors.toList());
        }
        if(!comboBoxWorker.isDisable()) {
            System.out.println(comboBoxWorker.getValue());
            checkTimeRecordsList = checkTimeRecordsList.stream()
                    .filter(inRecord -> inRecord.getWorker().equals(comboBoxWorker.getValue()))
                    .collect(Collectors.toList());
        }
        return checkTimeRecordsList;
    }

    public static WorkingTime<LocalTime,Integer> makeWorkingLocalTime(WorkingTime<String,String> workingTime){
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm");
        return new WorkingTime<>(
                LocalTime.parse(workingTime.getStartWorkingDay(),dtfTime),
                LocalTime.parse(workingTime.getEndWorkingDay(),dtfTime),
                LocalTime.parse(workingTime.getStartLunch(),dtfTime),
                LocalTime.parse(workingTime.getEndLunch(),dtfTime),
                Integer.valueOf(workingTime.getInterval().substring(3)),
                LocalTime.parse(workingTime.getHardWorkingTime(),dtfTime));
    }
}

