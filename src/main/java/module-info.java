module by.stormnet.projectjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires poi.ooxml;
    requires poi;

    opens by.stormnet.projectjavafx to javafx.fxml;
    exports by.stormnet.projectjavafx;
}