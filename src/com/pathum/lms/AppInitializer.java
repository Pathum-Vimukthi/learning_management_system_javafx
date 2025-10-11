package com.pathum.lms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class AppInitializer extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL resource = getClass().getResource("/com/pathum/lms/view/WelcomeScreenForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        stage.setScene(scene);
        stage.setTitle("Welcome to the LMS");
        stage.show();
    }
}
