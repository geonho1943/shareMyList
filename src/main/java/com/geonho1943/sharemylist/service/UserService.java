package com.geonho1943.sharemylist.service;


import com.geonho1943.sharemylist.dto.UserDto;
import com.geonho1943.sharemylist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public static UserDto login(UserDto loginInfo) {
        //TODO DB에 정보 체크 하기 (jpa)
        System.out.println("세션구현을 위해 일단 로그인이 무조건 성공하게 됩니다");
        loginInfo.setUserName("name_for_session_login");
        return loginInfo;
    }
}
