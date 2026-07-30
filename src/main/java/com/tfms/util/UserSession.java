package com.tfms.util;

import com.tfms.model.entity.User;

public class UserSession {
    private static User loggedInUser;

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static int getUserId() {
        return (loggedInUser != null) ? loggedInUser.getId() : -1;
    }

    public static void logout() {
        loggedInUser = null;
    }
}