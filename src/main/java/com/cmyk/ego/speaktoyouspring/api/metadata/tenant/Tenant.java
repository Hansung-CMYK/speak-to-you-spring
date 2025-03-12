package com.cmyk.ego.speaktoyouspring.api.metadata.tenant;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/// metadata.public.tenant 테이블 정보가 명시된 클래스이다.
@Entity
@Table(name = "tenant") // 테이블 이름
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Tenant {
  /// 시퀀스를 통해 테넌트의 아이디를 조정한다.
  @Id
  @Column(name = "tenant_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tenant_seq")
  @SequenceGenerator(name = "tenant_seq", sequenceName = "tenant_seq", allocationSize = 1)
  private Long tenantId;

  /// 테넌트가 생성한 스키마의 이름
  @Column(name = "tenant_name")
  private String tenantName;

  /// 생성된 시간
  @Builder.Default
  @Column(name = "created_at")
  private LocalDateTime createdAt = LocalDateTime.now();
}
