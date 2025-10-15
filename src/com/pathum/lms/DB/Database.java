package com.pathum.lms.DB;

import com.pathum.lms.model.User;

import java.util.ArrayList;

public class Database {
    public static ArrayList<User> userTable = new ArrayList<>();

    static {
        userTable.add(new User("Admin", "loopinfinite760@gmail.com", "1234", 25));
    }
}
