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
        if (checkedUserInfoEntity == null) {
            throw new IllegalArgumentException("입력된 정보와 일치하는 사용자가 없습니다.");
        }
        if (!checkedUserInfoEntity.isUserStatus()) {
            throw new RuntimeException("비활성화된 계정입니다. 로그인이 불가능합니다.");
        }
        return new UserDto(
                checkedUserInfoEntity.getUserIdx(), checkedUserInfoEntity.getUserId(),
                checkedUserInfoEntity.getUserName(), checkedUserInfoEntity.getUserReg(),
                checkedUserInfoEntity.getUserPrivileges());
        //Entity를 Dto로 변환하여 반환
    }

    public void resgin(UserDto resignInfo) {
        //회원탈퇴
        User resignInfoEntity = new User(resignInfo.getUserId(), resignInfo.getUserPw());
        User checkedResignInfoEntity = userRepository.findByUserIdAndUserPw(
                resignInfoEntity.getUserId(),resignInfoEntity.getUserPw());
        if (checkedResignInfoEntity == null) throw new IllegalArgumentException("입력된 정보와 일치하는 사용자가 없습니다.");

        if (checkedResignInfoEntity.isUserStatus()){
            checkedResignInfoEntity.setUserStatus(false);
            userRepository.save(checkedResignInfoEntity);
        }else {
            throw new IllegalArgumentException("이미 비활성화된 계정입니다. ");
        }

    }
}
