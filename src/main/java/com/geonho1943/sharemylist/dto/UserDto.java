package com.geonho1943.sharemylist.dto;

import java.time.LocalDateTime;

public class UserDto {

    private Long userIdx;
    private String userId;
    private String userName;
    private LocalDateTime userReg;
    private int userPrivileges;

    // 기본 생성자 및 모든 필드를 포함하는 생성자
    public UserDto(Long userIdx, String userId, String userName, LocalDateTime userReg, int userPrivileges) {
        this.userIdx = userIdx;
        this.userId = userId;
        this.userName = userName;
        this.userReg = userReg;
        this.userPrivileges = userPrivileges;
    }

    public Long getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(Long userIdx) {
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
}
