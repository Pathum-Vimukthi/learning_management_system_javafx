package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
import com.pathum.lms.DB.DbConnection;
import com.pathum.lms.model.Entroll;
import com.pathum.lms.model.Intake;
import com.pathum.lms.model.Program;
import com.pathum.lms.model.Student;
import com.pathum.lms.view.tm.EntrollTm;
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
import java.util.Optional;

public class RegistrationFormController {
    public AnchorPane context;
    public Button btnSave;
    public TextField txtStudentId;
    public ComboBox<String> cmbIntake;
    public RadioButton rbtnPaid;
    public RadioButton rbtnNonPaid;
    public ComboBox<String> cmbStudent;
    public TextField txtSearch;
    public ToggleGroup rbtnPayment;
    public TableView<EntrollTm> tblEntroll;
    public TableColumn<EntrollTm, String> colStudent;
    public TableColumn<EntrollTm, String> colIntake;
    public TableColumn<EntrollTm, String> colPaymentState;
    public TableColumn<EntrollTm, Button> colAction;
    String searchText = "";

    public void initialize() {
        colStudent.setCellValueFactory(new PropertyValueFactory<>("student"));
        colIntake.setCellValueFactory(new PropertyValueFactory<>("intake"));
        colPaymentState.setCellValueFactory(new PropertyValueFactory<>("paymentState"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setIntakeData();
        setStudentData(searchText);
        loadEntrollTableData();

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                searchText = newValue;
                setStudentData(searchText);
                cmbStudent.show();
            }
        });

        cmbStudent.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !newValue.equals("Select Student") && !newValue.equals("Student not found")) {
                setStudentId();
            }
        });

        tblEntroll.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           if(newValue != null) {
               setDataToForm((EntrollTm) newValue);
           }
        });
    }

    private void setDataToForm(EntrollTm newValue) {
        String studentId = newValue.getStudent();
        String[] splitedValue = studentId.split("-");

        txtStudentId.setText(splitedValue[0]+"-"+splitedValue[1]);
        cmbStudent.setValue(newValue.getStudent());
        cmbIntake.setValue(newValue.getIntake());
        if(newValue.getPaymentState().equals("Paid")) {
            rbtnPaid.setSelected(true);
        }else {
            rbtnNonPaid.setSelected(true);
        }
        btnSave.setText("Update");
    }

    private void loadEntrollTableData() {
        try {
            ObservableList<Entroll> entrollListDb = fetchEnrollmentData();
            ObservableList<EntrollTm> entrollList = FXCollections.observableArrayList();
            entrollList.clear();
            for(Entroll entrollItem : entrollListDb) {
                String state;
                if(entrollItem.isPaid()){
                    state = "Paid";
                }else {
                    state = "Not Paid";
                }
                Button btn = new Button("Delete");
                entrollList.add(new EntrollTm(
                        entrollItem.getStudent(),
                        entrollItem.getIntake(),
                        state,
                        btn
                ));
                btn.setOnAction((ActionEvent event) -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this entroll?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    try {
                        if(alert.getResult() == ButtonType.YES) {
                            boolean isDeleted = deleteEntrollment(entrollItem);
                            if(isDeleted) {
                                loadEntrollTableData();
                            }
                        }
                    }catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
            tblEntroll.setItems(entrollList);
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean deleteEntrollment(Entroll entrollItem) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM entroll WHERE student_id=? AND intake_id=?");
        ps.setString(1, splitedId(entrollItem.getStudent()));
        ps.setString(2, splitedId(entrollItem.getIntake()));
        return ps.executeUpdate() > 0;
    }

    private ObservableList<Entroll> fetchEnrollmentData() throws SQLException, ClassNotFoundException {
        ObservableList<Entroll> entrollList = FXCollections.observableArrayList();
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT s.id, s.name, i.id, i.name, e.isPaid FROM entroll e JOIN student s ON e.student_id = s.id JOIN intake i ON e.intake_id = i.id");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            entrollList.add(new Entroll(
                    rs.getString(1)+"-"+rs.getString(2),
                    rs.getString(3)+"-"+rs.getString(4),
                    rs.getBoolean(5)
            ));
        }
        return entrollList;
    }

    private void setStudentId() {
        if(cmbStudent.getValue() != null) {
            String studentId = cmbStudent.getValue();
            String[] splitedValue = studentId.split("-");
            txtStudentId.setText(splitedValue[0]+"-"+splitedValue[1]);
        }
    }

    private void setStudentData(String searchText) {
        try {
            ObservableList<String> studentsList = fetchStudents(searchText);
            if(!studentsList.isEmpty()) {
                cmbStudent.setItems(studentsList);
            }else {
                cmbStudent.setValue("Student not found");
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<String> fetchStudents(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<String> studentsList = FXCollections.observableArrayList();
        studentsList.clear();
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM student WHERE name LIKE ?");
        ps.setString(1, "%"+searchText+"%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            studentsList.add(
                    rs.getString(1)+"-"+rs.getString(2)
            );
        }
        return studentsList;
    }

    private void setIntakeData() {
        try {
            ObservableList<String> intakeList = fetchIntakeData();
            if(!intakeList.isEmpty()){
                cmbIntake.setItems(intakeList);
            }else{
                cmbIntake.setValue("Intake Not Found");
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<String> fetchIntakeData() throws SQLException, ClassNotFoundException {
        ObservableList<String> intakeList = FXCollections.observableArrayList();
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM intake");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            intakeList.add(rs.getString(1)+"-"+rs.getString(2));
        }
        return intakeList;
    }

    public void newRegistrationOnAction(ActionEvent actionEvent) {
        clearFields();
        loadEntrollTableData();
        setIntakeData();
    }

    public void backToDashboardOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void saveOnAction(ActionEvent actionEvent) {
        Entroll entroll = new Entroll(
                cmbStudent.getValue(),
                cmbIntake.getValue(),
                rbtnPaid.isSelected()
        );
        try {
            if(btnSave.getText().equals("Save")) {
                boolean isSaved = saveEntrllment(entroll);
                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Saved Successfully").show();
                    loadEntrollTableData();
                    clearFields();
                    setStudentData(searchText);
                    setIntakeData();
                }
            }else{
                boolean isUpdated = updateEntrollment(entroll);
                if(isUpdated) {
                    loadEntrollTableData();
                    clearFields();
                    btnSave.setText("Save");
                }
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private boolean updateEntrollment(Entroll entroll) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE entroll SET isPaid=? WHERE student_id=? AND intake_id=?");
        ps.setBoolean(1, entroll.isPaid());
        ps.setString(2, splitedId(entroll.getStudent()));
        ps.setString(3, splitedId(entroll.getIntake()));

        return ps.executeUpdate()>0;
    }

    private boolean saveEntrllment(Entroll entroll) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO entroll VALUES (?,?,?)");
        ps.setString(1,splitedId(entroll.getStudent()));
        ps.setString(2,splitedId(entroll.getIntake()));
        ps.setBoolean(3,entroll.isPaid());
        return ps.executeUpdate() > 0;
    }

    private void clearFields() {
        txtStudentId.clear();
        txtSearch.clear();
        rbtnPaid.setSelected(false);
        rbtnNonPaid.setSelected(false);
        cmbStudent.setValue("Select Student");
        cmbIntake.setValue("Select Intake");
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/pathum/lms/view/"+location+".fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(scene);
    }

    private String splitedId(String value) {
        String[] splitedTeacherId = value.split("-");
        return splitedTeacherId[0].trim()+"-"+splitedTeacherId[1].trim();
    }
}
