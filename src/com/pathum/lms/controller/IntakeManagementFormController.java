package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
import com.pathum.lms.model.Intake;
import com.pathum.lms.model.Program;
import com.pathum.lms.model.Teacher;
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
        dteStartDate.setValue(newValue.getIntakeDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        cmbIntakeProgram.setValue(newValue.getProgram());
        btnSave.setText("Update");
    }

    private void loadIntakeTableData(String searchText) {
        ObservableList<IntakeTm> intakeList = FXCollections.observableArrayList();
        intakeList.clear();
        for(Intake intake:Database.intakeTable){
            if(intake.getIntakeName().contains(searchText)){
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
                    if (alert.getResult() == ButtonType.YES) {
                        Database.intakeTable.remove(intake);
                        loadIntakeTableData(searchText);
                        setIntakeId();
                    }
                });
            }
        }
        tblIntake.setItems(intakeList);
    }

    private void setProgramData() {
        ObservableList<String> programsList = FXCollections.observableArrayList();
        for(Program program:Database.programTable){
            programsList.add(program.getProgramId()+"-"+program.getProgramName());
        }
        cmbIntakeProgram.setItems(programsList);
    }

    private void setIntakeId() {
        if(!Database.intakeTable.isEmpty()) {
            Intake lastIntake = Database.intakeTable.get(Database.intakeTable.size()-1);
            String intakeId = lastIntake.getIntakeId();
            String[] slitedIntakeId = intakeId.split("-");
            int lastDigit = Integer.parseInt(slitedIntakeId[1]);
            lastDigit++;
            txtIntakeId.setText("I-"+lastDigit);
        }else{
            txtIntakeId.setText("I-1");
        }
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
        if(btnSave.getText().equals("Save")) {
            Database.intakeTable.add(new Intake(
                    txtIntakeId.getText(),
                    Date.from(dteStartDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    txtIntakeName.getText(),
                    cmbIntakeProgram.getValue()
            ));
            new Alert(Alert.AlertType.INFORMATION, "Intake Saved!").show();
            setIntakeId();
            setProgramData();
            clearFields();
            loadIntakeTableData(searchText);
        }else {
            Optional<Intake> selectedIntake = Database.intakeTable.stream().filter(i -> i.getIntakeId().equals(txtIntakeId.getText())).findFirst();
            if(selectedIntake.isPresent()) {
                selectedIntake.get().setIntakeId(txtIntakeId.getText());
                selectedIntake.get().setIntakeName(txtIntakeName.getText());
                selectedIntake.get().setProgram(cmbIntakeProgram.getValue());
                selectedIntake.get().setStartDate(Date.from(dteStartDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                new Alert(Alert.AlertType.INFORMATION, "Intake Updated!").show();
                setIntakeId();
                loadIntakeTableData(searchText);
                clearFields();
                btnSave.setText("Save");
            }
        }
    }

    private void clearFields() {
        txtIntakeName.clear();
        dteStartDate.setValue(null);
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/pathum/lms/view/"+location+".fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(scene);
    }
}
