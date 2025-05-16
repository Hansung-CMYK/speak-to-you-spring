package com.cmyk.ego.speaktoyouspring.api.personalized_data.diary;

import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.diarykeyword.DiaryKeyword;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.diarykeyword.DiaryKeywordDTO;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.diarykeyword.DiaryKeywordService;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.topic.TopicDTO;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.topic.TopicService;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryApplicationService {
    private final DiaryService diaryService;
    private final TopicService topicService;
    private final DiaryKeywordService diaryKeywordService;
    private final UserAccountRepository userAccountRepository;

    /**
     * 일기 생성시 필요한 정보를 가져와서 일기 생성 리턴
     */
    public DiaryDTO create(DiaryDTO diaryDTO) {
        String uid = diaryDTO.getUid();
        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);

        // 일기 생성
        Diary diary = diaryService.upsert(diaryDTO);
        List<TopicDTO> topicDTOList = topicService.convertTopicListToDTOList(topicService.saveAll(diaryDTO.getTopics(), diary.getDiaryId()));

        // 키워드 저장
        List<DiaryKeywordDTO> diaryKeywordDTOList = diaryDTO.getKeywords().stream().map(keyword -> new DiaryKeywordDTO(null, diary.getDiaryId(), keyword)).toList();
        diaryKeywordService.saveAll(uid, diaryKeywordDTOList);

        return new DiaryDTO(diary.getDiaryId(), diary.getUid(), diary.getEgoId(), diary.getFeeling(), diary.getDailyComment(),diary.getCreatedAt(), diaryDTO.getKeywords(), topicDTOList);
    }

    /**
     * 일기/토픽 내용을 조회한다.
     */
    public DiaryDTO findByDiaryId(String userId, Long diaryId) {
        String uid = userId;
        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));
        TenantContext.setCurrentTenant(uid);

        Diary diary = diaryService.findByDiaryId(diaryId);
        List<TopicDTO> topicDTOList = topicService.convertTopicListToDTOList(topicService.findByDiaryId(diaryId));
        List<String> keyword = diaryKeywordService.findByDiaryId(userId, diaryId).stream().map(DiaryKeyword::getContent).toList();
        return new DiaryDTO(diary.getDiaryId(), diary.getUid(), diary.getEgoId(), diary.getFeeling(), diary.getDailyComment(),diary.getCreatedAt(), keyword, topicDTOList);
    }
}
