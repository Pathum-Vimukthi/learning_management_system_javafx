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
import java.util.Optional;

public class LoginFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;
    public TextField txtEmail;
    public PasswordField txtPassword;

    public void initialize() {
        setStaticData();
    }

    private void setStaticData() {
        lblCompany.setText(StaticResource.getCompany());
        lblVersion.setText(StaticResource.getVersion());
    }

    public void navigateDashboardOnAction(ActionEvent actionEvent) throws IOException {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        Optional<User> selectUser = Database.userTable.stream().filter(user -> user.getEmail().equals(email)).findFirst();
        if (selectUser.isPresent()) {
            if(new PasswordManager().checkPassword(password, selectUser.get().getPassword())) {
                new Alert(Alert.AlertType.INFORMATION,"Welcome").show();
                setUi("DashboardForm");
            }else {
                new Alert(Alert.AlertType.ERROR,"Invalid Password").show();
            }
        }else {
            new Alert(Alert.AlertType.ERROR,"User not found").show();
        }
    }

    public void navigateForgotPasswordOnAction(ActionEvent actionEvent) throws IOException {
        setUi("EmailVerificationForm");
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("WelcomeScreenForm");
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/pathum/lms/view/"+location+".fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(scene);
    }
}
