package com.cmyk.ego.speaktoyouspring.api.hub.ego_personality;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EgoPersonalityService {
    private final EgoPersonalityRepository egoPersonalityRepository;

    /// ego_id로 에고 성격 리스트 조회
    public List<EgoPersonality> findByEgoId(Long egoId) {
        return egoPersonalityRepository.findByEgoId(egoId);
    }

    /// ego_id와 personality_id로 에고 성격 추가
    public EgoPersonality add(Long egoId, Long personalityId) {
        return egoPersonalityRepository.findByEgoIdAndPersonalityId(egoId, personalityId)
                .orElseGet(() -> egoPersonalityRepository.save(new EgoPersonality(null, egoId, personalityId)));
    }

    /// ego_id로 에고 성격 리스트 전체 삭제
    public void deleteAllByEgoId(Long egoId) {
        egoPersonalityRepository.deleteAllByEgoId(egoId);
    }
}
