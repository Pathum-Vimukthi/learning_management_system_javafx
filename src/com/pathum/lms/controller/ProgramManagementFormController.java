package com.pathum.lms.controller;

import com.pathum.lms.DB.Database;
import com.pathum.lms.DB.DbConnection;
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
import java.sql.*;
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
    public static String programIdForModules;

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
        try{
            ObservableList<ProgramTm> programDetails = fetchProgramDetails(searchText);
            tblProgram.setItems(programDetails);
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private ObservableList<ProgramTm> fetchProgramDetails(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<ProgramTm> programDetails = FXCollections.observableArrayList();
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT p.id, p.name, p.cost, t.id, t.name FROM program p INNER JOIN teacher t ON t.id=p.teacher_id WHERE p.name LIKE ?");
        ps.setString(1, "%"+searchText+"%");
        ResultSet rs = ps.executeQuery();

        String programIdModule = "";
        while(rs.next()){
            programIdModule = rs.getString(1);
            Button btnModule = new Button("Module");
            Button btnDelete = new Button("Delete");
            ProgramTm tm = new ProgramTm(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(4)+"-"+rs.getString(5),
                    btnModule,
                    rs.getDouble(3),
                    btnDelete
            );
            programDetails.add(tm);
            btnDelete.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete this program?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.YES){
                    try {
                        boolean isDeleted = deleteProgram(tm.getProgramId());
                        if(isDeleted){
                            setProgramId();
                            setProgramTableData(searchText);
                            new Alert(Alert.AlertType.INFORMATION,"Program deleted successfully").show();
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            programIdForModules = programIdModule;
            btnModule.setOnAction((ActionEvent event) -> {
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pathum/lms/view/ModulePopup.fxml"));
                    Parent load = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(load));
                    stage.setTitle("Module List");
                    stage.show();

                }catch (IOException e){
                    throw new RuntimeException();
                }
            });
        }
        return programDetails;
    }

    private boolean deleteProgram(String programId) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        connection.setAutoCommit(false);
        try {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM module_has_program WHERE program_id=?")){
                ps.setString(1, programId);
                ps.executeUpdate();
            }
            boolean programDeleted;
            try (PreparedStatement ps2 = connection.prepareStatement("DELETE FROM program WHERE id=?")){
                ps2.setString(1, programId);
                programDeleted = ps2.executeUpdate() > 0;
            }
            try (PreparedStatement ps3 = connection.prepareStatement("DELETE FROM module WHERE id NOT IN (SELECT DISTINCT module_id FROM module_has_program)")){
                ps3.executeUpdate();
            }
            if(programDeleted){
                connection.commit();
                return true;
            }else{
                connection.rollback();
                return false;
            }

        }catch (Exception e){
            connection.rollback();
            e.printStackTrace();
            return false;
        }finally {
            connection.setAutoCommit(true);
        }
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
        try{
            ArrayList<String> teacherslist = fetchTeachers();
            ObservableList<String> teachers = FXCollections.observableArrayList();
            for(String teacher:teacherslist){
                teachers.add(teacher);
            }
            cmbTeacher.setItems(teachers);
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private ArrayList<String> fetchTeachers() throws SQLException, ClassNotFoundException {
        ArrayList<String> teachersList = new ArrayList<>();
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM teacher");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            teachersList.add(rs.getString("id")+" - "+rs.getString("name"));
        }
        return teachersList;
    }

    private void setProgramId() {
        try {
            String lastProgramId = getLastProgramId();
            if (lastProgramId != null) {
                String[] splitedProgramId = lastProgramId.split("-");
                String lastCharacter = splitedProgramId[1];
                int lastDigit = Integer.parseInt(lastCharacter);
                lastDigit++;
                String generatedId = "P-"+lastDigit;
                txtProgramId.setText(generatedId);
            }else{
                txtProgramId.setText("P-1");
            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private String getLastProgramId() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT id FROM program ORDER BY CAST(SUBSTRING(id,3)AS UNSIGNED) DESC LIMIT 1");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
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
        try {
            if(btnSave.getText().equals("Save")){
                boolean isSaved = saveProgram(new Program(
                        txtProgramId.getText(),
                        txtProgramName.getText(),
                        Double.parseDouble(txtProgramCost.getText()),
                        splitedTeacherId(cmbTeacher.getValue()),
                        selectedModules
                ));
                setProgramId();
                clearFields();
                setProgramTableData(searchText);
                new Alert(Alert.AlertType.INFORMATION, "Program Saved").show();
            }else {
                boolean isUpdated = updateProgram(new Program(
                        txtProgramId.getText(),
                        txtProgramName.getText(),
                        Double.parseDouble(txtProgramCost.getText()),
                        splitedTeacherId(cmbTeacher.getValue()),
                        selectedModules
                ));

                if(isUpdated){
                    new Alert(Alert.AlertType.INFORMATION, "Program Updated").show();
                    clearFields();
                    setProgramTableData(searchText);
                    setProgramId();
                    btnSave.setText("Save");
                }
            }
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    private boolean updateProgram(Program program) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE program SET name = ?, cost=?, teacher_id=? WHERE id = ?");
        ps.setString(1, program.getProgramName());
        ps.setDouble(2, program.getProgramCost());
        ps.setString(3, splitedTeacherId(program.getProgramTeacher()));
        ps.setString(4, program.getProgramId());

        return ps.executeUpdate() > 0;
    }

    private String splitedTeacherId(String value) {
        String[] splitedTeacherId = value.split("-");
        return splitedTeacherId[0]+"-"+splitedTeacherId[1];
    }

    private boolean saveProgram(Program program) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getDbConnection().getConnection();
        connection.setAutoCommit(false);
        try{
            PreparedStatement ps = connection.prepareStatement("INSERT INTO program(id, name, cost, teacher_id) VALUES (?,?,?,?)");
            ps.setString(1, program.getProgramId());
            ps.setString(2, program.getProgramName());
            ps.setDouble(3, program.getProgramCost());
            ps.setString(4, program.getProgramTeacher());

            if(ps.executeUpdate()==0){
                connection.rollback();
                return false;
            }

            for(String module:program.getProgramModules()){
                try(PreparedStatement ps2 = connection.prepareStatement("INSERT INTO module(name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)){
                    ps2.setString(1, module);
                    if(ps2.executeUpdate()==0){
                        connection.rollback();
                        return false;
                    }
                    try (ResultSet rs = ps2.getGeneratedKeys()) {
                        if(rs.next()){
                            int moduleId = rs.getInt(1);
                            try (PreparedStatement ps3=connection.prepareStatement("INSERT INTO module_has_program(module_id,program_id) VALUES (?,?)")){
                                ps3.setInt(1, moduleId);
                                ps3.setString(2, program.getProgramId());
                                if(ps3.executeUpdate()==0){
                                    connection.rollback();
                                    return false;
                                }
                            }
                        }else{
                            connection.rollback();
                            return false;
                        }
                    }
                }
            }
            connection.commit();
            return true;
        }catch (Exception e){
            connection.rollback();
            throw e;
        }finally {
            connection.setAutoCommit(true);
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
