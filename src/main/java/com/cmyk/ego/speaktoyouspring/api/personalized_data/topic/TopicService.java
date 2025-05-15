package com.cmyk.ego.speaktoyouspring.api.personalized_data.topic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;

    /**
     * 일기 토픽 내용을 저장한다.
     * @param topics
     * @param diaryId
     * @return
     */
    public List<Topic> saveAll(List<TopicDTO> topics, Long diaryId) {
        deleteByDiaryId(diaryId);
        topics.forEach(topic -> topic.setDiaryId(diaryId));
        return topicRepository.saveAll(topics.stream().map(TopicDTO::toEntity).toList());
    }

    /**
     * diaryId를 기반으로 과거에 일기 내용 지우기
     * @param diaryId
     */
    public void deleteByDiaryId(Long diaryId) {
        List<Topic> oldTopicList = topicRepository.findByDiaryId(diaryId);
        oldTopicList.forEach(topic -> topic.setIsDeleted(true));
        topicRepository.saveAll(oldTopicList);
    }

    public List<TopicDTO> convertTopicListToDTOList(List<Topic> topics){
        return topics.stream().map(topic -> new TopicDTO(topic.getTopicId(), topic.getDiaryId(), topic.getTitle(), topic.getContent(), topic.getIsDeleted())).toList();
    }
}
