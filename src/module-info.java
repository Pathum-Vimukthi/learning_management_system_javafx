module lms {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;

    opens com.pathum.lms to javafx.fxml, javafx.graphics, javafx.controls;
    exports com.pathum.lms.controller;
}