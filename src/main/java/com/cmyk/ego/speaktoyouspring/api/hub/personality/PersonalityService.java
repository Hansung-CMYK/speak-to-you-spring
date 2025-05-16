package com.cmyk.ego.speaktoyouspring.api.hub.personality;

import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.PersonalityErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonalityService {
    private final PersonalityRepository personalityRepository;

    /// personality_id로 특정 성격 조회
    public Personality findByPersonalityId(@PathVariable Long personalityId) {
        return personalityRepository.findByPersonalityId(personalityId).orElseThrow(
                () -> new ControlledException(PersonalityErrorCode.ERROR_PERSONALITY_ID_NOT_FOUND)
        );
    }

    /// 성격 전체 조회
    public List<Personality> findAll() {
        return personalityRepository.findAll();
    }

    /// 성격 추가
    public Personality add(ContentRequest contentRequest) {
        String content = contentRequest.getContent();
        return personalityRepository
                .findByContent(content)
                .orElseGet(() -> personalityRepository.save(new Personality(null, content)));
    }

    /// 성격 리스트 추가
    public List<String> addList(ContentListRequest contentListRequest) {
        List<String> newPersonalityList = new ArrayList<>();
        // 새로 생성
        for (String content : contentListRequest.getContentList()) {
            Personality personality = add(new ContentRequest(content));
            newPersonalityList.add(personality.getContent());
        }
        return newPersonalityList;
    }

    /// 성격 전체 삭제
    public void deleteAll() {
        personalityRepository.deleteAll();
    }


}
