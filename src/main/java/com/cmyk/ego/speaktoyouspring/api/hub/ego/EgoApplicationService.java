package com.cmyk.ego.speaktoyouspring.api.hub.ego;

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

import java.util.List;

@Service
@RequiredArgsConstructor
public class EgoApplicationService {
    private final EgoService egoService;
    private final ChatRoomService chatRoomService;
    private final EvaluationService evaluationService;
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
}
