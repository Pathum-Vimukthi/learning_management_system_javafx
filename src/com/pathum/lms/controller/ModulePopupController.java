package com.pathum.lms.controller;

import com.pathum.lms.view.tm.ModuleTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class ModulePopupController {
    public AnchorPane context;
    public ListView<String> lstModule;
    ObservableList<String> moduleList = FXCollections.observableArrayList();

    public void initialize() {
        setModuleList();
    }

    private void setModuleList() {
        for (ModuleTm module : ProgramManagementFormController.mList) {
            moduleList.add(module.getModuleId()+" : "+module.getModuleName());
        }
        lstModule.setItems(moduleList);
    }
}
