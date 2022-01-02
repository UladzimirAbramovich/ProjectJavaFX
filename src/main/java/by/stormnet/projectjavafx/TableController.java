package by.stormnet.projectjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TableController {

    private ObservableList<Record<String,String>> recordObservableList = FXCollections.observableArrayList();

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
}
