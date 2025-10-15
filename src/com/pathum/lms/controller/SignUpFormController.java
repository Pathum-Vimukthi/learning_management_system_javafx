package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
import com.pathum.lms.env.StaticResource;
import com.pathum.lms.model.User;
import com.pathum.lms.utils.security.PasswordManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SignUpFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;
    public TextField txtFullName;
    public PasswordField txtPassword;
    public TextField txtEmail;
    public TextField txtAge;

    public void initialize() {
        setStaticData();
    }

    private void setStaticData() {
        lblCompany.setText(StaticResource.getCompany());
        lblVersion.setText(StaticResource.getVersion());
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("WelcomeScreenForm");
    }

    public void signUpOnAction(ActionEvent actionEvent) throws IOException {

        String fullName = txtFullName.getText();
        String password = new PasswordManager().encodePassword(txtPassword.getText());
        String email = txtEmail.getText();
        int age = Integer.parseInt(txtAge.getText());

        boolean emailExists = Database.userTable.stream().anyMatch(user -> user.getEmail().equals(email));
        if (emailExists) {
            new Alert(Alert.AlertType.ERROR, "This email already exists!").show();
        }
        Database.userTable.add(new User(fullName, email, password, age));
        new Alert(Alert.AlertType.INFORMATION, "Sign up successful!").show();
        setUi("LoginForm");
    }

    public void alreadyHaveAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoginForm");
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/pathum/lms/view/"+location+".fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(scene);
    }
}
