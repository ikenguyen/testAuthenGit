package com.coolmind.ordertracker.web.app.dtos;

import com.coolmind.ordertracker.core.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * DTO used only for posting new users for creation
 *
 */
public class UserDTO {


    private String userName;
    private String fullName;
    private String email;
    private String password;

    private List<Long> groups;

    public UserDTO() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getGroups() {
        return groups;
    }

    public void setGroups(List<Long> groups) {
        this.groups = groups;
    }
}
