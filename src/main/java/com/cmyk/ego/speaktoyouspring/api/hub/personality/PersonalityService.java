package com.cmyk.ego.speaktoyouspring.api.hub.personality;

import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.PersonalityErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public Personality add(String content) {
        return personalityRepository
                .findByContent(content)
                .orElseGet(() -> personalityRepository.save(new Personality(null, content, null)));
    }

    /// 성격 추가
    public Personality add(ContentRequest content) {
        Personality existing = personalityRepository.findByContent(content.getContent()).orElse(null);

        if (existing != null) {
            // imageUrl이 다르면 업데이트
            if (!Objects.equals(existing.getImageUrl(), content.getImageUrl())) {
                existing.setImageUrl(content.getImageUrl());
                return personalityRepository.save(existing); // 변경 저장
            }
            return existing; // 동일하면 그대로 반환
        }

        // 없으면 새로 저장
        return personalityRepository.save(
                new Personality(null, content.getContent(), content.getImageUrl())
        );
    }

    /// 성격 리스트 추가
    public List<Personality> addList(ContentListRequest contentListRequest) {
        List<Personality> newPersonalityList = new ArrayList<>();
        // 새로 생성
        for (ContentRequest content : contentListRequest.getContentList()) {
            Personality personality = add(content);
            newPersonalityList.add(personality);
        }
        return newPersonalityList;
    }

    /// 성격 전체 삭제
    public void deleteAll() {
        personalityRepository.deleteAll();
    }


}
