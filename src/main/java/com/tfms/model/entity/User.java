package com.tfms.model.entity;

public class User {

    private int userId;
    private String username;
    private String password;
    private String fullName;
    private int role;
    private String status;

    public User() {}

    public User(int userId, String username, String password,
                String fullName, int role, String status) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.status = status;
    }


    public int getId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getRole() {
        return role;
    }
    
    public void setRoleInt(int role) {
        this.role = role;
    }

    public void setRole(String role) {
        switch (role) {
            case "ADMIN": 
                this.role = 0;
                break;
            case "SUPERVISOR": 
                this.role = 1;
                break;
            case "QUALITY_CONTROL": 
                this.role = 2;
                break;
            case "MANAGER": 
                this.role = 3;
                break;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //==========================
    // toString()
    //==========================

    @Override
    public String toString() {
        return fullName + " (" + role + ")";
    }
}