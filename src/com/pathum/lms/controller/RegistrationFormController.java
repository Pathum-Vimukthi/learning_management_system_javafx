package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
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
    public TableColumn<EntrollTm, String> colEntrollId;
    public TextField txtEntrollId;
    String searchText = "";

    public void initialize() {
        colEntrollId.setCellValueFactory(new PropertyValueFactory<>("entrollId"));
        colStudent.setCellValueFactory(new PropertyValueFactory<>("student"));
        colIntake.setCellValueFactory(new PropertyValueFactory<>("intake"));
        colPaymentState.setCellValueFactory(new PropertyValueFactory<>("paymentState"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setIntakeData();
        setStudentData(searchText);
        loadEntrollTableData();
        setEntrollId();

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

        txtEntrollId.setText(newValue.getEntrollId());
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
        ObservableList<EntrollTm> entrollList = FXCollections.observableArrayList();
        entrollList.clear();
        for(Entroll entrollItem : Database.entrollTable){
            String state;
            if(entrollItem.isPaid()){
                state = "Paid";
            }else {
                state = "Not Paid";
            }
            Button btn = new Button("Delete");
            entrollList.add(new EntrollTm(
                    entrollItem.getEntrollId(),
                    entrollItem.getStudent(),
                    entrollItem.getIntake(),
                    state,
                    btn
            ));
            btn.setOnAction((ActionEvent event) -> {
               Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this entroll?", ButtonType.YES, ButtonType.NO);
               alert.showAndWait();
               if(alert.getResult() == ButtonType.YES) {
                   Database.entrollTable.remove(entrollItem);
                   loadEntrollTableData();
               }
            });
        }
        tblEntroll.setItems(entrollList);
    }

    private void setStudentId() {
        if(cmbStudent.getValue() != null) {
            String studentId = cmbStudent.getValue();
            String[] splitedValue = studentId.split("-");
            txtStudentId.setText(splitedValue[0]+"-"+splitedValue[1]);
        }
    }

    private void setStudentData(String searchText) {
        ObservableList<String> studentsList = FXCollections.observableArrayList();
        studentsList.clear();
        if(!Database.studentTable.isEmpty()){
            for(Student student : Database.studentTable){
                if(student.getStudentName().toLowerCase().contains(searchText.toLowerCase())){
                    studentsList.add(student.getStudentID()+"-"+student.getStudentName());
                }
            }
            cmbStudent.setItems(studentsList);
        }else {
            cmbStudent.setValue("Student not found");
        }
    }

    private void setIntakeData() {
        ObservableList<String> intakeList = FXCollections.observableArrayList();
        intakeList.clear();
        if(!Database.intakeTable.isEmpty()){
            for(Intake intake : Database.intakeTable){
                intakeList.add(intake.getIntakeId()+"-"+intake.getIntakeName());
            }
            cmbIntake.setItems(intakeList);
        }else{
            cmbIntake.setValue("Intake Not Found");
        }
    }

    public void newRegistrationOnAction(ActionEvent actionEvent) {
        clearFields();
        loadEntrollTableData();
        setIntakeData();
        setEntrollId();
    }

    public void backToDashboardOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void saveOnAction(ActionEvent actionEvent) {
        if(btnSave.getText().equals("Save")) {
            Database.entrollTable.add(new Entroll(
                    txtEntrollId.getText(),
                    cmbStudent.getValue(),
                    cmbIntake.getValue(),
                    rbtnPaid.isSelected()
            ));
            new Alert(Alert.AlertType.INFORMATION, "Saved Successfully").show();
            loadEntrollTableData();
            clearFields();
            setStudentData(searchText);
            setEntrollId();
            setIntakeData();
        }else{
            Optional<Entroll> selectedtEntroll = Database.entrollTable.stream().filter(en->en.getEntrollId().equals(txtEntrollId.getText())).findFirst();
            if(selectedtEntroll.isPresent()) {
                selectedtEntroll.get().setEntrollId(txtEntrollId.getText());
                selectedtEntroll.get().setStudent(cmbStudent.getValue());
                selectedtEntroll.get().setIntake(cmbIntake.getValue());
                selectedtEntroll.get().setPaid(rbtnPaid.isSelected());
                loadEntrollTableData();
                clearFields();
                setEntrollId();
                btnSave.setText("Save");
            }
        }
    }

    private void clearFields() {
        txtStudentId.clear();
        txtSearch.clear();
        rbtnPaid.setSelected(false);
        rbtnNonPaid.setSelected(false);
        cmbStudent.setValue("Select Student");
        cmbIntake.setValue("Select Intake");
        setEntrollId();
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/pathum/lms/view/"+location+".fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(scene);
    }

    private void setEntrollId() {
        if(!Database.entrollTable.isEmpty()) {
            Entroll lastEntroll = Database.entrollTable.get(Database.entrollTable.size()-1);
            String entrollId = lastEntroll.getEntrollId();
            String[] slitedEntrollId = entrollId.split("-");
            int lastDigit = Integer.parseInt(slitedEntrollId[1]);
            lastDigit++;
            txtEntrollId.setText("E-"+lastDigit);
        }else{
            txtEntrollId.setText("E-1");
        }
    }
}
