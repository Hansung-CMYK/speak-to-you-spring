package com.cmyk.ego.speaktoyouspring.api.personalized_data.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;

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
}
