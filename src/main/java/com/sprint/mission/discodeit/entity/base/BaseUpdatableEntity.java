package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@MappedSuperclass // 부모 클래스의 필드를 자식 엔티티에게 전달하기 위해 필요
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 추가 (접근 제어는 protected)
public abstract class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  @Column(name = "updated_at")
  private Instant updatedAt;
}
