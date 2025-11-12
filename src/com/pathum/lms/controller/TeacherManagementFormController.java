package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
import com.pathum.lms.DB.DbConnection;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        try {
            ArrayList<Teacher> teachers = fetchTeachers(searchText);
            ObservableList<TeacherTm> teacherTms = FXCollections.observableArrayList();
            for(Teacher teacher:teachers){
                Button btn = new Button("Delete");
                TeacherTm teacherTm = new TeacherTm(
                        teacher.getId(),
                        teacher.getName(),
                        teacher.getContactNumber(),
                        teacher.getAddress(),
                        btn
                );
                btn.setOnAction((ActionEvent event) -> {
                    try{
                        boolean isDeleted = deleteTeacher(teacher.getId());
                        if(isDeleted){
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this teacher?", ButtonType.YES, ButtonType.NO);
                            alert.showAndWait();
                            if(alert.getResult() == ButtonType.YES){
                                Database.teacherTable.remove(teacher);
                                new Alert(Alert.AlertType.INFORMATION, "Teacher has been deleted").show();
                                setTableData(searchText);
                                setTeacherId();
                            }
                        }
                    }catch (SQLException | ClassNotFoundException e){
                        e.printStackTrace();
                    }
                });
                teacherTms.add(teacherTm);
            }
            tblTeacher.setItems(teacherTms);
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean deleteTeacher(String id) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM teacher WHERE id = ?");
        ps.setString(1, id);
        return ps.executeUpdate() > 0;
    }

    private ArrayList<Teacher> fetchTeachers(String searchText) throws SQLException, ClassNotFoundException {
        ArrayList<Teacher> teachers = new ArrayList<>();
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM teacher WHERE name LIKE ?");
        ps.setString(1, "%" + searchText + "%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            teachers.add(new Teacher(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
            ));
        }
        return teachers;
    }

    private void setTeacherId() {
        try{
            String lastTeacherId = getLastTeacherId();
            if(lastTeacherId != null){
                String[] splitData = lastTeacherId.split("-");
                String lastCharacter = splitData[1];
                int lastDigit = Integer.parseInt(lastCharacter);
                lastDigit++;
                String generatedId = "T-"+lastDigit;
                txtTeacherId.setText(generatedId);
            }else{
                txtTeacherId.setText("T-1");
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private String getLastTeacherId() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM teacher ORDER BY CAST(SUBSTRING(id,3)AS UNSIGNED)DESC LIMIT 1");
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return rs.getString("id");
        }
        return null;
    }

    public void saveOnAction(ActionEvent actionEvent) {
        Teacher teacher = new Teacher(
                txtTeacherId.getText(),
                txtTeacherName.getText(),
                txtContactNumber.getText(),
                txtAddress.getText()
        );
        try {
            if(btnSave.getText().equals("Save")){
                boolean isSaved = saveTeacher(teacher);
                if(isSaved){
                    setTeacherId();
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Teacher Saved!").show();
                    setTableData(searchText);
                }
            }else{
                boolean isUpdated = updateTeacher(teacher);
                if(isUpdated){
                    new Alert(Alert.AlertType.INFORMATION, "Teacher Updated!").show();
                    setTeacherId();
                    clearFields();
                    setTableData(searchText);
                    btnSave.setText("Save");
                }
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private boolean updateTeacher(Teacher teacher) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE teacher SET name=?, contact=?, address=? WHERE id=?");
        ps.setString(1, teacher.getName());
        ps.setString(2, teacher.getContactNumber());
        ps.setString(3, teacher.getAddress());
        ps.setString(4, teacher.getId());
        return ps.executeUpdate() > 0;
    }

    private boolean saveTeacher(Teacher teacher) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO teacher VALUES(?,?,?,?)");
        ps.setString(1, teacher.getId());
        ps.setString(2, teacher.getName());
        ps.setString(3, teacher.getContactNumber());
        ps.setString(4, teacher.getAddress());

        return ps.executeUpdate() > 0;
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
