package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
import com.pathum.lms.DB.DbConnection;
import com.pathum.lms.bo.BoFactory;
import com.pathum.lms.bo.custom.impl.StudentBoImpl;
import com.pathum.lms.dto.request.RequestStudentDto;
import com.pathum.lms.dto.response.ResponseStudentDto;
import com.pathum.lms.model.Student;
import com.pathum.lms.utils.BoType;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    String userEmail;
    StudentBoImpl studentBo = BoFactory.getInstance().getBo(BoType.STUDENT);

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
        try{
            List<ResponseStudentDto> studentDtos = studentBo.getStudents(searchText);
            ObservableList<StudentTm> studentTms = FXCollections.observableArrayList();
            for (ResponseStudentDto studentDto : studentDtos) {
                Button btn = new Button("Delete");
                studentTms.add(new StudentTm(
                        studentDto.getId(),
                        studentDto.getName(),
                        studentDto.getAddress(),
                        studentDto.getDob(),
                        btn
                ));
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this student?",ButtonType.YES,ButtonType.NO);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Delete Student");
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        try {
                            boolean isDeleted = studentBo.deleteStudent(studentDto.getId());
                            if (isDeleted) {
                                new Alert(Alert.AlertType.INFORMATION,"Student Deleted").show();
                                setTableData(searchText);
                                setStudentId();
                            }
                        } catch (SQLException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
            tblStudent.setItems(studentTms);
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setStudentId() {
        try{
            String lastStudentId = getLastStudent();

            if(lastStudentId != null){
                String[] splitted = lastStudentId.split("-");
                String lastCharAsString = splitted[1];
                int lastDigit = Integer.parseInt(lastCharAsString);
                lastDigit++;
                String generatedId = "S-"+lastDigit;
                txtStudentId.setText(generatedId);
            }else{
                txtStudentId.setText("S-1");
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private String getLastStudent() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT id FROM student ORDER BY CAST(SUBSTRING(id,3)AS UNSIGNED) DESC LIMIT 1");
        ResultSet set = statement.executeQuery();
        if(set.next()){
            return set.getString(1);
        }else {
            return null;
        }
    }

    public void saveOnAction(ActionEvent actionEvent) {
        try{
            if(btnSave.getText().equals("Save")){
                boolean isSaved = studentBo.saveStudent(new RequestStudentDto(
                        txtStudentId.getText(),
                        txtStudentName.getText(),
                        txtAddress.getText(),
                        Date.from(dteDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
                ));
                if(isSaved){
                    setStudentId();
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Student Saved!").show();
                    setTableData(searchText);
                }
            }else {
                boolean isUpdated = studentBo.updateStudent(new RequestStudentDto(
                        txtStudentId.getText(),
                        txtStudentName.getText(),
                        txtAddress.getText(),
                        Date.from(dteDOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())
                ));
                if(isUpdated){
                    new Alert(Alert.AlertType.INFORMATION, "Student Updated!").show();
                    setStudentId();
                    clearFields();
                    setTableData(searchText);
                    btnSave.setText("Save");
                }
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private boolean updateStudent(Student student, String userEmail) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE student SET name=?, address=?, dob=?, user_email=? WHERE id=?");
        ps.setString(1, student.getStudentName());
        ps.setString(2, student.getStudentAddress());
        ps.setObject(3, student.getDob());
        ps.setString(4, userEmail);
        ps.setString(5, student.getStudentID());

        return ps.executeUpdate()>0;
    }

    private void clearFields() {
        txtStudentName.clear();
        txtAddress.clear();
        dteDOB.setValue(null);
        setTableData(searchText);
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

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
