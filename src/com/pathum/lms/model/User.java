package com.pathum.lms.model;

public class User {
    private String fullName;
    private String email;
    private String password;
    private int age;

    public User() {
    }

    public User(String fullName, String email, String password, int age) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
