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
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDto verification(UserDto userInfo) {
        User userInfoByUserId = userRepository.findByUserId(userInfo.getUserId());
        String hashedPassword = hashPassword(userInfo.getUserPw(), userInfoByUserId.getUserSalt());
        if (!Objects.equals(userInfoByUserId.getUserPw(), hashedPassword)){
            throw new IllegalArgumentException("입력된 정보와 일치하는 사용자가 없습니다.");
        }
        if (!userInfoByUserId.isUserStatus()) {
            // 계정이 비활성화되어 있으면 예외 발생
            throw new IllegalArgumentException("비활성화된 계정입니다. 검증이 불가능합니다.");
        }
        // Entity를 Dto로 반환
        return new UserDto(userInfoByUserId);
    }

    public void resign(UserDto resignInfo, String userPw) {
        //회원탈퇴
        if (resignInfo.isUserStatus()){
            User resignInfoEntity = new User(resignInfo);
            resignInfoEntity.setUserStatus(false);
            resignInfoEntity.setUserPw(hashPassword(userPw, resignInfo.getUserSalt()));
            userRepository.save(resignInfoEntity);
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
        User joinInfoEntity = new User(joinInfo);
        joinInfoEntity.setUserStatus(true);
        joinInfoEntity.setUserPrivileges(1);
        joinInfoEntity.setUserSalt(salt);
        joinInfoEntity.setUserPw(hashedPassword);
        userRepository.save(joinInfoEntity);
        joinInfo.setUserIdx(joinInfoEntity.getUserIdx());
    }

    private String generateSalt() {
        //salt 생성
        SecureRandom random = new SecureRandom();
        byte[] salt1 = new byte[16];
        random.nextBytes(salt1);
        return Base64.getEncoder().encodeToString(salt1);
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(Base64.getDecoder().decode(salt));
            byte[] hashedBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
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
        User user = userRepository.findByUserId(userId);
        return user != null;
    }

}
