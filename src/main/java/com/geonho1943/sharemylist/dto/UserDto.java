package com.geonho1943.sharemylist.dto;

import com.geonho1943.sharemylist.model.User;

import java.time.LocalDateTime;

public class UserDto {

    private int userIdx;
    private String userId;
    private String userName;
    private LocalDateTime userReg;
    private int userPrivileges;
    private String userPw;
    private boolean userStatus;
    private String userSalt;

    public UserDto() {}

    public UserDto(User user) {
        this.userIdx = user.getUserIdx();
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userReg = user.getUserReg();
        this.userPrivileges = user.getUserPrivileges();
        this.userStatus = user.isUserStatus();
        this.userSalt = user.getUserSalt();
    }

    public int getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(int userIdx) {
        this.userIdx = userIdx;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getUserReg() {
        return userReg;
    }

    public void setUserReg(LocalDateTime userReg) {
        this.userReg = userReg;
    }

    public int getUserPrivileges() {
        return userPrivileges;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public boolean isUserStatus() {
        return userStatus;
    }

    public void setUserStatus(boolean userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserSalt() {
        return userSalt;
    }
}
