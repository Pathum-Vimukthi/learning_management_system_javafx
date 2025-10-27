package com.pathum.lms.model;

public class Modules {
    private int moduleID;
    private String moduleName;

    public Modules() {
    }

    public Modules(int moduleID, String moduleName) {
        this.moduleID = moduleID;
        this.moduleName = moduleName;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
