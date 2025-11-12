package com.pathum.lms.controller;

import com.pathum.lms.DB.DbConnection;
import com.pathum.lms.view.tm.ModuleTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModulePopupController {
    public AnchorPane context;
    public ListView<String> lstModule;
    ObservableList<String> moduleList = FXCollections.observableArrayList();

    public void initialize() {
        setModuleList(ProgramManagementFormController.programIdForModules);
    }

    private void setModuleList(String programId) {
        try{
            ObservableList<String> moduleList = fetchModuleList(programId);
            lstModule.setItems(moduleList);
        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private ObservableList<String> fetchModuleList(String programId) throws SQLException, ClassNotFoundException {
        ObservableList<String> moduleList = FXCollections.observableArrayList();
        Connection connection = DbConnection.getDbConnection().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT m.name FROM module_has_program mhp JOIN module m ON mhp.module_id=m.id WHERE mhp.program_id=?");
        ps.setString(1, programId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            moduleList.add(rs.getString(1));
        }
        return moduleList;
    }
}
