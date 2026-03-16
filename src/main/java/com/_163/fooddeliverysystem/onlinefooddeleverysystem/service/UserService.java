package com._163.fooddeliverysystem.onlinefooddeleverysystem.service;

import com._163.fooddeliverysystem.onlinefooddeleverysystem.model.User;

import java.util.List;

public class UserService extends BaseCsvService<User> {

    private static final String USER_FILE = "users.txt";

    @Override
    protected String getFileName() {
        return USER_FILE;
    }

    @Override
    protected User fromDataString(String line) {
        return User.fromDataString(line);
    }

    @Override
    protected String toDataString(User item) {
        return item.toDataString();
    }

    @Override
    protected String getId(User item) {
        return item == null ? null : item.getUserId();
    }

    public boolean registerUser(User user) {
        if (user == null || user.getUserId() == null) return false;

        boolean usernameTaken = getAll().stream()
            .anyMatch(u -> u.getUsername() != null && u.getUsername().equalsIgnoreCase(user.getUsername()));
        if (usernameTaken) {
            return false;
        }

        return add(user);
    }

    public User loginUser(String username, String password) {
        for (User user : getAll()) {
            if (user.authenticate(username, password)) {
                return user;
            }
        }
        return null;
    }

    public boolean updateUser(User updatedUser) {
        return update(updatedUser);
    }

    public boolean deleteUser(String userId) {
        return delete(userId);
    }

    public User searchUser(String userId) {
        return getById(userId);
    }

    public List<User> getAllUsers() {
        return getAll();
    }
}
