package com.cmyk.ego.speaktoyouspring.api.personalized_data.diary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Diary findByEgoIdAndCreatedAt(Long egoId, LocalDate createdAt);
}
