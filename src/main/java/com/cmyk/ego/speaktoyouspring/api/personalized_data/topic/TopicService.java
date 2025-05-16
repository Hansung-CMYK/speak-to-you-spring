package com.cmyk.ego.speaktoyouspring.api.personalized_data.topic;

import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.TopicErrorCode;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final UserAccountRepository userAccountRepository;

    /**
     * 일기 토픽 내용을 저장한다.
     */
    public List<Topic> saveAll(List<TopicDTO> topics, Long diaryId) {
        deleteByDiaryId(diaryId);
        topics.forEach(topic -> topic.setDiaryId(diaryId));
        return topicRepository.saveAll(topics.stream().map(TopicDTO::toEntity).toList());
    }

    /**
     * diaryId를 기반으로 과거에 일기 내용 지우기
     */
    public void deleteByDiaryId(Long diaryId) {
        List<Topic> oldTopicList = topicRepository.findByDiaryIdAndIsDeletedFalse(diaryId);
        oldTopicList.forEach(topic -> topic.setIsDeleted(true));
        topicRepository.saveAll(oldTopicList);
    }

    public List<TopicDTO> convertTopicListToDTOList(List<Topic> topics){
        return topics.stream().map(topic -> new TopicDTO(topic.getTopicId(), topic.getDiaryId(), topic.getTitle(), topic.getContent(), TopicDTO.encodedPicture(topic.getPicture()), topic.getIsDeleted())).toList();
    }

    /**
     * 일기 토픽 내용을 삭제한다.
     */
    public Topic deleteById(String userId, Long topicId) {
        String uid = userId;
        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);

        Topic topic = topicRepository.findByTopicIdAndIsDeletedFalse(topicId).orElseThrow(() -> new ControlledException(TopicErrorCode.ERROR_TOPIC_NOT_FOUND));
        topic.setIsDeleted(true);

        return topicRepository.save(topic);
    }

    /**
     * 특정 일기 토픽 내용을 조회한다.
     */
    public Topic findByTopicId(String userId, Long topicId) {
        String uid = userId;

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);

        return topicRepository.findByTopicIdAndIsDeletedFalse(topicId).orElseThrow(() -> new ControlledException(TopicErrorCode.ERROR_TOPIC_NOT_FOUND));
    }

    /**
     * 일기 사진을 업데이트 한다.
     */
    public Topic updateTopicImage(String userId, Long topicId, byte[] picture) {
        String uid = userId;
        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));
        TenantContext.setCurrentTenant(uid);

        Topic topic = topicRepository.findByTopicIdAndIsDeletedFalse(topicId).orElseThrow(() -> new ControlledException(TopicErrorCode.ERROR_TOPIC_NOT_FOUND));
        topic.setPicture(picture);

        return topicRepository.save(topic);
    }

    /**
     * diary_id로 Topic 리스트를 조회한다.
     */
    public List<Topic> findByDiaryId(Long diaryId) {
        return topicRepository.findByDiaryIdAndIsDeletedFalse(diaryId);
    }
}
