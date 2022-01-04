package by.stormnet.projectjavafx.controllers;

import by.stormnet.projectjavafx.models.Record;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static by.stormnet.projectjavafx.controllers.MainController.*;
import static by.stormnet.projectjavafx.service.DataService.writeFile;

public class TableController {
    @FXML
    private TableView<Record<String,String>> table;
    @FXML
    private TableColumn<Record<String,String>, String> worker;
    @FXML
    private TableColumn<Record<String,String>, String> date;
    @FXML
    private TableColumn<Record<String,String>, String> time;
    @FXML
    private TableColumn<Record<String,String>, String> department;
    @FXML
    private TableColumn<Record<String,String>, String> event;
    @FXML
    private Button buttonWriteExcelFile;

    private final ObservableList<Record<String,String>> recordObservableList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        worker.setCellValueFactory(new PropertyValueFactory<>("worker"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        department.setCellValueFactory(new PropertyValueFactory<>("department"));
        event.setCellValueFactory(new PropertyValueFactory<>("event"));
        table.setItems(recordObservableList);
    }

    public void initData(List<Record<LocalDate, LocalTime>> outRecordsList) {
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        for (Record<LocalDate, LocalTime> outRecord : outRecordsList) {
            Record<String,String> recordObservable = new Record<>();
            recordObservable.setWorker(outRecord.getWorker());
            recordObservable.setDate(dtfDate.format(outRecord.getDate()));
            recordObservable.setTime(dtfTime.format(outRecord.getTime()));
            recordObservable.setDepartment(outRecord.getDepartment());
            recordObservable.setEvent(outRecord.getEvent());
            recordObservableList.add(recordObservable);
        }
    }
    @FXML
    private void onWriteExcelFile() {
        writeFile(tableTitle, recordTitle, outRecordsList);
        Stage stage = (Stage) buttonWriteExcelFile.getScene().getWindow();
        stage.close();
    }
}