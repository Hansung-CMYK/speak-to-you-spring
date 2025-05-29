package com.cmyk.ego.speaktoyouspring.api.hub.ego;

import com.cmyk.ego.speaktoyouspring.api.hub.ego_personality.EgoPersonality;
import com.cmyk.ego.speaktoyouspring.api.hub.ego_personality.EgoPersonalityService;
import com.cmyk.ego.speaktoyouspring.api.hub.personality.Personality;
import com.cmyk.ego.speaktoyouspring.api.hub.personality.PersonalityService;
import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccount;
import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountService;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoom;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoomService;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.ego_relationship.EgoRelationshipService;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.egolike.EgoLikeService;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.evaluation.Evaluation;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.evaluation.EvaluationService;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.ErrorMessage;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.BasicErrorCode;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EgoApplicationService {
    private final EgoService egoService;
    private final EgoLikeService egoLikeService;
    private final ChatRoomService chatRoomService;
    private final EvaluationService evaluationService;
    private final EgoRelationshipService egoRelationshipService;
    private final UserAccountService userAccountService;
    private final EgoPersonalityService egoPersonalityService;
    private final PersonalityService personalityService;
    private final UserAccountRepository userAccountRepository;

    private static final HttpClient client =
            HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();

    public List<EgoDTO> getUserEgoList(String userId) {
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

        // 에고가 가지고 있는 성격의 리스트를 조회한다.(id 값으로 리턴되어 어떤 성격인지 모름)
        List<EgoPersonality> personalityList = egoPersonalityService.findByEgoId(egoId);

        // 조회한 있는 성격 id 값으로 어떤 성격인지 가져온다.
        List<String> personalityNameList = new ArrayList<>();
        for (EgoPersonality personality : personalityList) {
            personalityNameList.add(personalityService.findByPersonalityId(personality.getPersonalityId()).getContent());
        }

        // 에고 성격 리스트 에고 정보에 추가
        EgoDTO egoDTO = egoService.convertEgoDTO(ego);
        egoDTO.setPersonalityList(personalityNameList);
        return egoDTO;
    }

    /// ego_id로 ego 정보 및 personality 정보 갱신
    public EgoDTO updateEgoInfo(EgoDTO egoDTO) {
        Ego ego = egoService.update(egoDTO);

        savePersonality(ego.getId(), egoDTO.getPersonalityList());

        // 에고 성격 리스트 에고 정보에 추가
        EgoDTO convertedEgoDTO = egoService.convertEgoDTO(ego);
        convertedEgoDTO.setPersonalityList(egoDTO.getPersonalityList());
        return convertedEgoDTO;
    }

    /// uid로 ego 정보 조회
    public Ego getEgoInfoByUid(String uid) {
        // 전달받은 Uid가 있는지 확인
        UserAccount ua = userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);

        // 에고 기본 정보 조회
        return egoService.findById(ua.getEgoId());
    }

    /// 오늘의 에고 조회
    public Ego getTodayEgo(String uid) {
        // 오늘의 에고 조회
        // TODO : 현재 랜덤으로 불러와서 추천 알고리즘을 개선 수정해야 한다.
        Long egoId = chatRoomService.findRandomEgoIdByUid(uid);

        return egoService.findById(egoId);
    }

    public Map<String, Object> getEgoInfoByUidAndEgoId(String uid, Long egoId) {
        Map<String, Object> egoInfo = new HashMap<>();

        // 에고 성격 리스트 조회
        List<EgoPersonality> personalityList = egoPersonalityService.findByEgoId(egoId);
        List<String> personalityNameList = new ArrayList<>();
        for (EgoPersonality personality : personalityList) {
            personalityNameList.add(personalityService.findByPersonalityId(personality.getPersonalityId()).getContent());
        }

        egoInfo.put("personalityList", personalityNameList);

        // 전달받은 Uid가 있는지 확인
        UserAccount ua = userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);

        egoInfo.put("rating", evaluationService.findOverallScoreByEgoId(egoId));
        egoInfo.put("isLiked", egoLikeService.isLiked(uid, egoId));
        egoInfo.put("relation", egoRelationshipService.findEgoRelationshipIdByEgoIdAndUid(uid, egoId));
        return egoInfo;
    }

    public Map<String, Object> getUidByEgoId(Long egoId) {
        String uid = userAccountService.getUidByEgoId(egoId);
        Map<String, Object> uidMap = new HashMap<>();
        uidMap.put("uid", uid);
        return uidMap;
    }

    public Ego saveVoicePath(Long egoId, String voicePath) {
        Ego ego = egoService.findById(egoId);
        ego.setVoiceUrl(voicePath);
        return egoService.save(ego);
    }

    public Ego getVoicePath(String uid) {
        // 전달받은 Uid가 있는지 확인
        UserAccount ua = userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);

        return egoService.findById(ua.getEgoId());
    }

    public HashMap<String, String> savePersona(PersonaDTO personaDTO) {
        // TODO 1. 페르소나 개인정보 저장하기
        HashMap<String, String> persona = new HashMap<>();
        persona.put("ego_id", personaDTO.egoId.toString());
        persona.put("name", personaDTO.name);
        persona.put("mbti", personaDTO.mbti);

        // TODO 2. 질의응답 정제하기
        List<List<String>> interview = personaDTO.interview;

        StringBuilder interviewLog = new StringBuilder();
        if (interview.size() >= 2) {
            List<String> firstList = interview.get(0);
            List<String> secondList = interview.get(1);

            for (int i = 0; i < Math.min(firstList.size(), secondList.size()); i++) {
                interviewLog.append(firstList.get(i)).append("\n").append(secondList.get(i)).append("\n");
            }
        }

        persona.put("interview", interviewLog.toString());

        // TODO 3. 페르소나 질의응답 저장하기
        // HTTP POST 요청
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(persona);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:8003/api/persona"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            // 반환 에러 발생 시 로그 작성
            if (response.statusCode() != 200 && response.statusCode() != 201) {
                System.out.println("STATUS  = " + response.statusCode());
                System.out.println("RESPONSE= " + response.body());
                throw new ControlledException(BasicErrorCode.FAST_API_ERROR);
            }

            return persona;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ControlledException(BasicErrorCode.FAST_API_ERROR);
        }
    }
}
