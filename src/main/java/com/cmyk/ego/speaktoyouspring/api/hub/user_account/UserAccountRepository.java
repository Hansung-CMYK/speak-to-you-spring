package com.cmyk.ego.speaktoyouspring.api.hub.user_account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// 테넌트 레포지토리에 접근하기 위한 클래스이다.
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    Optional<UserAccount> findByUid(String uid);
    List<UserAccount> findAllByEmail(String email);
}