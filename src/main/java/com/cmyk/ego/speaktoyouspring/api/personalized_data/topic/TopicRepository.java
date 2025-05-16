package com.cmyk.ego.speaktoyouspring.api.personalized_data.topic;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByDiaryIdAndIsDeletedFalse(Long diaryId);
    Optional<Topic> findByTopicIdAndIsDeletedFalse(Long topicId);
}
