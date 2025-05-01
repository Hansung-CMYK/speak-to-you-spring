package com.cmyk.ego.speaktoyouspring.api.hub.user_account;

import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/// personalized-data에 추가되는 user정보를 관리할 수 있게 만들어둔 클래스이다.
@Service
@Transactional
@RequiredArgsConstructor
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;

    public Set<UserAccount> getAllUserData() {
        return new HashSet<>(userAccountRepository.findAll());
    }

    public Set<String> getAllUserUID(){
        return userAccountRepository.findAll().stream().map(UserAccount::getUid).collect(Collectors.toSet());
    }

    /// 파라미터 `UserAccount`를 hub.user_account table에 저장(생성)하는 함수
    public UserAccount create(UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }

    public UserAccount delete(String uid) {
        var userAccount = userAccountRepository.findByUid(uid)
                .orElseThrow(() -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        // User의 정보를 직접 삭제하지 않고, is_deleted를 true로 하여 탈퇴여부를 관리
        userAccount.setIsDeleted(true);

        return userAccount;
    }
}
