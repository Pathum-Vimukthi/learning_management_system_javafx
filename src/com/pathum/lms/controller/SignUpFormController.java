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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

        User user = new User(fullName, email, password, age);

        try{
            signUp(user);
            new Alert(Alert.AlertType.INFORMATION, "Sign up successful!").show();
            setUi("LoginForm");
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    private boolean signUp(User user) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lms_db", "root", "1234");
        String sql = "INSERT INTO user VALUES (?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getFullName());
        ps.setInt(3, user.getAge());
        ps.setString(4, user.getPassword());

        return ps.executeUpdate() > 0;
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
