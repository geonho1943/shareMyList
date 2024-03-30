package com.geonho1943.sharemylist.dto;

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

    public UserDto(int userIdx, String userId, String userName, LocalDateTime userReg, int userPrivileges, boolean userStatus, String userSalt) {
        //인증을 위한 생성자
        this.userIdx = userIdx;
        this.userId = userId;
        this.userName = userName;
        this.userReg = userReg;
        this.userPrivileges = userPrivileges;
        this.userStatus = userStatus;
        this.userSalt = userSalt;
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

    public void setUserName(String userName) {
        this.userName = userName;
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

    public void setUserPrivileges(int userPrivileges) {
        this.userPrivileges = userPrivileges;
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
