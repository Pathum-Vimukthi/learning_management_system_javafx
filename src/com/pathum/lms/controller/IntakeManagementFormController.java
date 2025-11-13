package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
import com.pathum.lms.DB.DbConnection;
import com.pathum.lms.model.Intake;
import com.pathum.lms.view.tm.IntakeTm;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class IntakeManagementFormController {
    public AnchorPane context;
    public TextField txtIntakeId;
    public TextField txtIntakeName;
    public DatePicker dteStartDate;
    public ComboBox<String> cmbIntakeProgram;
    public TextField txtSearch;
    public Button btnSave;
    public TableView<IntakeTm> tblIntake;
    public TableColumn<IntakeTm, String> colIntakeId;
    public TableColumn<IntakeTm, String> colIntakeName;
    public TableColumn<IntakeTm, Date> colStartDate;
    public TableColumn<IntakeTm, String> colIntakeProgram;
    public TableColumn<IntakeTm, Button> colAction;
    private String searchText = "";

    public void initialize() {
        colIntakeId.setCellValueFactory(new PropertyValueFactory<>("intakeId"));
        colIntakeName.setCellValueFactory(new PropertyValueFactory<>("intakeName"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("intakeDate"));
        colIntakeProgram.setCellValueFactory(new PropertyValueFactory<>("program"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
        setIntakeId();
        setProgramData();
        loadIntakeTableData(searchText);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            loadIntakeTableData(searchText);
        });
        tblIntake.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           if (newValue != null) {
               setDataToForm((IntakeTm) newValue);
           }
        });
    }

    private void setDataToForm(IntakeTm newValue) {
        txtIntakeId.setText(newValue.getIntakeId());
        txtIntakeName.setText(newValue.getIntakeName());
        dteStartDate.setValue(LocalDate.parse(newValue.getIntakeDate().toString()));
        cmbIntakeProgram.setValue(newValue.getProgram());
        btnSave.setText("Update");
    }

    private void loadIntakeTableData(String searchText) {
        try {
            ObservableList<Intake> intakeListDb = fetchIntakeData(searchText);
            ObservableList<IntakeTm> intakeList = FXCollections.observableArrayList();
            intakeList.clear();
            for(Intake intake:intakeListDb){
                    Button btn = new Button("Delete");
                    intakeList.add(new IntakeTm(
                            intake.getIntakeId(),
                            intake.getStartDate(),
                            intake.getIntakeName(),
                            intake.getProgram(),
                            btn
                    ));
                    btn.setOnAction(event -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this intake?", ButtonType.YES, ButtonType.NO);
                        alert.showAndWait();
                        try {
                            if (alert.getResult() == ButtonType.YES) {
                                boolean isDeleted = deleteIntake(intake);
                                if (isDeleted) {
                                    loadIntakeTableData(searchText);
                                    setIntakeId();
                                }
                            }
                        }catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
                }
            tblIntake.setItems(intakeList);
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private boolean deleteIntake(Intake intake) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM intake WHERE id = ?");
        ps.setString(1, intake.getIntakeId());
        return ps.executeUpdate() > 0;
    }

    private ObservableList<Intake> fetchIntakeData(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<Intake> intakeList = FXCollections.observableArrayList();
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT i.id, i.name, i.date, p.id, p.name FROM intake i JOIN program p ON p.id=i.program_id WHERE i.name LIKE ?");
        ps.setString(1, "%" + searchText + "%");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            intakeList.add(new Intake(
                    rs.getString(1),
                    rs.getDate(3),
                    rs.getString(2),
                    rs.getString(4)+"-"+rs.getString(5)
            ));
        }
        return intakeList;
    }

    private void setProgramData() {
        try {
            ObservableList<String> programsList = fetchProgramData();
            cmbIntakeProgram.setItems(programsList);
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<String> fetchProgramData() throws SQLException, ClassNotFoundException {
        ObservableList<String> programsList = FXCollections.observableArrayList();
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM program");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            programsList.add(rs.getString(1)+"-"+rs.getString(2));
        }
        return programsList;
    }

    private void setIntakeId() {
        try {
            String lastIntakeId = fetchLastIntakeId();
            if(lastIntakeId != null) {
                String[] slitedIntakeId = lastIntakeId.split("-");
                int lastDigit = Integer.parseInt(slitedIntakeId[1]);
                lastDigit++;
                txtIntakeId.setText("I-"+lastDigit);
            }else{
                txtIntakeId.setText("I-1");
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String fetchLastIntakeId() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT id FROM intake ORDER BY CAST(SUBSTRING(id,3)AS UNSIGNED)DESC LIMIT 1");
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    public void newIntakeOnAction(ActionEvent actionEvent) {
        clearFields();
        loadIntakeTableData(searchText);
        setIntakeId();
        setProgramData();
    }

    public void backToDashboardOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void saveOnAction(ActionEvent actionEvent) {
        Intake intake = new Intake(
                txtIntakeId.getText(),
                Date.from(dteStartDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                txtIntakeName.getText(),
                cmbIntakeProgram.getValue()
        );
        try {
            if(btnSave.getText().equals("Save")) {
                boolean isSaved = saveIntake(intake);
                if(isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Intake Saved!").show();
                    setIntakeId();
                    setProgramData();
                    clearFields();
                    loadIntakeTableData(searchText);
                }
            }else {
                boolean isUpdated = updateIntake(intake);
                if(isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Intake Updated!").show();
                    setIntakeId();
                    loadIntakeTableData(searchText);
                    clearFields();
                    btnSave.setText("Save");
                }
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private boolean updateIntake(Intake intake) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE intake SET name=?, date=?, program_id=? WHERE id=?");
        ps.setString(1, intake.getIntakeName());
        ps.setObject(2,intake.getStartDate());
        ps.setString(3, splitedId(intake.getProgram()));
        ps.setString(4, splitedId(intake.getIntakeId()));

        return ps.executeUpdate() > 0;
    }

    private boolean saveIntake(Intake intake) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO intake VALUES (?,?,?,?)");
        ps.setString(1, intake.getIntakeId());
        ps.setString(2, intake.getIntakeName());
        ps.setObject(3, intake.getStartDate());
        ps.setString(4, splitedId(intake.getProgram()));
        return ps.executeUpdate() > 0;
    }

    private String splitedId(String value) {
        String[] splitedTeacherId = value.split("-");
        return splitedTeacherId[0].trim()+"-"+splitedTeacherId[1].trim();
    }

    private void clearFields() {
        txtIntakeName.clear();
        dteStartDate.setValue(null);
        cmbIntakeProgram.setValue(null);
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/pathum/lms/view/"+location+".fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(scene);
    }
}
