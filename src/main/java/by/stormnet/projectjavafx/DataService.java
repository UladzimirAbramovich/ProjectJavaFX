package by.stormnet.projectjavafx;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<Record<LocalDate, LocalTime>> makeOutRecordsList (List<Record<LocalDate, LocalTime>> inRecordsList,
                    WorkingTime <String,String> workingTime, ComboBox<String> comboBoxType,
                    CheckBox checkBoxAllCompany,ComboBox<String> comboBoxDepartament,
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
        return checkTimeRecordsList;
    }

}

