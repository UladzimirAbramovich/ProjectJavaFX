package by.stormnet.projectjavafx.controllers;

import by.stormnet.projectjavafx.ClockHouse;
import by.stormnet.projectjavafx.threads.ReadThread;
import by.stormnet.projectjavafx.models.Record;
import by.stormnet.projectjavafx.models.WorkingTime;
import by.stormnet.projectjavafx.service.ErrorAlert;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import static by.stormnet.projectjavafx.service.DataService.*;
import static javafx.stage.Modality.WINDOW_MODAL;

public class MainController {
    @FXML
    private ComboBox<String> comboBoxType;
    @FXML
    private ComboBox<String> comboBoxDepartmentOrWorker;
    @FXML
    private ComboBox<String> comboBoxDepartment;
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

    public static WorkingTime<String,String> workingTime = new WorkingTime<>("09:00",
            "18:00","13:00", "14:00", "00:15", "20:00");
    public static Record<String,String> recordTitle = new Record<>();
    public static List<Record<LocalDate, LocalTime>> inRecordsList = new ArrayList<>();
    public static List<Record<LocalDate, LocalTime>> outRecordsList = new ArrayList<>();
    public static String tableTitle;
    public static String errorReadThread;
    private Thread readThread;


    @FXML
    private void initialize() {
        readThread = new Thread(new ReadThread());
        readThread.start();
        WorkingTime<String,String> temp = readWorkingTime();
        if(temp != null) {
            workingTime = temp;
        }
        setLabelWorkingTime();
    }

    public void setLabelWorkingTime() {
        labelStartWorkingDay.setText(workingTime.getStartWorkingDay());
        labelEndWorkingDay.setText(workingTime.getEndWorkingDay());
        labelStartLunch.setText(workingTime.getStartLunch());
        labelEndLunch.setText(workingTime.getEndLunch());
        labelInterval.setText(workingTime.getInterval());
        labelHardWorkingTime.setText(workingTime.getHardWorkingTime());
    }

    @FXML
    private void onComboBoxDepartmentOrWorker() {
        if(comboBoxDepartmentOrWorker.getValue().equals("В рамках структурного подразделения")) {
           comboBoxDepartment.setDisable(false);
           comboBoxWorker.setDisable(true);
        } else {
            comboBoxDepartment.setDisable(true);
            comboBoxWorker.setDisable(false);
        }
    }

    @FXML
    private void onComboBoxPeriod() {
        if(comboBoxPeriod.getValue().equals("За период")) {
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
        if(checkBoxAllCompany.isSelected()) {
            comboBoxDepartmentOrWorker.setDisable(true);
            comboBoxDepartment.setDisable(true);
            comboBoxWorker.setDisable(true);
        } else {
            comboBoxDepartmentOrWorker.setDisable(false);
            if(comboBoxDepartmentOrWorker.getValue().equals("В рамках структурного подразделения")) {
                comboBoxDepartment.setDisable(false);
                comboBoxWorker.setDisable(true);
            } else {
                comboBoxDepartment.setDisable(true);
                comboBoxWorker.setDisable(false);
            }
        }
    }

    @FXML
    private void onSetWorkingTime(Event event) {
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

    ErrorAlert error = str -> {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(str);
        alert.showAndWait();
    };

    @FXML
    private void onWriteFile(Event event) {
        if(isDateWrong(comboBoxPeriod, datePicker1, datePicker2)) {
            error.alert("\tНеправильно указана дата отчета");
        } else if(readThread.isAlive()) {
            error.alert("\tПодождите загрузки данных");
        } else if(errorReadThread != null) {
            error.alert("\t" + errorReadThread);
            System.exit(0);
        } else {
            outRecordsList = makeOutRecordsList(inRecordsList, workingTime, comboBoxType, comboBoxDepartment,
                                                comboBoxWorker, comboBoxPeriod, datePicker1,datePicker2);
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader((ClockHouse.class.getResource("Table.fxml")));
            try {
                stage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setTitle(tableTitle);
            stage.setResizable(false);
            stage.setX(350);
            stage.setY(100);
            stage.initModality(WINDOW_MODAL);
            stage.initOwner(((Node)event.getSource()).getScene().getWindow());
            TableController tableController = loader.getController();
            tableController.initData(outRecordsList);
            stage.show();
        }
    }
}