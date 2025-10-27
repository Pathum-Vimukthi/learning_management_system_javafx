package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
import com.pathum.lms.model.Modules;
import com.pathum.lms.model.Program;
import com.pathum.lms.model.Teacher;
import com.pathum.lms.view.tm.ModuleTm;
import com.pathum.lms.view.tm.ProgramTm;
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
import java.util.ArrayList;
import java.util.Optional;

public class ProgramManagementFormController {
    public AnchorPane context;
    public TextField txtProgramId;
    public TextField txtProgramName;
    public TextField txtProgramCost;
    public ComboBox<String> cmbTeacher;
    public TextField txtModule;
    public TableView<ModuleTm> tblModule;
    public TableColumn<ModuleTm, Integer> colModuleId;
    public TableColumn<ModuleTm, String> colModuleName;
    public TableColumn<ModuleTm, Button> colModuleAction;
    public Button btnSave;
    public TableView<ProgramTm> tblProgram;
    public TableColumn<ProgramTm, String> colProgramId;
    public TableColumn<ProgramTm, String> colProgramName;
    public TableColumn<ProgramTm, String> colProgramTeacher;
    public TableColumn<ProgramTm, Button> colProgramModules;
    public TableColumn<ProgramTm, Double> colProgramCost;
    public TableColumn<ProgramTm, Button> colProgramAction;
    public TextField txtSearch;
    ArrayList<Modules> moduleList = new ArrayList<>();
    static ObservableList<ModuleTm> mList = FXCollections.observableArrayList();
    private String searchText="";

    public void initialize() {
        colModuleId.setCellValueFactory(new PropertyValueFactory<>("moduleId"));
        colModuleName.setCellValueFactory(new PropertyValueFactory<>("moduleName"));
        colModuleAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
        colProgramId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        colProgramName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colProgramTeacher.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        colProgramModules.setCellValueFactory(new PropertyValueFactory<>("btnModules"));
        colProgramCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colProgramAction.setCellValueFactory(new PropertyValueFactory<>("btnDelete"));
        setProgramId();
        setTeachers();
        setModuleTableData();
        setProgramTableData(searchText);

        tblProgram.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setData((ProgramTm)newValue);
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            setProgramTableData(searchText);
        });
    }

    private void setData(ProgramTm tm) {
        btnSave.setText("Update");
        txtProgramName.setText(tm.getProgramName());
        txtProgramId.setText(tm.getProgramId());
        txtProgramCost.setText(Double.toString(tm.getCost()));
        cmbTeacher.setValue(tm.getTeacher());
    }

    private void setProgramTableData(String searchText) {
        ObservableList<ProgramTm> programList = FXCollections.observableArrayList();
        for(Program program:Database.programTable){
            if(program.getProgramName().toLowerCase().contains(searchText.toLowerCase())){
                Button btnModule = new Button("Module");
                Button btnDelete = new Button("Delete");
                programList.add(new ProgramTm(
                        program.getProgramId(),
                        program.getProgramName(),
                        program.getProgramTeacher(),
                        btnModule,
                        program.getProgramCost(),
                        btnDelete
                ));

                btnDelete.setOnAction((ActionEvent event) -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this program?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        Database.programTable.remove(program);
                        setProgramTableData(searchText);
                    }
                });

                btnModule.setOnAction((ActionEvent event) -> {
                    try {
                        Stage stage = new Stage();
                        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/pathum/lms/view/ModulePopup.fxml"))));
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

        }
        tblProgram.setItems(programList);
    }

    private void setModuleTableData() {
        mList.clear();
        for (Modules module : moduleList) {
            Button btn = new Button("Delete");
            mList.add(
                    new ModuleTm(
                            module.getModuleID(),
                            module.getModuleName(),
                            btn
                    )
            );
            btn.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this module?",ButtonType.YES,ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    moduleList.remove(module);
                    setModuleTableData();
                }
            });
        }
        tblModule.setItems(mList);
    }

    private void setTeachers() {
        ObservableList<String> teachers = FXCollections.observableArrayList();
        for(Teacher t:Database.teacherTable){
            teachers.add(t.getId().trim()+" - "+t.getName());
        }
        cmbTeacher.setItems(teachers);
    }

    private void setProgramId() {
        if (!Database.programTable.isEmpty()) {
            Program program = Database.programTable.get(Database.programTable.size()-1);
            String programId = program.getProgramId();
            String[] splitedProgramId = programId.split("-");
            String lastCharacter = splitedProgramId[1];
            int lastDigit = Integer.parseInt(lastCharacter);
            lastDigit++;
            String generatedId = "P-"+lastDigit;
            txtProgramId.setText(generatedId);
        }else{
            txtProgramId.setText("P-1");
        }
    }

    public void newProgramOnAction(ActionEvent actionEvent) {
        clearFields();
        setProgramId();
        btnSave.setText("Save");
    }

    public void backToDashboardOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void saveOnAction(ActionEvent actionEvent) {
        String[] selectedModules = new String[moduleList.size()];
        for (int i = 0; i < moduleList.size(); i++) {
            selectedModules[i] = moduleList.get(i).getModuleName();
        }

        if(btnSave.getText().equals("Save")){
            Database.programTable.add(new Program(
                    txtProgramId.getText(),
                    txtProgramName.getText(),
                    Double.parseDouble(txtProgramCost.getText()),
                    cmbTeacher.getValue(),
                    selectedModules
            ));
            setProgramId();
            clearFields();
            setProgramTableData(searchText);
            new Alert(Alert.AlertType.INFORMATION, "Program Saved").show();
        }else {
            Optional<Program> selectedProgram = Database.programTable.stream().filter(e->e.getProgramId().equals(txtProgramId.getText())).findFirst();
            if(selectedProgram.isPresent()){
                selectedProgram.get().setProgramName(txtProgramName.getText());
                selectedProgram.get().setProgramCost(Double.parseDouble(txtProgramCost.getText()));
                selectedProgram.get().setProgramTeacher(cmbTeacher.getValue());
                selectedProgram.get().setProgramModules(selectedModules);
                new Alert(Alert.AlertType.INFORMATION, "Program Updated").show();
                clearFields();
                setProgramTableData(searchText);
                setProgramId();
                btnSave.setText("Save");
            }
        }
    }

    private void clearFields() {
        txtProgramName.clear();
        txtProgramCost.clear();
        txtProgramCost.clear();
        cmbTeacher.setValue("Teacher");
        moduleList.clear();
        setModuleTableData();
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/pathum/lms/view/"+location+".fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(scene);
    }

    public void addModuleOnAction(ActionEvent actionEvent) {
        if(txtModule.getText().equals(null)){
            return;
        }
        moduleList.add(new Modules(getModuleId(), txtModule.getText()));
        setModuleTableData();
        txtModule.clear();
    }

    private int getModuleId() {
        boolean listEmpty = moduleList.isEmpty();
        if(listEmpty){
            return 1;
        }
        Modules lastModule = moduleList.get(moduleList.size()-1);
        int lastId = lastModule.getModuleID();
        lastId++;
        return lastId;
    }
}
