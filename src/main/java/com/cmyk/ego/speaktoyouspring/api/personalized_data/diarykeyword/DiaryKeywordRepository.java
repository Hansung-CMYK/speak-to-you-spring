package com.cmyk.ego.speaktoyouspring.api.personalized_data.diarykeyword;

import com.cmyk.ego.speaktoyouspring.api.personalized_data.topic.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryKeywordRepository extends JpaRepository<DiaryKeyword, Long> {
    List<DiaryKeyword> findByDiaryId(Long diaryId);
    void deleteByDiaryId(Long diaryId);
}
