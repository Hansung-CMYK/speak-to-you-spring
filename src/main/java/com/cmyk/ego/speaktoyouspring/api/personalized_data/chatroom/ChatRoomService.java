package com.cmyk.ego.speaktoyouspring.api.personalized_data.chatroom;

import com.cmyk.ego.speaktoyouspring.api.hub.ego.Ego;
import com.cmyk.ego.speaktoyouspring.api.hub.ego.EgoService;
import com.cmyk.ego.speaktoyouspring.api.hub.user_account.UserAccountRepository;
import com.cmyk.ego.speaktoyouspring.config.multitenancy.TenantContext;
import com.cmyk.ego.speaktoyouspring.exception.ControlledException;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.ChatRoomErrorCode;
import com.cmyk.ego.speaktoyouspring.exception.errorcode.UserAccountErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final EgoService egoService;
    private final ChatRoomRepository chatRoomRepository;
    private final UserAccountRepository userAccountRepository;

    public ChatRoom create(ChatRoomDTO chatRoomDTO){
        egoService.findById(Long.valueOf(chatRoomDTO.getEgoId()));

        if (chatRoomDTO.getLastChatAt() == null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            String formattedDate = LocalDateTime.now().format(formatter);
            chatRoomDTO.setLastChatAt(LocalDateTime.parse(formattedDate, formatter));
        }

        if (chatRoomDTO.getIsDeleted() == null){
            chatRoomDTO.setIsDeleted(false);
        }

        ChatRoom cr = chatRoomRepository.findByUidAndEgoIdAndIsDeletedFalse(chatRoomDTO.getUid(), chatRoomDTO.getEgoId())
                .orElseGet(chatRoomDTO::toEntity);

        cr.setLastChatAt(chatRoomDTO.getLastChatAt());

        return chatRoomRepository.save(cr);
    }

    public ChatRoom delete(ChatRoomDTO targetChatRoom){

        var uid = targetChatRoom.getUid();
        var egoId = targetChatRoom.getEgoId();

        // uid와 egoid가 일치하면서 is_deleted가 false인 값 조회
        var foundChatRoom = chatRoomRepository.findByUidAndEgoIdAndIsDeletedFalse(uid, egoId)
                .orElseThrow(() -> new ControlledException(ChatRoomErrorCode.ERROR_CHATROOM_NOT_FOUND));

        // 값을 직접 삭제 하지 않고 is_deleted속성을 true로 변경
        foundChatRoom.setIsDeleted(true);

        return chatRoomRepository.save(foundChatRoom);
    }

    public Page<ChatRoom> getChatRooms(int pageNum, int pageSize) {

        // Pageable 객체를 생성 (from, to는 페이지 번호 기준으로 0부터 시작)
        // from: 페이지 번호, to: 페이지 크기
        // 하나의 페이지에 몇개의 데이터가 들어갈지 : pageSize
        // pageSize로 나뉘어진 page에서 몇번째 page를 조회할까요 : pageNum
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.desc("lastChatAt")));

        return chatRoomRepository.findByIsDeletedFalse(pageable);
    }

    public List<ChatRoom> getChatRooms() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom getChatRoom(String uid) {
        return chatRoomRepository.findByUidAndIsDeletedFalse(uid)
                .orElseThrow(() -> new ControlledException(ChatRoomErrorCode.ERROR_CHATROOM_NOT_FOUND));
    }

    public Long findRandomEgoIdByUid(String uid) {
        List<Long> egoIdList = egoService.readAll().stream().map(Ego::getId).toList();

        // 전달받은 Uid가 있는지 확인
        userAccountRepository.findByUid(uid).orElseThrow(
                () -> new ControlledException(UserAccountErrorCode.ERROR_USER_NOT_FOUND));

        TenantContext.setCurrentTenant(uid);

        List<Long> usedEgoIds  = chatRoomRepository.findEgoIdByUidList(uid);
        List<Long> unusedEgoIds = egoIdList.stream()
                .filter(id -> !usedEgoIds.contains(id))
                .toList();

        return unusedEgoIds.isEmpty()
                ? egoIdList.get(new Random().nextInt(egoIdList.size()))
                : unusedEgoIds.get(new Random().nextInt(unusedEgoIds.size()));
    }
}
