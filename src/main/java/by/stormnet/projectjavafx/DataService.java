package by.stormnet.projectjavafx;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class DataService {

    public static boolean isDateWrong(ComboBox comboBoxPeriod, DatePicker datePicker1, DatePicker datePicker2) {
        LocalDate today = LocalDate.now();
        if (comboBoxPeriod.getValue().equals("На дату") && (datePicker1.getValue() == null ||
            datePicker1.getValue().isAfter(today))) {
            return true;
        } else if ((comboBoxPeriod.getValue().equals("За период")) &&
                  ((datePicker1.getValue() == null || datePicker1.getValue().isAfter(today)) &&
                  (datePicker2.getValue() == null || datePicker2.getValue().isAfter(today)))) {
            return true;
        } else {
            return false;
        }
    }
}

