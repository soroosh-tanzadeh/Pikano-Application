package com.ternobo.pikano.database;

import java.io.Serializable;

public class MainDB implements Serializable {

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
