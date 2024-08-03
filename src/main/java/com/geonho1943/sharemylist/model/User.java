package com.geonho1943.sharemylist.model;

import com.geonho1943.sharemylist.dto.UserDto;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private int userIdx;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_pw")
    private String userPw;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_reg")
    private LocalDateTime userReg;

    @Column(name = "user_privileges")
    private int userPrivileges;

    @Column(name = "user_status")
    private boolean userStatus;

    @Column(name = "user_salt")
    private String userSalt;

    public User(){}

    public User(UserDto userDto) {
        this.userIdx = userDto.getUserIdx();
        this.userId = userDto.getUserId();
        this.userPw = userDto.getUserPw();
        this.userName = userDto.getUserName();
        this.userReg = userDto.getUserReg();
        this.userPrivileges = userDto.getUserPrivileges();
        this.userStatus = userDto.isUserStatus();
        this.userSalt = userDto.getUserSalt();
    }

    public int getUserIdx() {
        return userIdx;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPw() {
        return userPw;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getUserReg() {
        return userReg;
    }

    public int getUserPrivileges() {
        return userPrivileges;
    }

    public boolean isUserStatus() {
        return userStatus;
    }

    public void setUserStatus(boolean userStatus) {
        this.userStatus = userStatus;
    }

    public void setUserPrivileges(int userPrivileges) {
        this.userPrivileges = userPrivileges;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }

}
