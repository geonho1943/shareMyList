package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.dto.UserDto;
import com.geonho1943.sharemylist.model.User;
import com.geonho1943.sharemylist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDto userLogin(UserDto loginInfo) {
        User loginInfoEntity = new User(loginInfo.getUserId(), loginInfo.getUserPw());
        //Entity의 생성자를 사용하여 DTO를 Entity로 변경
        User checkedUserInfoEntity = userRepository.findByUserIdAndUserPw(
                loginInfoEntity.getUserId(), loginInfoEntity.getUserPw());
        //DB에 유저정보 대조
        return new UserDto(
            //loginInfoEntity 를 dto(loginInfo)로 변환,반환
            checkedUserInfoEntity.getUserIdx(),checkedUserInfoEntity.getUserId(),
            checkedUserInfoEntity.getUserName(), checkedUserInfoEntity.getUserReg(),
            checkedUserInfoEntity.getUserPrivileges());
    }

}
