package com.cmyk.ego.speaktoyouspring.api.personalized_data.diary;

import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.DiaryErrorCode;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final UserAccountRepository userAccountRepository;

    public Diary upsert(DiaryDTO diaryDTO){
        Diary diary = diaryRepository.findByEgoIdAndCreatedAt(diaryDTO.getEgoId(), diaryDTO.getCreatedAt());

        if (diary != null) {
            diary.setFeeling(diaryDTO.getFeeling());
            return diaryRepository.save(diary);
        }

        // 새로 생성
        Diary newDiary = diaryDTO.toEntity();
        return diaryRepository.save(newDiary);
    }

    public Diary findByDiaryId(Long diaryId){
        return diaryRepository.findByDiaryId(diaryId).orElseThrow(
                () -> new ControlledException(DiaryErrorCode.ERROR_DIARY_NOT_FOUND)
        );
    }

    /// userId와 createdAt으로 diary 리스트 조회
    public List<Diary> findByUserIdAndCreatedAt(String uid, LocalDate currentMonth){
        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));
        TenantContext.setCurrentTenant(uid);

        // 현재 월을 기준으로 앞뒤 가져올 월 개수 설정
        int range = 1;

        // 최초 달의 시작 날
        LocalDate prevMonth = currentMonth.minusMonths(range);

        // 마지막 달의 끝 날
        LocalDate nextMonth = currentMonth.plusMonths(1).withDayOfMonth(
                currentMonth.plusMonths(1).lengthOfMonth()
        );

        return diaryRepository.findByCreatedAtBetween(prevMonth, nextMonth);
    }
}
