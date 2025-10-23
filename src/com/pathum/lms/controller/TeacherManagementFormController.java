package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
import com.pathum.lms.model.Teacher;
import com.pathum.lms.view.tm.TeacherTm;
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
import java.util.Optional;

public class TeacherManagementFormController {
    public AnchorPane context;
    public TextField txtTeacherId;
    public TextField txtTeacherName;
    public TextField txtContactNumber;
    public TextField txtAddress;
    public TextField txtSearch;
    public Button btnSave;
    public TableView<TeacherTm> tblTeacher;
    public TableColumn<TeacherTm, String> colId;
    public TableColumn<TeacherTm, String> colName;
    public TableColumn<TeacherTm, String> colContactNumber;
    public TableColumn<TeacherTm, String> colAddress;
    public TableColumn<TeacherTm, Button> colAction;
    String searchText = "";

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setTeacherId();
        setTableData(searchText);

        tblTeacher.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setData((TeacherTm) newValue);
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
           this.searchText = newValue;
           setTableData(searchText);
        });
    }

    private void setData(TeacherTm newValue) {
        txtTeacherId.setText(newValue.getId());
        txtTeacherName.setText(newValue.getName());
        txtContactNumber.setText(newValue.getContactNumber());
        txtAddress.setText(newValue.getAddress());
        btnSave.setText("Update");
    }

    private void setTableData(String searchText) {
        ObservableList<TeacherTm> teacherTms = FXCollections.observableArrayList();
        for(Teacher teacher:Database.teacherTable){
            if(teacher.getName().toLowerCase().contains(searchText.toLowerCase())){
                Button btn = new Button("Delete");
                TeacherTm teacherTm = new TeacherTm(
                        teacher.getId(),
                        teacher.getName(),
                        teacher.getContactNumber(),
                        teacher.getAddress(),
                        btn
                );
                btn.setOnAction((ActionEvent event) -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this teacher?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if(alert.getResult() == ButtonType.YES){
                        Database.teacherTable.remove(teacher);
                        new Alert(Alert.AlertType.INFORMATION, "Teacher has been deleted").show();
                        setTableData(searchText);
                        setTeacherId();
                    }
                });
                teacherTms.add(teacherTm);
            }
        }
        tblTeacher.setItems(teacherTms);
    }

    private void setTeacherId() {
        if(!Database.teacherTable.isEmpty()){
            Teacher lastTeacher = Database.teacherTable.get(Database.teacherTable.size() - 1);
            String lastTeacherId = lastTeacher.getId();
            String[] splitData = lastTeacherId.split("-");
            String lastCharacter = splitData[1];
            int lastDigit = Integer.parseInt(lastCharacter);
            lastDigit++;
            String generatedId = "T-"+lastDigit;
            txtTeacherId.setText(generatedId);
        }else{
            txtTeacherId.setText("T-1");
        }
    }

    public void saveOnAction(ActionEvent actionEvent) {
        Teacher teacher = new Teacher(
                txtTeacherId.getText(),
                txtTeacherName.getText(),
                txtContactNumber.getText(),
                txtAddress.getText()
        );
        if(btnSave.getText().equals("Save")){
            Database.teacherTable.add(teacher);
            setTeacherId();
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Teacher Saved!").show();
            setTableData(searchText);
        }else{
            Optional<Teacher> selectedTeacher = Database.teacherTable.stream().filter(t -> t.getId().equals(txtTeacherId.getText())).findFirst();
            if(selectedTeacher.isPresent()){
                selectedTeacher.get().setName(txtTeacherName.getText());
                selectedTeacher.get().setContactNumber(txtContactNumber.getText());
                selectedTeacher.get().setAddress(txtAddress.getText());
                new Alert(Alert.AlertType.INFORMATION, "Teacher Updated!").show();
                setTeacherId();
                clearFields();
                setTableData(searchText);
                btnSave.setText("Save");
            }
        }
    }

    public void newTeacherOnAction(ActionEvent actionEvent) {
        clearFields();
        setTeacherId();
    }

    private void clearFields() {
        txtTeacherName.clear();
        txtContactNumber.clear();
        txtAddress.clear();
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
