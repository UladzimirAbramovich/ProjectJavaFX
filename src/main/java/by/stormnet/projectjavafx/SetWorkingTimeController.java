package by.stormnet.projectjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import static by.stormnet.projectjavafx.DataService.*;

public class SetWorkingTimeController {

    private MainController mainController;

    public void setParent (MainController mainController){
        this.mainController = mainController;
    }

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
    private Button ButtonSaveWorkingTime;

    @FXML
    private void onButtonSaveWorkingTime() {

        mainController.workingTime.setStartWorkingDay(comboBoxStartWorkingDay.getValue());
        mainController.workingTime.setEndWorkingDay(comboBoxEndWorkingDay.getValue());
        mainController.workingTime.setStartLunch(comboBoxStartLunch.getValue());
        mainController.workingTime.setEndLunch(comboBoxEndLunch.getValue());
        mainController.workingTime.setInterval(comboBoxInterval.getValue());
        mainController.workingTime.setHardWorkingTime(comboBoxHardWorkingTime.getValue());
        mainController.setLabelWorkingTime();
        writeWorkingTime(mainController.workingTime);
        Stage stage = (Stage) ButtonSaveWorkingTime.getScene().getWindow();
        stage.close();
    }

    protected void setValueWorkingTimeController(WorkingTime <String,String> workingTime) {
        comboBoxStartWorkingDay.setValue(workingTime.getStartWorkingDay());
        comboBoxEndWorkingDay.setValue(workingTime.getEndWorkingDay());
        comboBoxStartLunch.setValue(workingTime.getStartLunch());
        comboBoxEndLunch.setValue(workingTime.getEndLunch());
        comboBoxInterval.setValue(workingTime.getInterval());
        comboBoxHardWorkingTime.setValue(workingTime.getHardWorkingTime());
    }
}
