package com.geonho1943.sharemylist.repository;

import com.geonho1943.sharemylist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserIdAndUserPw(String userId, String userPw);

    User findUserSaltByUserId(String userId);

    Optional<User> findByUserId(String userId);
}
