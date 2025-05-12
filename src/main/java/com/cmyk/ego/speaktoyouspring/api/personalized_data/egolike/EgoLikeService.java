package com.cmyk.ego.speaktoyouspring.api.personalized_data.egolike;

import com.cmyk.ego.speaktoyouspring.api.hub.ego.Ego;
import com.cmyk.ego.speaktoyouspring.api.hub.ego.EgoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EgoLikeService {
    private final EgoLikeRepository egoLikeRepository;
    private final EgoService egoService;
    /**
     * ego 좋아요 상태 등록
     * */
    public Ego updateLikes(EgoLikeDTO egoLikeDTO) {
        EgoLike egoLike = egoLikeRepository.findByEgoIdAndUid(egoLikeDTO.getEgoId(), egoLikeDTO.getUid())
                .orElse(egoLikeDTO.toEntity());

        // Ego에 좋아요 개수 업데이트
        Ego ego = egoService.updateLike(egoLikeDTO, calculateLikeDelta(egoLike.getIsLike(), egoLikeDTO.getIsLike()));

        egoLike.setIsLike(egoLikeDTO.getIsLike()); // Like 상태 업데이트
        egoLikeRepository.save(egoLike); // egoLike 업데이트 후 저장

        return ego; // ego 업데이트 후 반환
    }

    // ego 좋아요 상태 변경 (좋아요, 좋아요 취소)
    public int calculateLikeDelta(boolean oldIsLike, boolean newIsLike) {
        if (oldIsLike == newIsLike) return 0;
        return newIsLike ? 1 : -1; // 좋아요가 설정되었으면 1, 좋아요가 해제되었으면 -1
    }
}
