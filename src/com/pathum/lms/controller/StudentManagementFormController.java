package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
import com.pathum.lms.model.Student;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.time.ZoneId;
import java.util.Date;

public class StudentManagementFormController {
    public AnchorPane context;
    public TextField txtStudentId;
    public TextField txtStudentName;
    public TextField txtAddress;
    public DatePicker dteDOB;
    public TextField txtSearch;
    public Button btnSave;
    
    public void initialize() {
        setStudentId();
    }

    private void setStudentId() {
        if(!Database.studentTable.isEmpty()){
            Student lastStudent = Database.studentTable.get(Database.studentTable.size()-1);
            String lastStudentId = lastStudent.getStudentID();
            String[] splitData = lastStudentId.split("-");
            String lastCharacter = splitData[1];
            int lastDigit = Integer.parseInt(lastCharacter);
            lastDigit++;
            String generatedId = "S-"+lastDigit;
            txtStudentId.setText(generatedId);
        }else{
            txtStudentId.setText("S-1");
        }
    }

    public void saveOnAction(ActionEvent actionEvent) {
        Student student = new Student(
                txtStudentId.getText(),
                txtStudentName.getText(),
                txtAddress.getText(),
                Date.from(dteDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
        Database.studentTable.add(student);
        setStudentId();
        clearFields();
        new Alert(Alert.AlertType.INFORMATION, "Student Saved!").show();
    }

    private void clearFields() {
        txtStudentName.clear();
        txtAddress.clear();
        dteDOB.setValue(null);
    }
}
