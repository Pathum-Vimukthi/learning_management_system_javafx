package com.pathum.lms.view.tm;

import javafx.scene.control.Button;

import java.util.Date;

public class IntakeTm {
    private String intakeId;
    private Date intakeDate;
    private String intakeName;
    private String program;
    private Button btn;

    public IntakeTm() {
    }

    public IntakeTm(String intakeId, Date intakeDate, String intakeName, String program, Button btn) {
        this.intakeId = intakeId;
        this.intakeDate = intakeDate;
        this.intakeName = intakeName;
        this.program = program;
        this.btn = btn;
    }

    public String getIntakeId() {
        return intakeId;
    }

    public void setIntakeId(String intakeId) {
        this.intakeId = intakeId;
    }

    public Date getIntakeDate() {
        return intakeDate;
    }

    public void setIntakeDate(Date intakeDate) {
        this.intakeDate = intakeDate;
    }

    public String getIntakeName() {
        return intakeName;
    }

    public void setIntakeName(String intakeName) {
        this.intakeName = intakeName;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
