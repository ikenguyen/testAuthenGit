package com.coolmind.ordertracker.web.app.dtos;

import com.coolmind.ordertracker.core.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * JSON-serializable DTO containing user data
 *
 */
public class UserInfoDTO {

    private long id;
    private String userName;
    private String fullName;
    private String email;

    public UserInfoDTO(long id, String userName, String fullName, String email) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public static UserInfoDTO fromUser(User user){
        return new UserInfoDTO(user.getId(), user.getUsername(), user.getFullname(), user.getEmail());
    }

    public static List<UserInfoDTO> fromUserList(List<User> users){

        List<UserInfoDTO> userInfoDTOList = new ArrayList<>();

        for (User usr : users){
            userInfoDTOList.add(UserInfoDTO.fromUser(usr));
        }

        return userInfoDTOList;

    }
}
