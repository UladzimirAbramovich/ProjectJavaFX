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
    private ComboBox comboBoxStartWorkingDay;

    @FXML
    private ComboBox comboBoxEndWorkingDay;

    @FXML
    private ComboBox comboBoxStartLunch;

    @FXML
    private ComboBox comboBoxEndLunch;

    @FXML
    private ComboBox comboBoxInterval;

    @FXML
    private ComboBox comboBoxHardWorkingTime;

    @FXML
    private Button ButtonSaveWorkingTime;

    @FXML
    private void onButtonSaveWorkingTime() {

        mainController.workingTime.setStartWorkingDay((String) comboBoxStartWorkingDay.getValue());
        mainController.workingTime.setEndWorkingDay((String) comboBoxEndWorkingDay.getValue());
        mainController.workingTime.setStartLunch((String) comboBoxStartLunch.getValue());
        mainController.workingTime.setEndLunch((String) comboBoxEndLunch.getValue());
        mainController.workingTime.setInterval((String) comboBoxInterval.getValue());
        mainController.workingTime.setHardWorkingTime((String) comboBoxHardWorkingTime.getValue());
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
