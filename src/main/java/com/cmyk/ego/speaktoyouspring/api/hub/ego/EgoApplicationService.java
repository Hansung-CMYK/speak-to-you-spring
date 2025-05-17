package com.cmyk.ego.speaktoyouspring.api.hub.ego;

import com.cmyk.ego.speaktoyouspring.api.hub.ego_personality.EgoPersonality;
import com.cmyk.ego.speaktoyouspring.api.hub.ego_personality.EgoPersonalityService;
import com.cmyk.ego.speaktoyouspring.api.hub.personality.Personality;
import com.cmyk.ego.speaktoyouspring.api.hub.personality.PersonalityService;
import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoom;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoomService;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.evaluation.Evaluation;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.evaluation.EvaluationService;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EgoApplicationService {
    private final EgoService egoService;
    private final ChatRoomService chatRoomService;
    private final EvaluationService evaluationService;
    private final EgoPersonalityService egoPersonalityService;
    private final PersonalityService personalityService;
    private final UserAccountRepository userAccountRepository;

    public List<Ego> getUserEgoList(String userId) {
        List<Ego> egoList = egoService.readAll();

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(userId).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(userId);

        List<ChatRoom> userEgoList = chatRoomService.getChatRooms();
        List<Evaluation> userEvaluationList = evaluationService.readAll();

        return egoService.joinEgoList(egoList, userEgoList, userEvaluationList);
    }

    /// personality 배열을 테이블에 저장
    public void savePersonality(Long egoId, List<String> personalityList) {
        egoPersonalityService.deleteAllByEgoId(egoId);

        for (String personality : personalityList) {
            Personality personalityEntity = personalityService.add(personality);
            if (personalityEntity != null) {
                egoPersonalityService.add(egoId, personalityEntity.getPersonalityId());
            }
        }

    }

    // ego_id로 에고 정보와 성격 정보 조회
    public EgoDTO getEgoInfo(Long egoId) {
        // 에고 기본 정보 조회
        Ego ego = egoService.findById(egoId);

        // 에고 성격 리스트 조회
        List<EgoPersonality> personalityList = egoPersonalityService.findByEgoId(egoId);
        List<String> personalityNameList = new ArrayList<>();
        for (EgoPersonality personality : personalityList) {
            personalityNameList.add(personalityService.findByPersonalityId(personality.getPersonalityId()).getContent());
        }

        // 에고 성격 리스트 에고 정보에 추가
        EgoDTO egoDTO = convertEgoDTO(ego);
        egoDTO.setPersonalityList(personalityNameList);
        return egoDTO;
    }

    public EgoDTO convertEgoDTO(Ego ego) {
        return new EgoDTO(ego.getId(), ego.getName(), ego.getIntroduction(), ego.getProfileImage(), ego.getMbti(), ego.getCreatedAt(), ego.getLikes(), new ArrayList<>());
    }

    /// ego_id로 ego 정보 및 personality 정보 갱신
    public EgoDTO updateEgoInfo(EgoDTO egoDTO) {
        Ego ego = egoService.update(egoDTO);

        savePersonality(ego.getId(), egoDTO.getPersonalityList());

        // 에고 성격 리스트 에고 정보에 추가
        EgoDTO convertedEgoDTO = convertEgoDTO(ego);
        convertedEgoDTO.setPersonalityList(egoDTO.getPersonalityList());
        return convertedEgoDTO;
    }
}
