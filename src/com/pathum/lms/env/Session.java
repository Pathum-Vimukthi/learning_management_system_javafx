package com.pathum.lms.env;

public class Session {
    private static String email;

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Session.email = email;
    }

    public static void clear() {
        email = null;
    }
}
