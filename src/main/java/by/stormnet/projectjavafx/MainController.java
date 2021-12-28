package by.stormnet.projectjavafx;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static by.stormnet.projectjavafx.DataService.isDateWrong;
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

    protected WorkingTime <String> workingTime = new WorkingTime<>("09:00", "18:00",
            "13:00", "14:00", "00:15", "20:00");

    public static List<InRecord> inRecordsList = new ArrayList<>();
    public static InRecord inRecordTitle = new InRecord();

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
        String erroremptyfieldsstart = "Заполните следующие поля :\n";
        String erroremptyfields = erroremptyfieldsstart;
//        if (comboBoxType.getValue() == null) {
//            erroremptyfields += "Гражданство\n";
//        } else {
//            workingTime.setEndLunch(comboBoxType.getValue());
//        }
        if (erroremptyfields.equals(erroremptyfieldsstart)) {
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
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка ввода данных");
            alert.setHeaderText(erroremptyfields);
            alert.showAndWait();
        }
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
            System.out.println("Hello");
            System.out.println(inRecordTitle);
            long count = inRecordsList.stream()
                   //.map(InRecord::getWorker)
                   .peek(System.out::println)
                   .count();
            System.out.println(count);
     //       for (InRecord in: inRecordsList) System.out.println(in);

        }
    }
}
