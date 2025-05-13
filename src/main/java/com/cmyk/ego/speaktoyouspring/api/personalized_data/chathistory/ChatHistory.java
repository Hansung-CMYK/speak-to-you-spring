package com.cmyk.ego.speaktoyouspring.api.personalized_data.chathistory;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;  // 채팅 내역 ID (자동 증가)

  @Column(name = "uid", nullable = false)
  private String uid; // 사용자 UID

  @Column(name = "chat_room_id", nullable = false)
  private Long chatRoomId;  // 채팅방 ID

  @Column(columnDefinition = "TEXT")
  private String content;  // 사용자가 보낸 메시지

  @Column(length = 1, nullable = false)
  private String type;  // 대화 유형 (U - User, E - Ego)

  @Column(name = "chat_at")
  private LocalDateTime chatAt = LocalDateTime.now();  // 대화 발생 시간

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;  // 삭제 여부

  @Column(name = "message_hash")
  private String messageHash;
}