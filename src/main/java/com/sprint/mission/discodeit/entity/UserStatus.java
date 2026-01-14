package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {

  private static final long ONLINE_THRESHOLD_SECONDS = 300L;
  @Serial
  private final static long serialVersionUID = 1L;

  private final UUID id;
  private final Instant createdAt;
  private Instant updatedAt;
  private final UUID userId;
  private Instant lastActiveAt;

  public UserStatus(UUID userId, Instant lastActiveAt) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.userId = userId;
    this.lastActiveAt = lastActiveAt;
  }

  public void update(Instant lastActiveAt) {
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      this.updatedAt = Instant.now();
    }
  }

  public boolean isOnline() {
    return Instant.now().minusSeconds(ONLINE_THRESHOLD_SECONDS).isBefore(this.lastActiveAt);
  }
}
