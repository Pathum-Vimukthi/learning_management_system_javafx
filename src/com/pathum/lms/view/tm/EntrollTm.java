package com.pathum.lms.view.tm;

import javafx.scene.control.Button;

public class EntrollTm {
    private String entrollId;
    private String student;
    private String intake;
    private String paymentState;
    private Button btn;

    public EntrollTm() {
    }

    public EntrollTm(String entrollId, String student, String intake, String paymentState, Button btn) {
        this.entrollId = entrollId;
        this.student = student;
        this.intake = intake;
        this.paymentState = paymentState;
        this.btn = btn;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getIntake() {
        return intake;
    }

    public void setIntake(String intake) {
        this.intake = intake;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public String getEntrollId() {
        return entrollId;
    }

    public void setEntrollId(String entrollId) {
        this.entrollId = entrollId;
    }
}
