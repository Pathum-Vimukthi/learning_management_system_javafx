package com.pathum.lms.model;

public class Entroll {
    private String student;
    private String intake;
    private boolean isPaid;

    public Entroll() {
    }

    public Entroll(String student, String intake, boolean isPaid) {
        this.student = student;
        this.intake = intake;
        this.isPaid = isPaid;
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

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
