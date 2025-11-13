package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
import com.pathum.lms.DB.DbConnection;
import com.pathum.lms.bo.BoFactory;
import com.pathum.lms.bo.custom.impl.UserBoImpl;
import com.pathum.lms.dto.response.ResponseUserDto;
import com.pathum.lms.env.StaticResource;
import com.pathum.lms.model.User;
import com.pathum.lms.utils.BoType;
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
import java.sql.*;
import java.util.Optional;

public class LoginFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;
    public TextField txtEmail;
    public PasswordField txtPassword;
    UserBoImpl user = BoFactory.getInstance().getBo(BoType.USER);

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

        try{
            ResponseUserDto loginState = user.loginUser(email, password);
            if(loginState!=null){
                if(loginState.getStatusCode()==200){
                    new Alert(Alert.AlertType.INFORMATION,loginState.getMessage()).show();
                    setUi("DashboardForm");
                }else if(loginState.getStatusCode()==401){
                    new Alert(Alert.AlertType.INFORMATION,loginState.getMessage()).show();
                } else if (loginState.getStatusCode()==404) {
                    new Alert(Alert.AlertType.INFORMATION,loginState.getMessage()).show();
                }
            }else {
                new Alert(Alert.AlertType.INFORMATION,"Something went wrong!").show();
            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
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
