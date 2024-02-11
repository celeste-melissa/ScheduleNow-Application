module com.software.schedulenow {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    opens com.software.schedulenow.model to javafx.base;
    opens com.software.schedulenow.controller to javafx.fxml;
    exports com.software.schedulenow;
}