package com.pathum.lms.DB;

import com.pathum.lms.model.Student;
import com.pathum.lms.model.User;
import com.pathum.lms.utils.security.PasswordManager;

import java.util.ArrayList;

public class Database {
    public static ArrayList<User> userTable = new ArrayList<>();
    public static ArrayList<Student> studentTable = new ArrayList<>();

    static {
        userTable.add(new User("Admin", "loopinfinite760@gmail.com", new PasswordManager().encodePassword("1234"), 25));
    }
}
