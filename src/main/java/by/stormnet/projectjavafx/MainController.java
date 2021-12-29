package by.stormnet.projectjavafx;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormat;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static by.stormnet.projectjavafx.DataService.isDateWrong;
import static by.stormnet.projectjavafx.DataService.makeOutRecordsList;
import static javafx.stage.Modality.WINDOW_MODAL;

public class MainController {

    @FXML
    private ComboBox<String> comboBoxType;

    @FXML
    private ComboBox<String> comboBoxDepartmentOrWorker;

    @FXML
    private ComboBox<String> comboBoxDepartament;

    @FXML
    private CheckBox checkBoxAllCompany;

    @FXML
    private ComboBox<String> comboBoxWorker;

    @FXML
    private ComboBox<String> comboBoxPeriod;

    @FXML
    private DatePicker datePicker1;

    @FXML
    private DatePicker datePicker2;

    @FXML
    protected Label labelStartWorkingDay;

    @FXML
    protected Label labelEndWorkingDay;

    @FXML
    protected Label labelStartLunch;

    @FXML
    protected Label labelEndLunch;

    @FXML
    protected Label labelInterval;

    @FXML
    protected Label labelHardWorkingTime;

    @FXML
    private Label labelDatePicker1;

    @FXML
    private Label labelDatePicker2;

    protected WorkingTime <String,String> workingTime = new WorkingTime<>("09:00", "18:00",
            "13:00", "14:00", "00:15", "20:00");

    public static List<Record<LocalDate, LocalTime>> inRecordsList = new ArrayList<>();
    public static Record<String,String> recordTitle = new Record<>();

    private Thread readThread;

    @FXML
    private void initialize() {

        readThread = new Thread(new ReadThread());
        readThread.start();
        setLabelWorkingTime();

    }

    protected void setLabelWorkingTime() {
        labelStartWorkingDay.setText(workingTime.getStartWorkingDay());
        labelEndWorkingDay.setText(workingTime.getEndWorkingDay());
        labelStartLunch.setText(workingTime.getStartLunch());
        labelEndLunch.setText(workingTime.getEndLunch());
        labelInterval.setText(workingTime.getInterval());
        labelHardWorkingTime.setText(workingTime.getHardWorkingTime());
    }

    @FXML
    private void onComboBoxDepartmentOrWorker() {
        if (comboBoxDepartmentOrWorker.getValue().equals("В рамках структурного подразделения")) {
            comboBoxDepartament.setDisable(false);
            comboBoxWorker.setDisable(true);
        } else {
            comboBoxDepartament.setDisable(true);
            comboBoxWorker.setDisable(false);
        }
    }

    @FXML
    private void onComboBoxPeriod() {
        if (comboBoxPeriod.getValue().equals("За период")) {
            labelDatePicker1.setVisible(true);
            labelDatePicker2.setVisible(true);
            datePicker2.setVisible(true);
        } else {
            labelDatePicker1.setVisible(false);
            labelDatePicker2.setVisible(false);
            datePicker2.setVisible(false);
        }
    }

    @FXML
    private void onCheckBoxAllCompany() {
        if (checkBoxAllCompany.isSelected()) {
            comboBoxDepartmentOrWorker.setDisable(true);
            comboBoxDepartament.setDisable(true);
            comboBoxWorker.setDisable(true);
        } else {
            comboBoxDepartmentOrWorker.setDisable(false);
            if (comboBoxDepartmentOrWorker.getValue().equals("В рамках структурного подразделения")) {
                comboBoxDepartament.setDisable(false);
                comboBoxWorker.setDisable(true);
            } else {
                comboBoxDepartament.setDisable(true);
                comboBoxWorker.setDisable(false);
            }
        }
    }

    @FXML
    protected void onSetWorkingTime(Event event) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader((ClockHouse.class.getResource("SetWorkingTime.fxml")));
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Настройка рабочего времени");
        stage.setResizable(false);
        stage.setX(600);
        stage.setY(200);
        stage.initModality(WINDOW_MODAL);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        SetWorkingTimeController setWorkingTimeController = loader.getController();
        setWorkingTimeController.setParent(this);
        setWorkingTimeController.setValueWorkingTimeController(workingTime);
        stage.show();
    }

    Consumer<String> errorAlert = str -> {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(str);
        alert.showAndWait();
    };

    @FXML
    protected void onWriteFile() {
        if (isDateWrong(comboBoxPeriod, datePicker1, datePicker2)) {
            errorAlert.accept("\tНеправильно указана дата отчета");
        } else if (readThread.isAlive()) {
                errorAlert.accept("\tПодождите загрузки данных");
        } else {
            System.out.println("Начинается запись файла ...");
            final String outReportsFolder = "C:\\ClockHouse\\out";
            String outReportFileName = outReportsFolder + File.separator + "ClockHouseOut" + ".xlsx";

            List<Record<LocalDate, LocalTime>> outRecordsList = makeOutRecordsList(inRecordsList, workingTime,
                                comboBoxType, checkBoxAllCompany, comboBoxDepartament, comboBoxWorker, comboBoxPeriod,
                                datePicker1,datePicker2);
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
    }
}

