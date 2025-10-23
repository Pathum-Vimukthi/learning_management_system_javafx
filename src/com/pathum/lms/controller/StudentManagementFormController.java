package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
import com.pathum.lms.model.Student;
import com.pathum.lms.view.tm.StudentTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class StudentManagementFormController {
    public AnchorPane context;
    public TextField txtStudentId;
    public TextField txtStudentName;
    public TextField txtAddress;
    public DatePicker dteDOB;
    public TextField txtSearch;
    public Button btnSave;
    public TableView<StudentTm> tblStudent;
    public TableColumn<StudentTm, String> colId;
    public TableColumn<StudentTm, String> colName;
    public TableColumn<StudentTm, String> colAddress;
    public TableColumn<StudentTm, Date> colDob;
    public TableColumn<StudentTm, Button> colAction;
    String searchText = "";

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setStudentId();
        setTableData(searchText);

        tblStudent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setData((StudentTm) newValue);
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            this.searchText = newValue;
            setTableData(searchText);
        });
    }

    private void setData(StudentTm newValue) {
        txtStudentId.setText(newValue.getId());
        txtStudentName.setText(newValue.getName());
        txtAddress.setText(newValue.getAddress());
        dteDOB.setValue(LocalDate.parse(newValue.getDob()));
        btnSave.setText("Update");
    }

    private void setTableData(String searchText) {
        ObservableList<StudentTm> studentTms = FXCollections.observableArrayList();
        for(Student student:Database.studentTable){
            if(student.getStudentName().toLowerCase().contains(searchText.toLowerCase())){
                Button btn = new Button("Delete");
                StudentTm studentTm = new StudentTm(
                        student.getStudentID(),
                        student.getStudentName(),
                        student.getStudentAddress(),
                        new SimpleDateFormat("yyyy-MM-dd").format(student.getDob()),
                        btn
                );
                btn.setOnAction((ActionEvent event) -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this student?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        Database.studentTable.remove(student);
                        new Alert(Alert.AlertType.INFORMATION, "Student deleted successfully").show();
                        setTableData(searchText);
                        setStudentId();
                    }
                });
                studentTms.add(studentTm);
            }

        }
        tblStudent.setItems(studentTms);
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
        if(btnSave.getText().equals("Save")){
            Database.studentTable.add(student);
            setStudentId();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Student Saved!").show();
            setTableData(searchText);
        }else {
            Optional<Student> selectedStudent = Database.studentTable.stream().filter(e->e.getStudentID().equals(txtStudentId.getText())).findFirst();
            if(selectedStudent.isPresent()){
                selectedStudent.get().setStudentName(txtStudentName.getText());
                selectedStudent.get().setStudentAddress(txtAddress.getText());
                selectedStudent.get().setDob(Date.from(dteDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                new Alert(Alert.AlertType.INFORMATION, "Student Updated!").show();
                setStudentId();
                clearFields();
                setTableData(searchText);
                btnSave.setText("Save");
            }
        }
    }

    private void clearFields() {
        txtStudentName.clear();
        txtAddress.clear();
        dteDOB.setValue(null);
    }

    public void newStudentOnAction(ActionEvent actionEvent) {
        clearFields();
        setStudentId();
    }

    public void backToDashboardOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/pathum/lms/view/"+location+".fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(scene);
    }
}
