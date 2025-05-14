package com.messageprocessingapp.interfaces;

import com.messageprocessingapp.models.User;

public interface IUserRepository {
    boolean createUser(String user, String password);
    User login(String user, String password);
    boolean getUserByName(String username);
}
