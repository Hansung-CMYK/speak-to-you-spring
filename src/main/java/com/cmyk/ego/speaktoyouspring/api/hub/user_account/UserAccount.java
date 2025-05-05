package com.cmyk.ego.speaktoyouspring.api.hub.user_account;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_account") // 테이블 이름
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserAccount {

  /// Firebase UID (Primary Key)
  @Id
  @Column(name = "uid", nullable = false, unique = true)
  private String uid;

  /// 사용자가 만든 EGO ID
  @Column(name="ego_id")
  private Integer egoId;

  /// 사용자 email
  @Column(name="email")
  private String email;

  /// 사용자 생년월일 (YYYY-MM-DD)
  @Column(name = "birth_date")
  private LocalDate birthDate;

  /// 사용자 역할 (ROLE_USER, ROLE_ADMIN)
  @Column(name = "role")
  private String role;

  /// 생성된 시간 (YYYY-MM-DD)
  @Builder.Default
  @Column(name = "created_at")
  private LocalDate createdAt = LocalDate.now();

  /// 탈퇴 여부 (true: 탈퇴, false: 정상)
  @Builder.Default
  @Column(name = "is_deleted")
  private Boolean isDeleted = false;
}