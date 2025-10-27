package com.pathum.lms.view.tm;

import javafx.scene.control.Button;

public class ProgramTm {
    private String programId;
    private String programName;
    private String teacher;
    private Button btnModules;
    private double cost;
    private Button btnDelete;

    public ProgramTm() {
    }

    public ProgramTm(String programId, String programName, String teacher, Button btnModules, double cost, Button btnDelete) {
        this.programId = programId;
        this.programName = programName;
        this.teacher = teacher;
        this.btnModules = btnModules;
        this.cost = cost;
        this.btnDelete = btnDelete;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Button getBtnModules() {
        return btnModules;
    }

    public void setBtnModules(Button btnModules) {
        this.btnModules = btnModules;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public void setBtnDelete(Button btnDelete) {
        this.btnDelete = btnDelete;
    }
}
