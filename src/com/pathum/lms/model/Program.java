package com.pathum.lms.model;

public class Program {
    private String programId;
    private String programName;
    private double programCost;
    private String programTeacher;

    public Program() {
    }

    public Program(String programId, String programName, double programCost, String programTeacher, String[] programModules) {
        this.programId = programId;
        this.programName = programName;
        this.programCost = programCost;
        this.programTeacher = programTeacher;
        this.programModules = programModules;
    }

    private String[] programModules;

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

    public double getProgramCost() {
        return programCost;
    }

    public void setProgramCost(double programCost) {
        this.programCost = programCost;
    }

    public String getProgramTeacher() {
        return programTeacher;
    }

    public void setProgramTeacher(String programTeacher) {
        this.programTeacher = programTeacher;
    }

    public String[] getProgramModules() {
        return programModules;
    }

    public void setProgramModules(String[] programModules) {
        this.programModules = programModules;
    }
}
