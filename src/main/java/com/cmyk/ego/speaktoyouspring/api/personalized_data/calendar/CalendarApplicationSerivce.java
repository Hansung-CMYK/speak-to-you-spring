package com.cmyk.ego.speaktoyouspring.api.personalized_data.calendar;

import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.diary.Diary;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.diary.DiaryService;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarApplicationSerivce {
    private final DiaryService diaryService;
    private final UserAccountRepository userAccountRepository;

    /**
     * user_id로 월별 일기 리스트 조회하는 API
     */
    public List<List<CalendarDTO>> findByMonth(String uid, int month, int year) {
        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));
        TenantContext.setCurrentTenant(uid);

        List<Diary> diaries = diaryService.findByUserIdAndCreatedAt(uid, LocalDate.of(year, month, 1));

        return CalendarService.findByMonth(diaries);
    }
}
