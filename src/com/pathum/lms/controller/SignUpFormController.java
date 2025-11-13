package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
import com.pathum.lms.DB.DbConnection;
import com.pathum.lms.bo.BoFactory;
import com.pathum.lms.bo.custom.UserBo;
import com.pathum.lms.bo.custom.impl.UserBoImpl;
import com.pathum.lms.dto.request.RequestUserDto;
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
import java.sql.SQLException;

public class SignUpFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;
    public TextField txtFullName;
    public PasswordField txtPassword;
    public TextField txtEmail;
    public TextField txtAge;
    UserBoImpl user = BoFactory.getInstance().getBo(BoType.USER);

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
        String password = txtPassword.getText();
        String email = txtEmail.getText();
        int age = Integer.parseInt(txtAge.getText());

        try {
            boolean isSaved = user.registerUser(new RequestUserDto(
               email,
               fullName,
               age,
               password
            ));
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Sign up successful!").show();
                setUi("LoginForm");
            }else {
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
