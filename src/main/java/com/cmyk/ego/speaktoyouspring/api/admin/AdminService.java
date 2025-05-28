package com.cmyk.ego.speaktoyouspring.api.admin;

import com.cmyk.ego.speaktoyouspring.api.hub.ego.EgoService;
import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory.ChatHistory;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory.ChatHistoryDTO;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory.ChatHistoryService;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoom;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoomDTO;
import com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom.ChatRoomService;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final EgoService egoService;
    private final ChatRoomService chatRoomService;
    private final ChatHistoryService chatHistoryService;
    private final UserAccountRepository userAccountRepository;

    /**
     * String uid = diaryDTO.getUid();
     *         // 전달받은 Uid가 있는지 확인
     *         userAccountRepository.findByUid(uid).orElseThrow(
     *                 () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));
     *
     *         TenantContext.setCurrentTenant(uid);
     * @param adminChatListDTO
     */
    public void createChatList(AdminChatListDTO adminChatListDTO) {
        for (AdminChatDTO adminChatDTO : adminChatListDTO.getChatList()) {
            String uid = adminChatDTO.getUid();
            
            // 사용자 존재 여부 확인
            userAccountRepository.findByUid(uid).orElseThrow(
                    () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

            // 에고 존재 여부 확인
            egoService.findById(adminChatDTO.getEgoId());

            TenantContext.setCurrentTenant(uid);

            // 채팅방 생성
            ChatRoom cr = chatRoomService.create(new ChatRoomDTO(null, uid, Math.toIntExact(adminChatDTO.getEgoId()), LocalDateTime.now(), false));
            
            Long chatRoomId = cr.getId();
            List<ChatHistory> chatHistoryList = new ArrayList<>();
            for (AdminChatHistoryDTO ch : adminChatDTO.getContent()) {
                // 채팅 기록 생성
                ChatHistory chNew = chatHistoryService.create(new ChatHistoryDTO(
                        null,
                        uid,
                        chatRoomId,
                        ch.getContent(),
                        ch.getType(),
                        LocalDateTime.now(),
                        false,
                        null,
                        ch.getContentType()
                ));
                chatHistoryList.add(chNew);
            }
        }
    }
}
