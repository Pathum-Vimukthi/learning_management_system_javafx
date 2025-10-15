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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class ResetPasswordFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;
    public PasswordField txtNewPassword;
    public PasswordField txtConfirmPassword;
    private String email;

    public void initialize() {
        setStaticData();
    }

    public void setUserData(String email) {
        this.email = email;
    }

    private void setStaticData() {
        lblCompany.setText(StaticResource.getCompany());
        lblVersion.setText(StaticResource.getVersion());
    }

    public void navigateVerifyOTPFormOnAction(ActionEvent actionEvent) throws IOException {
        setUi("VerifyOTPForm");
    }

    public void resetPasswordOnAction(ActionEvent actionEvent) throws IOException {
        Optional<User> selectedUser = Database.userTable.stream().filter(user ->
            user.getEmail().equals(email)
        ).findFirst();

        if (selectedUser.isPresent()) {
            if(txtNewPassword.getText().trim().equals(txtConfirmPassword.getText().trim())) {
                selectedUser.get().setPassword(new PasswordManager().encodePassword(txtNewPassword.getText().trim()));
                new Alert(Alert.AlertType.INFORMATION, "Password Reset Successful").show();
                setUi("LoginForm");
            }else{
                new Alert(Alert.AlertType.ERROR, "Password doesn't matched").show();
            }
        }
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/pathum/lms/view/"+location+".fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(scene);
    }
}
