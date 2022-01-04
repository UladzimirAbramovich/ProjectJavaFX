package by.stormnet.projectjavafx.controllers;

import by.stormnet.projectjavafx.models.WorkingTime;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import static by.stormnet.projectjavafx.controllers.MainController.workingTime;
import static by.stormnet.projectjavafx.service.DataService.*;

public class SetWorkingTimeController {
    @FXML
    private ComboBox<String> comboBoxStartWorkingDay;
    @FXML
    private ComboBox<String> comboBoxEndWorkingDay;
    @FXML
    private ComboBox<String> comboBoxStartLunch;
    @FXML
    private ComboBox<String> comboBoxEndLunch;
    @FXML
    private ComboBox<String> comboBoxInterval;
    @FXML
    private ComboBox<String> comboBoxHardWorkingTime;
    @FXML
    private Button buttonSaveWorkingTime;

    private MainController mainController;

    public void setParent (MainController mainController){
        this.mainController = mainController;
    }

    @FXML
    private void onButtonSaveWorkingTime() {
        workingTime.setStartWorkingDay(comboBoxStartWorkingDay.getValue());
        workingTime.setEndWorkingDay(comboBoxEndWorkingDay.getValue());
        workingTime.setStartLunch(comboBoxStartLunch.getValue());
        workingTime.setEndLunch(comboBoxEndLunch.getValue());
        workingTime.setInterval(comboBoxInterval.getValue());
        workingTime.setHardWorkingTime(comboBoxHardWorkingTime.getValue());
        mainController.setLabelWorkingTime();
        writeWorkingTime(workingTime);
        Stage stage = (Stage) buttonSaveWorkingTime.getScene().getWindow();
        stage.close();
    }

    protected void setValueWorkingTimeController(WorkingTime<String,String> workingTime) {
        comboBoxStartWorkingDay.setValue(workingTime.getStartWorkingDay());
        comboBoxEndWorkingDay.setValue(workingTime.getEndWorkingDay());
        comboBoxStartLunch.setValue(workingTime.getStartLunch());
        comboBoxEndLunch.setValue(workingTime.getEndLunch());
        comboBoxInterval.setValue(workingTime.getInterval());
        comboBoxHardWorkingTime.setValue(workingTime.getHardWorkingTime());
    }
}