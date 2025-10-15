module lms {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires jbcrypt;
//    requires mail;
    requires jakarta.mail;
    requires jakarta.activation;

    opens com.pathum.lms to javafx.fxml, javafx.graphics, javafx.controls, jakarta.activation, jakarta.mail;
    exports com.pathum.lms.controller;
}