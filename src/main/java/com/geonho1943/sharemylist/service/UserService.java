package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.dto.UserDto;
import com.geonho1943.sharemylist.model.User;
import com.geonho1943.sharemylist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDto userLogin(UserDto loginInfo) {
        // 로그인
        User storedSalt = userRepository.findUserSaltByUserId(loginInfo.getUserId());
        // salt 조회

        String hashedPassword = hashPassword(loginInfo.getUserPw(), storedSalt.getUserSalt());
        // 입력된 비밀번호와 저장된 salt를 사용하여 해싱

        User loginInfoEntity = new User(loginInfo.getUserId(), hashedPassword);
        // Entity의 생성자를 사용하여 DTO를 Entity로 변경

        User checkedUserInfoEntity = userRepository.findByUserIdAndUserPw(
                loginInfoEntity.getUserId(), loginInfoEntity.getUserPw());
        // DB에 유저정보 대조

        // 대조된 유저정보가 없으면 예외 발생
        if (checkedUserInfoEntity == null) {
            throw new IllegalArgumentException("입력된 정보와 일치하는 사용자가 없습니다.");
        }
        // 계정이 비활성화되어 있으면 예외 발생
        if (!checkedUserInfoEntity.isUserStatus()) {
            throw new RuntimeException("비활성화된 계정입니다. 로그인이 불가능합니다.");
        }

    // Entity를 Dto로 반환
    return new UserDto(
        checkedUserInfoEntity.getUserIdx(), checkedUserInfoEntity.getUserId(),
        checkedUserInfoEntity.getUserName(), checkedUserInfoEntity.getUserReg(),
        checkedUserInfoEntity.getUserPrivileges(), checkedUserInfoEntity.getUserSalt());
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

    public void saveAccount(UserDto joinInfo) {
        //회원 가입
        String salt = generateSalt();
        //salt 생성
        String hashedPassword = hashPassword(joinInfo.getUserPw(), salt);
        //비밀번호 해싱

        // 회원 가입
        User joinInfoEntity = new User(joinInfo.getUserId(), hashedPassword, joinInfo.getUserName());
        joinInfoEntity.setUserStatus(true);
        joinInfoEntity.setUserPrivileges(1);
        joinInfoEntity.setUserSalt(salt);

        userRepository.save(joinInfoEntity);
    }

    private String generateSalt() {
        //salt 생성
        SecureRandom random = new SecureRandom();
        byte[] salt1 = new byte[16];
        random.nextBytes(salt1);
        return Base64.getEncoder().encodeToString(salt1);
    }

    private String hashPassword(String password, String salt) {
        //해싱
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(Base64.getDecoder().decode(salt));
            byte[] hashedBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkAccount(UserDto joinInfo) {
        //id check
        if (joinInfo.getUserId().length() < 8 ) {
            return false;
        }
        if (!joinInfo.getUserId().matches("[a-zA-Z0-9]+")) {
            return false;
        }

        // pw check
        if (joinInfo.getUserPw().length() < 8) {
            return false;
        }
        if (!joinInfo.getUserPw().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$")) {
            return false;
        }
        return true;
    }
}
