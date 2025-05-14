package com.messageprocessingapp.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPass(String pass, String hash){
        return BCrypt.checkpw(pass, hash);
    }
}
