package com.geonho1943.sharemylist.service;

import com.geonho1943.sharemylist.dto.UserDto;
import com.geonho1943.sharemylist.model.User;
import com.geonho1943.sharemylist.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto verification(UserDto userInfo) {
        User userInfoByUserId = userRepository.findByUserId(userInfo.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("입력된 정보와 일치하는 사용자가 없습니다."));
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

    @Transactional
    public void resign(UserDto resignInfo) {
        User resignInfoEntity = userRepository.findByUserIdx(resignInfo.getUserIdx())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
        if (!resignInfoEntity.isUserStatus()) {
            throw new IllegalArgumentException("이미 비활성화된 계정입니다.");
        }
        resignInfoEntity.setUserStatus(false);
    }

    @Transactional
    public void saveAccount(UserDto joinInfo) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(joinInfo.getUserPw(), salt);

        User joinInfoEntity = new User(joinInfo);
        joinInfoEntity.setUserStatus(true);
        joinInfoEntity.setUserPrivileges(1);
        joinInfoEntity.setUserSalt(salt);
        joinInfoEntity.setUserPw(hashedPassword);
        joinInfoEntity.setUserReg(LocalDateTime.now());
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
        return userRepository.findByUserId(userId).isPresent();
    }

}
