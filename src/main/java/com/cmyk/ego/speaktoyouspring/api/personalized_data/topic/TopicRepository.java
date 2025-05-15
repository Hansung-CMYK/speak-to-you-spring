package com.cmyk.ego.speaktoyouspring.api.personalized_data.topic;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByDiaryId(Long diaryId);
}
