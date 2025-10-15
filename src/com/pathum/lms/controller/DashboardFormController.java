package com.pathum.lms.controller;

import com.pathum.lms.env.StaticResource;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardFormController {
    public AnchorPane context;
    public Label lblCompany;
    public Label lblVersion;
    public Label lblDate;
    public Label lblTime;

    public void initialize() {
        setStaticData();
    }

    private void setStaticData() {
        lblCompany.setText(StaticResource.getCompany());
        lblVersion.setText(StaticResource.getVersion());
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        lblDate.setText(dateFormat + " " + dateFormat);

        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            String timeFormat = new SimpleDateFormat("HH:mm:ss").format(new Date());
            lblTime.setText(timeFormat + " " + timeFormat);
        }));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/pathum/lms/view/"+location+".fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(scene);
    }

    public void logoutOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoginForm");
    }

    public void studentManageOnAction(ActionEvent actionEvent) throws IOException {
        setUi("StudentManagementForm");
    }
}
