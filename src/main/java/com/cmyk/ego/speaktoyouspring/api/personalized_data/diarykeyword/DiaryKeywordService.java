package com.cmyk.ego.speaktoyouspring.api.personalized_data.diarykeyword;

import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryKeywordService {
    private final UserAccountRepository userAccountRepository;
    private final DiaryKeywordRepository diaryKeywordRepository;

    /**
     * diaryId에 해당하는 키워드를 모두 조회한다.
     */
    public List<DiaryKeyword> findByDiaryId(String userId, Long diaryId) {
        String uid = userId;

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);

        return diaryKeywordRepository.findByDiaryId(diaryId);
    }

    /**
     * DiaryKeyword 객체를 DiaryKeywordDTO 객체로 변환한다.
     *
     * @param diaryKeyword
     * @return
     */
    public DiaryKeywordDTO convertDiaryKeywordToDTO(DiaryKeyword diaryKeyword) {
        return new DiaryKeywordDTO(diaryKeyword.getKeywordId(), diaryKeyword.getDiaryId(), diaryKeyword.getContent());
    }

    /**
     * diaryId에 해당하는 키워드를 모두 삭제한다.
     */
    public void deleteByDiaryId(Long diaryId) {
        diaryKeywordRepository.deleteByDiaryId(diaryId);
    }

    /**
     * 키워드를 저장한다.
     */
    public DiaryKeyword save(DiaryKeywordDTO diaryKeywordDTO) {
        DiaryKeyword diaryKeyword = diaryKeywordDTO.toEntity();
        return diaryKeywordRepository.save(diaryKeyword);
    }

    /**
     * 키워드 목록을 저장한다.
     */
    public void saveAll(String userId, List<DiaryKeywordDTO> diaryKeywordDTOList) {
        String uid = userId;

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));
        TenantContext.setCurrentTenant(uid);

        diaryKeywordDTOList.forEach(diaryKeywordDTO -> {
            DiaryKeyword diaryKeyword = diaryKeywordDTO.toEntity();
            diaryKeywordRepository.save(diaryKeyword);
        });
    }

    /**
     * diaryId에 해당하는 키워드를 삭제한다.
     */
    public void deleteByDiaryId(String userId, Long diaryId) {
        String uid = userId;

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));
        TenantContext.setCurrentTenant(uid);

        this.deleteByDiaryId(diaryId);
    }
}
