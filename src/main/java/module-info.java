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
    exports by.stormnet.projectjavafx.models;
    opens by.stormnet.projectjavafx.models to javafx.fxml;
    exports by.stormnet.projectjavafx.service;
    opens by.stormnet.projectjavafx.service to javafx.fxml;
    exports by.stormnet.projectjavafx.controllers;
    opens by.stormnet.projectjavafx.controllers to javafx.fxml;
}