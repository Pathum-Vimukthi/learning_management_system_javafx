package com.pathum.lms.view.tm;

import javafx.scene.control.Button;

public class ModuleTm {
    private int moduleId;
    private String moduleName;
    private Button btn;

    public ModuleTm() {
    }

    public ModuleTm(int moduleId, String moduleName, Button btn) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.btn = btn;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
