package com.cmyk.ego.speaktoyouspring.api.hub.ego;

import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoom;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.egolike.EgoLikeDTO;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.evaluation.Evaluation;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.EgoErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EgoService {
    private final EgoRepository egoRepository;

    // ego 생성
    public Ego create(EgoDTO egoDTO) {

        Ego ego = egoDTO.toEntity();

        if (ego.getCreatedAt() == null) {
            ego.setCreatedAt(LocalDate.now());
        }

        return egoRepository.save(ego);
    }

    // ego 전체 조회
    public List<Ego> readAll() {
        return egoRepository.findAll();
    }

    // egoid와 일치하는 ego 조회
    public Ego findById(Long egoId) {
        return egoRepository.findById(egoId)
                .orElseThrow(() -> new ControlledException(EgoErrorCode.ERROR_EGO_NOT_FOUND));
    }

    // ego 정보 수정
    public Ego update(EgoDTO egoDTO) {
        Ego ego = egoRepository.findById(egoDTO.getId())
                .orElseThrow(() -> new ControlledException(EgoErrorCode.ERROR_EGO_NOT_FOUND));

        if (egoDTO.getName() != null)
            ego.setName(egoDTO.getName());
        if (egoDTO.getIntroduction() != null)
            ego.setIntroduction(egoDTO.getIntroduction());
        if (egoDTO.getProfileImage() != null)
            ego.setProfileImage(egoDTO.getProfileImage());
        if (egoDTO.getMbti() != null)
            ego.setMbti(egoDTO.getMbti());
        if (egoDTO.getLikes() != null)
            ego.setLikes(egoDTO.getLikes());
        if (egoDTO.getCreatedAt() != null)
            ego.setCreatedAt(egoDTO.getCreatedAt());

        return ego;
    }

    // 좋아요 개수 수정
    public Ego updateLike(EgoLikeDTO egoLikeDTO, int delta) {
        Ego ego = egoRepository.findById(egoLikeDTO.getEgoId())
                .orElseThrow(() -> new ControlledException(EgoErrorCode.ERROR_EGO_NOT_FOUND));

        ego.setLikes(ego.getLikes() + delta);

        return egoRepository.save(ego);
    }

    /**
     * 에고 정보 취합 (기본 에고 정보 + 평가 점수)
     * - 전체 에고 정보 : egoList
     * - 대화한 에고 목록 : userEgoList -- 리스트 합치는 기준
     * - 사용자의 에고 평가 점수 : userEvaluationList
     */
    public List<Ego> joinEgoList(List<Ego> egoList, List<ChatRoom> userEgoList, List<Evaluation> userEvaluationList) {

        // Map으로 만들어 Id로 검색하기 쉽게 변환 // egoList
        Map<Long, Ego> egoMap = egoList.stream()
                .collect(Collectors.toMap(Ego::getId, Function.identity()));

        // Map으로 만들어 Id로 검색하기 쉽게 변환 // userEvaluationList
        Map<Long, Evaluation> evaluationMap = userEvaluationList.stream()
                .collect(Collectors.toMap(Evaluation::getEgoId, Function.identity()));

        List<Ego> result = new ArrayList<>();

        for (ChatRoom userEgo : userEgoList) {
            // 사용자가 대화한 에고 ID
            Integer egoId = userEgo.getEgoId();

            // egoId로 검색
            Ego ego = egoMap.get(Long.valueOf(egoId));
            Evaluation evaluation = evaluationMap.get(Long.valueOf(egoId));

            if (ego != null) {
                if (evaluation != null) {
                    ego.setRating(evaluation.getOverallScore());
                }
                result.add(ego);
            } else {
                throw new ControlledException(EgoErrorCode.ERROR_EGO_NOT_FOUND);
            }

        }

        return result;
    }
}
