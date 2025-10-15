package com.pathum.lms.controller;

import com.pathum.lms.env.StaticResource;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class VerifyOTPFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;
    public Label lblEmail;
    public TextField txtOTP;
    private String email;
    private int OTP;

    public void initialize() {
        setStaticData();
    }

    private void setStaticData() {
        lblCompany.setText(StaticResource.getCompany());
        lblVersion.setText(StaticResource.getVersion());
        lblEmail.setText(email);
    }

    public void navigateEmailVerificationFormOnAction(ActionEvent actionEvent) throws IOException {
        setUi("EmailVerificationForm");
    }

    public void setVerificationData(int verificationCode, String email){
        this.email = email;
        this.OTP = verificationCode;
        
    }

    public void navigatePasswordResetOnAction(ActionEvent actionEvent) throws IOException {
        if(OTP==Integer.parseInt(txtOTP.getText())){
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pathum/lms/view/ResetPasswordForm.fxml"));
           Parent load = loader.load();
           ResetPasswordFormController controller = loader.getController();
           controller.setUserData(email);
           Stage stage = (Stage) context.getScene().getWindow();
           stage.setScene(new Scene(load));
        }else {
            new Alert(Alert.AlertType.ERROR, "Invalid OTP", ButtonType.OK).show();
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
