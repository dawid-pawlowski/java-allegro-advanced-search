module api.allegro.app {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
	requires java.net.http;
    requires org.json;
    requires jakarta.persistence;
    requires java.sql;

    opens api.allegro.app to javafx.fxml;
    opens api.allegro.controller to javafx.fxml;
    exports api.allegro.app;
    exports api.allegro.controller;
    exports api.allegro.entity;
    exports api.allegro.converter;
    exports api.allegro.bean;
    exports api.allegro.exception;
    exports api.allegro.enums;
}
