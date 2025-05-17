package com.cmyk.ego.speaktoyouspring.api.personalized_data.calendar;

import com.cmyk.ego.speaktoyouspring.api.personalized_data.diary.Diary;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.diary.EmotionIconMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {
    /**
     * user_id로 월별 일기 리스트 조회하는 API
     */
    public static List<List<CalendarDTO>> findByMonth(List<Diary> diaries) {

        // 1. 먼저 CalendarDTO로 변환 + 연월 그룹화
        Map<YearMonth, List<CalendarDTO>> groupedByYearMonth = diaries.stream()
                .map(diary -> new CalendarDTO(
                        diary.getDiaryId(),
                        diary.getCreatedAt(),
                        EmotionIconMapper.getFirstIconFromText(diary.getFeeling().isEmpty() ? "평범" : diary.getFeeling())
                                .orElse(EmotionIconMapper.getDefaultIcon()) // "평범" 대체 메서드 사용
                ))
                .collect(Collectors.groupingBy(
                        dto -> YearMonth.from(dto.getCreatedAt()),
                        TreeMap::new, // 정렬된 Map
                        Collectors.toList()
                ));

        // 2. Map의 값들만 꺼내면 원하는 결과 타입
       return new ArrayList<>(groupedByYearMonth.values());
    }
}
