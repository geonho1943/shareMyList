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
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDto verification(UserDto resignInfo) {
        User storedSalt = userRepository.findUserSaltByUserId(resignInfo.getUserId());
        // salt 조회

        String hashedPassword = hashPassword(resignInfo.getUserPw(), storedSalt.getUserSalt());
        // 입력된 비밀번호와 저장된 salt를 사용하여 해싱

        User loginInfoEntity = new User(resignInfo.getUserId(), hashedPassword);
        // Entity의 생성자를 사용하여 DTO를 Entity로 변경

        Optional<User> checkedUserInfoEntity = userRepository.findByUserIdAndUserPw(loginInfoEntity.getUserId(),loginInfoEntity.getUserPw());
        // DB에 유저정보 대조
        if (checkedUserInfoEntity.isEmpty()) {
            // 대조된 유저정보가 없으면 예외 발생
            throw new IllegalArgumentException("입력된 정보와 일치하는 사용자가 없습니다.");
        }
        User user = checkedUserInfoEntity.get();
        if (!user.isUserStatus()) {
            // 계정이 비활성화되어 있으면 예외 발생
            throw new RuntimeException("비활성화된 계정입니다. 검증이 불가능합니다.");
        }

        // Entity를 Dto로 반환
        return new UserDto(
            user.getUserIdx(), user.getUserId(),
            user.getUserName(), user.getUserReg(),
            user.getUserPrivileges(), user.isUserStatus(), user.getUserSalt());
    }


    public void resgin(UserDto resignInfo, String userPw) {
        //회원탈퇴
        if (resignInfo.isUserStatus()){
            User resginInfoEntity = new User(resignInfo);
            resginInfoEntity.setUserStatus(false);
            resginInfoEntity.setUserPw(hashPassword(userPw, resignInfo.getUserSalt()));
            userRepository.save(resginInfoEntity);
        }else {
            throw new IllegalArgumentException("이미 비활성화된 계정입니다.");
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

    public String checkAccount(UserDto joinInfo) {
        // id check
        if (joinInfo.getUserId().length() < 8 ) {
            return "ID는 8자 이상이어야 합니다.";
        }
        if (!joinInfo.getUserId().matches("[a-zA-Z0-9]+")) {
            return "ID는 알파벳 대소문자와 숫자만 포함해야 합니다.";
        }

        // pw check
        if (joinInfo.getUserPw().length() < 8) {
            return "비밀번호는 8자 이상이어야 합니다.";
        }
        if (!joinInfo.getUserPw().matches("^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$")) {
            return "비밀번호는 최소한 하나의 소문자, 숫자, 특수 문자를 포함해야 합니다.";
        }
        return null; // 모든 검사를 통과한 경우
    }

    public boolean isDuplicateId(String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        //Optional - 중복이 있다면 true 없다면 false
        return user.isPresent();
    }

}
