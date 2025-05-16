package com.cmyk.ego.speaktoyouspring.api.personalized_data.diarykeyword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DiaryKeywordRepository extends JpaRepository<DiaryKeyword, Long> {

    List<DiaryKeyword> findByDiaryId(Long diaryId);

    @Modifying
    @Transactional
    void deleteByDiaryId(Long diaryId);
}
