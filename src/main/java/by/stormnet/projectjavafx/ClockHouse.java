package by.stormnet.projectjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ClockHouse extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClockHouse.class.getResource("ClockHouse.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 335, 537);
        stage.setTitle("Отчет по проходной");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}