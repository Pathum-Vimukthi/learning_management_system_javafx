package com.pathum.lms.utils.security;

import org.mindrot.BCrypt;

public class PasswordManager {
    public String encodePassword(String rawPassword){
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(10));
    }
    public boolean checkPassword(String rawPassword, String encodedPassword){
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
