package com.pathum.lms.entity;

public class Entroll {
    private String studen;
    private String intake;
    private Boolean isPaid;

    public Entroll() {
    }

    public Entroll(String studen, String intake, Boolean isPaid) {
        this.studen = studen;
        this.intake = intake;
        this.isPaid = isPaid;
    }

    public String getStuden() {
        return studen;
    }

    public void setStuden(String studen) {
        this.studen = studen;
    }

    public String getIntake() {
        return intake;
    }

    public void setIntake(String intake) {
        this.intake = intake;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }
}
