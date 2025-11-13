package com.pathum.lms.dto.response;

public class ResponseUserDto {
    private String email;
    private String fullName;
    private int statusCode;
    private String message;

    public ResponseUserDto() {
    }

    public ResponseUserDto(String email, String fullName, int statusCode, String message) {
        this.email = email;
        this.fullName = fullName;
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
