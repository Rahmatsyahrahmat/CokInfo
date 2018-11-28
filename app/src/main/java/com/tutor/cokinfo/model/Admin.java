package com.tutor.cokinfo.model;

public class Admin {
    private String email, password;

    public Admin(){
        email = "admin@admin.com";
        password = "admin123";
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
